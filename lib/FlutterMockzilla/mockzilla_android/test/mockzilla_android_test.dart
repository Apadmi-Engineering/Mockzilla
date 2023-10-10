import 'package:flutter_test/flutter_test.dart';
import 'package:mockzilla_android/mockzilla_android.dart';
import 'package:mockzilla_android/mockzilla_android_platform_interface.dart';
import 'package:mockzilla_android/mockzilla_android_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockMockzillaAndroidPlatform
    with MockPlatformInterfaceMixin
    implements MockzillaAndroidPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final MockzillaAndroidPlatform initialPlatform = MockzillaAndroidPlatform.instance;

  test('$MethodChannelMockzillaAndroid is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelMockzillaAndroid>());
  });

  test('getPlatformVersion', () async {
    MockzillaAndroid mockzillaAndroidPlugin = MockzillaAndroid();
    MockMockzillaAndroidPlatform fakePlatform = MockMockzillaAndroidPlatform();
    MockzillaAndroidPlatform.instance = fakePlatform;

    expect(await mockzillaAndroidPlugin.getPlatformVersion(), '42');
  });
}
