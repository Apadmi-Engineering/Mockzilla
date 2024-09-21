//
//  BridgeUtils.swift
//  mockzilla_ios
//
//  Created by Tom Handcock on 20/03/2024.
//

import Foundation
import mockzilla
import SwiftMockzilla

enum MockzillaError: Error {
    case argumentError
}

extension BridgeHttpMethod {
    func toNative() -> Ktor_httpHttpMethod {
        return switch self {
        case BridgeHttpMethod.get: Ktor_httpHttpMethod(value: "GET")
        case BridgeHttpMethod.head: Ktor_httpHttpMethod(value: "HEAD")
        case BridgeHttpMethod.delete: Ktor_httpHttpMethod(value: "DELETE")
        case BridgeHttpMethod.options: Ktor_httpHttpMethod(value: "OPTIONS")
        case BridgeHttpMethod.patch: Ktor_httpHttpMethod(value: "PATCH")
        case BridgeHttpMethod.post: Ktor_httpHttpMethod(value: "POST")
        case BridgeHttpMethod.put: Ktor_httpHttpMethod(value: "PUT")
        }
    }
    
    static func fromNative(_ httpMethod: Ktor_httpHttpMethod) throws -> BridgeHttpMethod {
        return switch(httpMethod.value) {
        case "GET": BridgeHttpMethod.get
        case "HEAD": BridgeHttpMethod.head
        case "DELETE": BridgeHttpMethod.delete
        case "OPTIONS": BridgeHttpMethod.options
        case "PATCH": BridgeHttpMethod.patch
        case "POST": BridgeHttpMethod.post
        case "PUT": BridgeHttpMethod.put
        default: throw MockzillaError.argumentError
        }
    }
}

extension BridgeLogLevel {
    func toNative() -> MockzillaConfig.LogLevel {
        return switch self {
        case BridgeLogLevel.assertion: MockzillaConfig.LogLevel.assert
        case BridgeLogLevel.debug: MockzillaConfig.LogLevel.debug
        case BridgeLogLevel.error: MockzillaConfig.LogLevel.error
        case BridgeLogLevel.info: MockzillaConfig.LogLevel.info
        case BridgeLogLevel.verbose: MockzillaConfig.LogLevel.verbose
        case BridgeLogLevel.warn: MockzillaConfig.LogLevel.warn
        }
    }
    
    static func fromNative(_ logLevel: MockzillaConfig.LogLevel) throws -> BridgeLogLevel {
        return switch(logLevel) {
        case MockzillaConfig.LogLevel.info: BridgeLogLevel.info
        case MockzillaConfig.LogLevel.debug: BridgeLogLevel.debug
        case MockzillaConfig.LogLevel.assert: BridgeLogLevel.assertion
        case MockzillaConfig.LogLevel.verbose: BridgeLogLevel.verbose
        case MockzillaConfig.LogLevel.warn: BridgeLogLevel.warn
        case MockzillaConfig.LogLevel.error: BridgeLogLevel.error
        default: throw MockzillaError.argumentError
        }
    }
}

extension BridgeMockzillaHttpRequest {
    static func fromNative(_ request: MockzillaHttpRequest) throws -> BridgeMockzillaHttpRequest {
        return try BridgeMockzillaHttpRequest(
            uri: request.uri,
            headers: request.headers,
            body: request.bodyAsString(),
            method: BridgeHttpMethod.fromNative(request.method)
        )
    }
}

extension BridgeMockzillaHttpResponse {
    func toNative() -> MockzillaHttpResponse {
        return MockzillaHttpResponse(
            statusCode: Ktor_httpHttpStatusCode.init(value: Int32(self.statusCode), description: ""),
            headers: DictionaryUtils.removeNils(self.headers),
            body: self.body
        )
    }
    
    static func fromNative(_ response: MockzillaHttpResponse) -> BridgeMockzillaHttpResponse {
        return BridgeMockzillaHttpResponse(
            statusCode: Int64(response.statusCode.value),
            headers: response.headers,
            body: response.body
        )
    }
}

extension BridgeDashboardOverridePreset {
    func toNative() -> Mockzilla_commonDashboardOverridePreset {
        return Mockzilla_commonDashboardOverridePreset(
            name: name,
            description: description,
            response: response.toNative()
        )
    }
    
    static func fromNative(_ data: Mockzilla_commonDashboardOverridePreset) -> BridgeDashboardOverridePreset {
        return BridgeDashboardOverridePreset(
            name: data.name,
            description: data.description_,
            response: BridgeMockzillaHttpResponse.fromNative(data.response)
        )
    }
}

extension BridgeDashboardOptionsConfig {
    func toNative() -> Mockzilla_commonDashboardOptionsConfig {
        return Mockzilla_commonDashboardOptionsConfig(
            errorPresets: errorPresets.map {
                preset in preset?.toNative()
            }.filter {
                preset in preset != nil
            } as! Array<Mockzilla_commonDashboardOverridePreset>,
            successPresets: successPresets.map {
                preset in preset?.toNative()
            }.filter {
                preset in preset != nil
            } as! Array<Mockzilla_commonDashboardOverridePreset>
        )
    }
    
