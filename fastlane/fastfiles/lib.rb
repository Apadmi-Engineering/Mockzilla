platform :ios do
    desc "Generate Podspec and Framework"
    lane :generate_framework_and_podspec do |options|
        gradle(
            tasks: [":mockzilla:podPublishReleaseXCFramework"],
            properties: createSnapshotProp(options[:is_snapshot], get_core_mockzilla_version_name(options))
        )

        # Copy the Podspec to where the publish lane can find it
        sh("cp -rf #{lane_context[:repo_root]}/mockzilla/build/cocoapods/publish/release/mockzilla.xcframework #{lane_context[:repo_root]}/SwiftMockzilla")
        sh("cp -rf #{lane_context[:repo_root]}/mockzilla/build/cocoapods/publish/release/SwiftMockzilla.podspec #{lane_context[:repo_root]}/SwiftMockzilla")
    end

    desc "Deploy the package to github & push podspec"
    lane :publish_swift_package do |options|
        generate_framework_and_podspec(is_snapshot: options[:is_snapshot])

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
            git commit -m "Updating Package #{get_core_mockzilla_version_name(options)}";
            git push;
        })

        if !options[:is_snapshot]
            sh(%{
                cd apadmi-mockzilla-ios;
                git push
                git tag v#{get_core_mockzilla_version_name(options)}
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
        properties: createSnapshotProp(options[:is_snapshot], get_core_mockzilla_version_name(options))
    )
end

desc "Publish to maven remote"
lane :publish_to_maven do |options|
    # Dry run
    publish_to_maven_local(is_snapshot: options[:is_snapshot])
    FastlaneCore::UI.success("Published to maven local")

    FastlaneCore::UI.message("Publishing to remote")
    gradle(
        tasks: [
            ":mockzilla-common:publishToMavenCentral",
            ":mockzilla:publishToMavenCentral",
            ":mockzilla-management:publishToMavenCentral",
        ],
        properties: createSnapshotProp(options[:is_snapshot], get_core_mockzilla_version_name(options))
    )
end

desc "Generate JavaScript artifacts"
lane :build_js_artifacts do |options|
    gradle(
        tasks: [
            ":mockzilla:jsBrowserProductionWebpack",
        ],
        properties: createSnapshotProp(options[:is_snapshot], get_core_mockzilla_version_name(options))
    )
end

def createSnapshotProp(is_snapshot, version)
    {
        "is_snapshot" => is_snapshot,
        "version" => version
    }
end

lane :get_core_mockzilla_version_name do |options|
    build_gradle_text = IO.read("#{lane_context[:repo_root]}/mockzilla/build.gradle.kts")
    version_pattern = /.*"(.*?)" \/\/ x-release-please-version/
    version = build_gradle_text.match(version_pattern)[1]

    options[:is_snapshot] ? "#{version}-SNAPSHOT" : version
end
