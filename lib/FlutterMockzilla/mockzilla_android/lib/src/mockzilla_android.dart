import 'package:mockzilla_android/src/api_utils.dart';
import 'package:mockzilla_android/src/messages.g.dart';
import 'package:mockzilla_android/src/utils/list_utils.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

class MockzillaAndroid extends MockzillaPlatform {
  final mockzillaHostApi = MockzillaHostApi();

  @override
  Future<void> startMockzilla(MockzillaConfig config) {
    final callbackProvider = CallbackProvider(
      config.endpoints,
      () => Future.value(ApiAuthHeader(key: "Authorization", value: "Bearer")),
      config.additionalLogWriters,
    );
    MockzillaFlutterApi.setup(callbackProvider);
    return mockzillaHostApi.startServer(config.toApi());
  }

  @override
  stopMockzilla() => mockzillaHostApi.stopServer();

  static void registerWith() {
    MockzillaPlatform.instance = MockzillaAndroid();
  }
}

class CallbackProvider extends MockzillaFlutterApi {
  final List<EndpointConfig> endpoints;
  final Future<ApiAuthHeader> Function() _generateAuthHeader;
  final List<MockzillaLogger> _additionalLoggers;

  CallbackProvider(
    this.endpoints,
    this._generateAuthHeader,
    this._additionalLoggers,
  );

  /// Used to resolve the endpoint matching the specified key.
  EndpointConfig? _determineEndpoint(String key) => endpoints.firstWhereOrNull(
        (endpoint) => endpoint.key == key,
      );

  /// Calls the matcher on the specified endpoint.
  @override
  bool endpointMatcher(ApiMockzillaHttpRequest request, String key) {
    return _determineEndpoint(key)?.endpointMatcher(request.toDart()) ?? false;
  }

  /// Returns the default response for the endpoint associated with [key].
  @override
  ApiMockzillaHttpResponse defaultHandler(
      ApiMockzillaHttpRequest request, String key) {
    return _determineEndpoint(key)?.defaultHandler(request.toDart()).toApi();
  }

  /// Returns the default error response for the endpoint associated with
  /// [key].
  @override
  ApiMockzillaHttpResponse errorHandler(
      ApiMockzillaHttpRequest request, String key) {
    return _determineEndpoint(key)?.errorHandler(request.toDart()).toApi();
  }

  @override
  Future<ApiAuthHeader> generateAuthHeader() => _generateAuthHeader().then(
        (result) => result.toDart(),
      );

  @override
  void log(
    ApiLogLevel logLevel,
    String message,
    String tag,
    String? exception,
  ) {
    for (final logger in _additionalLoggers) {
      logger.log(
        logLevel.toDart(),
        message,
        tag,
        exception != null ? Exception(exception) : null,
      );
    }
  }
}
