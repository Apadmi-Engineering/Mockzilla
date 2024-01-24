flutter_directory = "#{lane_context[:repo_root]}/FlutterMockzilla"
main_package_root = "#{flutter_directory}/mockziila"
platform_interface_root = "#{flutter_directory}/mockzilla_platform_interface"
android_plugin_root = "#{flutter_directory}/mockzilla_android"

lane :flutter_setup do
    flutter_bootstrap(flutter_channel: 'stable')
    sh("cd #{main_package_root}; flutter pub get")
    sh("cd #{platform_interface_root}; flutter pub get")
    sh("cd #{android_plugin_root}; flutter pub get")
end

desc "Executes Dart unit tests"
lane :flutter_dart_test do
    sh("cd #{main_package_root}; flutter test")
    sh("cd #{platform_interface_root}; flutter test")
    sh("cd #{android_plugin_root}; flutter test")
end

desc "Executes Android unit tests"
lane :flutter_android_test do
    gradle(
        project_dir: "#{android_plugin_root}/android",
        task: "test"
    )
end