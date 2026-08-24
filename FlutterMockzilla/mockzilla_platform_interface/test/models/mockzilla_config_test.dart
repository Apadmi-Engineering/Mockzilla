import 'package:flutter_test/flutter_test.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

class _FakeLogger implements MockzillaLogger {
  @override
  void log(LogLevel level, String message, String tag, Exception? exception) {}
}

Future<bool> _matcher(MockzillaHttpRequest request) async => true;

Future<MockzillaHttpResponse> _defaultHandler(
  MockzillaHttpRequest request,
) async => MockzillaHttpResponse();

Future<MockzillaHttpResponse> _errorHandler(
  MockzillaHttpRequest request,
) async => MockzillaHttpResponse(statusCode: 500);

EndpointConfig _endpoint(String name) => EndpointConfig(
  name: name,
  endpointMatcher: _matcher,
  defaultHandler: _defaultHandler,
  errorHandler: _errorHandler,
);

void main() {
  group("Mockzilla config unit tests", () {
    test("== - with equal operands - returns true", () {
      final operandA = MockzillaConfig(port: 8080, endpoints: [_endpoint("A")]);
      final operandB = MockzillaConfig(port: 8080, endpoints: [_endpoint("A")]);

      expect(operandA == operandB, true);
    });

    test("== - with differing operands - returns false", () {
      final operandA = MockzillaConfig(port: 8080, endpoints: [_endpoint("A")]);
      final operandB = MockzillaConfig(port: 9090, endpoints: [_endpoint("B")]);

      expect(operandA == operandB, false);
    });

    test("hashCode - with equal operands - returns same hash", () {
      final operandA = MockzillaConfig(port: 8080, endpoints: [_endpoint("A")]);
      final operandB = MockzillaConfig(port: 8080, endpoints: [_endpoint("A")]);

      expect(operandA.hashCode, operandB.hashCode);
    });

    test("copyWith - no values - returns equal instance", () {
      final receiver = MockzillaConfig(port: 8080, endpoints: [_endpoint("A")]);

      expect(receiver.copyWith(), receiver);
    });

    test("copyWith - new values - returns instance with expected values", () {
      final logger = _FakeLogger();
      final receiver = MockzillaConfig();
      final actual = receiver.copyWith(
        port: 9090,
        endpoints: [_endpoint("A")],
        localHostOnly: true,
        logLevel: LogLevel.debug,
        isNetworkDiscoveryEnabled: false,
        loggers: [logger],
      );
      final expected = MockzillaConfig(
        port: 9090,
        endpoints: [_endpoint("A")],
        localHostOnly: true,
        logLevel: LogLevel.debug,
        isNetworkDiscoveryEnabled: false,
        loggers: [logger],
      );
      expect(actual, expected);
    });

    test("toString - returns expected", () {
      final receiver = MockzillaConfig(port: 8080, endpoints: const []);
      final expected =
          'MockzillaConfig('
          'port=8080, endpoints=[], localHostOnly=false, '
          'logLevel=LogLevel.info, isNetworkDiscoveryEnabled=true, '
          'loggers=[]'
          ')';
      expect(receiver.toString(), expected);
    });
  });
}
