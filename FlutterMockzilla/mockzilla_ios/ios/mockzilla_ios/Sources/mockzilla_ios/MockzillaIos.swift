//
//  MockzillaIos.swift
//  mockzilla_ios
//
//  Created by Tom Handcock on 01/06/2024.
//

import Foundation
import Flutter
import mockzilla
import SwiftMockzilla

class MockzillaIos: Thread, MockzillaHostApi {
    
    private let handler: MockzillaFlutterApi
    private let proxyLogger: ProxyMockzillaLogger
    
    private let waiter = DispatchGroup()
    
    init(handler: MockzillaFlutterApi, proxyLogger: ProxyMockzillaLogger) {
        self.handler = handler
        self.proxyLogger = proxyLogger
    }
    
    override func main() {
        waiter.wait()
    }
    
    func startServer(config: BridgeMockzillaConfig) throws -> BridgeMockzillaRuntimeParams {
        waiter.enter()
        let nativeConfig = config.toNative(
            endpointMatcher: { key, request in
                do {
                    let matcherSemaphore = DispatchSemaphore(value: 0)
                    var result: Result<Bool, PigeonError>!
                    let nativeRequest = try await BridgeMockzillaHttpRequest.fromNative(request)
                    DispatchQueue.main.async {
                        self.handler.endpointMatcher(
                            request: nativeRequest,
                            key: key,
                            completion: { localResult in
                                result = localResult
                                matcherSemaphore.signal()
                            }
                        )
                    }
                    matcherSemaphore.wait()
                    return try result.get()
                } catch {
                    return false
                }
            },
            defaultHandler: { key, request in
                do {
                    let handlerSemaphore = DispatchSemaphore(value: 0)
                    var result: Result<BridgeMockzillaHttpResponse, PigeonError>!
                    let nativeRequest = try await BridgeMockzillaHttpRequest.fromNative(request)
                    DispatchQueue.main.async {
                        self.handler.defaultHandler(
                            request: nativeRequest,
                            key: key,
                            completion: { localResult in
                                result = localResult
                                handlerSemaphore.signal()
                            }
                        )
                    }
                    handlerSemaphore.wait()
                    return try result.map { response in response.toNative() }.get()
                } catch {
                    return MockzillaHttpResponse(statusCode: HttpStatusCode.InternalServerError, headers: [:], body: "")
                }
            },
            errorHandler: { key, request in
                do {
                    let errorHandlerSemaphore = DispatchSemaphore(value: 0)
                    var result: Result<BridgeMockzillaHttpResponse, PigeonError>!
                    let nativeRequest = try await BridgeMockzillaHttpRequest.fromNative(request)
                    DispatchQueue.main.async {
                        self.handler.errorHandler(
                            request: nativeRequest,
                            key: key,
                            completion: { localResult in
                                result = localResult
                                errorHandlerSemaphore.signal()
                            }
                        )
                    }
                    errorHandlerSemaphore.wait()
                    return try result.map { response in response.toNative() }.get()
                } catch {
                    return MockzillaHttpResponse(
                        statusCode: HttpStatusCode.InternalServerError,
                        headers: [:],
                        body: ""
                    
                    )
                }
            },
            proxyLogger: proxyLogger
        )
        do {
            let params = try MockzillaKt.startMockzilla(config: nativeConfig)
            return try BridgeMockzillaRuntimeParams.fromNative(params)
        } catch let error as NSError where error.userInfo["KotlinException"] is Mockzilla_commonPortConflictException {
            throw PigeonError(code: "PortConflictException", message: nil, details: nil)
        } catch {
            throw error
        }
    }

    func stopServer() throws {
        stopMockzilla()
        waiter.leave()
    }
}
