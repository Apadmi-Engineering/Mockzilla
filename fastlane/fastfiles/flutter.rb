def fastlane_directory
    "#{lane_context[:repo_root]}/fastlane"
end

def flutter_directory
    "#{lane_context[:repo_root]}/FlutterMockzilla"
end

desc "Returns whether Flutter lanes should run due to either the Flutter "
desc "source changing or Fastlane config changing"
lane :should_flutter_run do
    changed_files = sh("cd #{flutter_directory}; git --no-pager diff --name-only HEAD $(git merge-base develop HEAD)")
    flutter_source_changed = changed_files.include? flutter_directory
    fastlane_changed = changed_files.include? fastlane_directory
    puts flutter_source_changed || fastlane_changed
end

desc "Installs Flutter SDK and project dependencies"
lane :flutter_setup do
    flutter_bootstrap(flutter_channel: 'stable')
    sh("cd #{flutter_directory}/mockzilla; flutter pub get")
    sh("cd #{flutter_directory}/mockzilla_platform_interface; flutter pub get")
    sh("cd #{flutter_directory}/mockzilla_android; flutter pub get")
end

desc "Executes Dart unit tests"
lane :flutter_dart_test do
    # Currently, unit tests are only present in `mockzilla_android`.
    sh("cd #{flutter_directory}/mockzilla_android; flutter test")
end

desc "Executes Android unit tests"
lane :flutter_android_test do
    sh("cd #{flutter_directory}/mockzilla_android/example; flutter build apk --config-only")
    gradle(
        project_dir: "#{flutter_directory}/mockzilla_android/example/android",
        task: "testDebugUnitTest"
    )
end

lane :demo_flutter_pull_request do
    sh("cd #{flutter_directory}/mockzilla/example; dart run build_runner build")
    sh("cd #{flutter_directory}/mockzilla/example; flutter build apk")
end