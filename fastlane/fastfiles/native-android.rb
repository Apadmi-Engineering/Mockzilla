platform :android do 


    desc "Android target for the pure android demo"
    lane :demo_android_pull_request do 
        gradle(
            tasks: [":app:assembleDebug"],
            project_dir: "./demo-android"
        )
    end
end
