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

        # Copy the XCFramework to where the publish lane can find it
        sh("cp -rf #{lane_context[:repo_root]}/mockzilla/build/XCFrameworks/release/mockzilla.xcframework #{lane_context[:repo_root]}/SwiftMockzilla")
    end

    desc "Generate Podspec"
    lane :generate_podspec do
        gradle(
            tasks: [":mockzilla:podPublishReleaseXCFramework"]
        )

        # Copy the Podspec to where the publish lane can find it
        sh("cp -rf #{lane_context[:repo_root]}/mockzilla/build/cocoapods/publish/release/Mockzilla.podspec #{lane_context[:repo_root]}/SwiftMockzilla")
    end

    desc "Deploy the Swift package to github & push new podspec"
    lane :publish_swift_package do
        # Create the XCFramework
        generate_xcframework
        # Create podspec
        generate_podspec

        sh("rm -rf apadmi-mockzilla-ios")

        sh("git clone #{ENV["IOS_DEPLOY_URL"]} apadmi-mockzilla-ios")
        sh(%{
            cd apadmi-mockzilla-ios;  
            rm -rf ./*;
            cp -r #{lane_context[:repo_root]}/SwiftMockzilla/ .;

            git add .;
            git add --force mockzilla.xcframework
            git commit -m "Updating Package"
            git push
            git tag v#{get_version_name}
            git push --tags
        })
    end
end

desc "Publish to maven local"
lane :publish_to_maven_local do
    gradle(
        tasks: [":mockzilla-common:publishToMavenLocal", ":mockzilla:publishToMavenLocal"]
    )
end

desc "Publish to maven remote"
lane :publish_to_maven do
    gradle(
        tasks: [":mockzilla-common:publish", ":mockzilla:publish"],
        properties: {
            "signing.gnupg.keyName" => ENV["GPG_KEY_ID"],
            "signing.gnupg.passphrase" => ENV["GPG_PASSPHRASE"]
        }
    )
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
    str = IO.read("#{lane_context[:repo_root]}/version.txt")    

    if str.nil?
        raise "Failed to extract version from gradle file"
    end

    str
end