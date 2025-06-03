import Flutter
import UIKit
import mockzilla
import SwiftMockzilla

public class MockzillaIosPlugin: NSObject, FlutterPlugin {
    
    let handler: MockzillaFlutterApi
    let proxyLogger: ProxyMockzillaLogger
    
    init(binaryMessenger: FlutterBinaryMessenger) {
        handler = MockzillaFlutterApi(binaryMessenger: binaryMessenger)
        proxyLogger = ProxyMockzillaLogger(flutterApi: handler)
    }
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let plugin = MockzillaIosPlugin(binaryMessenger: registrar.messenger())
        let mockzilla = MockzillaIos(handler: plugin.handler, proxyLogger: plugin.proxyLogger)
        MockzillaHostApiSetup.setUp(binaryMessenger: registrar.messenger(), api: mockzilla)
        registrar.publish(plugin)
    }
}
