import 'package:flutter_test/flutter_test.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

void main() {
  group("Mockzilla HTTP response unit tests", () {
    test("== - with equal operands - returns true", () {
      final operandA = MockzillaHttpResponse(
        statusCode: 404,
        headers: {"Content-Type": "application/json"},
        body: '{"key": "value"}',
      );
      final operandB = MockzillaHttpResponse(
        statusCode: 404,
        headers: {"Content-Type": "application/json"},
        body: '{"key": "value"}',
      );

      expect(operandA == operandB, true);
    });

    test("== - with differing operands - returns false", () {
      final operandA = MockzillaHttpResponse(
        statusCode: 404,
        headers: {"Content-Type": "application/json"},
        body: '{"key": "foo"}',
      );
      final operandB = MockzillaHttpResponse(
        statusCode: 500,
        headers: {"Content-Type": "application/json"},
        body: '{"key": "baa"}',
      );

      expect(operandA == operandB, false);
    });

    test("hashCode - with equal operands - returns same hash", () {
      final operandA = MockzillaHttpResponse(
        statusCode: 404,
        headers: {"Content-Type": "application/json"},
        body: '{"key": "value"}',
      );
      final operandB = MockzillaHttpResponse(
        statusCode: 404,
        headers: {"Content-Type": "application/json"},
        body: '{"key": "value"}',
      );

      expect(operandA.hashCode, operandB.hashCode);
    });

    test("copyWith - no values - returns equal instance", () {
      final receiver = MockzillaHttpResponse(statusCode: 200, body: "Foo");

      expect(receiver.copyWith(), receiver);
    });

    test("copyWith - new values - returns instance with expected values", () {
      final receiver = MockzillaHttpResponse(statusCode: 200);
      final actual = receiver.copyWith(
        statusCode: 404,
        body: "Baa",
        headers: {"Content-Type": "text/plain"},
      );
      final expected = MockzillaHttpResponse(
        statusCode: 404,
        body: "Baa",
        headers: {"Content-Type": "text/plain"},
      );
      expect(actual, expected);
    });

    test("toString - returns expected", () {
      final receiver = MockzillaHttpResponse(
        statusCode: 404,
        headers: {"Content-Type": "application/json"},
        body: '{"key": "value"}',
      );
      final expected =
          'MockzillaHttpResponse('
          'statusCode=404, '
          'headers={Content-Type: application/json}, '
          'body={"key": "value"}'
          ')';
      expect(receiver.toString(), expected);
    });
  });
}
