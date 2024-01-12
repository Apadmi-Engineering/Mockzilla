platform :ios do 

    desc "iOS target for the kmm demo"
    lane :demo_kmm_pull_request do
        build_ios_app(
            project: "./demo-kmm/iosApp/iosApp.xcodeproj",
            skip_package_ipa: true,
            skip_archive: true,
            destination: "generic/platform=iOS Simulator"
        )
    end
end

platform :android do
    desc "Android target for the kmm demo"
    lane :demo_kmm_pull_request do 
        gradle(tasks: [":samples:demo-kmm:androidApp:assembleDebug"])
    end
end
