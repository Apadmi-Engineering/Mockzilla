import 'package:flutter/services.dart';
import 'package:mockzilla_android/src/api_utils.dart';
import 'package:mockzilla_android/src/messages.g.dart';
import 'package:mockzilla_android/src/model/mockzilla_error.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';
import 'package:meta/meta.dart';

class MockzillaAndroid extends MockzillaPlatform {
  final mockzillaHostBridge = MockzillaHostApi();

  @override
  Future<MockzillaRuntimeParams> startMockzilla(MockzillaConfig config) async {
    final callbackProvider = CallbackProvider(config.endpoints, config.loggers);
    MockzillaFlutterApi.setUp(callbackProvider);
    try {
      final bridgeParams =
          await mockzillaHostBridge.startServer(config.toBridge());

      /// As an alternative, we could use the endpoints in `config`, however
      /// using `callbackProvider` means that the returned runtime params and
      /// server will be consistent in using the cached endpoints. This will make
      /// debugging much easier.
      return bridgeParams.toDart(
          endpointMatcher: callbackProvider.flutterEndpointMatcher,
          defaultHandler: callbackProvider.flutterDefaultHandler,
          errorHandler: callbackProvider.flutterErrorHandler);
    } on PlatformException catch (exception) {
      if (exception.code == "PortConflictException") {
        throw MockzillaPortConflictException(config.port);
      }
      rethrow;
    }
  }

  @override
  stopMockzilla() => mockzillaHostBridge.stopServer();

  static void registerWith() {
    MockzillaPlatform.instance = MockzillaAndroid();
  }
}

class CallbackProvider extends MockzillaFlutterApi {
  final List<EndpointConfig> endpoints;
  final List<MockzillaLogger> loggers;

  CallbackProvider(
    this.endpoints,
    this.loggers,
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
  Future<bool> endpointMatcher(
      BridgeMockzillaHttpRequest request, String key) async {
    return _determineEndpoint(key).endpointMatcher(request.toDart());
  }

  /// Returns the default response for the endpoint associated with [key].
  @override
  Future<BridgeMockzillaHttpResponse> defaultHandler(
      BridgeMockzillaHttpRequest request, String key) async {
    final response =
        await _determineEndpoint(key).defaultHandler(request.toDart());
    return response.toBridge();
  }

  /// Returns the default error response for the endpoint associated with
  /// [key].
  @override
  Future<BridgeMockzillaHttpResponse> errorHandler(
      BridgeMockzillaHttpRequest request, String key) async {
    final response =
        await _determineEndpoint(key).errorHandler(request.toDart());
    return response.toBridge();
  }

  @override
  void log(
    BridgeLogLevel logLevel,
    String message,
    String tag,
    String? exception,
  ) {
    final mappedLogLevel = logLevel.toDart();
    final mappedException = switch (exception) {
      null => null,
      _ => MockzillaPlatformException(exception),
    };
    for (final logger in loggers) {
      logger.log(mappedLogLevel, message, tag, mappedException);
    }
  }
}

@internal
extension FlutterCallbackProvider on CallbackProvider {
  Future<bool> flutterEndpointMatcher(
      MockzillaHttpRequest request, String key) {
    return endpointMatcher(request.toBridge(), key);
  }

  Future<MockzillaHttpResponse> flutterDefaultHandler(
      MockzillaHttpRequest request, String key) {
    return defaultHandler(request.toBridge(), key).then((it) => it.toDart());
  }

  Future<MockzillaHttpResponse> flutterErrorHandler(
      MockzillaHttpRequest request, String key) {
    return errorHandler(request.toBridge(), key).then((it) => it.toDart());
  }
}
