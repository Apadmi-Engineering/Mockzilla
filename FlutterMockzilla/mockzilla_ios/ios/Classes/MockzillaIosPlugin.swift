import Flutter
import UIKit
import mockzilla
import SwiftMockzilla

public class MockzillaIosPlugin: NSObject, FlutterPlugin, MockzillaHostApi {
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let plugin = MockzillaIosPlugin()
            MockzillaHostApiSetup.setUp(binaryMessenger: registrar.messenger(), api: plugin)
            registrar.publish(plugin)
    }
    
    func startServer(config: BridgeMockzillaConfig) throws -> BridgeMockzillaRuntimeParams {
        print("Received call to start Mockzilla server")
        let params = MockzillaKt.startMockzilla(
            config: config.toNative(endpointMatcher: { key, request in request.uri.contains("packages") },
                                    defaultHandler: { key, request in MockzillaHttpResponse(statusCode: HttpStatusCode.OK, headers: [:], body: "")},
                                    errorHandler: { key, request in MockzillaHttpResponse(statusCode: HttpStatusCode.InternalServerError, headers: [:], body: "")}
                                   )
        )
        return try BridgeMockzillaRuntimeParams.fromNative(params)
    }

    func stopServer() throws {
        MockzillaKt.stopMockzilla()
    }
}
