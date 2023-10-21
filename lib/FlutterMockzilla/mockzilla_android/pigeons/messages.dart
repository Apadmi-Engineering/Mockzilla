import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(
  PigeonOptions(
    dartOut: "lib/src/messages.g.dart",
    dartOptions: DartOptions(),
    kotlinOut: "android/src/main/kotlin/com/apadmi/mockzilla/Messages.g.kt",
    kotlinOptions: KotlinOptions(),
  ),
)
enum ApiHttpMethod {
  get,
  head,
  post,
  put,
  delete,
  connect,
  options,
  trace,
  patch;
}

enum ApiLogLevel {
  debug,
  error,
  info,
  verbose,
  warn;
}

class ApiMockzillaHttpRequest {
  final String uri;
  final Map<String?, String?> headers;
  final String body;
  final ApiHttpMethod method;

  const ApiMockzillaHttpRequest(
    this.uri,
    this.headers,
    this.method, [
    this.body = "",
  ]);
}

class ApiMockzillaHttpResponse {
  final int statusCode;
  final Map<String?, String?> headers;
  final String body;

  const ApiMockzillaHttpResponse([
    this.statusCode = 200,
    this.headers = const {},
    this.body = "",
  ]);
}

class ApiEndpointConfig {
  final String name;
  final String key;
  final int? failureProbability;
  final int? delayMean;
  final int? delayVariance;
  final ApiMockzillaHttpResponse? webApiDefaultResponse;
  final ApiMockzillaHttpResponse? webApiErrorResponse;

  const ApiEndpointConfig(
    this.name,
    this.key, [
    this.failureProbability,
    this.delayMean,
    this.delayVariance,
    this.webApiDefaultResponse,
    this.webApiErrorResponse,
  ]);
}

class ApiReleaseModeConfig {
  final int rateLimit;
  final Duration rateLimitRefillPeriod;
  final Duration tokenLifeSpan;

  const ApiReleaseModeConfig([
    this.rateLimit = 60,
    this.rateLimitRefillPeriod = const Duration(seconds: 60),
    this.tokenLifeSpan = const Duration(milliseconds: 500),
  ]);
}

class ApiMockzillaLogger {
  final ApiLogLevel logLevel;
  final String message;
  final String tag;
  final String? exception;

  const ApiMockzillaLogger(this.logLevel, this.message, this.tag,
      [this.exception]);
}

class ApiMockzillaConfig {
  final int port;
  final List<ApiEndpointConfig?> endpoints;
  final bool isRelease;
  final bool localHostOnly;
  final ApiLogLevel logLevel;
  final List<ApiMockzillaLogger?> additionalLogWriters;

  const ApiMockzillaConfig(
    this.port,
    this.endpoints,
    this.isRelease,
    this.localHostOnly,
    this.logLevel,
    this.additionalLogWriters,
  );
}

class ApiMockzillaRuntimeParams {
  final ApiMockzillaConfig config;
  final String mockBaseUrl;
  final String apiBaseUrl;
  final int port;

  const ApiMockzillaRuntimeParams(
    this.config,
    this.mockBaseUrl,
    this.apiBaseUrl,
    this.port,
  );
}

class ApiAuthHeader {
  final String key;
  final String value;

  const ApiAuthHeader(
    this.key,
    this.value,
  );
}

@HostApi()
abstract class MockzillaHostApi {
  ApiMockzillaRuntimeParams startServer(ApiMockzillaConfig config);

  void stopServer();
}

@FlutterApi()
abstract class MockzillaFlutterApi {
  bool endpointMatcher(ApiMockzillaHttpRequest request);

  ApiMockzillaHttpResponse defaultHandler(ApiMockzillaHttpRequest request);

  ApiMockzillaHttpResponse errorHandler(ApiMockzillaHttpRequest request);

  @async
  ApiAuthHeader generateAuthHeader();
}
