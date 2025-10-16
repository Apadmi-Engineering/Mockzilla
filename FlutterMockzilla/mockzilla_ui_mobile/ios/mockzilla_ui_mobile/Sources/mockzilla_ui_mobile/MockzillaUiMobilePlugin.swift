import Flutter
import UIKit
import mockzillamobileui
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
