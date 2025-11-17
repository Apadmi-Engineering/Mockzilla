//
//  BridgeUtilsTests.swift
//  RunnerTests
//
//  Created by Tom Handcock on 13/06/2024.
//

import Foundation
import XCTest
@testable import mockzilla_ios
@testable import SwiftMockzilla
@testable import mockzilla

class BridgeUtilsTests: XCTestCase {
    
    func testHttpMethodMarshalling() throws {
        let bridgeToNative = [
            BridgeHttpMethod.delete: Ktor_httpHttpMethod(value: "DELETE"),
            BridgeHttpMethod.get: Ktor_httpHttpMethod(value: "GET"),
            BridgeHttpMethod.head: Ktor_httpHttpMethod(value: "HEAD"),
            BridgeHttpMethod.options: Ktor_httpHttpMethod(value: "OPTIONS"),
            BridgeHttpMethod.patch: Ktor_httpHttpMethod(value: "PATCH"),
            BridgeHttpMethod.post: Ktor_httpHttpMethod(value: "POST"),
            BridgeHttpMethod.put: Ktor_httpHttpMethod(value: "PUT")
        ]
        
        try bridgeToNative.forEach { (bridge, native) in
            XCTAssertEqual(bridge.toNative(), native)
            XCTAssertEqual(try BridgeHttpMethod.fromNative(native), bridge)
        }
    }
    
    func testLogLevelMarshalling() throws {
        let bridgeToNative = [
            BridgeLogLevel.assertion: MockzillaConfig.LogLevel.assert,
            BridgeLogLevel.debug: MockzillaConfig.LogLevel.debug,
            BridgeLogLevel.error: MockzillaConfig.LogLevel.error,
            BridgeLogLevel.info: MockzillaConfig.LogLevel.info,
            BridgeLogLevel.verbose: MockzillaConfig.LogLevel.verbose,
            BridgeLogLevel.warn: MockzillaConfig.LogLevel.warn
        ]
        
        try bridgeToNative.forEach { (bridge, native) in
            XCTAssertEqual(bridge.toNative(), native)
            XCTAssertEqual(try BridgeLogLevel.fromNative(native), bridge)
        }
    }
    
    func testMockzillaHttpResponseMarshalling() throws {
        let nativeToBridge = [
            MockzillaHttpResponse(
                status: HttpStatusCode.OK,
                headers: ["Authorisation": "Bearer token"],
                body: "Body"
            ): BridgeMockzillaHttpResponse(
                statusCode: 200,
                headers: ["Authorisation": "Bearer token"],
                body: "Body"
            )
        ]
        
        nativeToBridge.forEach { (native, bridge) in
            // From bridge to native
            XCTAssertEqual(bridge.toNative(), native)
            
            // From native to bridge
            let actualBridge = BridgeMockzillaHttpResponse.fromNative(native)
            XCTAssertEqual(actualBridge.statusCode, bridge.statusCode)
            XCTAssertEqual(actualBridge.headers, bridge.headers)
            XCTAssertEqual(actualBridge.body, bridge.body)
        }
    }
    
    func testDashboardOverridePresetMarshalling() throws {
        let nativeToBridge = [
            Mockzilla_commonDashboardOverridePreset(
                name: "Default response",
                description: nil,
                response: MockzillaHttpResponse(
                    statusCode: HttpStatusCode.OK,
                    headers: ["content-type": "text/plain"],
                    body: "Hello world"
                )
            ): BridgeDashboardOverridePreset(
                name: "Default response",
                response: BridgeMockzillaHttpResponse(
                    statusCode: 200,
                    headers: ["content-type": "text/plain"],
                    body: "Hello world"
                )
            ),
            Mockzilla_commonDashboardOverridePreset(
                name: "Error response",
                description: "Unauthorized response",
                response: MockzillaHttpResponse(
                    statusCode: HttpStatusCode.Unauthorized,
                    headers: ["content-type":"application/json"],
                    body: ""
                )
            ): BridgeDashboardOverridePreset(
                name: "Error response",
                description: "Unauthorized response",
                response: BridgeMockzillaHttpResponse(
                    statusCode: 401,
                    headers: ["content-type":"application/json"],
                    body: ""
                )
            )
        ]
        
        nativeToBridge.forEach { (native, bridge) in
            XCTAssertEqual(bridge.toNative(), native)
            
            let actualBridge = BridgeDashboardOverridePreset.fromNative(native)
            XCTAssertEqual(actualBridge.name, bridge.name)
            XCTAssertEqual(actualBridge.description, bridge.description)
            XCTAssertEqual(actualBridge.response.statusCode, bridge.response.statusCode)
            XCTAssertEqual(actualBridge.response.body, bridge.response.body)
        }
    }
    
