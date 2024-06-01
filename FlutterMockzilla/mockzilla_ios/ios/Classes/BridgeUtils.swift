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
        case BridgeHttpMethod.get: Ktor_httpHttpMethod(value: "get")
        case BridgeHttpMethod.head: Ktor_httpHttpMethod(value: "head")
        case BridgeHttpMethod.delete: Ktor_httpHttpMethod(value: "delete")
        case BridgeHttpMethod.options: Ktor_httpHttpMethod(value: "options")
        case BridgeHttpMethod.patch: Ktor_httpHttpMethod(value: "patch")
        case BridgeHttpMethod.post: Ktor_httpHttpMethod(value: "post")
        case BridgeHttpMethod.put: Ktor_httpHttpMethod(value: "put")
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

extension BridgeEndpointConfig {
    func toNative(
        endpointMatcher: @escaping (_ key: String, _ request: MockzillaHttpRequest) -> Bool,
        defaultHandler: @escaping (_ key: String, _ request: MockzillaHttpRequest) -> MockzillaHttpResponse,
        errorHandler: @escaping (_ key: String, _ request: MockzillaHttpRequest) -> MockzillaHttpResponse
    ) -> EndpointConfiguration {
        var kotlinFailureProbability: KotlinInt?
        var kotlinDelayMean: KotlinInt?
        var kotlinDelayVariance: KotlinInt?
        if let _failureProbability = failureProbability {
            kotlinFailureProbability = KotlinInt(int: Int32(truncatingIfNeeded: _failureProbability))
        }
        if let _delayMean = delayMean {
            kotlinDelayMean = KotlinInt(int: Int32(truncatingIfNeeded: _delayMean))
        }
        if let _delayVariance = delayVariance {
            kotlinDelayVariance = KotlinInt(int: Int32(truncatingIfNeeded: _delayVariance))
        }
        return EndpointConfiguration(
            name: name,
            key: key,
            failureProbability: kotlinFailureProbability,
            delayMean: kotlinDelayMean,
            delayVariance: kotlinDelayVariance,
            endpointMatcher: { request in KotlinBoolean(value: endpointMatcher(key, request))},
            webApiDefaultResponse: webApiErrorResponse?.toNative(),
            webApiErrorResponse: webApiErrorResponse?.toNative(),
            defaultHandler: { request in defaultHandler(key, request) },
            errorHandler: { request in errorHandler(key, request) }
        )
    }
    
    static func fromNative(_ endpoint: EndpointConfiguration) -> BridgeEndpointConfig {
        return BridgeEndpointConfig(
            name: endpoint.name,
            key: endpoint.key,
            failureProbability: endpoint.failureProbability?.int64Value,
            delayMean: endpoint.delayMean?.int64Value,
            delayVariance: endpoint.delayVariance?.int64Value,
            webApiDefaultResponse: endpoint.webApiDefaultResponse.map {
                response in BridgeMockzillaHttpResponse.fromNative(response)
            },
            webApiErrorResponse: endpoint.webApiErrorResponse.map {
                response in BridgeMockzillaHttpResponse.fromNative(response)
            }
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
