import 'package:flutter_test/flutter_test.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

void main() {
  group("Partial Mockzilla HTTP response unit tests", () {
    test("== - with equal operands - returns true", () {
      final operandA = PartialMockzillaHttpResponse(
        statusCode: 404,
        headers: {"Content-Type": "application/json"},
        body: '{"key": "value"}',
      );
      final operandB = PartialMockzillaHttpResponse(
        statusCode: 404,
        headers: {"Content-Type": "application/json"},
        body: '{"key": "value"}',
      );

      expect(operandA == operandB, true);
    });

    test("== - with differing operands - returns false", () {
      final operandA = PartialMockzillaHttpResponse(
        statusCode: 404,
        body: '{"key": "foo"}',
      );
      final operandB = PartialMockzillaHttpResponse(
        statusCode: null,
        body: '{"key": "baa"}',
      );

      expect(operandA == operandB, false);
    });

    test("hashCode - with equal operands - returns same hash", () {
      final operandA = PartialMockzillaHttpResponse(
        statusCode: 404,
        headers: {"Content-Type": "application/json"},
      );
      final operandB = PartialMockzillaHttpResponse(
        statusCode: 404,
        headers: {"Content-Type": "application/json"},
      );

      expect(operandA.hashCode, operandB.hashCode);
    });

    test("copyWith - no values - returns equal instance", () {
      final receiver = PartialMockzillaHttpResponse(statusCode: 200);

      expect(receiver.copyWith(), receiver);
    });

    test("copyWith - new values - returns instance with expected values", () {
      final receiver = PartialMockzillaHttpResponse(statusCode: 200);
      final actual = receiver.copyWith(
        statusCode: 404,
        body: "Baa",
        headers: {"Content-Type": "text/plain"},
      );
      final expected = PartialMockzillaHttpResponse(
        statusCode: 404,
        body: "Baa",
        headers: {"Content-Type": "text/plain"},
      );
      expect(actual, expected);
    });

    test("toString - returns expected", () {
      final receiver = PartialMockzillaHttpResponse(
        statusCode: 404,
        headers: {"Content-Type": "application/json"},
        body: '{"key": "value"}',
      );
      final expected =
          'PartialMockzillaHttpResponse('
          'statusCode=404, '
          'headers={Content-Type: application/json}, '
          'body={"key": "value"}'
          ')';
      expect(receiver.toString(), expected);
    });
  });
}
