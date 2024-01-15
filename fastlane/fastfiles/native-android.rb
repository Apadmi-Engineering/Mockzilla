platform :android do 


    desc "Android target for the pure android demo"
    lane :demo_android_pull_request do 
        gradle(
            tasks: [":samples:demo-android:assembleDebug"]
        )
    end
end
