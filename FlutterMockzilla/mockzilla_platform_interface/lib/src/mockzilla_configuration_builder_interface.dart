import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

class MockzillaConfigBuilder {
  MockzillaConfigBuilder();

  int _port = 8080;

  final List<EndpointConfig> _endpoints = [];
  bool _isRelease = false;

  bool _localHostOnly = false;

  LogLevel _logLevel = LogLevel.info;

  ReleaseModeConfig _releaseModeConfig = const ReleaseModeConfig();

  final List<MockzillaLogger> _additionalLogWriters = [];

  MockzillaConfigBuilder setPort(int port) {
    _port = port;
    return this;
  }

  MockzillaConfigBuilder setReleaseMode(bool isRelease) {
    _isRelease = isRelease;
    return this;
  }

  MockzillaConfigBuilder setLocalHostOnly(bool localHostOnly) {
    _localHostOnly = localHostOnly;
    return this;
  }

  MockzillaConfigBuilder setLogLevel(LogLevel logLevel) {
    _logLevel = logLevel;
    return this;
  }

  MockzillaConfigBuilder setReleaseModeConfig(
    ReleaseModeConfig releaseModeConfig,
  ) {
    _releaseModeConfig = releaseModeConfig;
    return this;
  }

  MockzillaConfigBuilder addAdditionalLogWriter(MockzillaLogger logger) {
    _additionalLogWriters.add(logger);
    return this;
  }

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
