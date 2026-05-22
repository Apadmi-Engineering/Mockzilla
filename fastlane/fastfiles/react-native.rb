desc "Lint and typecheck React Native package"
lane :react_native_lint_pull_request do
    root = "#{lane_context[:repo_root]}/ReactNativeMockzilla"
    sh("cd #{root} && yarn lint")
    sh("cd #{root} && yarn typecheck")
    sh("cd #{root} && yarn test")
end

desc "Build React Native library"
lane :react_native_library_pull_request do
    root = "#{lane_context[:repo_root]}/ReactNativeMockzilla"
    sh("cd #{root} && yarn prepare")
end

platform :android do
    desc "Build React Native example for Android"
    lane :react_native_pull_request do
        root = "#{lane_context[:repo_root]}/ReactNativeMockzilla"
        sh("cd #{root} && JAVA_OPTS='-XX:MaxHeapSize=6g' yarn turbo run build:android")
    end
end

platform :ios do
    desc "Build React Native example for iOS"
    lane :react_native_pull_request do
        root = "#{lane_context[:repo_root]}/ReactNativeMockzilla"
        sh("cd #{root}/example && bundle install && bundle exec pod install --project-directory=ios")
        sh("cd #{root} && yarn turbo run build:ios")
    end
end

desc "Build and publish react-native-mockzilla to npm"
lane :react_native_publish do
    root = "#{lane_context[:repo_root]}/ReactNativeMockzilla"
    sh("cd #{root} && yarn prepare")
    sh("cd #{root} && yarn npm publish --access public")
end
