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

/// A representation of a request to the Mockzilla server; this is passed to
/// an endpoint handler in order to generate an appropriate response.
@freezed
class MockzillaHttpRequest with _$MockzillaHttpRequest {
  const factory MockzillaHttpRequest({
    required String uri,
    @Default({}) Map<String, String> headers,
    @Default("") String body,
    required HttpMethod method,
  }) = _MockzillaHttpRequest;
}

/// Created and returned by an endpoint handler in response to an incoming
/// HTTP request.
@freezed
class MockzillaHttpResponse with _$MockzillaHttpResponse {
  const factory MockzillaHttpResponse({
    /// The HTTP status to use for the response, defaults to 200 - OK.
    @Default(HttpStatus.ok) int statusCode,

    /// The response headers, defaults a single `Content-Type` header with a
    /// value of `application/json`.
    @Default({"Content-Type": "application/json"}) Map<String, String> headers,
    @Default("") String body,
  }) = _MockzillaHttpResponse;
}

/// Definition for a preset response that can be selected in the desktop
/// management app.
@freezed
class DashboardOverridePreset with _$DashboardOverridePreset {
  const factory DashboardOverridePreset({
    required String name,
    required String? description,
    required MockzillaHttpResponse response,
  }) = _DashboardOverridePreset;
}

/// A collection of preset responses from an endpoint that can be selected in
/// the desktop management app.
@freezed
class DashboardOptionsConfig with _$DashboardOptionsConfig {
  const factory DashboardOptionsConfig({
    @Default([]) List<DashboardOverridePreset> successPresets,
    @Default([]) List<DashboardOverridePreset> errorPresets,
  }) = _DashboardOptionsConfig;
}

/// Configuration for an endpoint including how requests should be handled
/// and desktop app presets.
///
/// Please see [https://apadmi-engineering.github.io/Mockzilla/endpoints/]()
/// for more information.
@freezed
class EndpointConfig with _$EndpointConfig {
  const EndpointConfig._();

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
    @Default(DashboardOptionsConfig())
    DashboardOptionsConfig dashboardOptionsConfig,

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

    /// Whether Mockzilla server should only be available on the host device.
    @Default(false) bool localHostOnly,

    /// The level of logging that should be used by Mockzilla.
    @Default(LogLevel.info) LogLevel logLevel,

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
  }) = _MockzillaRuntimeParams;
}

/// Thrown when attempting to start Mockzilla on a port currently occupied by
/// another process. To resolve, either terminate the other process or choose a
/// different port for the Mockzilla server.
class MockzillaPortConflictException implements Exception {
  final int port;

  const MockzillaPortConflictException(this.port);

  @override
  String toString() =>
      "Attempted to start Mockzilla server on a port that is already occupied "
          "by another process ($port).";
}