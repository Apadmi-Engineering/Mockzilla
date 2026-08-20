import 'package:flutter_test/flutter_test.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

DashboardOverridePreset _preset(String name) => DashboardOverridePreset(
  name: name,
  description: null,
  response: PartialMockzillaHttpResponse(),
);

void main() {
  group("Dashboard options config unit tests", () {
    test("== - with equal operands - returns true", () {
      final operandA = DashboardOptionsConfig(presets: [_preset("A")]);
      final operandB = DashboardOptionsConfig(presets: [_preset("A")]);

      expect(operandA == operandB, true);
    });

    test("== - with differing operands - returns false", () {
      final operandA = DashboardOptionsConfig(presets: [_preset("A")]);
      final operandB = DashboardOptionsConfig(presets: [_preset("B")]);

      expect(operandA == operandB, false);
    });

    test("hashCode - with equal operands - returns same hash", () {
      final operandA = DashboardOptionsConfig(presets: [_preset("A")]);
      final operandB = DashboardOptionsConfig(presets: [_preset("A")]);

      expect(operandA.hashCode, operandB.hashCode);
    });

    test("copyWith - no values - returns equal instance", () {
      final receiver = DashboardOptionsConfig(presets: [_preset("A")]);

      expect(receiver.copyWith(), receiver);
    });

    test("copyWith - new values - returns instance with expected values", () {
      final receiver = DashboardOptionsConfig(presets: [_preset("A")]);
      final actual = receiver.copyWith(
        // ignore: deprecated_member_use_from_same_package
        successPresets: [_preset("Success")],
        // ignore: deprecated_member_use_from_same_package
        errorPresets: [_preset("Error")],
        presets: [_preset("B")],
      );
      final expected = DashboardOptionsConfig(
        // ignore: deprecated_member_use_from_same_package
        successPresets: [_preset("Success")],
        // ignore: deprecated_member_use_from_same_package
        errorPresets: [_preset("Error")],
        presets: [_preset("B")],
      );
      expect(actual, expected);
    });

    test("toString - returns expected", () {
      final receiver = DashboardOptionsConfig(presets: [_preset("A")]);
      final expected =
          'DashboardOptionsConfig('
          'successPresets=[], errorPresets=[], '
          'presets=[DashboardOverridePreset('
          'name=A, description=null, '
          'response=PartialMockzillaHttpResponse('
          'statusCode=null, headers=null, body=null), type=null)]'
          ')';
      expect(receiver.toString(), expected);
    });
  });
}
