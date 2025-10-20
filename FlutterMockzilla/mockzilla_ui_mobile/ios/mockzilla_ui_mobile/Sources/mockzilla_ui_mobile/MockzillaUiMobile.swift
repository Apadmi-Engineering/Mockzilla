//
// Created by Sam DC on 24/09/2025.
//

import Foundation
import Flutter
import mockzillamobileui
import SwiftMockzillaMobileUi

class MockzillaUiMobile: Thread, MockzillaUiMobileHostApi {

    func launchManagementUi() {
        launchManagementUiSwift()
    }
}