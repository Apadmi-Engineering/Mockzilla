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
    MockzillaKt.startMockzilla(config: mockzillaConfig)
}

public func stopMockzilla() {
    MockzillaKt.stopMockzilla()
}

public extension EndpointConfigurationBuilder {
    func setSwiftPatternMatcher(block: @escaping (MockzillaHttpRequest) -> Bool) -> EndpointConfigurationBuilder {
        setPatternMatcher {
            KotlinBoolean(bool: block($0))
        }
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
