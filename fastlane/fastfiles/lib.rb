platform :ios do
    desc "Generate XCFramework"
    lane :generate_xcframework do |options|
        gradle(
            tasks: [":mockzilla:assembleXCFramework"],
            properties: createSnapshotProp(options[:is_snapshot], get_version_name(options))
        )

        # Copy the XCFramework to where the SPM package can find it
        sh("cp -rf #{lane_context[:repo_root]}/mockzilla/build/XCFrameworks/release/mockzilla.xcframework #{lane_context[:repo_root]}/SwiftMockzilla")
    end

    desc "Generate Podspec"
    lane :generate_podspec do |options|
        gradle(
            tasks: [":mockzilla:podPublishReleaseXCFramework"],
            properties: createSnapshotProp(options[:is_snapshot], get_version_name(options))
        )

        # Copy the Podspec to where the publish lane can find it
        sh("cp -rf #{lane_context[:repo_root]}/mockzilla/build/cocoapods/publish/release/SwiftMockzilla.podspec #{lane_context[:repo_root]}/SwiftMockzilla")
    end

    desc "Deploy the package to github & push podspec"
    lane :publish_swift_package do |options|
        # Create the XCFramework
        generate_xcframework(is_snapshot: options[:is_snapshot])

        # Generate the podspec
        generate_podspec(is_snapshot: options[:is_snapshot])

        sh("rm -rf apadmi-mockzilla-ios")


        if !options[:is_snapshot]
            sh("git clone #{ENV["IOS_DEPLOY_URL"]} apadmi-mockzilla-ios")
        else
            sh("git clone -b deployment/snapshot #{ENV["IOS_DEPLOY_URL"]} apadmi-mockzilla-ios")
        end

        sh(%{
            cd apadmi-mockzilla-ios;
            rm -rf ./*;
            cp -r #{lane_context[:repo_root]}/SwiftMockzilla/ .;

            git add .;
            git add --force mockzilla.xcframework;
            git add --force SwiftMockzilla.podspec;
            git commit -m "Updating Package #{get_version_name(options)}";
            git push;
        })

        if !options[:is_snapshot]
            sh(%{
                cd apadmi-mockzilla-ios;
                git push
                git tag v#{get_version_name(options)}
                git push --tags
            })

            # Push podspec to trunk
            sh(%{
                cd apadmi-mockzilla-ios
                pod trunk push
            })
        end
    end
end

desc "Publish to maven local"
lane :publish_to_maven_local do |options|
    gradle(
        tasks: [
            ":mockzilla-common:publishToMavenLocal",
            ":mockzilla:publishToMavenLocal",
            ":mockzilla-management:publishToMavenLocal",
        ],
        properties: {
            "signing.gnupg.keyName" => ENV["GPG_KEY_ID"],
            "signing.gnupg.passphrase" => ENV["GPG_PASSPHRASE"]
        }.merge(createSnapshotProp(options[:is_snapshot], get_version_name(options)))
    )
end

desc "Publish to maven remote"
lane :publish_to_maven do |options|
    # Dry run
    publish_to_maven_local(is_snapshot: options[:is_snapshot])
    FastlaneCore::UI.success("Published to maven local")

    # Real Thing
    FastlaneCore::UI.message("Publishing to remote")
    gradle(
        tasks: [
            ":mockzilla-common:publish",
            ":mockzilla:publish",
            ":mockzilla-management:publish",
        ],
        properties: {
            "signing.gnupg.keyName" => ENV["GPG_KEY_ID"],
            "signing.gnupg.passphrase" => ENV["GPG_PASSPHRASE"]
        }.merge(createSnapshotProp(options[:is_snapshot], get_version_name(options)))
    )

    begin
      # Let the maven APIs update
      sleep 10
      complete_maven_upload
    rescue => e
      complete_maven_upload # Try twice
    end

end

def createSnapshotProp(is_snapshot, version)
    {
        "is_snapshot" => is_snapshot,
        "version" => version
    }
end

private_lane :get_version_name do |options|
    build_gradle_text = IO.read("#{lane_context[:repo_root]}/mockzilla/build.gradle.kts")
    version_pattern = /.*"(.*?)" \/\/ x-release-please-version/
    version = build_gradle_text.match(version_pattern)[1]

    options[:is_snapshot] ? "#{version}-SNAPSHOT" : version
end

# Needed as per: https://central.sonatype.org/publish/publish-portal-ossrh-staging-api/#configuring-the-repository
lane :complete_maven_upload do
    require 'net/http'

    uri = URI('https://ossrh-staging-api.central.sonatype.com/manual/upload/defaultRepository/com.apadmi')
    uri.query = URI.encode_www_form( {
      :publishing_type => 'automatic',
    })
    req = Net::HTTP::Post.new(uri)
    req.content_type = 'application/x-www-form-urlencoded'
    req['accept'] = '*/*'
    req.basic_auth ENV["OSSRH_USERNAME"], ENV["OSSRH_PASSWORD"]

    req.body = ''

    http = Net::HTTP.new(uri.hostname, uri.port)
    http.use_ssl = true

    # Set timeouts, request is slow
    http.open_timeout = 300
    http.read_timeout = 300
    http.write_timeout = 300
    http.ssl_timeout = 300

    # Perform the request
    res = http.request(req)

    puts res.body
end
