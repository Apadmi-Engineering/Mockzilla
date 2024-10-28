import 'package:mockzilla_android/src/messages.g.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

extension BridgeHttpMethodBridge on BridgeHttpMethod {
  HttpMethod toDart() => HttpMethod.values.firstWhere(
        (element) => element.name == name,
      );
}

extension HttpMethodBridge on HttpMethod {
  BridgeHttpMethod toBridge() => BridgeHttpMethod.values.firstWhere(
        (element) => element.name == name,
      );
}

extension BridgeLogLevelBridge on BridgeLogLevel {
  LogLevel toDart() => LogLevel.values.firstWhere(
        (element) => element.name == name,
      );
}

extension LogLevelBridge on LogLevel {
  BridgeLogLevel toBridge() => BridgeLogLevel.values.firstWhere(
        (element) => element.name == name,
      );
}

extension BridgeMockzillaHttpRequestBridge on BridgeMockzillaHttpRequest {
  MockzillaHttpRequest toDart() => MockzillaHttpRequest(
        uri: uri,
        headers: headers,
        body: body,
        method: method.toDart(),
      );
}

extension MockzillaHttpRequestBridge on MockzillaHttpRequest {
  BridgeMockzillaHttpRequest toBridge() => BridgeMockzillaHttpRequest(
        uri: uri,
        headers: headers,
        body: body,
        method: method.toBridge(),
      );
}

extension BridgeMockzillaHttpResponseBridge on BridgeMockzillaHttpResponse {
  MockzillaHttpResponse toDart() => MockzillaHttpResponse(
        statusCode: statusCode,
        headers: headers,
        body: body,
      );
}

extension MockzillaHttpResponseBridge on MockzillaHttpResponse {
  BridgeMockzillaHttpResponse toBridge() => BridgeMockzillaHttpResponse(
        statusCode: statusCode,
        headers: headers,
        body: body,
      );
}

extension BridgeDashboardOverridePresetBridge on BridgeDashboardOverridePreset {
  DashboardOverridePreset toDart() => DashboardOverridePreset(
        name: name,
        description: description,
        response: response.toDart(),
      );
}

extension DashboardOverridePresetBridge on DashboardOverridePreset {
  BridgeDashboardOverridePreset toBridge() => BridgeDashboardOverridePreset(
        name: name,
        description: description,
        response: response.toBridge(),
      );
}

extension BridgeDashboardOverrideConfigBridge on BridgeDashboardOptionsConfig {
  DashboardOptionsConfig toDart() => DashboardOptionsConfig(
        successPresets: successPresets
            .map((it) => it.toDart())
            .toList(),
        errorPresets: errorPresets
            .map((it) => it.toDart())
            .toList(),
      );
}

extension DashboardOverrideConfigBridge on DashboardOptionsConfig {
  BridgeDashboardOptionsConfig toBridge() => BridgeDashboardOptionsConfig(
        successPresets: successPresets.map((it) => it.toBridge()).toList(),
        errorPresets: errorPresets.map((it) => it.toBridge()).toList(),
      );
}

extension BridgeEndpointConfigBridge on BridgeEndpointConfig {
  EndpointConfig toDart(
    bool Function(MockzillaHttpRequest request) endpointMatcher,
    MockzillaHttpResponse Function(MockzillaHttpRequest request) defaultHandler,
    MockzillaHttpResponse Function(MockzillaHttpRequest request) errorHandler,
  ) =>
      EndpointConfig(
        name: name,
        customKey: key,
        endpointMatcher: endpointMatcher,
        defaultHandler: defaultHandler,
        errorHandler: errorHandler,
        versionCode: versionCode,
        delay: Duration(milliseconds: delayMs),
        shouldFail: shouldFail,
        dashboardOptionsConfig: config.toDart(),
      );
}

extension EndpointConfigBridge on EndpointConfig {
  BridgeEndpointConfig toBridge() => BridgeEndpointConfig(
        name: name,
        key: key,
        shouldFail: shouldFail,
        delayMs: delay.inMilliseconds,
        versionCode: versionCode,
        config: dashboardOptionsConfig.toBridge(),
      );
}

extension BridgeReleaseModeConfigBridge on BridgeReleaseModeConfig {
  ReleaseModeConfig toDart() => ReleaseModeConfig(
        rateLimit: rateLimit,
        rateLimitRefillPeriod:
            Duration(milliseconds: rateLimitRefillPeriodMillis),
        tokenLifeSpan: Duration(milliseconds: tokenLifeSpanMillis),
      );
}

extension ReleaseModeConfigBridge on ReleaseModeConfig {
  BridgeReleaseModeConfig toBridge() => BridgeReleaseModeConfig(
        rateLimit: rateLimit,
        rateLimitRefillPeriodMillis: rateLimitRefillPeriod.inMilliseconds,
        tokenLifeSpanMillis: tokenLifeSpan.inMilliseconds,
      );
}

extension BridgeAuthHeaderBridge on BridgeAuthHeader {
  AuthHeader toDart() => AuthHeader(
        key: key,
        value: value,
      );
}

extension AuthHeaderBridge on AuthHeader {
  BridgeAuthHeader toBridge() => BridgeAuthHeader(
        key: key,
        value: value,
      );
}

extension MockzillaConfigBridge on MockzillaConfig {
  BridgeMockzillaConfig toBridge() => BridgeMockzillaConfig(
        port: port,
        endpoints: endpoints
            .map(
              (endpoint) => endpoint.toBridge(),
            )
            .toList(),
        isRelease: isRelease,
        localHostOnly: localHostOnly,
        logLevel: logLevel.toBridge(),
        releaseModeConfig: releaseModeConfig.toBridge(),
        isNetworkDiscoveryEnabled: isNetworkDiscoveryEnabled,
      );
}

extension BridgeMockzillaConfigBridge on BridgeMockzillaConfig {
  MockzillaConfig toDart(
    bool Function(MockzillaHttpRequest request) endpointMatcher,
    MockzillaHttpResponse Function(MockzillaHttpRequest request) defaultHandler,
    MockzillaHttpResponse Function(MockzillaHttpRequest request) errorHandler,
  ) =>
      MockzillaConfig(
        port: port,
        endpoints: endpoints
            .map(
              (endpoint) => endpoint.toDart(
                endpointMatcher,
                defaultHandler,
                errorHandler,
              ),
            )
            .toList(),
        isRelease: isRelease,
        localHostOnly: localHostOnly,
        logLevel: logLevel.toDart(),
        isNetworkDiscoveryEnabled: isNetworkDiscoveryEnabled,
      );
}
