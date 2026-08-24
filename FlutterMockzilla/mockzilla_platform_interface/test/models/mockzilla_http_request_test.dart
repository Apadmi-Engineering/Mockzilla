import 'package:flutter_test/flutter_test.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

void main() {
  group("Mockzilla HTTP request unit tests", () {
    test("== - with equal operands - returns true", () {
      final operandA = MockzillaHttpRequest(
        uri: "http://localhost:8080/local-mock/some-endpoint",
        method: HttpMethod.get,
        headers: {"Content-Type": "application/json"},
        body: '{"key": "value"}',
      );
      final operandB = MockzillaHttpRequest(
        uri: "http://localhost:8080/local-mock/some-endpoint",
        method: HttpMethod.get,
        headers: {"Content-Type": "application/json"},
        body: '{"key": "value"}',
      );

      expect(operandA == operandB, true);
    });

    test("== - with differing operands - returns false", () {
      final operandA = MockzillaHttpRequest(
        uri: "http://localhost:8080/local-mock/some-endpoint",
        method: HttpMethod.get,
        headers: {"Content-Type": "application/json"},
        body: '{"key": "foo"}',
      );
      final operandB = MockzillaHttpRequest(
        uri: "http://localhost:8080/local-mock/some-endpoont",
        method: HttpMethod.get,
        headers: {"Content-Type": "application/json"},
        body: '{"key": "baa"}',
      );

      expect(operandA == operandB, false);
    });

    test("copyWith - no values - returns equal instance", () {
      final receiver = MockzillaHttpRequest(
        uri: "http://localhost:8080/local-mock/some-endpoint",
        method: HttpMethod.post,
        body: "Foo",
      );

      expect(receiver.copyWith(), receiver);
    });

    test("copyWith - new values - returns instance with expected values", () {
      final receiver = MockzillaHttpRequest(
        uri: "http://localhost:8080/local-mock/some-endpoint",
        method: HttpMethod.post,
      );
      final actual = receiver.copyWith(
        uri: "http://localhost:8080/local-mock/another-endpoint",
        method: HttpMethod.patch,
        body: "Baa",
        headers: {"Content-Type": "text/plain"},
      );
      final expected = MockzillaHttpRequest(
        uri: "http://localhost:8080/local-mock/another-endpoint",
        method: HttpMethod.patch,
        body: "Baa",
        headers: {"Content-Type": "text/plain"},
      );
      expect(actual, expected);
    });

    test("toString - returns expected", () {
      final receiver = MockzillaHttpRequest(
        uri: "http://localhost:8080/local-mock/some-endpoint",
        method: HttpMethod.delete,
        headers: {"Content-Type": "application/json"},
        body: '{"key": "value"}',
      );
      final expected =
          'MockzillaHttpRequest('
          'uri=http://localhost:8080/local-mock/some-endpoint, '
          'method=HttpMethod.delete, '
          'headers={Content-Type: application/json}, '
          'body={"key": "value"}'
          ')';
      expect(receiver.toString(), expected);
    });
  });
}
