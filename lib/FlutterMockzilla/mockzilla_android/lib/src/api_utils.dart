import 'package:mockzilla_android/src/messages.g.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

extension ApiHttpMethodBridge on ApiHttpMethod {
  HttpMethod toDart() => HttpMethod.values.firstWhere(
        (element) => element.name == name,
      );
}

extension HttpMethodBridge on HttpMethod {
  ApiHttpMethod toApi() => ApiHttpMethod.values.firstWhere(
        (element) => element.name == name,
      );
}

extension ApiLogLevelBridge on ApiLogLevel {
  LogLevel toDart() => LogLevel.values.firstWhere(
        (element) => element.name == name,
      );
}

extension LogLevelBridge on LogLevel {
  ApiLogLevel toApi() => ApiLogLevel.values.firstWhere(
        (element) => element.name == name,
      );
}

extension ApiMockzillaHttpRequestBridge on ApiMockzillaHttpRequest {
  toDart() => MockzillaHttpRequest(
        uri: uri,
        headers: Map.fromEntries(
          headers.entries.whereType<MapEntry<String, String>>(),
        ),
        method: method.toDart(),
      );
}

extension MockzillaHttpRequestBridge on MockzillaHttpRequest {
  toApi() => ApiMockzillaHttpRequest(
        uri: uri,
        headers: headers,
        body: body,
        method: method.toApi(),
      );
}

extension ApiMockzillaHttpResponseBridge on ApiMockzillaHttpResponse {
  toDart() => MockzillaHttpResponse(
        statusCode: statusCode,
        headers: Map.fromEntries(
          headers.entries.whereType<MapEntry<String, String>>(),
        ),
        body: body,
      );
}

extension MockzillaHttpResponseBridge on MockzillaHttpResponse {
  toApi() => ApiMockzillaHttpResponse(
        statusCode: statusCode,
        headers: headers,
        body: body,
      );
}

extension ApiEndpointConfigBridge on ApiEndpointConfig {
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
  ApiEndpointConfig toApi() => ApiEndpointConfig(
        name: name,
        key: key,
        failureProbability: failureProbability,
        delayMean: delayMean,
        delayVariance: delayVariance,
        webApiDefaultResponse: webApiDefaultResponse?.toApi(),
        webApiErrorResponse: webApiErrorResponse?.toApi(),
      );
}

extension ApiReleaseModeConfigBridge on ApiReleaseModeConfig {
  toDart() => ReleaseModeConfig(
        rateLimit: rateLimit,
        rateLimitRefillPeriod:
            Duration(milliseconds: rateLimitRefillPeriodMillis),
        tokenLifeSpan: Duration(milliseconds: tokenLifeSpanMillis),
      );
}

extension ReleaseModeConfigBridge on ReleaseModeConfig {
  toApi() => ApiReleaseModeConfig(
        rateLimit: rateLimit,
        rateLimitRefillPeriodMillis: rateLimitRefillPeriod.inMilliseconds,
        tokenLifeSpanMillis: tokenLifeSpan.inMilliseconds,
      );
}

extension ApiAuthHeaderBridge on ApiAuthHeader {
  toDart() => AuthHeader(
        key: key,
        value: value,
      );
}

extension AuthHeaderBridge on AuthHeader {
  toApi() => ApiAuthHeader(
        key: key,
        value: value,
      );
}

extension MockzillaConfigBridge on MockzillaConfig {
  toApi() => ApiMockzillaConfig(
        port: port,
        endpoints: endpoints
            .map(
              (endpoint) => endpoint.toApi(),
            )
            .toList(),
        isRelease: isRelease,
        localHostOnly: localHostOnly,
        logLevel: logLevel.toApi(),
        releaseModeConfig: releaseModeConfig.toApi(),
      );
}

extension ApiMockzillaConfigBridge on ApiMockzillaConfig {
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
