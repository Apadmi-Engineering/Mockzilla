import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

abstract class Mockzilla {
  /// Starts the Mockzilla server with the given configuration.
  static Future<void> startMockzilla(MockzillaConfig config) =>
      MockzillaPlatform.instance.startMockzilla(config);

  /// Stops the Mockzilla server.
  static Future<void> stopMockzilla() =>
      MockzillaPlatform.instance.stopMockzilla();
}