    func testDashboardOptionsConfigMarshalling() throws {
        let nativeToBridge = [
            Mockzilla_commonDashboardOptionsConfig(
                errorPresets: [
                    Mockzilla_commonDashboardOverridePreset(
                        name: "Error response",
                        description: nil,
                        response: MockzillaHttpResponse(
                            status: HttpStatusCode.InternalServerError,
                            body: ""
                        )
                    )
                ],
                successPresets: [
                    Mockzilla_commonDashboardOverridePreset(
                        name: "Default response",
                        description: "Description",
                        response: MockzillaHttpResponse(
                            status: HttpStatusCode.OK,
                            body: ""
                        )
                    )
                ]
            ): BridgeDashboardOptionsConfig(
                successPresets: [
                    BridgeDashboardOverridePreset(
                        name: "Default response",
                        description: "Description",
                        response: BridgeMockzillaHttpResponse(
                            statusCode: 200,
                            headers: [:],
                            body: ""
                        )
                    )
                ],
                errorPresets: [
                    BridgeDashboardOverridePreset(
                        name: "Error response",
                        description: nil,
                        response: BridgeMockzillaHttpResponse(
                            statusCode: 500,
                            headers: [:],
                            body: ""
                        )
                    )
                ]
            )
        ]
        
        nativeToBridge.forEach { (native, bridge) in
            XCTAssertEqual(bridge.toNative(), native)
            
            let actualBridge = BridgeDashboardOptionsConfig.fromNative(native)
            XCTAssertEqual(actualBridge.successPresets.count, bridge.successPresets.count)
            XCTAssertEqual(actualBridge.successPresets.first?.name, bridge.successPresets.first?.name)
            XCTAssertEqual(actualBridge.errorPresets.count, bridge.errorPresets.count)
            XCTAssertEqual(actualBridge.errorPresets.first?.name, bridge.errorPresets.first?.name)
        }
    }
    
    func testEndpointConfigMarshalling() throws {
        
        let endpointMatcher = { (key: String, request: MockzillaHttpRequest) in true}
        let defaultHandler = { (key: String, request: MockzillaHttpRequest) in MockzillaHttpResponse() }
        let errorHandler = { (key: String, request: MockzillaHttpRequest) in MockzillaHttpResponse() }
        
        let nativeToBridge = [
            EndpointConfiguration(
                name: "Endpoint",
                key: "endpoint",
                shouldFail: false,
                delay: KotlinInt(int: 1000),
                dashboardOptionsConfig: Mockzilla_commonDashboardOptionsConfig(
                    errorPresets: [], successPresets: []
                ),
                versionCode: 1,
                endpointMatcher: SwiftEndpointMatcher(key: "endpoint", block: endpointMatcher),
                defaultHandler: SwiftHandler(key: "endpoint", block: defaultHandler),
                errorHandler: SwiftHandler(key: "endpoint", block: errorHandler)
            ) : BridgeEndpointConfig(
                name: "Endpoint",
                key: "endpoint",
                shouldFail: false,
                delayMs: 1000,
                versionCode: 1,
                config: BridgeDashboardOptionsConfig(successPresets: [], errorPresets: [])
            )
        ]
        
        nativeToBridge.forEach { (native, bridge) in
            // From bridge to native
            let actualNative = bridge.toNative(
                endpointMatcher: endpointMatcher,
                defaultHandler: defaultHandler,
                errorHandler: errorHandler
            )
            XCTAssertEqual(actualNative.key as! String, native.key as! String)
            XCTAssertEqual(actualNative.name, native.name)
            XCTAssertEqual(actualNative.shouldFail, native.shouldFail)
            XCTAssertEqual(actualNative.delay, native.delay)
            XCTAssertEqual(actualNative.dashboardOptionsConfig, native.dashboardOptionsConfig)
            XCTAssertEqual(actualNative.versionCode, native.versionCode)
          
            // From native to bridge
            let actualBridge = BridgeEndpointConfig.fromNative(native)
            XCTAssertEqual(actualBridge.name, bridge.name)
            XCTAssertEqual(actualBridge.key, bridge.key)
            XCTAssertEqual(actualBridge.shouldFail, bridge.shouldFail)
            XCTAssertEqual(actualBridge.delayMs, bridge.delayMs)
            XCTAssertEqual(actualBridge.versionCode, bridge.versionCode)
            XCTAssertEqual(actualBridge.config.successPresets.first?.name, bridge.config.successPresets.first?.name)
            XCTAssertEqual(actualBridge.config.errorPresets.first?.name, bridge.config.errorPresets.first?.name)
        }
    }
    
