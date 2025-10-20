import 'package:mockzilla_ui_mobile/src/messages.g.dart';

class MockzillaUiMobile {
  static final hostBridge = MockzillaUiMobileHostApi();

  static void launchManagementUi() {
    hostBridge.launchManagementUi();
  }
}
