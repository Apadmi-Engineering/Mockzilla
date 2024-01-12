import 'package:mockzilla_platform_interface/src/models.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

abstract class MockzillaPlatform extends PlatformInterface {

  static final _token = Object();

  MockzillaPlatform() : super(token: _token);

  /// Starts the Mockzilla server with the given configuration.
  Future<void> startMockzilla(MockzillaConfig config) {
    throw UnimplementedError("startMockzilla() has not been implemented");
  }

  /// Stops the Mockzilla server.
  Future<void> stopMockzilla() {
    throw UnimplementedError("stopMockzilla() has not been implemented");
  }

  static MockzillaPlatform _instance = DefaultImpl();

  static set instance(MockzillaPlatform instance) {
    PlatformInterface.verify(instance, _token);
    _instance = instance;
  }

  static MockzillaPlatform get instance => _instance;
}

class DefaultImpl extends MockzillaPlatform {}