require "apadmi_grout"

platform :android do 

    screenshots_output_directory = "#{Apadmi::Grout::GitUtils.git_root}/lib/mockzilla-management-ui/common/src/test/snapshots/images"

    lane :management_ui_pull_request do
        gradle(
            tasks: [
                ":mockzilla-management-ui:common:desktopTest",
                ":mockzilla-management-ui:android:bundleDebug"
            ],
            project_dir: "./lib"
        )
        update_reference_screenshots
    end

    desc "Generate screenshots and upload them"
    lane :update_reference_screenshots do
        # Compile and Test
        gradle(
            tasks: [":mockzilla-management-ui:common:recordPaparazziDebug"],
            project_dir: "./lib"
        )
        upload_screenshots
    end

    private_lane :upload_screenshots do
        file_name_prefix = "screenshots_PaparazziScreenshotTest_previewTests\["

        # Remove the prefix
        sh("cd #{screenshots_output_directory}; find . -name '*png' -exec sh -c 'mv \"$0\" \"$(dirname \"$0\")/${0\#./#{file_name_prefix}}\"' {} \\;")

        # Remove remaining ']' chars
        sh("cd #{screenshots_output_directory}; find . -name '*png' -exec sh -c 'mv \"$0\" \"$(dirname \"$0\")/${0//\]/}\"' {} \\;")

        screenshotbot_installer
        screenshotbot(
          channel: "mockzilla-management-ui",
          repo_url: ENV["GIT_REPOSITORY_URL"],
          is_pr: ENV["PR"],
          git_branch: ENV["GITHUB_REF_NAME"],
          screenshots_directory: screenshots_output_directory,
          pr_destination: ENV["GITHUB_BASE_REF"]
        )
    end
end