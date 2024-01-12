import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

abstract class Mockzilla {
  static Future<void> startMockzilla(MockzillaConfig config) =>
      MockzillaPlatform.instance.startMockzilla(config);

  static Future<void> stopMockzilla() => MockzillaPlatform.instance.stopMockzilla();
}
