//
//  MockzillaIos.swift
//  mockzilla_ios
//
//  Created by Tom Handcock on 20/03/2024.
//

import Foundation
import Flutter
import mockzilla

extension FlutterError: Error {}

class MockzillaIos: MockzillaHostApi {
    func startServer(config: BridgeMockzillaConfig) throws -> BridgeMockzillaRuntimeParams {
        let params = MockzillaKt.startMockzilla(
            config: config.toNative(endpointMatcher: { key, request in true },
                                    defaultHandler: { key, request in Mockzilla_commonMockzillaHttpResponse(statusCode: Ktor_httpHttpStatusCode(value: 200, description: "Ok"), headers: [:], body: "")},
                                    errorHandler: { key, request in Mockzilla_commonMockzillaHttpResponse(statusCode: Ktor_httpHttpStatusCode(value: 500, description: "Internal error"), headers: [:], body: "")}
                                   )
        )
        return try BridgeMockzillaRuntimeParams.fromNative(params)
    }
    
    func stopServer() throws {
        MockzillaKt.stopMockzilla()
    }
}
