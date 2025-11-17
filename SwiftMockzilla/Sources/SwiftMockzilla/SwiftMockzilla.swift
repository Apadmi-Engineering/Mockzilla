import mockzilla

public let HttpStatusCode = Ktor_httpHttpStatusCode.Companion.shared
public typealias MockzillaConfig = Mockzilla_commonMockzillaConfig
public typealias MockzillaLogLevel = Mockzilla_commonMockzillaConfig.LogLevel
public typealias MockzillaConfigBuilder = Mockzilla_commonMockzillaConfig.Builder
public typealias MockzillaRuntimeParams = Mockzilla_commonMockzillaRuntimeParams
public typealias EndpointConfiguration = Mockzilla_commonEndpointConfiguration
public typealias MockzillaHttpRequest = Mockzilla_commonMockzillaHttpRequest
public typealias MockzillaHttpResponse = Mockzilla_commonMockzillaHttpResponse
public typealias EndpointConfigurationBuilder = Mockzilla_commonEndpointConfiguration.Builder
public typealias AuthHeaderProvider = Mockzilla_commonAuthHeaderProvider
public typealias ReleaseModeConfig = Mockzilla_commonMockzillaConfig.ReleaseModeConfig
public typealias MockzillaLogWriter = Mockzilla_commonMockzillaLogWriter

public func startMockzilla(config mockzillaConfig: MockzillaConfig) -> MockzillaRuntimeParams {
    do {
        return try MockzillaKt.startMockzilla(config: mockzillaConfig)
    } catch {
        fatalError("Failed to start Mockzilla server: \n\(error)")
    }
}

public func stopMockzilla() {
    MockzillaKt.stopMockzilla()
}

public extension EndpointConfigurationBuilder {
    func setSwiftPatternMatcher(block: @escaping (MockzillaHttpRequest) async -> Bool) -> EndpointConfigurationBuilder {
        AsyncUtilsKt.setPatternMatcherCallback(builder: self) { request, callback in
            Task {
                let result = await block(request)
                callback(KotlinBoolean(bool: result))
            }
        }
        return self
    }
    
    func setSwiftDefaultHandler(block: @escaping (MockzillaHttpRequest) async -> MockzillaHttpResponse) -> EndpointConfigurationBuilder {
        AsyncUtilsKt.setDefaultHandlerCallback(builder: self) { request, callback in
            Task {
                let result = await block(request)
                callback(result)
            }
        }
        return self
    }
    
    func setSwiftErrorHandler(block: @escaping (MockzillaHttpRequest) async -> MockzillaHttpResponse) -> EndpointConfigurationBuilder {
        AsyncUtilsKt.setErrorHandlerCallback(builder: self) { request, callback in
            Task {
                let result = await block(request)
                callback(result)
            }
        }
        return self
    }
}

public extension MockzillaHttpResponse {
    convenience init(
        status: Ktor_httpHttpStatusCode = HttpStatusCode.OK,
        headers: [String: String] = [:],
        body: String = ""
    ) {
        self.init(statusCode: status, headers: headers, body: body)
    }
}
