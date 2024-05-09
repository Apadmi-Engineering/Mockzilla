platform :ios do 

    desc "iOS target for the lib"
    lane :lib_pull_request do
        gradle(
            tasks: [":mockzilla:iosX64Test"],
            project_dir: "./lib",
            flags: "--info"
        )

        # Create the XCFramework
        generate_xcframework

        sh("cd #{lane_context[:repo_root]}/lib/SwiftMockzilla; xcodebuild -scheme SwiftMockzilla test -destination 'platform=iOS Simulator,name=iPhone 15 Pro Max,OS=17.5'")
    end
    
    desc "Generate XCFramework"
    lane :generate_xcframework do
        gradle(
            tasks: [":mockzilla:assembleXCFramework"],
            project_dir: "./lib"
        )

        # Copy the XCFramework to where the SPM package can find it
        sh("cp -rf #{lane_context[:repo_root]}/lib/mockzilla/build/XCFrameworks/release/mockzilla.xcframework #{lane_context[:repo_root]}/lib/SwiftMockzilla")
    end

    desc "Deploy the package to github"
    lane :publish_spm_package do
        # Create the XCFramework
        generate_xcframework

        sh("rm -rf apadmi-mockzilla-ios")

        sh("git clone #{ENV["IOS_DEPLOY_URL"]} apadmi-mockzilla-ios")
        sh(%{
            cd apadmi-mockzilla-ios;  
            rm -rf ./*;
            cp -r #{lane_context[:repo_root]}/lib/SwiftMockzilla/ .;

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
        tasks: [":mockzilla:publishToMavenLocal"],
        project_dir: "./lib"
    )
end

desc "Publish to maven remote"
lane :publish_to_maven do
    gradle(
        tasks: [":mockzilla:publish"],
        project_dir: "./lib",
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
            tasks: [":mockzilla:testDebugUnitTest"],
            project_dir: "./lib",
            flags: "--info"
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