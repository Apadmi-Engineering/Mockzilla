import 'package:mockzilla_ios/src/api_utils.dart';
import 'package:mockzilla_ios/src/messages.g.dart';
import 'package:mockzilla_ios/src/utils/list_utils.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

class MockzillaIos extends MockzillaPlatform {
  final mockzillaHostBridge = MockzillaHostApi();

  @override
  Future<void> startMockzilla(MockzillaConfig config) {
    final callbackProvider = CallbackProvider(
      config.endpoints,
      () => Future.value(
        BridgeAuthHeader(key: "Authorization", value: "Bearer"),
      ),
    );
    MockzillaFlutterApi.setUp(callbackProvider);
    return mockzillaHostBridge.startServer(config.toBridge());
  }

  @override
  stopMockzilla() => mockzillaHostBridge.stopServer();

  static void registerWith() {
    MockzillaPlatform.instance = MockzillaIos();
  }
}

class CallbackProvider extends MockzillaFlutterApi {
  final List<EndpointConfig> endpoints;
  final Future<BridgeAuthHeader> Function() _generateAuthHeader;

  CallbackProvider(
    this.endpoints,
    this._generateAuthHeader,
  );

  /// Used to resolve the endpoint matching the specified key.
  EndpointConfig? _determineEndpoint(String key) => endpoints.firstWhereOrNull(
        (endpoint) => endpoint.key == key,
      );

  /// Calls the matcher on the specified endpoint.
  @override
  bool endpointMatcher(BridgeMockzillaHttpRequest request, String key) {
    return _determineEndpoint(key)?.endpointMatcher(request.toDart()) ?? false;
  }

  /// Returns the default response for the endpoint associated with [key].
  @override
  BridgeMockzillaHttpResponse defaultHandler(
      BridgeMockzillaHttpRequest request, String key) {
    return _determineEndpoint(key)?.defaultHandler(request.toDart()).toBridge();
  }

  /// Returns the default error response for the endpoint associated with
  /// [key].
  @override
  BridgeMockzillaHttpResponse errorHandler(
      BridgeMockzillaHttpRequest request, String key) {
    return _determineEndpoint(key)?.errorHandler(request.toDart()).toBridge();
  }

  @override
  Future<BridgeAuthHeader> generateAuthHeader() => _generateAuthHeader().then(
        (result) => result.toDart(),
      );

  @override
  void log(
    BridgeLogLevel logLevel,
    String message,
    String tag,
    String? exception,
  ) {
    /* TODO: Implement */
  }
}
