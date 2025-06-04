//
//  DummyMockzillaLogger.swift
//  Runner
//
//  Created by Tom Handcock on 03/06/2025.
//
@testable import mockzilla_ios
@testable import SwiftMockzilla
@testable import mockzilla
import Flutter

final class DummyFlutterBinaryMessenger: NSObject, FlutterBinaryMessenger {
        
    func send(onChannel channel: String, message: Data?) {
        // Intentionally blank
    }
    
    func send(onChannel channel: String, message: Data?, binaryReply callback: FlutterBinaryReply? = nil) {
        // Intentionally blank
    }
    
    func setMessageHandlerOnChannel(_ channel: String, binaryMessageHandler handler: FlutterBinaryMessageHandler? = nil) -> FlutterBinaryMessengerConnection {
        return FlutterBinaryMessengerConnection()
    }
    
    func cleanUpConnection(_ connection: FlutterBinaryMessengerConnection) {
        // Intentionally blank
    }
    
}

class DummyFlutterApi: MockzillaFlutterApi {
    // Intentionally blank
}

class DummyProxyMockzillaLogger: ProxyMockzillaLogger {
    // Intentionally blank
}
