import mockzillamobileui
import UIKit

public func launchManagementUiSwift() {
    Launcher_iosKt.launchManagementUi()
}

public func createManagementUiViewControllerSwift(onClose: @escaping () -> Void) -> UIViewController {
    return Launcher_iosKt.createManagementUiViewController(onClose: onClose)
}
