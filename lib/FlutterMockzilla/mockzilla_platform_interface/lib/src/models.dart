import 'dart:io';

import 'package:freezed_annotation/freezed_annotation.dart';

part 'models.freezed.dart';

enum HttpMethod {
  get,
  head,
  post,
  put,
  delete,
  connect,
  options,
  trace,
  patch;
}

enum LogLevel {
  debug,
  error,
  info,
  verbose,
  warn;
}

@freezed
class MockzillaHttpRequest with _$MockzillaHttpRequest {
  const factory MockzillaHttpRequest({
    required String uri,
    required Map<String, String> headers,
    @Default("") String body,
    required HttpMethod method,
  }) = _MockzillaHttpRequest;
}

@freezed
class MockzillaHttpResponse with _$MockzillaHttpResponse {
  const factory MockzillaHttpResponse({
    @Default(HttpStatus.ok) int statusCode,
    @Default({}) Map<String, String> headers,
    @Default("") String body,
  }) = _MockzillaHttpResponse;
}

@freezed
class EndpointConfig with _$EndpointConfig {
  const factory EndpointConfig({
    required String name,
    required String key,
    int? failureProbability,
    int? delayMean,
    int? delayVariance,
    required bool Function(MockzillaHttpRequest request) endpointMatcher,
    MockzillaHttpResponse? webApiDefaultResponse,
    MockzillaHttpResponse? webApiErrorResponse,
    required MockzillaHttpResponse Function(MockzillaHttpRequest request)
        defaultHandler,
    required MockzillaHttpResponse Function(MockzillaHttpRequest request)
        errorHandler,
  }) = _EndpointConfig;
}

@freezed
class ReleaseModeConfig with _$ReleaseModeConfig {
  const factory ReleaseModeConfig({
    @Default(60) int rateLimit,
    @Default(Duration(seconds: 60)) Duration rateLimitRefillPeriod,
    @Default(Duration(milliseconds: 500)) Duration tokenLifeSpan,
  }) = _ReleaseModeConfig;
}

@freezed
class MockzillaLogger with _$MockzillaLogger {
  const factory MockzillaLogger({
    required LogLevel logLevel,
    required String message,
    required String tag,
    Exception? exception,
  }) = _MockzillaLogger;
}

@freezed
class MockzillaConfig with _$MockzillaConfig {
  const factory MockzillaConfig({
    required int port,
    required List<EndpointConfig> endpoints,
    required bool isRelease,
    required bool localHostOnly,
    required LogLevel logLevel,
    required List<MockzillaLogger> additionalLogWriters,
  }) = _MockzillaConfig;
}

@freezed
class MockzillaRuntimeParams with _$MockzillaRuntimeParams {
  const factory MockzillaRuntimeParams({
    required MockzillaConfig config,
    required String mockBaseUrl,
    required String apiBaseUrl,
    required int port,
    required AuthHeaderProvider authHeaderProvider,
  }) = _MockzillaRuntimeParams;
}

@freezed
class AuthHeader with _$AuthHeader {
  const factory AuthHeader({
    required String key,
    required String value,
  }) = _AuthHeader;
}

abstract class AuthHeaderProvider {
  Future<AuthHeader> generateHeader();
}
