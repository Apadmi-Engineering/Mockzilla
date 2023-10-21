import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(
  PigeonOptions(
    dartOut: "lib/src/messages.g.dart",
    dartOptions: DartOptions(),
    kotlinOut: "android/src/main/kotlin/com/apadmi/mockzilla/Messages.g.kt",
    kotlinOptions: KotlinOptions(),
  ),
)
enum HttpMethod {
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

enum LogLevel {
  debug,
  error,
  info,
  verbose,
  warn;
}

class MockzillaHttpRequest {
  final String uri;
  final Map<String?, String?> headers;
  final String body;
  final HttpMethod method;

  const MockzillaHttpRequest(
    this.uri,
    this.headers,
    this.method, [
    this.body = "",
  ]);
}

class MockzillaHttpResponse {
  final int statusCode;
  final Map<String?, String?> headers;
  final String body;

  const MockzillaHttpResponse([
    this.statusCode = 200,
    this.headers = const {},
    this.body = "",
  ]);
}

class EndpointConfig {
  final String name;
  final String key;
  final int? failureProbability;
  final int? delayMean;
  final int? delayVariance;
  final MockzillaHttpResponse? webApiDefaultResponse;
  final MockzillaHttpResponse? webApiErrorResponse;

  const EndpointConfig(
    this.name,
    this.key, [
    this.failureProbability,
    this.delayMean,
    this.delayVariance,
    this.webApiDefaultResponse,
    this.webApiErrorResponse,
  ]);
}

class ReleaseModeConfig {
  final int rateLimit;
  final Duration rateLimitRefillPeriod;
  final Duration tokenLifeSpan;

  const ReleaseModeConfig([
    this.rateLimit = 60,
    this.rateLimitRefillPeriod = const Duration(seconds: 60),
    this.tokenLifeSpan = const Duration(milliseconds: 500),
  ]);
}

class MockzillaLogger {
  final LogLevel logLevel;
  final String message;
  final String tag;
  final String? exception;

  const MockzillaLogger(this.logLevel, this.message, this.tag,
      [this.exception]);
}

class MockzillaConfig {
  final int port;
  final List<EndpointConfig?> endpoints;
  final bool isRelease;
  final bool localHostOnly;
  final LogLevel logLevel;
  final List<MockzillaLogger?> additionalLogWriters;

  const MockzillaConfig(
    this.port,
    this.endpoints,
    this.isRelease,
    this.localHostOnly,
    this.logLevel,
    this.additionalLogWriters,
  );
}

class MockzillaRuntimeParams {
  final MockzillaConfig config;
  final String mockBaseUrl;
  final String apiBaseUrl;
  final int port;

  const MockzillaRuntimeParams(
    this.config,
    this.mockBaseUrl,
    this.apiBaseUrl,
    this.port,
  );
}

class AuthHeader {
  final String key;
  final String value;

  const AuthHeader(
    this.key,
    this.value,
  );
}

@HostApi()
abstract class MockzillaHostApi {
  MockzillaRuntimeParams startServer(MockzillaConfig config);

  void stopServer();
}

@FlutterApi()
abstract class MockzillaFlutterApi {
  bool endpointMatcher(MockzillaHttpRequest request);

  MockzillaHttpResponse defaultHandler(MockzillaHttpRequest request);

  MockzillaHttpResponse errorHandler(MockzillaHttpRequest request);

  @async
  AuthHeader generateAuthHeader();
}
