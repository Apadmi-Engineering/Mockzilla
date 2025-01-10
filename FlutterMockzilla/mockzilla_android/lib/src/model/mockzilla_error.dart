import 'package:flutter/services.dart';

sealed class MockzillaError implements PlatformException {}

class EndpointNotFoundError extends MockzillaError {
  String key;
  StackTrace trace;

  EndpointNotFoundError(this.key, this.trace);

  @override
  String get code => "endpoint-not-found";

  @override
  get details => key;

  @override
  String? get message =>
      "Mockzilla tried to find an endpoint with key $key but "
      "it doesn't exist.";

  @override
  String? get stacktrace => trace.toString();
}
