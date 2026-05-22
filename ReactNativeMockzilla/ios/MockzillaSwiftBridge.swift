import Foundation
import SwiftMockzilla

@objc public protocol MockzillaEventSender: AnyObject {
    func emitRequestEvent(_ body: [String: Any])
}

@objc public class MockzillaSwiftBridge: NSObject {
    private weak var eventSender: MockzillaEventSender?
    private var pendingMatchers: [String: CheckedContinuation<Bool, Never>] = [:]
    private var pendingHandlers: [String: CheckedContinuation<MockzillaHttpResponse, Never>] = [:]
    private let lock = NSLock()

    @objc public init(eventSender: MockzillaEventSender) {
        self.eventSender = eventSender
    }

    @objc public func startMockzilla(
        config: [String: Any],
        resolve: @escaping ([String: Any]) -> Void,
        reject: @escaping (String, String) -> Void
    ) {
        Task {
            do {
                let mockzillaConfig = try self.buildConfig(from: config)
                let params = SwiftMockzilla.startMockzilla(config: mockzillaConfig)
                resolve([
                    "mockBaseUrl": params.mockBaseUrl,
                    "apiBaseUrl": params.apiBaseUrl,
                    "port": Int(params.port),
                ])
            } catch {
                reject("MOCKZILLA_START_ERROR", error.localizedDescription)
            }
        }
    }

    @objc public func stopMockzilla(
        resolve: @escaping () -> Void,
        reject: @escaping (String, String) -> Void
    ) {
        SwiftMockzilla.stopMockzilla()
        resolve()
    }

    @objc public func respondToMatcher(requestId: String, matches: Bool) {
        lock.lock()
        let cont = pendingMatchers.removeValue(forKey: requestId)
        lock.unlock()
        cont?.resume(returning: matches)
    }

    @objc public func respondToHandler(requestId: String, response: [String: Any]) {
        let statusCode = response["statusCode"] as? Int ?? 200
        let headers = response["headers"] as? [String: String] ?? [:]
        let body = response["body"] as? String ?? ""
        let httpResponse = MockzillaHttpResponse(
            status: Ktor_httpHttpStatusCode(value: Int32(statusCode), description: ""),
            headers: headers,
            body: body
        )
        lock.lock()
        let cont = pendingHandlers.removeValue(forKey: requestId)
        lock.unlock()
        cont?.resume(returning: httpResponse)
    }

    private func buildConfig(from config: [String: Any]) throws -> MockzillaConfig {
        let builder = MockzillaConfigBuilder()
        if let port = config["port"] as? Int { builder.setPort(port: Int32(port)) }
        if let lo = config["localHostOnly"] as? Bool { builder.setLocalhostOnly(localhostOnly: lo) }
        if let nd = config["isNetworkDiscoveryEnabled"] as? Bool {
            builder.setIsNetworkDiscoveryEnabled(isEnabled: nd)
        }
        if let logLevelStr = config["logLevel"] as? String {
            builder.setLogLevel(level: logLevelStr.toMockzillaLogLevel())
        }

        for epDict in config["endpoints"] as? [[String: Any]] ?? [] {
            guard let key = epDict["key"] as? String else { continue }
            let b = EndpointConfigurationBuilder(key: key)
            if let name = epDict["name"] as? String { b.setName(name: name) }
            if let fail = epDict["shouldFail"] as? Bool { b.setShouldFail(shouldFail: fail) }
            if let delay = epDict["delayMs"] as? Int { b.setMeanDelayMillis(delay: Int32(delay)) }
            if let vc = epDict["versionCode"] as? Int { b.setVersionCode(code: Int32(vc)) }

            let k = key
            _ = b
                .setSwiftPatternMatcher { [weak self] req in
                    await self?.callMatcher(key: k, request: req) ?? false
                }
                .setSwiftDefaultHandler { [weak self] req in
                    await self?.callHandler(type: .defaultHandler, key: k, request: req)
                        ?? MockzillaHttpResponse()
                }
                .setSwiftErrorHandler { [weak self] req in
                    await self?.callHandler(type: .errorHandler, key: k, request: req)
                        ?? MockzillaHttpResponse()
                }

            builder.addEndpoint(endpoint_: b)
        }
        return builder.build()
    }

    private func callMatcher(key: String, request: MockzillaHttpRequest) async -> Bool {
        let id = UUID().uuidString
        let body = (try? await request.bodyAsString()) ?? ""
        return await withCheckedContinuation { cont in
            lock.lock()
            pendingMatchers[id] = cont
            lock.unlock()
            eventSender?.emitRequestEvent([
                "requestId": id,
                "key": key,
                "type": RequestEventType.endpointMatcher.rawValue,
                "request": [
                    "uri": request.uri,
                    "headers": request.headers,
                    "body": body,
                    "method": request.method.value,
                ],
            ])
        }
    }

    private func callHandler(
        type: RequestEventType,
        key: String,
        request: MockzillaHttpRequest
    ) async -> MockzillaHttpResponse {
        let id = UUID().uuidString
        let body = (try? await request.bodyAsString()) ?? ""
        return await withCheckedContinuation { cont in
            lock.lock()
            pendingHandlers[id] = cont
            lock.unlock()
            eventSender?.emitRequestEvent([
                "requestId": id,
                "key": key,
                "type": type.rawValue,
                "request": [
                    "uri": request.uri,
                    "headers": request.headers,
                    "body": body,
                    "method": request.method.value,
                ],
            ])
        }
    }
}

private enum RequestEventType: String {
    case endpointMatcher
    case defaultHandler
    case errorHandler
}

private extension String {
    func toMockzillaLogLevel() -> MockzillaConfig.LogLevel {
        switch self.uppercased() {
        case "DEBUG": return .debug
        case "ERROR": return .error
        case "VERBOSE": return .verbose
        case "WARN": return .warn
        case "ASSERT": return .assert
        default: return .info
        }
    }
}
