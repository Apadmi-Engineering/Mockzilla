import 'dart:io';

import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

part 'models.freezed.dart';

enum HttpMethod {
  get,
  head,
  post,
  put,
  delete,
  options,
  patch;
}

enum LogLevel {
  debug,
  error,
  info,
  verbose,
  warn,
  assertion;
}

@freezed
class MockzillaHttpRequest with _$MockzillaHttpRequest {
  /// A representation of a request to the Mockzilla server; this is passed to
  /// an endpoint handler in order to generate an appropriate response.
  const factory MockzillaHttpRequest({
    required String uri,
    @Default({}) Map<String, String> headers,
    @Default("") String body,
    required HttpMethod method,
  }) = _MockzillaHttpRequest;
}

@freezed
class MockzillaHttpResponse with _$MockzillaHttpResponse {
  /// Created and returned by an endpoint handler in response to an incoming
  /// HTTP request.
  const factory MockzillaHttpResponse({
    @Default(HttpStatus.ok) int statusCode,
    @Default({"Content-Type": "application/json"}) Map<String, String> headers,
    @Default("") String body,
  }) = _MockzillaHttpResponse;
}

@freezed
class DashboardOverridePreset with _$DashboardOverridePreset {
  const factory DashboardOverridePreset({
    required String name,
    required String? description,
    required MockzillaHttpResponse response,
  }) = _DashboardOverridePreset;
}

@freezed
class DashboardOptionsConfig with _$DashboardOptionsConfig {
  const factory DashboardOptionsConfig({
    @Default([]) List<DashboardOverridePreset> successPresets,
    @Default([]) List<DashboardOverridePreset> errorPresets,
}) = _DashboardOptionsConfig;
}

@freezed
class EndpointConfig with _$EndpointConfig {
  const EndpointConfig._();

  /// This configuration defines how Mockzilla should deal with a subset of
  /// requests such as configuring the response and meta-data such as the
  /// latency and failure rate.
  ///
  /// Please see [https://apadmi-engineering.github.io/Mockzilla/endpoints/]()
  /// for more information.
  const factory EndpointConfig({
    required String name,
    String? customKey,

    /// Whether the Mockzilla server should return an artificial error for a
    /// request to this endpoint.
    @Default(false) bool shouldFail,

    /// Optional, the artificial delay in milliseconds that Mockzilla should use to
    /// simulate latency.
    @Default(100) int delay,

    /// Used to determine whether a particular `request` should be evaluated by
    /// this endpoint.
    required bool Function(MockzillaHttpRequest request) endpointMatcher,

    /// Optional, configures the preset responses for the endpoint in the
    /// Mockzilla dashboard.
    @Default(DashboardOptionsConfig()) DashboardOptionsConfig dashboardOptionsConfig,

    /// This function is called when a network request is made to this endpoint,
    /// note that if an error is being returned due to `failureProbability`
    /// then `errorHandler` is used instead.
    required MockzillaHttpResponse Function(MockzillaHttpRequest request)
        defaultHandler,

    /// This function is called when, in response to a network request, the
    /// server returns an error due to`failureProbability`.
    required MockzillaHttpResponse Function(MockzillaHttpRequest request)
        errorHandler,
  }) = _EndpointConfig;

  String get key => customKey ?? name;
}

@freezed
class ReleaseModeConfig with _$ReleaseModeConfig {
  /// Rate limiting uses Ktor's implementation, please see
  /// [https://ktor.io/docs/rate-limit.html#configure-rate-limiting]() for more
  /// info.
  const factory ReleaseModeConfig({
    @Default(60) int rateLimit,
    @Default(Duration(seconds: 60)) Duration rateLimitRefillPeriod,
    @Default(Duration(milliseconds: 500)) Duration tokenLifeSpan,
  }) = _ReleaseModeConfig;
}

abstract class MockzillaLogger {
  void log(LogLevel level, String message, String tag, Exception? exception);
}

@freezed
class MockzillaConfig with _$MockzillaConfig {
  const factory MockzillaConfig({
    /// The port that the Mockzilla should be available through.
    @Default(8080) int port,

    /// The list of available mocked endpoints.
    @Default([]) List<EndpointConfig> endpoints,

    /// Can be used to add rudimentary restrictions to the Mockzilla server
    /// such as rate limiting. See [https://apadmi-engineering.github.io/Mockzilla/additional_config/#release-mode]()
    /// for more information.
    @Default(false) bool isRelease,

    /// Whether Mockzilla server should only be available on the host device.
    @Default(false) bool localHostOnly,

    /// The level of logging that should be used by Mockzilla.
    @Default(LogLevel.info) LogLevel logLevel,

    /// Used for additional configuration when [isRelease] is [true].
    @Default(ReleaseModeConfig()) ReleaseModeConfig releaseModeConfig,
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
