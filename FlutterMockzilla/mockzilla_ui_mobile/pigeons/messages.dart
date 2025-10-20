import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(
  PigeonOptions(
    dartOut: "lib/src/messages.g.dart",
    dartOptions: DartOptions(),
    swiftOut:
        "ios/mockzilla_ui_mobile/Sources/mockzilla_ui_mobile/Messages.g.swift",
    swiftOptions: SwiftOptions(),
    kotlinOut:
        "android/src/main/kotlin/com/apadmi/mockzilla/mobile/ui/Messages.g.kt",
    kotlinOptions: KotlinOptions(
        package: "com.apadmi.mockzilla.mobile.ui",
        errorClassName: "MockzillaMobileUiFlutterError"),
  ),
)
@HostApi()
abstract class MockzillaUiMobileHostApi {
  void launchManagementUi();
}
