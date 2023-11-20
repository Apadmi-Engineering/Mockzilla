import 'package:mockzilla_android/src/api_utils.dart';
import 'package:mockzilla_android/src/messages.g.dart';
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

  EndpointConfig _determineEndpoint(MockzillaHttpRequest request) =>
      endpoints.firstWhere(
        (endpoint) => endpoint.endpointMatcher(request),
      );

  @override
  String endpointMatcher(ApiMockzillaHttpRequest request) {
    return _determineEndpoint(request.toDart()).key;
  }

  @override
  ApiMockzillaHttpResponse defaultHandler(ApiMockzillaHttpRequest request) {
    return _determineEndpoint(request.toDart())
        .defaultHandler(request.toDart())
        .toApi();
  }

  @override
  ApiMockzillaHttpResponse errorHandler(ApiMockzillaHttpRequest request) {
    return _determineEndpoint(request.toDart())
        .errorHandler(request.toDart())
        .toApi();
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
