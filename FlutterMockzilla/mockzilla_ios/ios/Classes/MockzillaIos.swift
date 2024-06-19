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

extension FlutterError: Error {}

class MockzillaIos: Thread, MockzillaHostApi {
    
    private let handler: MockzillaFlutterApi
    
    private let waiter = DispatchGroup()
    private let matcherSemaphore = DispatchSemaphore(value: 0)
    private let handlerSemaphore = DispatchSemaphore(value: 0)
    private let errorHandlerSemaphore = DispatchSemaphore(value: 0)
    
    init(handler: MockzillaFlutterApi) {
        self.handler = handler
    }
    
    override func main() {
        waiter.wait()
    }
    
    func startServer(config: BridgeMockzillaConfig) throws -> BridgeMockzillaRuntimeParams {
        waiter.enter()
        let nativeConfig = config.toNative(
            endpointMatcher: { key, request in
                do {
                    var result: Result<Bool, FlutterError> = Result.failure(FlutterError())
                    let nativeRequest = try BridgeMockzillaHttpRequest.fromNative(request)
                    self.handler.endpointMatcher(
                        request: nativeRequest,
                        key: key,
                        completion: { localResult in
                            result = localResult
                            self.matcherSemaphore.signal()
                        }
                    )
                    self.matcherSemaphore.wait()
                    return try result.get()
                } catch {
                    return false
                }
            },
            defaultHandler: { key, request in
                do {
                    var result: Result<BridgeMockzillaHttpResponse, FlutterError> = Result.failure(FlutterError())
                    let nativeRequest = try BridgeMockzillaHttpRequest.fromNative(request)
                    self.handler.defaultHandler(
                        request: nativeRequest,
                        key: key,
                        completion: { localResult in
                            result = localResult
                            self.handlerSemaphore.signal()
                        }
                    )
                    self.handlerSemaphore.wait()
                    return try result.map { response in response.toNative() }.get()
                } catch {
                    return MockzillaHttpResponse(statusCode: HttpStatusCode.InternalServerError, headers: [:], body: "")
                }
            },
            errorHandler: { key, request in
                do {
                    var result: Result<BridgeMockzillaHttpResponse, FlutterError> = Result.failure(FlutterError())
                    let nativeRequest = try BridgeMockzillaHttpRequest.fromNative(request)
                    self.handler.errorHandler(
                        request: nativeRequest,
                        key: key,
                        completion: { localResult in
                            result = localResult
                            self.errorHandlerSemaphore.signal()
                        }
                    )
                    self.errorHandlerSemaphore.wait()
                    return try result.map { response in response.toNative() }.get()
                } catch {
                    return MockzillaHttpResponse(statusCode: HttpStatusCode.InternalServerError, headers: [:], body: "")
                }
            }
        )
        let params = startMockzilla(config: nativeConfig)
        return try BridgeMockzillaRuntimeParams.fromNative(params)
    }

    func stopServer() throws {
        stopMockzilla()
        waiter.leave()
    }
}
