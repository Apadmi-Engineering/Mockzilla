import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'mockzilla_android_method_channel.dart';

abstract class MockzillaAndroidPlatform extends PlatformInterface {
  /// Constructs a MockzillaAndroidPlatform.
  MockzillaAndroidPlatform() : super(token: _token);

  static final Object _token = Object();

  static MockzillaAndroidPlatform _instance = MethodChannelMockzillaAndroid();

  /// The default instance of [MockzillaAndroidPlatform] to use.
  ///
  /// Defaults to [MethodChannelMockzillaAndroid].
  static MockzillaAndroidPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [MockzillaAndroidPlatform] when
  /// they register themselves.
  static set instance(MockzillaAndroidPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
