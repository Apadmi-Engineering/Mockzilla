import 'package:mockzilla_platform_interface/src/models.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

abstract class MockzillaPlatform extends PlatformInterface {
  static final _token = Object();

  MockzillaPlatform() : super(token: _token);

  MockzillaRuntimeParams startMockzilla(MockzillaConfig config) {
    throw UnimplementedError("startMockzilla() has not been implemented");
  }

  stopMockzilla() {
    throw UnimplementedError("stopMockzilla() has not been implemented");
  }
}
