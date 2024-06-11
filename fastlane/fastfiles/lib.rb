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
    lane :generate_xcframework do |options|
        gradle(
            tasks: [":mockzilla:assembleXCFramework"],
            properties: createSnapshotProp(options[:is_snapshot])
        )

        # Copy the XCFramework to where the SPM package can find it
        sh("cp -rf #{lane_context[:repo_root]}/mockzilla/build/XCFrameworks/release/mockzilla.xcframework #{lane_context[:repo_root]}/SwiftMockzilla")
    end

    desc "Generate Podspec"
    lane :generate_podspec do |options|
        gradle(
            tasks: [":mockzilla:podPublishReleaseXCFramework"],
            properties: createSnapshotProp(options[:is_snapshot])
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

        sh("git clone #{ENV["IOS_DEPLOY_URL"]} apadmi-mockzilla-ios")

        if options[:is_snapshot]
            sh(%{
                cd apadmi-mockzilla-ios;
                git checkout -b deployment/new-snapshot
            })
        end

        sh(%{
            cd apadmi-mockzilla-ios;
            rm -rf ./*;
            cp -r #{lane_context[:repo_root]}/SwiftMockzilla/ .;

            git add .;
            git add --force mockzilla.xcframework;
            git add --force SwiftMockzilla.podspec
            git commit -m "Updating Package #{get_version_name(options)}";
        })

        if options[:is_snapshot]
            sh(%{
                cd apadmi-mockzilla-ios;
                git push --force origin deployment/new-snapshot:deployment/snapshot
            })
        else
            sh(%{
                cd apadmi-mockzilla-ios;
                git push
                git tag v#{get_version_name(options)}
                git push --tags
            })

            # Push podspec to trunk
            sh("pod trunk push")
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
        }.merge(createSnapshotProp(options[:is_snapshot]))
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
            ":mockzilla-common:publish",
            ":mockzilla:publish",
            ":mockzilla-management:publish",
        ],
        properties: {
            "signing.gnupg.keyName" => ENV["GPG_KEY_ID"],
            "signing.gnupg.passphrase" => ENV["GPG_PASSPHRASE"]
        }.merge(createSnapshotProp(options[:is_snapshot]))
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

def createSnapshotProp(is_snapshot)
    {
        "is_snapshot" => is_snapshot
    }
end

private_lane :get_version_name do |options|
    build_gradle_text = IO.read("#{lane_context[:repo_root]}/mockzilla/build.gradle.kts")
    version_pattern = /version\s*=\s*"(.*?)".*/
    version = build_gradle_text.match(version_pattern)[1]

    options[:is_snapshot] ? "#{version}-SNAPSHOT" : version
end

desc "Flutter target for the lib"
lane :flutter_lib_pull_request do
    flutter_dart_test
    flutter_android_test
end