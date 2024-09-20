import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(
  PigeonOptions(
    dartOut: "lib/src/messages.g.dart",
    dartOptions: DartOptions(),
    dartTestOut: "test/messages_test.g.dart",
    swiftOut: "ios/Classes/Messages.g.swift",
    swiftOptions: SwiftOptions(),
  ),
)
@TaskQueue(type: TaskQueueType.serialBackgroundThread)
enum BridgeHttpMethod {
  get,
  head,
  post,
  put,
  delete,
  options,
  patch;
}

enum BridgeLogLevel {
  debug,
  error,
  info,
  verbose,
  warn,
  assertion;
}

class BridgeMockzillaHttpRequest {
  final String uri;
  final Map<String?, String?> headers;
  final String body;
  final BridgeHttpMethod method;

  const BridgeMockzillaHttpRequest(
    this.uri,
    this.headers,
    this.method, [
    this.body = "",
  ]);
}

class BridgeMockzillaHttpResponse {
  final int statusCode;
  final Map<String?, String?> headers;
  final String body;

  const BridgeMockzillaHttpResponse([
    this.statusCode = 200,
    this.headers = const {},
    this.body = "",
  ]);
}
class BridgeDashboardOverridePreset {
  final String name;
  final String? description;
  final BridgeMockzillaHttpResponse response;

  const BridgeDashboardOverridePreset({
    required this.name,
    this.description,
    required this.response,
  });
}

class BridgeDashboardOptionsConfig {
  final List<BridgeDashboardOverridePreset?> successPresets;
  final List<BridgeDashboardOverridePreset?> errorPresets;

  const BridgeDashboardOptionsConfig({
    required this.successPresets,
    required this.errorPresets,
  });
}

class BridgeEndpointConfig {
  final String name;
  final String key;
  final bool shouldFail;
  final int? delay;
  final int versionCode;
  final BridgeDashboardOptionsConfig config;

  const BridgeEndpointConfig({
    required this.name,
    required this.key,
    required this.shouldFail,
    required this.delay,
    required this.versionCode,
    required this.config,
  });
}

class BridgeReleaseModeConfig {
  final int rateLimit;
  final int rateLimitRefillPeriodMillis;
  final int tokenLifeSpanMillis;

  const BridgeReleaseModeConfig([
    this.rateLimit = 60,
    this.rateLimitRefillPeriodMillis = 60000,
    this.tokenLifeSpanMillis = 500,
  ]);
}

class BridgeMockzillaConfig {
  final int port;
  final List<BridgeEndpointConfig?> endpoints;
  final bool isRelease;
  final bool localHostOnly;
  final BridgeLogLevel logLevel;
  final BridgeReleaseModeConfig releaseModeConfig;

  const BridgeMockzillaConfig(
    this.port,
    this.endpoints,
    this.isRelease,
    this.localHostOnly,
    this.logLevel,
    this.releaseModeConfig,
  );
}

class BridgeMockzillaRuntimeParams {
  final BridgeMockzillaConfig config;
  final String mockBaseUrl;
  final String apiBaseUrl;
  final int port;

  const BridgeMockzillaRuntimeParams(
    this.config,
    this.mockBaseUrl,
    this.apiBaseUrl,
    this.port,
  );
}

class BridgeAuthHeader {
  final String key;
  final String value;

  const BridgeAuthHeader(
    this.key,
    this.value,
  );
}

@HostApi()
abstract class MockzillaHostApi {
  BridgeMockzillaRuntimeParams startServer(BridgeMockzillaConfig config);

  void stopServer();
}

@FlutterApi()
abstract class MockzillaFlutterApi {
  bool endpointMatcher(BridgeMockzillaHttpRequest request, String key);

  BridgeMockzillaHttpResponse defaultHandler(
      BridgeMockzillaHttpRequest request, String key);

  BridgeMockzillaHttpResponse errorHandler(
      BridgeMockzillaHttpRequest request, String key);

  @async
  BridgeAuthHeader generateAuthHeader();

  void log(
    BridgeLogLevel logLevel,
    String message,
    String tag,
    String? exception,
  );
}
