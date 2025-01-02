import Flutter
import UIKit
import mockzilla
import SwiftMockzilla

public class MockzillaIosPlugin: NSObject, FlutterPlugin {
    
    let handler: MockzillaFlutterApi
    
    init(binaryMessenger: FlutterBinaryMessenger) {
        handler = MockzillaFlutterApi(binaryMessenger: binaryMessenger)
    }
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let plugin = MockzillaIosPlugin(binaryMessenger: registrar.messenger())
        let mockzilla = MockzillaIos(handler: plugin.handler)
        MockzillaHostApiSetup.setUp(binaryMessenger: registrar.messenger(), api: mockzilla)
        registrar.publish(plugin)
    }
}
