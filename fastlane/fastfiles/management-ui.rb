require "apadmi_grout"

platform :android do 

    lane :management_ui_pull_request do
        gradle(
            tasks: [
                ":mockzilla-management-ui:desktopTest",
                ":mockzilla-management-ui:bundleDebug"
            ]
        )
    end
end