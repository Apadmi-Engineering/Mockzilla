import 'package:flutter_test/flutter_test.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

void main() {
  group("Mockzilla runtime params unit tests", () {
    test("== - with equal operands - returns true", () {
      final operandA = MockzillaRuntimeParams(
        config: MockzillaConfig(),
        mockBaseUrl: "http://localhost:8080/local-mock",
        apiBaseUrl: "http://localhost:8080/api",
        port: 8080,
      );
      final operandB = MockzillaRuntimeParams(
        config: MockzillaConfig(),
        mockBaseUrl: "http://localhost:8080/local-mock",
        apiBaseUrl: "http://localhost:8080/api",
        port: 8080,
      );

      expect(operandA == operandB, true);
    });

    test("== - with differing operands - returns false", () {
      final operandA = MockzillaRuntimeParams(
        config: MockzillaConfig(),
        mockBaseUrl: "http://localhost:8080/local-mock",
        apiBaseUrl: "http://localhost:8080/api",
        port: 8080,
      );
      final operandB = MockzillaRuntimeParams(
        config: MockzillaConfig(port: 9090),
        mockBaseUrl: "http://localhost:9090/local-mock",
        apiBaseUrl: "http://localhost:9090/api",
        port: 9090,
      );

      expect(operandA == operandB, false);
    });

    test("hashCode - with equal operands - returns same hash", () {
      final operandA = MockzillaRuntimeParams(
        config: MockzillaConfig(),
        mockBaseUrl: "http://localhost:8080/local-mock",
        apiBaseUrl: "http://localhost:8080/api",
        port: 8080,
      );
      final operandB = MockzillaRuntimeParams(
        config: MockzillaConfig(),
        mockBaseUrl: "http://localhost:8080/local-mock",
        apiBaseUrl: "http://localhost:8080/api",
        port: 8080,
      );

      expect(operandA.hashCode, operandB.hashCode);
    });

    test("copyWith - no values - returns equal instance", () {
      final receiver = MockzillaRuntimeParams(
        config: MockzillaConfig(),
        mockBaseUrl: "http://localhost:8080/local-mock",
        apiBaseUrl: "http://localhost:8080/api",
        port: 8080,
      );

      expect(receiver.copyWith(), receiver);
    });

    test("copyWith - new values - returns instance with expected values", () {
      final receiver = MockzillaRuntimeParams(
        config: MockzillaConfig(),
        mockBaseUrl: "http://localhost:8080/local-mock",
        apiBaseUrl: "http://localhost:8080/api",
        port: 8080,
      );
      final actual = receiver.copyWith(
        config: MockzillaConfig(port: 9090),
        mockBaseUrl: "http://localhost:9090/local-mock",
        apiBaseUrl: "http://localhost:9090/api",
        port: 9090,
      );
      final expected = MockzillaRuntimeParams(
        config: MockzillaConfig(port: 9090),
        mockBaseUrl: "http://localhost:9090/local-mock",
        apiBaseUrl: "http://localhost:9090/api",
        port: 9090,
      );
      expect(actual, expected);
    });

    test("copyWith - config extension - returns expected values", () {
      final receiver = MockzillaRuntimeParams(
        config: MockzillaConfig(),
        mockBaseUrl: "http://localhost:8080/local-mock",
        apiBaseUrl: "http://localhost:8080/api",
        port: 8080,
      );
      final actual = receiver.copyWith.config(
        port: 9090,
        logLevel: LogLevel.assertion,
        isNetworkDiscoveryEnabled: false,
      );
      final expected = MockzillaRuntimeParams(
        config: MockzillaConfig(
          port: 9090,
          logLevel: LogLevel.assertion,
          isNetworkDiscoveryEnabled: false,
        ),
        mockBaseUrl: "http://localhost:8080/local-mock",
        apiBaseUrl: "http://localhost:8080/api",
        port: 8080,
      );

      expect(actual, expected);
    });

    test("toString - returns expected", () {
      final receiver = MockzillaRuntimeParams(
        config: MockzillaConfig(),
        mockBaseUrl: "http://localhost:8080/local-mock",
        apiBaseUrl: "http://localhost:8080/api",
        port: 8080,
      );
      final expected =
          'MockzillaRuntimeParams('
          'config=${MockzillaConfig()}, '
          'mockBaseUrl=http://localhost:8080/local-mock, '
          'apiBaseUrl=http://localhost:8080/api, '
          'port=8080'
          ')';
      expect(receiver.toString(), expected);
    });
  });
}
