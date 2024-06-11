def flutter_directory
    "FlutterMockzilla"
end

def fastlane_directory
    "fastlane"
end

def flutter_root
    "#{lane_context[:repo_root]}/#{flutter_directory}"
end

desc "Flutter target for the lib"
lane :flutter_lib_pull_request do
    flutter_dart_test
    flutter_android_test
end

desc "Executes Dart unit tests"
private_lane :flutter_dart_test do
    # Currently, unit tests are only present in `mockzilla_android`.
    sh("cd #{flutter_root}/mockzilla_android; flutter test")
end

desc "Executes Android unit tests"
private_lane :flutter_android_test do
    sh("cd #{flutter_root}/mockzilla_android/example; flutter build apk --config-only")
    gradle(
        project_dir: "#{flutter_root}/mockzilla_android/example/android",
        task: "testDebugUnitTest"
    )
end

lane :demo_flutter_pull_request do
    sh("cd #{flutter_root}/mockzilla/example; dart run build_runner build")
    sh("cd #{flutter_root}/mockzilla/example; flutter build apk")
end