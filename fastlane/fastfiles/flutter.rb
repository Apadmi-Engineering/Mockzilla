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
    sh("cd #{flutter_root}; dart pub global activate melos; melos bootstrap")
    flutter_dart_test
    flutter_android_test
    flutter_ios_test
end

desc "Executes Dart unit tests"
private_lane :flutter_dart_test do
    sh("cd #{flutter_root}; melos run test:all")
end

desc "Executes Android unit tests"
private_lane :flutter_android_test do
    sh("cd #{flutter_root}; melos run buildConfig:android")
    gradle(
        project_dir: "#{flutter_root}/mockzilla_android/example/android",
        task: "testDebugUnitTest"
    )
end

desc "Executes iOS unit tests"
private_lane :flutter_ios_test do
    sh("cd #{flutter_root}; melos run buildConfig:ios")
    scan(
        workspace: "#{flutter_root}/mockzilla_ios/example/ios/Runner.xcworkspace",
        scheme: "Runner",
        configuration: "Debug"
    )
end

lane :demo_flutter_pull_request do
    sh("cd #{flutter_root}; melos run buildExample")
end