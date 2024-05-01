import 'package:flutter_test/flutter_test.dart';
import 'package:mockzilla_ios/src/mockzilla_ios.dart';
import 'package:mockzilla_ios/mockzilla_ios_platform_interface.dart';
import 'package:mockzilla_ios/mockzilla_ios_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockMockzillaIosPlatform
    with MockPlatformInterfaceMixin
    implements MockzillaIosPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final MockzillaIosPlatform initialPlatform = MockzillaIosPlatform.instance;

  test('$MethodChannelMockzillaIos is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelMockzillaIos>());
  });

  test('getPlatformVersion', () async {
    MockzillaIos mockzillaIosPlugin = MockzillaIos();
    MockMockzillaIosPlatform fakePlatform = MockMockzillaIosPlatform();
    MockzillaIosPlatform.instance = fakePlatform;

    expect(await mockzillaIosPlugin.getPlatformVersion(), '42');
  });
}
