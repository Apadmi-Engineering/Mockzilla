//
//  ProxyMockzillaLogger.swift
//  Pods
//
//  Created by Tom Handcock on 03/06/2025.
//
import mockzilla
import SwiftMockzilla

class ProxyMockzillaLogger: MockzillaLogWriter {
    let flutterApi: MockzillaFlutterApi
    
    init(flutterApi: MockzillaFlutterApi) {
        self.flutterApi = flutterApi
    }
    
    func log(logLevel: MockzillaLogLevel, message: String, tag: String, throwable: KotlinThrowable?) {
        do {
            try flutterApi.log(
                logLevel: BridgeLogLevel.fromNative(logLevel),
                message: message as String,
                tag: tag as String,
                exception: throwable?.message,
                completion: { localResult in
                    // Intentionally blank.
                })
        } catch {
            // Intentionally blank as this is a fire and forget call.
        }
    }
}
