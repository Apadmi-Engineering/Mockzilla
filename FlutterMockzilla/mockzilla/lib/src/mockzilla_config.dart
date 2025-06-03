import 'package:mockzilla/mockzilla.dart';

extension MockzillaConfigExtensions on MockzillaConfig {
  /// Returns a copy of this Mockzilla config with the result of [builder]
  /// added.
  MockzillaConfig addEndpoint(EndpointConfig Function() builder) =>
      copyWith(endpoints: endpoints + [builder()]);

  /// Returns a copy of this Mockzilla config with an additional [logger] added.
  MockzillaConfig addLogger(MockzillaLogger logger) =>
      copyWith(loggers: loggers + [logger]);
}
