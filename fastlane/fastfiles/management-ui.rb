require "apadmi_grout"

platform :android do 

    screenshots_output_directory = "#{Apadmi::Grout::GitUtils.git_root}/lib/mockzilla-management-ui/src/test/snapshots/images"

    desc "Android target for the lib"
    lane :management_ui_pull_request do
        gradle(
            tasks: [
                ":mockzilla-management-ui:recordPaparazziDebug",
                ":mockzilla-management-ui:bundleDebug"
            ],
            project_dir: "./lib"
        )
        upload_screenshots
    end

    desc "Generate screenshots and upload them"
    lane :update_reference_screenshots do
        # Compile and Test
        gradle(
            tasks: [":mockzilla-management-ui:recordPaparazziDebug"],
            project_dir: "./lib"
        )
        upload_screenshots
    end

    private_lane :upload_screenshots do
        file_name_prefix = "screenshots_PaparazziScreenshotTest_previewTests\["
        file_name_suffix = "\]"
        sh("find #{screenshots_output_directory} -name '*png' -exec bash -c 'mv $0 ${0/#{file_name_prefix}/}' {} \\;")
        sh("find #{screenshots_output_directory} -name '*png' -exec bash -c 'mv $0 ${0/#{file_name_suffix}/}' {} \\;")

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