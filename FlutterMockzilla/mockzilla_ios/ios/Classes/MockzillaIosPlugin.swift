import Flutter
import UIKit
import mockzilla

public class MockzillaIosPlugin: NSObject, FlutterPlugin {
    
  public static func register(with registrar: FlutterPluginRegistrar) {
    let plugin = MockzillaIos()
      MockzillaHostApiSetup.setUp(binaryMessenger: registrar.messenger(), api: plugin)
  }
}