    static func fromNative(_ data: Mockzilla_commonDashboardOptionsConfig) -> BridgeDashboardOptionsConfig {
        return BridgeDashboardOptionsConfig(
            successPresets: data.successPresets.map {
                it in BridgeDashboardOverridePreset.fromNative(it)
            },
            errorPresets: data.errorPresets.map {
                it in BridgeDashboardOverridePreset.fromNative(it)
            }
        )
    }
}

extension BridgeEndpointConfig {
    func toNative(
        endpointMatcher: @escaping (_ key: String, _ request: MockzillaHttpRequest) -> Bool,
        defaultHandler: @escaping (_ key: String, _ request: MockzillaHttpRequest) -> MockzillaHttpResponse,
        errorHandler: @escaping (_ key: String, _ request: MockzillaHttpRequest) -> MockzillaHttpResponse
    ) -> EndpointConfiguration {

        return EndpointConfiguration(
            name: name,
            key: key,
            shouldFail: shouldFail,
            delay: KotlinInt(int: Int32(delay)),
            dashboardOptionsConfig: config.toNative(),
            versionCode: Int32(truncatingIfNeeded: versionCode),
            endpointMatcher: { request in KotlinBoolean(value: endpointMatcher(key, request))},
            defaultHandler: { request in defaultHandler(key, request) },
            errorHandler: { request in errorHandler(key, request) }
        )
    }
    
    static func fromNative(_ endpoint: EndpointConfiguration) -> BridgeEndpointConfig {
        return BridgeEndpointConfig(
            name: endpoint.name,
            key: endpoint.key as! String,
            shouldFail: endpoint.shouldFail,
            delay: endpoint.delay?.int64Value ?? 100,
            versionCode: Int64(endpoint.versionCode),
            config: BridgeDashboardOptionsConfig.fromNative(endpoint.dashboardOptionsConfig)
        )
    }
}

extension BridgeReleaseModeConfig {
    func toNative() -> MockzillaConfig.ReleaseModeConfig {
        return MockzillaConfig.ReleaseModeConfig(
            rateLimit: Int32(rateLimit),
            rateLimitRefillPeriod: rateLimitRefillPeriodMillis,
            tokenLifeSpan: tokenLifeSpanMillis
        )
    }
    
    static func fromNative(_ config: MockzillaConfig.ReleaseModeConfig) -> BridgeReleaseModeConfig {
        return BridgeReleaseModeConfig(
            rateLimit: Int64(config.rateLimit),
            rateLimitRefillPeriodMillis: config.rateLimitRefillPeriod,
            tokenLifeSpanMillis: config.tokenLifeSpan
        )
    }
}

extension BridgeMockzillaConfig {
    func toNative(
        endpointMatcher: @escaping (_ key: String, _ request: MockzillaHttpRequest) -> Bool,
        defaultHandler: @escaping (_ key: String, _ request: MockzillaHttpRequest) -> MockzillaHttpResponse,
        errorHandler: @escaping (_ key: String, _ request: MockzillaHttpRequest) -> MockzillaHttpResponse
    ) -> MockzillaConfig {
        return MockzillaConfig(
            port: Int32(port),
            endpoints: endpoints.map {
                endpoint in endpoint?.toNative(endpointMatcher: endpointMatcher, defaultHandler: defaultHandler, errorHandler: errorHandler)
            }.filter {
                endpoint in endpoint != nil
            } as! Array<EndpointConfiguration>,
            isRelease: isRelease,
            localhostOnly: false, logLevel: logLevel.toNative(),
            releaseModeConfig: releaseModeConfig.toNative(),
            isNetworkDiscoveryEnabled: false,
            additionalLogWriters: []
        )
    }
    
    static func fromNative(_ config: MockzillaConfig) throws -> BridgeMockzillaConfig {
        return BridgeMockzillaConfig(
            port: Int64(config.port),
            endpoints: config.endpoints.map {
                endpoint in BridgeEndpointConfig.fromNative(endpoint)
            },
            isRelease: config.isRelease,
            localHostOnly: config.isRelease,
            logLevel: try BridgeLogLevel.fromNative(config.logLevel),
            releaseModeConfig: BridgeReleaseModeConfig.fromNative(config.releaseModeConfig)
        )
    }
}

extension BridgeMockzillaRuntimeParams {
    static func fromNative(_ params: MockzillaRuntimeParams) throws -> BridgeMockzillaRuntimeParams {
        return BridgeMockzillaRuntimeParams(
            config: try BridgeMockzillaConfig.fromNative(params.config),
            mockBaseUrl: params.mockBaseUrl,
            apiBaseUrl: params.apiBaseUrl,
            port: Int64(params.port)
        )
    }
}
