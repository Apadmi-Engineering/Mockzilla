import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'mockzilla_android_platform_interface.dart';

/// An implementation of [MockzillaAndroidPlatform] that uses method channels.
class MethodChannelMockzillaAndroid extends MockzillaAndroidPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('mockzilla_android');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
