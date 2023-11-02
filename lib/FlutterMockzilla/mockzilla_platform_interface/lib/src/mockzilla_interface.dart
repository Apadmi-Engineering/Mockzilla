import 'package:mockzilla_platform_interface/src/models.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

abstract class MockzillaPlatform extends PlatformInterface {
  static final _token = Object();

  MockzillaPlatform() : super(token: _token);

  Future<void> startMockzilla(MockzillaConfig config) {
    throw UnimplementedError("startMockzilla() has not been implemented");
  }

  Future<void> stopMockzilla() {
    throw UnimplementedError("stopMockzilla() has not been implemented");
  }
}
