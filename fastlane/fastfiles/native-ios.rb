platform :ios do 

    desc "iOS target for the pure iOS demo"
    lane :demo_ios_pull_request do
        build_ios_app(
            project: "./demo-ios/demo-ios.xcodeproj",
            skip_package_ipa: true,
            skip_archive: true,
            destination: "generic/platform=iOS Simulator"
        )
    end
end
