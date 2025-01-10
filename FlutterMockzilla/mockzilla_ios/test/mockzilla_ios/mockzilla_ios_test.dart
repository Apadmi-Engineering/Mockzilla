import 'package:flutter_test/flutter_test.dart';
import 'package:mockzilla_ios/mockzilla_ios.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

void main() {
  group("Mockzilla iOS unit tests", () {
    test("registerWith - registers platform instance", () {
      // Run test
      MockzillaIos.registerWith();

      // Verify
      expect(MockzillaPlatform.instance, isA<MockzillaIos>());
    });
  });
}
