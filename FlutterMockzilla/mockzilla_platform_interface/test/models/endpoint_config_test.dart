import 'package:flutter_test/flutter_test.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

Future<bool> _matcher(MockzillaHttpRequest request) async => true;

Future<MockzillaHttpResponse> _defaultHandler(
  MockzillaHttpRequest request,
) async => MockzillaHttpResponse();

Future<MockzillaHttpResponse> _errorHandler(
  MockzillaHttpRequest request,
) async => MockzillaHttpResponse(statusCode: 500);

void main() {
  group("Endpoint config unit tests", () {
    test("== - with equal operands - returns true", () {
      final operandA = EndpointConfig(
        name: "Some endpoint",
        endpointMatcher: _matcher,
        defaultHandler: _defaultHandler,
        errorHandler: _errorHandler,
      );
      final operandB = EndpointConfig(
        name: "Some endpoint",
        endpointMatcher: _matcher,
        defaultHandler: _defaultHandler,
        errorHandler: _errorHandler,
      );

      expect(operandA == operandB, true);
    });

    test("== - with differing operands - returns false", () {
      final operandA = EndpointConfig(
        name: "Some endpoint",
        endpointMatcher: _matcher,
        defaultHandler: _defaultHandler,
        errorHandler: _errorHandler,
      );
      final operandB = EndpointConfig(
        name: "Some other endpoint",
        shouldFail: true,
        endpointMatcher: _matcher,
        defaultHandler: _defaultHandler,
        errorHandler: _errorHandler,
      );

      expect(operandA == operandB, false);
    });

    test("hashCode - with equal operands - returns same hash", () {
      final operandA = EndpointConfig(
        name: "Some endpoint",
        endpointMatcher: _matcher,
        defaultHandler: _defaultHandler,
        errorHandler: _errorHandler,
      );
      final operandB = EndpointConfig(
        name: "Some endpoint",
        endpointMatcher: _matcher,
        defaultHandler: _defaultHandler,
        errorHandler: _errorHandler,
      );

      expect(operandA.hashCode, operandB.hashCode);
    });

    test("copyWith - no values - returns equal instance", () {
      final receiver = EndpointConfig(
        name: "Some endpoint",
        endpointMatcher: _matcher,
        defaultHandler: _defaultHandler,
        errorHandler: _errorHandler,
      );

      expect(receiver.copyWith(), receiver);
    });

    test("copyWith - new values - returns instance with expected values", () {
      final receiver = EndpointConfig(
        name: "Some endpoint",
        endpointMatcher: _matcher,
        defaultHandler: _defaultHandler,
        errorHandler: _errorHandler,
      );
      final actual = receiver.copyWith(
        name: "Some other endpoint",
        customKey: "custom-key",
        shouldFail: true,
        delay: const Duration(milliseconds: 500),
        versionCode: 2,
        dashboardOptionsConfig: DashboardOptionsConfig(),
      );
      final expected = EndpointConfig(
        name: "Some other endpoint",
        customKey: "custom-key",
        shouldFail: true,
        delay: const Duration(milliseconds: 500),
        versionCode: 2,
        endpointMatcher: _matcher,
        dashboardOptionsConfig: DashboardOptionsConfig(),
        defaultHandler: _defaultHandler,
        errorHandler: _errorHandler,
      );
      expect(actual, expected);
    });

    test(
      "copyWith - dashboard options config access - returns expected values",
      () {
        final receiver = EndpointConfig(
          name: "Some endpoint",
          endpointMatcher: _matcher,
          defaultHandler: _defaultHandler,
          errorHandler: _errorHandler,
        );
        final actual = receiver.copyWith.dashboardOptionsConfig(
          presets: [
            DashboardOverridePreset(
              name: "Preset",
              description: "My preset",
              response: MockzillaHttpResponse(),
            ),
          ],
        );
        final expected = EndpointConfig(
          name: "Some endpoint",
          endpointMatcher: _matcher,
          defaultHandler: _defaultHandler,
          errorHandler: _errorHandler,
          dashboardOptionsConfig: DashboardOptionsConfig(
            presets: [
              DashboardOverridePreset(
                name: "Preset",
                description: "My preset",
                response: MockzillaHttpResponse(),
              ),
            ],
          ),
        );

        expect(actual, expected);
      },
    );

    test("toString - returns expected", () {
      final receiver = EndpointConfig(
        name: "Some endpoint",
        endpointMatcher: _matcher,
        defaultHandler: _defaultHandler,
        errorHandler: _errorHandler,
      );
      final expected =
          'EndpointConfig('
          'name=Some endpoint, customKey=null, shouldFail=false, '
          'delay=0:00:00.100000, versionCode=1, '
          'endpointMatcher=$_matcher, '
          'dashboardOptionsConfig=${const DashboardOptionsConfig()}, '
          'defaultHandler=$_defaultHandler, errorHandler=$_errorHandler'
          ')';
      expect(receiver.toString(), expected);
    });

    test("key - with no customKey - returns name", () {
      final receiver = EndpointConfig(
        name: "Some endpoint",
        endpointMatcher: _matcher,
        defaultHandler: _defaultHandler,
        errorHandler: _errorHandler,
      );

      expect(receiver.key, "Some endpoint");
    });

    test("key - with customKey - returns customKey", () {
      final receiver = EndpointConfig(
        name: "Some endpoint",
        customKey: "custom-key",
        endpointMatcher: _matcher,
        defaultHandler: _defaultHandler,
        errorHandler: _errorHandler,
      );

      expect(receiver.key, "custom-key");
    });
  });
}
