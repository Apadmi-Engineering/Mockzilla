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

        # Scan fails to find .xcresult unless output dir explicitly defined.
        # See: https://github.com/fastlane/fastlane/issues/20012
        scan(
            package_path: "#{lane_context[:repo_root]}/SwiftMockzilla",
            scheme: "SwiftMockzilla",
            destination: "platform=iOS Simulator,name=iPhone 15 Pro Max,OS=17.5",
            result_bundle: true,
            output_directory: "#{lane_context[:repo_root]}/fastlane/test_output"
        )
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