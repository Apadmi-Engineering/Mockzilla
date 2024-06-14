import 'package:flutter_test/flutter_test.dart';
import 'package:mockzilla_ios/src/utils/list_utils.dart';

void main() {
  group("List Utils unit tests", () {
    test("firstWhereOrNull - returns expected result", () {
      // Setup
      final inputToExpected = {
        [0, 1, 2]: null,
        [0, 1, 3]: 3,
        []: null,
        [3]: 3,
        [1, 2, 3, 4]: 3
      };

      // Run test & verify
      inputToExpected.forEach((input, expected) {
        expect(input.firstWhereOrNull((element) => element >= 3), expected);
      });
    });
  });
}
