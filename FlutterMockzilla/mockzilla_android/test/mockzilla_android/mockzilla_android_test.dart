import 'package:flutter_test/flutter_test.dart';
import 'package:mockzilla_android/mockzilla_android.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

void main() async {
  group("Mockzilla android unit tests", () {
    test("registerWith - registers platform instance", () {
      // Run test
      MockzillaAndroid.registerWith();

      // Verify
      expect(MockzillaPlatform.instance, isA<MockzillaAndroid>());
    });
  });
}
