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
  options,
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
  final int rateLimitRefillPeriodMillis;
  final int tokenLifeSpanMillis;

  const ApiReleaseModeConfig([
    this.rateLimit = 60,
    this.rateLimitRefillPeriodMillis = 60000,
    this.tokenLifeSpanMillis = 500,
  ]);
}

class ApiMockzillaConfig {
  final int port;
  final List<ApiEndpointConfig?> endpoints;
  final bool isRelease;
  final bool localHostOnly;
  final ApiLogLevel logLevel;
  final ApiReleaseModeConfig releaseModeConfig;

  const ApiMockzillaConfig(
    this.port,
    this.endpoints,
    this.isRelease,
    this.localHostOnly,
    this.logLevel,
    this.releaseModeConfig,
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
  String endpointMatcher(ApiMockzillaHttpRequest request);

  ApiMockzillaHttpResponse defaultHandler(ApiMockzillaHttpRequest request);

  ApiMockzillaHttpResponse errorHandler(ApiMockzillaHttpRequest request);

  @async
  ApiAuthHeader generateAuthHeader();

  void log(
    ApiLogLevel logLevel,
    String message,
    String tag,
    String? exception,
  );
}
