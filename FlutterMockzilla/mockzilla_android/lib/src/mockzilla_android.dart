import 'package:mockzilla_android/src/api_utils.dart';
import 'package:mockzilla_android/src/messages.g.dart';
import 'package:mockzilla_android/src/model/mockzilla_error.dart';
import 'package:mockzilla_android/src/utils/list_utils.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

class MockzillaAndroid extends MockzillaPlatform {
  final mockzillaHostBridge = MockzillaHostApi();

  @override
  Future<void> startMockzilla(MockzillaConfig config) {
    final callbackProvider = CallbackProvider(
      config.endpoints,
      () =>
          Future.value(const AuthHeader(key: "Authorization", value: "Bearer")),
    );
    MockzillaFlutterApi.setUp(callbackProvider);
    return mockzillaHostBridge.startServer(config.toBridge());
  }

  @override
  stopMockzilla() => mockzillaHostBridge.stopServer();

  static void registerWith() {
    MockzillaPlatform.instance = MockzillaAndroid();
  }
}

class CallbackProvider extends MockzillaFlutterApi {
  final List<EndpointConfig> endpoints;
  final Future<AuthHeader> Function() _generateAuthHeader;

  CallbackProvider(
    this.endpoints,
    this._generateAuthHeader,
  );

  /// Utility function to find a cached endpoint config with a given [key].
  /// This is used to determine which endpoint handler to use for matching,
  /// request handling etc. as Dart functions can't be nested in objects going
  /// across the pigeon bridge.
  ///
  /// The endpoints that are searched here are cached upon a call to
  /// [startMockzilla].
  EndpointConfig _determineEndpoint(String key) => endpoints.firstWhere(
        (endpoint) => endpoint.key == key,
        orElse: () => throw EndpointNotFoundError(key, StackTrace.current),
      );

  /// Calls the matcher on the specified endpoint.
  @override
  bool endpointMatcher(BridgeMockzillaHttpRequest request, String key) {
    return _determineEndpoint(key).endpointMatcher(request.toDart());
  }

  /// Returns the default response for the endpoint associated with [key].
  @override
  BridgeMockzillaHttpResponse defaultHandler(
      BridgeMockzillaHttpRequest request, String key) {
    return _determineEndpoint(key).defaultHandler(request.toDart()).toBridge();
  }

  /// Returns the default error response for the endpoint associated with
  /// [key].
  @override
  BridgeMockzillaHttpResponse errorHandler(
      BridgeMockzillaHttpRequest request, String key) {
    return _determineEndpoint(key).errorHandler(request.toDart()).toBridge();
  }

  @override
  Future<BridgeAuthHeader> generateAuthHeader() => _generateAuthHeader().then(
        (result) => result.toBridge(),
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
