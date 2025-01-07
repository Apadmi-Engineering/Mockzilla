import 'package:flutter_test/flutter_test.dart';
import 'package:mockito/annotations.dart';
import 'package:mockito/mockito.dart';
import 'package:mockzilla_ios/mockzilla_ios.dart';
import 'package:mockzilla_ios/src/api_utils.dart';
import 'package:mockzilla_ios/src/messages.g.dart';
import 'package:mockzilla_ios/src/model/mockzilla_error.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

import 'callback_provider_test.mocks.dart';

part 'fixtures.dart';

@GenerateNiceMocks([
  MockSpec<EndpointConfig>(),
  MockSpec<MockzillaHttpRequest>(),
])
void main() {
  group("Callback provider unit tests", () {
    final mockEndpoint = MockEndpointConfig();
    final mockRequest = MockMockzillaHttpRequest();
    late CallbackProvider sut;

    setUp(() {
      reset(mockEndpoint);
      sut = CallbackProvider(
        [mockEndpoint],
      );
    });

    test("endpointMatcher - with known, matching endpoint - returns true", () {
      // Setup
      when(mockEndpoint.key).thenReturn("key");
      when(mockEndpoint.endpointMatcher).thenReturn((_) => true);

      // Run test & verify
      expect(sut.endpointMatcher(mockRequest.toBridge(), "key"), true);
      verify(mockEndpoint.endpointMatcher(mockRequest)).called(1);
    });

    test("endpointMatcher - with known, non-matching endpoint - returns false",
        () {
      // Setup
      when(mockEndpoint.key).thenReturn("key");
      when(mockEndpoint.endpointMatcher).thenReturn((_) => false);

      // Run test & verify
      expect(sut.endpointMatcher(mockRequest.toBridge(), "key"), false);
      verify(mockEndpoint.endpointMatcher(mockRequest)).called(1);
    });

    test("endpointMatcher - with unknown endpoint - throws", () {
      // Setup
      when(mockEndpoint.key).thenReturn("key");
      when(mockEndpoint.endpointMatcher).thenReturn((_) => false);

      // Run test & verify
      expect(
        () => sut.endpointMatcher(mockRequest.toBridge(), "unknown"),
        throwsA(isA<EndpointNotFoundError>()),
      );
      verifyNever(mockEndpoint.endpointMatcher(mockRequest));
    });

    test("defaultHandler - with known endpoint - returns response", () {
      // Setup
      when(mockEndpoint.key).thenReturn("key");
      when(mockEndpoint.defaultHandler).thenReturn((_) => _responseFixture);

      // Run test
      final result = sut.defaultHandler(_bridgeRequestFixture, "key");

      // Verify
      expect(result.statusCode, _responseFixture.statusCode);
      expect(result.headers, _responseFixture.headers);
      expect(result.body, _responseFixture.body);
    });

    test("defaultHandler - with unknown endpoint - throws", () {
      // Setup
      when(mockEndpoint.key).thenReturn("key");
      when(mockEndpoint.defaultHandler).thenReturn((_) => _responseFixture);

      // Run test & verify
      expect(
        () => sut.defaultHandler(_bridgeRequestFixture, "unknown"),
        throwsA(isA<EndpointNotFoundError>()),
      );
    });

    test("errorHandler - with known endpoint - returns response", () {
      // Setup
      when(mockEndpoint.key).thenReturn("key");
      when(mockEndpoint.errorHandler).thenReturn((_) => _responseFixture);

      // Run test
      final result = sut.errorHandler(_bridgeRequestFixture, "key");

      // Verify
      expect(result.statusCode, _responseFixture.statusCode);
      expect(result.headers, _responseFixture.headers);
      expect(result.body, _responseFixture.body);
    });

    test("errorHandler - with unknown endpoint - throws", () {
      // Setup
      when(mockEndpoint.key).thenReturn("key");
      when(mockEndpoint.errorHandler).thenReturn((_) => _responseFixture);

      // Run test & verify
      expect(
        () => sut.errorHandler(_bridgeRequestFixture, "unknown"),
        throwsA(isA<EndpointNotFoundError>()),
      );
    });

    test(
        "flutterEndpointMatcher - with known, matching endpoint - returns true",
        () {
      // Setup
      when(mockEndpoint.key).thenReturn("key");
      when(mockEndpoint.endpointMatcher).thenReturn((_) => true);

      // Run test
      final result = sut.flutterEndpointMatcher(_requestFixture, "key");

      // Verify
      expect(result, true);
    });

    test(
        "flutterEndpointMatcher - with known, non-matching endpoint - returns false",
        () {
      // Setup
      when(mockEndpoint.key).thenReturn("key");
      when(mockEndpoint.endpointMatcher).thenReturn((_) => false);

      // Run test
      final result = sut.flutterEndpointMatcher(_requestFixture, "key");

      // Verify
      expect(result, false);
    });

    test("flutterEndpointMatcher - with unknown endpoint - throws", () {
      // Setup
      when(mockEndpoint.key).thenReturn("key");
      when(mockEndpoint.endpointMatcher).thenReturn((_) => true);

      // Run test & verify
      expect(
        () => sut.flutterEndpointMatcher(_requestFixture, "unknown"),
        throwsA(isA<EndpointNotFoundError>()),
      );
    });

    test("flutterDefaultHandler - with known endpoint - returns response", () {
      // Setup
      when(mockEndpoint.key).thenReturn("key");
      when(mockEndpoint.defaultHandler).thenReturn((_) => _responseFixture);

      // Run test
      final result = sut.flutterDefaultHandler(_requestFixture, "key");

      // Verify
      expect(result, _responseFixture);
    });

    test("flutterDefaultHandler - with unknown endpoint - throws", () {
      // Setup
      when(mockEndpoint.key).thenReturn("key");
      when(mockEndpoint.defaultHandler).thenReturn((_) => _responseFixture);

      // Run test & verify
      expect(
        () => sut.flutterDefaultHandler(_requestFixture, "unknown"),
        throwsA(isA<EndpointNotFoundError>()),
      );
    });

    test("flutterErrorHandler - with known endpoint - returns response", () {
      // Setup
      when(mockEndpoint.key).thenReturn("key");
      when(mockEndpoint.errorHandler).thenReturn((_) => _responseFixture);

      // Run test
      final result = sut.flutterErrorHandler(_requestFixture, "key");

      // Verify
      expect(result, _responseFixture);
    });

    test("flutterErrorHandler - with unknown endpoint - throws", () {
      // Setup
      when(mockEndpoint.key).thenReturn("key");
      when(mockEndpoint.defaultHandler).thenReturn((_) => _responseFixture);

      // Run test & verify
      expect(
        () => sut.flutterErrorHandler(_requestFixture, "unknown"),
        throwsA(isA<EndpointNotFoundError>()),
      );
    });
  });
}
