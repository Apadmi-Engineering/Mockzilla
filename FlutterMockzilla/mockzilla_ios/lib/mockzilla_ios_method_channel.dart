import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'mockzilla_ios_platform_interface.dart';

/// An implementation of [MockzillaIosPlatform] that uses method channels.
class MethodChannelMockzillaIos extends MockzillaIosPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('mockzilla_ios');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
