import 'package:mockzilla_ios/src/messages.g.dart';
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
  toDart() => MockzillaHttpRequest(
        uri: uri,
        headers: Map.fromEntries(
          headers.entries.whereType<MapEntry<String, String>>(),
        ),
        method: method.toDart(),
      );
}

extension MockzillaHttpRequestBridge on MockzillaHttpRequest {
  toBridge() => BridgeMockzillaHttpRequest(
        uri: uri,
        headers: headers,
        body: body,
        method: method.toBridge(),
      );
}

extension BridgeMockzillaHttpResponseBridge on BridgeMockzillaHttpResponse {
  toDart() => MockzillaHttpResponse(
        statusCode: statusCode,
        headers: Map.fromEntries(
          headers.entries.whereType<MapEntry<String, String>>(),
        ),
        body: body,
      );
}

extension MockzillaHttpResponseBridge on MockzillaHttpResponse {
  toBridge() => BridgeMockzillaHttpResponse(
        statusCode: statusCode,
        headers: headers,
        body: body,
      );
}

extension BridgeEndpointConfigBridge on BridgeEndpointConfig {
  toDart(
    bool Function(MockzillaHttpRequest request) endpointMatcher,
    MockzillaHttpResponse Function(MockzillaHttpRequest request) defaultHandler,
    MockzillaHttpResponse Function(MockzillaHttpRequest request) errorHandler,
  ) =>
      EndpointConfig(
        name: name,
        key: key,
        endpointMatcher: endpointMatcher,
        defaultHandler: defaultHandler,
        errorHandler: errorHandler,
        delayMean: delayMean,
        delayVariance: delayVariance,
        webApiDefaultResponse: webApiDefaultResponse?.toDart(),
        webApiErrorResponse: webApiErrorResponse?.toDart(),
      );
}

extension EndpointConfigBridge on EndpointConfig {
  BridgeEndpointConfig toBridge() => BridgeEndpointConfig(
        name: name,
        key: key,
        failureProbability: failureProbability,
        delayMean: delayMean,
        delayVariance: delayVariance,
        webApiDefaultResponse: webApiDefaultResponse?.toBridge(),
        webApiErrorResponse: webApiErrorResponse?.toBridge(),
      );
}

extension BridgeReleaseModeConfigBridge on BridgeReleaseModeConfig {
  toDart() => ReleaseModeConfig(
        rateLimit: rateLimit,
        rateLimitRefillPeriod:
            Duration(milliseconds: rateLimitRefillPeriodMillis),
        tokenLifeSpan: Duration(milliseconds: tokenLifeSpanMillis),
      );
}

extension ReleaseModeConfigBridge on ReleaseModeConfig {
  toBridge() => BridgeReleaseModeConfig(
        rateLimit: rateLimit,
        rateLimitRefillPeriodMillis: rateLimitRefillPeriod.inMilliseconds,
        tokenLifeSpanMillis: tokenLifeSpan.inMilliseconds,
      );
}

extension BridgeAuthHeaderBridge on BridgeAuthHeader {
  toDart() => AuthHeader(
        key: key,
        value: value,
      );
}

extension AuthHeaderBridge on AuthHeader {
  toBridge() => BridgeAuthHeader(
        key: key,
        value: value,
      );
}

extension MockzillaConfigBridge on MockzillaConfig {
  toBridge() => BridgeMockzillaConfig(
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
      );
}

extension BridgeMockzillaConfigBridge on BridgeMockzillaConfig {
  toDart(
    bool Function(MockzillaHttpRequest request) endpointMatcher,
    MockzillaHttpResponse Function(MockzillaHttpRequest request) defaultHandler,
    MockzillaHttpResponse Function(MockzillaHttpRequest request) errorHandler,
  ) =>
      MockzillaConfig(
        port: port,
        endpoints: endpoints
            .map(
              (endpoint) => endpoint?.toDart(
                endpointMatcher,
                defaultHandler,
                errorHandler,
              ),
            )
            .whereType<EndpointConfig>()
            .toList(),
        isRelease: isRelease,
        localHostOnly: localHostOnly,
        logLevel: logLevel.toDart(),
        releaseModeConfig: releaseModeConfig.toDart(),
        additionalLogWriters: [],
      );
}
