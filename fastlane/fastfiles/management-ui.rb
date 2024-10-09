require "apadmi_grout"

platform :android do 

    screenshots_output_directory = "#{Apadmi::Grout::GitUtils.git_root}/mockzilla-management-ui/common/src/test/snapshots/images"

    lane :management_ui_pull_request do
        gradle(
            tasks: [
                ":mockzilla-management-ui:desktopTest",
                ":mockzilla-management-ui:bundleDebug"
            ]
        )
    end
end