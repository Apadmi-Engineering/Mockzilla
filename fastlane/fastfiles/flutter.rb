def flutter_directory
    "FlutterMockzilla"
end

def fastlane_directory
    "fastlane"
end

def flutter_root
    "#{lane_context[:repo_root]}/#{flutter_directory}"
end

desc "Returns whether Flutter lanes should run due to either the Flutter "
desc "source changing or Fastlane config changing"
lane :should_flutter_run do
    sh("git fetch origin develop:develop")
    changed_files = sh("cd #{flutter_root}; git --no-pager diff --name-only HEAD $(git merge-base develop HEAD)")
    flutter_source_changed = changed_files.include? flutter_directory
    fastlane_changed = changed_files.include? fastlane_directory
    flutter_source_changed || fastlane_changed
end

desc "Executes Dart unit tests"
lane :flutter_dart_test do
    # Currently, unit tests are only present in `mockzilla_android`.
    sh("cd #{flutter_root}/mockzilla_android; flutter test")
end

desc "Executes Android unit tests"
lane :flutter_android_test do
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