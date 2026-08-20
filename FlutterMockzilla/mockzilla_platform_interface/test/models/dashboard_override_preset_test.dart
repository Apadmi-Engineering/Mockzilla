import 'package:flutter_test/flutter_test.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

void main() {
  group("Dashboard override preset unit tests", () {
    test("== - with equal operands - returns true", () {
      final operandA = DashboardOverridePreset(
        name: "Error Preset",
        description: "An error preset",
        response: PartialMockzillaHttpResponse(statusCode: 404),
        type: DashboardOverridePresetType.clientError,
      );
      final operandB = DashboardOverridePreset(
        name: "Error Preset",
        description: "An error preset",
        response: PartialMockzillaHttpResponse(statusCode: 404),
        type: DashboardOverridePresetType.clientError,
      );

      expect(operandA == operandB, true);
    });

    test("== - with differing operands - returns false", () {
      final operandA = DashboardOverridePreset(
        name: "Error Preset",
        description: "An error preset",
        response: PartialMockzillaHttpResponse(statusCode: 404),
        type: DashboardOverridePresetType.clientError,
      );
      final operandB = DashboardOverridePreset(
        name: "Success Preset",
        description: "A success preset",
        response: PartialMockzillaHttpResponse(statusCode: 200),
        type: DashboardOverridePresetType.success,
      );

      expect(operandA == operandB, false);
    });

    test("hashCode - with equal operands - returns same hash", () {
      final operandA = DashboardOverridePreset(
        name: "Error Preset",
        description: "An error preset",
        response: PartialMockzillaHttpResponse(statusCode: 404),
        type: DashboardOverridePresetType.clientError,
      );
      final operandB = DashboardOverridePreset(
        name: "Error Preset",
        description: "An error preset",
        response: PartialMockzillaHttpResponse(statusCode: 404),
        type: DashboardOverridePresetType.clientError,
      );

      expect(operandA.hashCode, operandB.hashCode);
    });

    test("copyWith - no values - returns equal instance", () {
      final receiver = DashboardOverridePreset(
        name: "Error Preset",
        description: "An error preset",
        response: PartialMockzillaHttpResponse(statusCode: 404),
      );

      expect(receiver.copyWith(), receiver);
    });

    test("copyWith - new values - returns instance with expected values", () {
      final receiver = DashboardOverridePreset(
        name: "Error Preset",
        description: "An error preset",
        response: PartialMockzillaHttpResponse(statusCode: 404),
      );
      final actual = receiver.copyWith(
        name: "Success Preset",
        description: "A success preset",
        response: PartialMockzillaHttpResponse(statusCode: 200),
        type: DashboardOverridePresetType.success,
      );
      final expected = DashboardOverridePreset(
        name: "Success Preset",
        description: "A success preset",
        response: PartialMockzillaHttpResponse(statusCode: 200),
        type: DashboardOverridePresetType.success,
      );
      expect(actual, expected);
    });

    test("toString - returns expected", () {
      final receiver = DashboardOverridePreset(
        name: "Error Preset",
        description: "An error preset",
        response: PartialMockzillaHttpResponse(statusCode: 404),
        type: DashboardOverridePresetType.clientError,
      );
      final expected =
          'DashboardOverridePreset('
          'name=Error Preset, '
          'description=An error preset, '
          'response=PartialMockzillaHttpResponse('
          'statusCode=404, headers=null, body=null), '
          'type=DashboardOverridePresetType.clientError'
          ')';
      expect(receiver.toString(), expected);
    });
  });
}
