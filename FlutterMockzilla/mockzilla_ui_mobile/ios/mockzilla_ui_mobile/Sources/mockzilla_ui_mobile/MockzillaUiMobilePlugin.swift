import Flutter
import UIKit
import mockzilla_mobile_ui
import SwiftMockzillaMobileUi

public class MockzillaUiMobilePlugin: NSObject, FlutterPlugin {

  init(binaryMessenger: FlutterBinaryMessenger) {
    /* No-Op */
  }

  public static func register(with registrar: FlutterPluginRegistrar) {
    let plugin = MockzillaUiMobilePlugin(binaryMessenger: registrar.messenger())
    let mockzilla = MockzillaUiMobile()
    MockzillaUiMobileHostApiSetup.setUp(binaryMessenger: registrar.messenger(), api: mockzilla)
    registrar.publish(plugin)
  }
}
