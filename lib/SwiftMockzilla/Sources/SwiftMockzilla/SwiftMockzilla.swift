import mockzilla

public let HttpStatusCode = Ktor_httpHttpStatusCode.Companion.shared

public func startMockzilla(config mockzillaConfig: MockzillaConfig) -> MockzillaRuntimeParams {
    MockzillaKt.startMockzilla(config: mockzillaConfig)
}

public func stopMockzilla() {
    MockzillaKt.stopMockzilla()
}

public extension EndpointConfiguration.Builder {
    func setSwiftPatternMatcher(block: @escaping (MockzillaHttpRequest) -> Bool) -> EndpointConfiguration.Builder {
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
