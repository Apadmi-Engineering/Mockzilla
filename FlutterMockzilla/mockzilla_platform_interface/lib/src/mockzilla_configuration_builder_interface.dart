import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

class MockzillaConfigBuilder {
  MockzillaConfigBuilder();

  /// The port that the Mockzilla should be available through.
  int _port = 8080;

  /// The list of available mocked endpoints.
  List<EndpointConfig> _endpoints = [];

  /// Can be used to add rudimentary restrictions to the Mockzilla server
  /// such as rate limiting. See [https://apadmi-engineering.github.io/Mockzilla/additional_config/#release-mode]()
  /// for more information.
  bool _isRelease = false;

  /// Whether Mockzilla server should only be available on the host device.
  bool _localHostOnly = false;

  /// The level of logging that should be used by Mockzilla.
  LogLevel _logLevel = LogLevel.info;

  /// The configuration for rate limiting.
  /// Rate limiting uses Ktor's implementation, please see
  /// [https://ktor.io/docs/rate-limit.html#configure-rate-limiting]() for more
  /// info.
  ReleaseModeConfig _releaseModeConfig = const ReleaseModeConfig();

  /// The list of additional log writers that should be used by Mockzilla.
  List<MockzillaLogger> _additionalLogWriters = [];

  /// Sets the [port] that the Mockzilla should be available through.
  MockzillaConfigBuilder setPort(int port) {
    _port = port;
    return this;
  }

  /// Sets the [MockzillaConfigBuilder.isRelease] mode of the Mockzilla server.
  MockzillaConfigBuilder setReleaseMode(bool isRelease) {
    _isRelease = isRelease;
    return this;
  }

  /// Sets the [MockzillaConfigBuilder.localHostOnly] mode of the Mockzilla server.
  MockzillaConfigBuilder setLocalHostOnly(bool localHostOnly) {
    _localHostOnly = localHostOnly;
    return this;
  }

  /// Sets the [MockzillaConfigBuilder.logLevel] of the Mockzilla server.
  MockzillaConfigBuilder setLogLevel(LogLevel logLevel) {
    _logLevel = logLevel;
    return this;
  }

  /// Sets the [MockzillaConfigBuilde.releaseModeConfig] of the Mockzilla server.
  MockzillaConfigBuilder setReleaseModeConfig(
    ReleaseModeConfig releaseModeConfig,
  ) {
    _releaseModeConfig = releaseModeConfig;
    return this;
  }

  /// Adds an [additionalLogWriter] to the Mockzilla server.
  MockzillaConfigBuilder addAdditionalLogWriter(MockzillaLogger logger) {
    _additionalLogWriters.add(logger);
    return this;
  }

  /// Adds an [Mockzilla.endpoint] to the Mockzilla server.
  MockzillaConfigBuilder addEndpoint(EndpointConfig Function() endpoint) {
    _endpoints.add(endpoint());
    return this;
  }

  MockzillaConfig build() {
    return MockzillaConfig(
      additionalLogWriters: _additionalLogWriters,
      port: _port,
      endpoints: _endpoints,
      isRelease: _isRelease,
      localHostOnly: _localHostOnly,
      logLevel: _logLevel,
      releaseModeConfig: _releaseModeConfig,
    );
  }
}
