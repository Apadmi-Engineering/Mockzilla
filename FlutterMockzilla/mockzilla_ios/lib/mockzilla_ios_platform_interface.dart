import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'mockzilla_ios_method_channel.dart';

abstract class MockzillaIosPlatform extends PlatformInterface {
  /// Constructs a MockzillaIosPlatform.
  MockzillaIosPlatform() : super(token: _token);

  static final Object _token = Object();

  static MockzillaIosPlatform _instance = MethodChannelMockzillaIos();

  /// The default instance of [MockzillaIosPlatform] to use.
  ///
  /// Defaults to [MethodChannelMockzillaIos].
  static MockzillaIosPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [MockzillaIosPlatform] when
  /// they register themselves.
  static set instance(MockzillaIosPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
