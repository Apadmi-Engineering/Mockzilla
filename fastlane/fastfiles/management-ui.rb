require "apadmi_grout"

platform :ios do
    desc "Generate Podspec"
    private_lane :generate_mobile_ui_framework_and_podspec do |options|
        gradle(
            tasks: [":mockzilla-management-ui:mockzilla-mobile-ui:podPublishReleaseXCFramework"],
            properties: createMobileUiSnapshotProp(options[:is_snapshot], get_mobile_ui_version_name(options))
        )

        # Copy the Podspec to where the publish lane can find it
        sh("cp -rf #{lane_context[:repo_root]}/mockzilla-management-ui/mockzilla-mobile-ui/build/cocoapods/publish/release/mockzillamobileui.xcframework #{lane_context[:repo_root]}/SwiftMockzillaMobileUi")
        sh("cp -rf #{lane_context[:repo_root]}/mockzilla-management-ui/mockzilla-mobile-ui/build/cocoapods/publish/release/SwiftMockzillaMobileUi.podspec #{lane_context[:repo_root]}/SwiftMockzillaMobileUi")

        # Remove the resources line from Podspec since it causes it to fail validation (and it's not used)
        file_path = "#{lane_context[:repo_root]}/SwiftMockzillaMobileUi/SwiftMockzillaMobileUi.podspec"
        filtered_lines = File.readlines(file_path).reject do |line|
          line.include?("spec.resources = ")
        end

        File.open(file_path, "w") do |file|
          file.puts(filtered_lines)
        end
    end

    desc "Deploy the package to github & push podspec"
    lane :publish_mobile_ui_swift_package do |options|
        generate_mobile_ui_framework_and_podspec(is_snapshot: options[:is_snapshot])

        sh("rm -rf apadmi-mockzilla-mobile-ui-ios")

        if !options[:is_snapshot]
            sh("git clone #{ENV["IOS_MOBILE_UI_DEPLOY_URL"]} apadmi-mockzilla-mobile-ui-ios")
        else
            sh("git clone -b deployment/snapshot #{ENV["IOS_MOBILE_UI_DEPLOY_URL"]} apadmi-mockzilla-mobile-ui-ios")
        end

        sh(%{
            cd apadmi-mockzilla-mobile-ui-ios;
            rm -rf ./*;
            cp -r #{lane_context[:repo_root]}/SwiftMockzillaMobileUi/ .;

            git add .;
            git add --force mockzillamobileui.xcframework;
            git add --force SwiftMockzillaMobileUi.podspec;
            git commit -m "Updating Package #{get_mobile_ui_version_name(options)}";
            git push;
        })

        if !options[:is_snapshot]
            sh(%{
                cd apadmi-mockzilla-mobile-ui-ios;
                git push
                git tag v#{get_mobile_ui_version_name(options)}
                git push --tags
            })

            # Push podspec to trunk
            sh(%{
                cd apadmi-mockzilla-mobile-ui-ios
                pod trunk push
            })
        end
    end
end

desc "Publish to maven local"
lane :publish_mobile_ui_to_maven_local do |options|
    gradle(
        tasks: [
            ":mockzilla-management-ui:mockzilla-management-ui-common:publishToMavenLocal",
            ":mockzilla-management-ui:mockzilla-mobile-ui:publishToMavenLocal"
        ],
        properties: createMobileUiSnapshotProp(options[:is_snapshot], get_mobile_ui_version_name(options)) 
    )
end

desc "Publish to maven remote"
lane :publish_mobile_ui_to_maven do |options|
    # Dry run
    publish_mobile_ui_to_maven_local(is_snapshot: options[:is_snapshot])
    FastlaneCore::UI.success("Published to maven local")

    FastlaneCore::UI.message("Publishing to remote")
    gradle(
        tasks: [
            ":mockzilla-management-ui:mockzilla-management-ui-common:publishToMavenCentral",
            ":mockzilla-management-ui:mockzilla-mobile-ui:publishToMavenCentral"
        ],
        properties: createMobileUiSnapshotProp(options[:is_snapshot], get_mobile_ui_version_name(options))
    )
end

desc "Generate JavaScript artifacts"
lane :build_mobile_ui_js_artifacts do |options|
    gradle(
        tasks: [
            # Production build currently doesn't work, using development temporarily
            ":mockzilla-management-ui:mockzilla-mobile-ui:jsBrowserDevelopmentWebpack",
        ],
        properties: createMobileUiSnapshotProp(options[:is_snapshot], get_mobile_ui_version_name(options))
    )
end

def createMobileUiSnapshotProp(is_snapshot, version)
    {
        "is_snapshot" => is_snapshot,
        "version" => version,
        "is_building_for_deployment" => true
    }
end

private_lane :get_mobile_ui_version_name do |options|
    build_gradle_text = IO.read("#{lane_context[:repo_root]}/mockzilla-management-ui/mockzilla-mobile-ui/build.gradle.kts")
    version_pattern = /.*"(.*?)" \/\/ x-release-please-version/
    version = build_gradle_text.match(version_pattern)[1]

    options[:is_snapshot] ? "#{version}-SNAPSHOT" : version
end

desc "During deployment we don't use local packages, but for deployment we have to, so run tests against the deployed core binaries"
lane :management_ui_pre_deploy_checks do |options|
    gradle(
        tasks: [
            ":mockzilla-management-ui:mockzilla-management-ui-common:desktopTest",
            ":mockzilla-management-ui:mockzilla-mobile-ui:testDebugUnitTest"
        ],
        properties: createMobileUiSnapshotProp(options[:is_snapshot], get_mobile_ui_version_name(options))
    )
end

lane :management_ui_pull_request do
    gradle(
        tasks: [
            ":mockzilla-management-ui:mockzilla-desktop:assembleDebug",
            ":mockzilla-management-ui:mockzilla-desktop:desktopTest",
            ":mockzilla-management-ui:mockzilla-management-ui-common:desktopTest",
            ":mockzilla-management-ui:mockzilla-management-ui-common:jsBrowserTest",
            ":mockzilla-management-ui:mockzilla-mobile-ui:testDebugUnitTest",
            ":mockzilla-management-ui:mockzilla-mobile-ui:jsBrowserTest"
        ]
    )
end