    func testMockzillaConfigMarshalling() throws {
        let endpointMatcher = { (key: String, request: MockzillaHttpRequest) in true}
        let defaultHandler = { (key: String, request: MockzillaHttpRequest) in MockzillaHttpResponse() }
        let errorHandler = { (key: String, request: MockzillaHttpRequest) in MockzillaHttpResponse() }

        let native = MockzillaConfig(
            port: 8080,
            endpoints: [
                EndpointConfiguration(
                    name: "Endpoint",
                    key: "endpoint",
                    shouldFail: false,
                    delay: KotlinInt(int: 100),
                    dashboardOptionsConfig: Mockzilla_commonDashboardOptionsConfig(errorPresets: [], successPresets: []),
                    versionCode: 1,
                    endpointMatcher: SwiftEndpointMatcher(key: "endpoint", block: endpointMatcher),
                    defaultHandler: SwiftHandler(key: "endpoint", block: defaultHandler),
                    errorHandler: SwiftHandler(key: "endpoint", block: errorHandler)
                )
            ],
            isRelease: false,
            localhostOnly: false,
            logLevel: MockzillaConfig.LogLevel.debug,
            releaseModeConfig: MockzillaConfig.ReleaseModeConfig(
                rateLimit: 60,
                rateLimitRefillPeriod: 60_000,
                tokenLifeSpan: 500
            ),
            isNetworkDiscoveryEnabled: false,
            additionalLogWriters: []
        )
        
        let bridge = BridgeMockzillaConfig(
            port: 8080,
            endpoints: [
                BridgeEndpointConfig(
                    name: "Endpoint",
                    key: "endpoint",
                    shouldFail: false,
                    delayMs: 1000,
                    versionCode: 1,
                    config: BridgeDashboardOptionsConfig(successPresets: [], errorPresets: [])
                )
            ],
            localHostOnly: false,
            logLevel: BridgeLogLevel.debug,
            isNetworkDiscoveryEnabled: false
        )
        
        let actualNative = bridge.toNative(
            endpointMatcher: { (key, request) in true },
            defaultHandler: { (key, request) in MockzillaHttpResponse() },
            errorHandler: { (key, request) in MockzillaHttpResponse()},
            proxyLogger: DummyProxyMockzillaLogger(
                flutterApi: DummyFlutterApi(
                    binaryMessenger: DummyFlutterBinaryMessenger()
                )
            )
        )
        
        let actualBridge = try BridgeMockzillaConfig.fromNative(native)
        
        // Verify from bridge to native
        XCTAssertEqual(actualNative.port, native.port)
        XCTAssertEqual(actualNative.endpoints.first?.key as! String, native.endpoints.first?.key as! String)
        XCTAssertEqual(actualNative.isRelease, native.isRelease)
        XCTAssertEqual(actualNative.localhostOnly, native.localhostOnly)
        XCTAssertEqual(actualNative.logLevel, native.logLevel)
        XCTAssertEqual(actualNative.releaseModeConfig, native.releaseModeConfig)
        
        // Verify from native to bridge
        XCTAssertEqual(actualBridge.port, bridge.port)
        XCTAssertEqual(actualBridge.endpoints.first?.key, bridge.endpoints.first?.key)
        XCTAssertEqual(actualBridge.localHostOnly, bridge.localHostOnly)
        XCTAssertEqual(actualBridge.logLevel, bridge.logLevel)
    }
}
