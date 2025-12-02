import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(
  PigeonOptions(
    dartOut: "lib/src/messages.g.dart",
    dartOptions: DartOptions(),
    swiftOut: "ios/mockzilla_ios/Sources/mockzilla_ios/Messages.g.swift",
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

enum BridgeDashboardOverridePresetType {
  clientError,
  informational,
  other,
  redirect,
  serverError,
  success,
}

class BridgeMockzillaHttpRequest {
  final String uri;
  final Map<String, String> headers;
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
  final Map<String, String> headers;
  final String body;

  const BridgeMockzillaHttpResponse([
    this.statusCode = 200,
    this.headers = const {},
    this.body = "",
  ]);
}

class BridgePartialMockzillaHttpResponse {
  final int? statusCode;
  final Map<String, String>? headers;
  final String? body;

  const BridgePartialMockzillaHttpResponse([
    this.statusCode,
    this.headers,
    this.body,
  ]);
}

class BridgeDashboardOverridePreset {
  final String name;
  final String? description;
  final BridgePartialMockzillaHttpResponse response;
  final BridgeDashboardOverridePresetType? type;

  const BridgeDashboardOverridePreset(
      {required this.name,
      this.description,
      required this.response,
      required this.type});
}

class BridgeDashboardOptionsConfig {
  final List<BridgeDashboardOverridePreset> presets;

  const BridgeDashboardOptionsConfig({
    required this.presets,
  });
}

class BridgeEndpointConfig {
  final String name;
  final String key;
  final bool shouldFail;
  final int delayMs;
  final int versionCode;
  final BridgeDashboardOptionsConfig config;

  const BridgeEndpointConfig({
    required this.name,
    required this.key,
    required this.shouldFail,
    required this.delayMs,
    required this.versionCode,
    required this.config,
  });
}

class BridgeMockzillaConfig {
  final int port;
  final List<BridgeEndpointConfig> endpoints;
  final bool localHostOnly;
  final BridgeLogLevel logLevel;
  final bool isNetworkDiscoveryEnabled;

  const BridgeMockzillaConfig(
    this.port,
    this.endpoints,
    this.localHostOnly,
    this.logLevel,
    this.isNetworkDiscoveryEnabled,
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

@HostApi()
abstract class MockzillaHostApi {
  BridgeMockzillaRuntimeParams startServer(BridgeMockzillaConfig config);

  void stopServer();
}

@FlutterApi()
abstract class MockzillaFlutterApi {
  @async
  bool endpointMatcher(BridgeMockzillaHttpRequest request, String key);

  @async
  BridgeMockzillaHttpResponse defaultHandler(
      BridgeMockzillaHttpRequest request, String key);

  @async
  BridgeMockzillaHttpResponse errorHandler(
      BridgeMockzillaHttpRequest request, String key);

  void log(
    BridgeLogLevel logLevel,
    String message,
    String tag,
    String? exception,
  );
}
