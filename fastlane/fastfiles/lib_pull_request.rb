platform :ios do

    desc "iOS target for the lib"
    lane :lib_mockzilla_pull_request do
        gradle(
            tasks: [":mockzilla-common:iosX64Test", ":mockzilla:iosX64Test"]
        )
    end

    desc "Build and test SwiftMockzilla"
    lane :lib_swift_mockzilla_pull_request do
        # Create the XCFramework
        generate_xcframework

        # If running this locally check the simulator in the command exists locally, if it doesn't, change it
        # to one that does but remember to change it back before committing changes.
        sh("cd #{lane_context[:repo_root]}/SwiftMockzilla; xcodebuild -scheme SwiftMockzilla test -destination 'platform=iOS Simulator,name=iPhone 15 Pro Max,OS=17.5'")
    end
end

platform :android do
    desc "Android target for the lib"
    lane :lib_mockzilla_pull_request do
        gradle(
            tasks: [
                ":mockzilla-common:testDebugUnitTest",
                ":mockzilla:testDebugUnitTest"
            ]
        )
    end
end

desc "Run tests for management module"
lane :lib_mockzilla_management_pull_request do
    gradle(
        tasks: [":mockzilla-management:jvmTest"]
    )
end