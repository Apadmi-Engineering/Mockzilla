
import 'mockzilla_ios_platform_interface.dart';

class MockzillaIos {
  Future<String?> getPlatformVersion() {
    return MockzillaIosPlatform.instance.getPlatformVersion();
  }
}
