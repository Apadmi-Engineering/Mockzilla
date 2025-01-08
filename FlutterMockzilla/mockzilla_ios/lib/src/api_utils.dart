import 'package:meta/meta.dart';
import 'package:mockzilla_ios/src/messages.g.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

@internal
extension BridgeHttpMethodBridge on BridgeHttpMethod {
  HttpMethod toDart() => HttpMethod.values.firstWhere(
        (element) => element.name == name,
      );
}

@internal
extension HttpMethodBridge on HttpMethod {
  BridgeHttpMethod toBridge() => BridgeHttpMethod.values.firstWhere(
        (element) => element.name == name,
      );
}

@internal
extension BridgeLogLevelBridge on BridgeLogLevel {
  LogLevel toDart() => LogLevel.values.firstWhere(
        (element) => element.name == name,
      );
}

@internal
extension LogLevelBridge on LogLevel {
  BridgeLogLevel toBridge() => BridgeLogLevel.values.firstWhere(
        (element) => element.name == name,
      );
}

@internal
extension BridgeMockzillaHttpRequestBridge on BridgeMockzillaHttpRequest {
  MockzillaHttpRequest toDart() => MockzillaHttpRequest(
        uri: uri,
        headers: headers,
        body: body,
        method: method.toDart(),
      );
}

@internal
extension MockzillaHttpRequestBridge on MockzillaHttpRequest {
  BridgeMockzillaHttpRequest toBridge() => BridgeMockzillaHttpRequest(
        uri: uri,
        headers: headers,
        body: body,
        method: method.toBridge(),
      );
}

@internal
extension BridgeMockzillaHttpResponseBridge on BridgeMockzillaHttpResponse {
  MockzillaHttpResponse toDart() => MockzillaHttpResponse(
        statusCode: statusCode,
        headers: headers,
        body: body,
      );
}

@internal
extension MockzillaHttpResponseBridge on MockzillaHttpResponse {
  BridgeMockzillaHttpResponse toBridge() => BridgeMockzillaHttpResponse(
        statusCode: statusCode,
        headers: headers,
        body: body,
      );
}

@internal
extension BridgeDashboardOverridePresetBridge on BridgeDashboardOverridePreset {
  DashboardOverridePreset toDart() => DashboardOverridePreset(
        name: name,
        description: description,
        response: response.toDart(),
      );
}

@internal
extension DashboardOverridePresetBridge on DashboardOverridePreset {
  BridgeDashboardOverridePreset toBridge() => BridgeDashboardOverridePreset(
        name: name,
        description: description,
        response: response.toBridge(),
      );
}

@internal
extension BridgeDashboardOverrideConfigBridge on BridgeDashboardOptionsConfig {
  DashboardOptionsConfig toDart() => DashboardOptionsConfig(
        successPresets: successPresets.map((it) => it.toDart()).toList(),
        errorPresets: errorPresets.map((it) => it.toDart()).toList(),
      );
}

@internal
extension DashboardOverrideConfigBridge on DashboardOptionsConfig {
  BridgeDashboardOptionsConfig toBridge() => BridgeDashboardOptionsConfig(
        successPresets: successPresets.map((it) => it.toBridge()).toList(),
        errorPresets: errorPresets.map((it) => it.toBridge()).toList(),
      );
}

@internal
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

@internal
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

@internal
extension MockzillaConfigBridge on MockzillaConfig {
  BridgeMockzillaConfig toBridge() => BridgeMockzillaConfig(
        port: port,
        endpoints: endpoints
            .map(
              (endpoint) => endpoint.toBridge(),
            )
            .toList(),
        localHostOnly: localHostOnly,
        logLevel: logLevel.toBridge(),
        isNetworkDiscoveryEnabled: isNetworkDiscoveryEnabled,
      );
}

@internal
extension BridgeMockzillaConfigBridge on BridgeMockzillaConfig {
  MockzillaConfig toDart({
    required bool Function(MockzillaHttpRequest request, String key) endpointMatcher,
    required MockzillaHttpResponse Function(MockzillaHttpRequest request, String key) defaultHandler,
    required MockzillaHttpResponse Function(MockzillaHttpRequest request, String key) errorHandler,
  }) =>
      MockzillaConfig(
        port: port,
        endpoints: endpoints
            .map(
              (endpoint) => endpoint.toDart(
                (request) => endpointMatcher(request, endpoint.key),
                (request) => defaultHandler(request, endpoint.key),
                (request) => errorHandler(request, endpoint.key),
              ),
            )
            .toList(),
        localHostOnly: localHostOnly,
        logLevel: logLevel.toDart(),
        isNetworkDiscoveryEnabled: isNetworkDiscoveryEnabled,
      );
}

@internal
extension BridgeMockzillaRuntimeParamsBridge on BridgeMockzillaRuntimeParams {
  MockzillaRuntimeParams toDart({
    required bool Function(MockzillaHttpRequest request, String key)
    endpointMatcher,
    required MockzillaHttpResponse Function(
        MockzillaHttpRequest request, String key)
    defaultHandler,
    required MockzillaHttpResponse Function(
        MockzillaHttpRequest request, String key)
    errorHandler,
  }) =>
      MockzillaRuntimeParams(
        config: config.toDart(
          endpointMatcher: endpointMatcher,
          defaultHandler: defaultHandler,
          errorHandler: errorHandler,
        ),
        mockBaseUrl: mockBaseUrl,
        apiBaseUrl: apiBaseUrl,
        port: port,
      );
}

