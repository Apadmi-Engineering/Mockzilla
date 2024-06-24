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
    
    func testMockzilaHttpResponseMarshalling() throws {
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
    
    func testEndpointConfigMarshalling() throws {
        
        let endpointMatcher = { (request: MockzillaHttpRequest) in KotlinBoolean(bool: true)}
        let defaultHandler = { (request: MockzillaHttpRequest) in MockzillaHttpResponse() }
        let errorHandler = { (request: MockzillaHttpRequest) in MockzillaHttpResponse() }
        
        let nativeToBridge = [
            EndpointConfiguration(
                name: "Endpoint",
                key: "endpoint",
                failureProbability: KotlinInt(int: 50),
                delayMean: KotlinInt(int: 1000),
                delayVariance: KotlinInt(int: 20),
                endpointMatcher: endpointMatcher,
                webApiDefaultResponse: MockzillaHttpResponse(status: HttpStatusCode.OK),
                webApiErrorResponse: MockzillaHttpResponse(status: HttpStatusCode.BadRequest),
                defaultHandler: defaultHandler,
                errorHandler: errorHandler
            ) : BridgeEndpointConfig(
                name: "Endpoint",
                key: "endpoint",
                failureProbability: 50,
                delayMean: 1000,
                delayVariance: 20,
                webApiDefaultResponse: BridgeMockzillaHttpResponse(
                    statusCode: 200,
                    headers: [:],
                    body: ""
                ),
                webApiErrorResponse: BridgeMockzillaHttpResponse(
                    statusCode: 400,
                    headers: [:],
                    body: ""
                )
            )
        ]
        
        nativeToBridge.forEach { (native, bridge) in
            // From bridge to native
            let actualNative = bridge.toNative(
                endpointMatcher: { (key, request) in endpointMatcher(request) as! Bool},
                defaultHandler: { (key, request) in MockzillaHttpResponse() },
                errorHandler: { (key, request) in errorHandler(request)}
            )
            XCTAssertEqual(actualNative.key, native.key)
            XCTAssertEqual(actualNative.name, native.name)
            XCTAssertEqual(actualNative.failureProbability, native.failureProbability)
            XCTAssertEqual(actualNative.delayMean, native.delayMean)
            XCTAssertEqual(actualNative.delayVariance, native.delayVariance)
            XCTAssertEqual(actualNative.webApiDefaultResponse, native.webApiDefaultResponse)
            XCTAssertEqual(actualNative.webApiErrorResponse, native.webApiErrorResponse)
          
            // From native to bridge
            let actualBridge = BridgeEndpointConfig.fromNative(native)
            XCTAssertEqual(actualBridge.name, bridge.name)
            XCTAssertEqual(actualBridge.key, bridge.key)
            XCTAssertEqual(actualBridge.failureProbability, bridge.failureProbability)
            XCTAssertEqual(actualBridge.delayMean, bridge.delayMean)
            XCTAssertEqual(actualBridge.delayVariance, bridge.delayVariance)
            // Full test of response marshalling above
            XCTAssertEqual(actualBridge.webApiDefaultResponse?.statusCode, bridge.webApiDefaultResponse?.statusCode)
            XCTAssertEqual(actualBridge.webApiErrorResponse?.statusCode, bridge.webApiErrorResponse?.statusCode)
        }
    }
    
    func testBridgeReleaseModeConfigMarshalling() throws {
        let nativeToBridge = [
            MockzillaConfig.ReleaseModeConfig(
                rateLimit: 500,
                rateLimitRefillPeriod: 7200,
                tokenLifeSpan: 3600
            ): BridgeReleaseModeConfig(
                rateLimit: 500,
                rateLimitRefillPeriodMillis: 7200,
                tokenLifeSpanMillis: 3600
            )
        ]
        
        nativeToBridge.forEach { (native, bridge) in
            // From bridge to native
            let actualNative = bridge.toNative()
            XCTAssertEqual(actualNative.rateLimit, native.rateLimit)
            XCTAssertEqual(actualNative.rateLimitRefillPeriod, native.rateLimitRefillPeriod)
            XCTAssertEqual(actualNative.tokenLifeSpan, native.tokenLifeSpan)
            
            // From native to bridge
            let actualBridge = BridgeReleaseModeConfig.fromNative(native)
            XCTAssertEqual(actualBridge.rateLimit, bridge.rateLimit)
            XCTAssertEqual(actualBridge.rateLimitRefillPeriodMillis, bridge.rateLimitRefillPeriodMillis)
            XCTAssertEqual(actualBridge.tokenLifeSpanMillis, bridge.tokenLifeSpanMillis)
        }
    }
    
    func testMockzillaConfigMarshalling() throws {
        let native = MockzillaConfig(
            port: 8080,
            endpoints: [
                EndpointConfiguration(
                    name: "Endpoint",
                    key: "endpoint",
                    failureProbability: KotlinInt(int: 0),
                    delayMean: KotlinInt(int: 100),
                    delayVariance: KotlinInt(int: 20),
                    endpointMatcher: { (request) in true },
                    webApiDefaultResponse: MockzillaHttpResponse(),
                    webApiErrorResponse: MockzillaHttpResponse(),
                    defaultHandler: { (request) in MockzillaHttpResponse()},
                    errorHandler: { (request) in MockzillaHttpResponse()}
                )
            ],
            isRelease: false,
            localhostOnly: false,
            logLevel: MockzillaConfig.LogLevel.debug,
            releaseModeConfig: MockzillaConfig.ReleaseModeConfig(
                rateLimit: 500,
                rateLimitRefillPeriod: 3600,
                tokenLifeSpan: 7200
            ),
            additionalLogWriters: []
        )
        
        let bridge = BridgeMockzillaConfig(
            port: 8080,
            endpoints: [
                BridgeEndpointConfig(
                    name: "Endpoint",
                    key: "endpoint",
                    failureProbability: 0,
                    delayMean: 100,
                    delayVariance: 20
                )
            ],
            isRelease: false,
            localHostOnly: false,
            logLevel: BridgeLogLevel.debug,
            releaseModeConfig: BridgeReleaseModeConfig(
                rateLimit: 500,
                rateLimitRefillPeriodMillis: 3600,
                tokenLifeSpanMillis: 7200
            )
        )
        
        let actualNative = bridge.toNative(
            endpointMatcher: { (key, request) in true },
            defaultHandler: { (key, request) in MockzillaHttpResponse() },
            errorHandler: { (key, request) in MockzillaHttpResponse()}
        )
        
        let actualBridge = try BridgeMockzillaConfig.fromNative(native)
        
        // Verify from bridge to native
        XCTAssertEqual(actualNative.port, native.port)
        XCTAssertEqual(actualNative.endpoints.first?.key, native.endpoints.first?.key)
        XCTAssertEqual(actualNative.isRelease, native.isRelease)
        XCTAssertEqual(actualNative.localhostOnly, native.localhostOnly)
        XCTAssertEqual(actualNative.logLevel, native.logLevel)
        XCTAssertEqual(actualNative.releaseModeConfig, native.releaseModeConfig)
        
        // Verify from native to bridge
        XCTAssertEqual(actualBridge.port, bridge.port)
        XCTAssertEqual(actualBridge.endpoints.first??.key, bridge.endpoints.first??.key)
        XCTAssertEqual(actualBridge.isRelease, bridge.isRelease)
        XCTAssertEqual(actualBridge.localHostOnly, bridge.localHostOnly)
        XCTAssertEqual(actualBridge.logLevel, bridge.logLevel)
        XCTAssertEqual(actualBridge.releaseModeConfig.rateLimit, bridge.releaseModeConfig.rateLimit)
    }
}
