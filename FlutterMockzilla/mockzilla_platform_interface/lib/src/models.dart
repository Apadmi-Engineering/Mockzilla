import 'dart:io';

import 'package:freezed_annotation/freezed_annotation.dart';

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
    /// The HTTP status to use for the response, defaults to 200 - OK.
    @Default(HttpStatus.ok) int statusCode,
    /// The response headers, defaults a single `Content-Type` header with a
    /// value of `application/json`.
    @Default({"Content-Type": "application/json"}) Map<String, String> headers,
    @Default("") String body,
  }) = _MockzillaHttpResponse;
}

@freezed
class DashboardOverridePreset with _$DashboardOverridePreset {
  /// Definition for a preset response that can be selected in the desktop
  /// management app.
  const factory DashboardOverridePreset({
    required String name,
    required String? description,
    required MockzillaHttpResponse response,
  }) = _DashboardOverridePreset;
}

@freezed
class DashboardOptionsConfig with _$DashboardOptionsConfig {
  /// A collection of preset responses from an endpoint that can be selected in
  /// the desktop management app.
  const factory DashboardOptionsConfig({
    @Default([]) List<DashboardOverridePreset> successPresets,
    @Default([]) List<DashboardOverridePreset> errorPresets,
}) = _DashboardOptionsConfig;
}

@freezed
class EndpointConfig with _$EndpointConfig {
  const EndpointConfig._();

  /// Configuration for an endpoint including how requests should be handled
  /// and desktop app presets.
  ///
  /// Please see [https://apadmi-engineering.github.io/Mockzilla/endpoints/]()
  /// for more information.
  const factory EndpointConfig({
    required String name,

    /// Identifier for this endpoint, defaults to [name].
    String? customKey,

    /// Whether the Mockzilla server should return an artificial error for a
    /// request to this endpoint. Defaults to [false].
    @Default(false) bool shouldFail,

    /// The artificial delay that Mockzilla should apply to responses
    /// to simulate latency. Defaults to 100ms.
    @Default(Duration(milliseconds: 100)) Duration delay,

    /// Incrementing this will indicate a breaking change has been
    /// made to this endpoint and will invalidate any cached data on the host
    /// device without intervention by the user. Defaults to 1.
    @Default(1) int versionCode,

    /// Used to determine whether a particular `request` should be evaluated by
    /// this endpoint.
    required bool Function(MockzillaHttpRequest request) endpointMatcher,

    /// Optional, configures the preset responses for the endpoint in the
    /// Mockzilla dashboard.
    @Default(DashboardOptionsConfig()) DashboardOptionsConfig dashboardOptionsConfig,

    /// This function is called when a network request is made to this endpoint,
    /// note that if an error is being returned due to [shouldFail] then
    /// `errorHandler` is used instead.
    required MockzillaHttpResponse Function(MockzillaHttpRequest request)
        defaultHandler,

    /// This function is called when, in response to a network request, the
    /// server returns an error due to [shouldFail].
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

    /// Whether devices running Mockzilla are discoverable on the local network
    /// through the desktop management app.
    @Default(true) bool isNetworkDiscoveryEnabled,
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
