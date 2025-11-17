import 'package:mockzilla_ui_mobile/mockzilla_ui_mobile.dart';
import 'package:mockzilla_ui_mobile/src/messages.g.dart';

class MockzillaMobileUiAndroidAndiOS extends MockzillaUiMobilePlatform {
  static final hostBridge = MockzillaUiMobileHostApi();

  static final MockzillaMobileUiAndroidAndiOS instance =
      MockzillaMobileUiAndroidAndiOS();

  @override
  void launchManagementUi() {
    hostBridge.launchManagementUi();
  }

  @override
  void preloadAssets() {
    // No need to preload assets on Android or iOS
  }
}
