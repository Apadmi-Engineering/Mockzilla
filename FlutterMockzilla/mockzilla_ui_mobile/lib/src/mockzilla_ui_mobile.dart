import 'package:mockzilla_ui_mobile/src/android_and_ios.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockzillaUiMobile {
  static void launchManagementUi() {
    MockzillaUiMobilePlatform.instance.launchManagementUi();
  }

  static void preloadAssets() {
    MockzillaUiMobilePlatform.instance.preloadAssets();
  }
}

abstract class MockzillaUiMobilePlatform extends PlatformInterface {
  MockzillaUiMobilePlatform() : super(token: _token);

  static final Object _token = Object();

  // The static instance that the main library will call.
  static MockzillaUiMobilePlatform _instance =
      MockzillaMobileUiAndroidAndiOS.instance;

  static MockzillaUiMobilePlatform get instance => _instance;

  /// Platform-specific plugins should set this with their own platform-specific
  /// class that extends [MockzillaUiMobilePlatform] when they register themselves.
  static set instance(MockzillaUiMobilePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  void launchManagementUi() {
    throw UnimplementedError('launchManagementUi() has not been implemented.');
  }

  void preloadAssets() {
    throw UnimplementedError('preloadAssets() has not been implemented.');
  }
}
