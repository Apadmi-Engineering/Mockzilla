platform :ios do 

    desc "iOS target for the lib"
    lane :lib_pull_request do
        gradle(
            tasks: [":mockzilla-common:iosX64Test", ":mockzilla:iosX64Test", ":mockzilla-management:jvmTest"]
        )

        # Create the XCFramework
        generate_xcframework

        # If running this locally check the simulator in the command exists locally, if it doesn't, change it
        # to one that does but remember to change it back before committing changes.
        sh("cd #{lane_context[:repo_root]}/SwiftMockzilla; xcodebuild -scheme SwiftMockzilla test -destination 'platform=iOS Simulator,name=iPhone 15 Pro Max,OS=17.5'")
    end
    
    desc "Generate XCFramework"
    lane :generate_xcframework do
        gradle(
            tasks: [":mockzilla:assembleXCFramework"]
        )

        # Copy the XCFramework to where the SPM package can find it
        sh("cp -rf #{lane_context[:repo_root]}/mockzilla/build/XCFrameworks/release/mockzilla.xcframework #{lane_context[:repo_root]}/SwiftMockzilla")
    end

    desc "Deploy the package to github"
    lane :publish_spm_package do |options|
        prepare_for_snapshot_if_needed(options)

        # Create the XCFramework
        generate_xcframework

        sh("rm -rf apadmi-mockzilla-ios")

        sh("git clone #{ENV["IOS_DEPLOY_URL"]} apadmi-mockzilla-ios")
        sh(%{
            cd apadmi-mockzilla-ios;
            rm -rf ./*;
            cp -r #{lane_context[:repo_root]}/SwiftMockzilla/ .;
        })

        if options[:is_snapshot]
            sh(%{
                cd apadmi-mockzilla-ios;
                git checkout deployment/snapshot;
            })
        end

        sh(%{
            cd apadmi-mockzilla-ios;
            git add .;
            git add --force mockzilla.xcframework;
            git commit -m "Updating Package #{get_version_name}";
        })

        if options[:is_snapshot]
            sh(%{
                cd apadmi-mockzilla-ios;
                git push --force
            })
        else
            sh(%{
                cd apadmi-mockzilla-ios;
                git push
                git tag v#{get_version_name}
                git push --tags
            })
        end
    end
end

desc "Publish to maven local"
lane :publish_to_maven_local do
    gradle(
        tasks: [
            ":mockzilla-common:publishToMavenLocal",
            ":mockzilla:publishToMavenLocal",
            ":mockzilla-management:publishToMavenLocal",
        ],
        properties: {
            "signing.gnupg.keyName" => ENV["GPG_KEY_ID"],
            "signing.gnupg.passphrase" => ENV["GPG_PASSPHRASE"]
        }
    )
end

desc "Publish to maven remote"
lane :publish_to_maven do |options|
    prepare_for_snapshot_if_needed(options)

    # Dry run
    publish_to_maven_local
    FastlaneCore::UI.success("Published to maven local")

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
        }
    )
    sh("git checkout -- #{lane_context[:version_file]}")
end

platform :android do 

    desc "Android target for the lib"
    lane :lib_pull_request do
        gradle(
            tasks: [
                ":mockzilla-common:testDebugUnitTest",
                ":mockzilla:testDebugUnitTest", 
                ":mockzilla-management:jvmTest"
            ]
        )
    end
end
 
lane :get_version_name do
    str = IO.read(lane_context[:version_file])

    if str.nil?
        raise "Failed to extract version from gradle file"
    end

    str.strip
end

private_lane :prepare_for_snapshot_if_needed do |options|
    is_snapshot = options[:is_snapshot]
    if is_snapshot
        version = get_version_name
        if !version.ends_with? "SNAPSHOT"
            File.open(lane_context[:version_file], "w") do |file|
              file.puts "#{version}-SNAPSHOT"
            end
        end
    end
end