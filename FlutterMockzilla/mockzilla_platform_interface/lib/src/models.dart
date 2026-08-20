import 'dart:async';

import 'package:collection/collection.dart';

enum HttpMethod { get, head, post, put, delete, options, patch }

enum LogLevel { debug, error, info, verbose, warn, assertion }

enum DashboardOverridePresetType {
  clientError,
  informational,
  other,
  redirect,
  serverError,
  success,
}

/// A representation of a request to the Mockzilla server; this is passed to
/// an endpoint handler in order to generate an appropriate response.
class MockzillaHttpRequest({
  required final String uri,
  required final HttpMethod method,
  final Map<String, String> headers = const <String, String>{},
  final String body = "",
}) {
  @override
  int get hashCode => Object.hashAll([
    uri,
    const DeepCollectionEquality().hash(headers),
    method,
    body,
  ]);

  @override
  bool operator ==(covariant MockzillaHttpRequest other) =>
      uri == other.uri &&
      DeepCollectionEquality().equals(headers, other.headers) &&
      method == other.method &&
      body == other.body;

  /// Returns a **new instance** of this object with non-null parameters
  /// updated.
  @pragma("vm:prefer-inline")
  MockzillaHttpRequest copyWith({
    String? uri,
    HttpMethod? method,
    Map<String, String>? headers,
    String? body,
  }) => MockzillaHttpRequest(
    uri: uri ?? this.uri,
    method: method ?? this.method,
    headers: headers ?? this.headers,
    body: body ?? this.body,
  );

  @override
  String toString() =>
      "MockzillaHttpRequest("
      "uri=$uri, method=$method, headers=$headers, body=$body"
      ")";
}

/// Created and returned by an endpoint handler in response to an incoming
/// HTTP request.
class const MockzillaHttpResponse({
  /// The HTTP status to use for the response, defaults to 200 - OK.
  final int statusCode = 200,

  /// The response headers, defaults to a single `Content-Type` header with a
  /// value of `application/json`.
  final Map<String, String> headers = const <String, String>{
    "Content-Type": "application/json",
  },
  final String body = "",
}) implements CommonPartialMockzillaHttpResponse {
  @override
  int? nullableStatusCode() => statusCode;

  @override
  Map<String, String>? nullableHeaders() => headers;

  @override
  String? nullableBody() => body;

  @override
  int get hashCode => Object.hashAll([
    statusCode,
    const DeepCollectionEquality().hash(headers),
    body,
  ]);

  @override
  bool operator ==(covariant MockzillaHttpResponse other) =>
      statusCode == other.statusCode &&
      const DeepCollectionEquality().equals(headers, other.headers) &&
      body == other.body;

  /// Returns a **new instance** of this object with non-null parameters
  /// updated.
  @pragma("vm:prefer-inline")
  MockzillaHttpResponse copyWith({
    int? statusCode,
    Map<String, String>? headers,
    String? body,
  }) => MockzillaHttpResponse(
    statusCode: statusCode ?? this.statusCode,
    headers: headers ?? this.headers,
    body: body ?? this.body,
  );

  @override
  String toString() =>
      "MockzillaHttpResponse("
      "statusCode=$statusCode, headers=$headers, body=$body"
      ")";
}

/// Used to define partial overrides of standard responses in Dashboard overrides
abstract class CommonPartialMockzillaHttpResponse {
  int? nullableStatusCode();

  Map<String, String>? nullableHeaders();

  String? nullableBody();
}

class const PartialMockzillaHttpResponse({
  final int? statusCode,
  final Map<String, String>? headers,
  final String? body,
}) implements CommonPartialMockzillaHttpResponse {
  @override
  int? nullableStatusCode() => statusCode;

  @override
  Map<String, String>? nullableHeaders() => headers;

  @override
  String? nullableBody() => body;

  @override
  int get hashCode => Object.hashAll([
    statusCode,
    const DeepCollectionEquality().hash(headers),
    body,
  ]);

  @override
  bool operator ==(covariant PartialMockzillaHttpResponse other) =>
      statusCode == other.statusCode &&
      const DeepCollectionEquality().equals(headers, other.headers) &&
      body == other.body;

  /// Returns a **new instance** of this object with non-null parameters
  /// updated.
  @pragma("vm:prefer-inline")
  PartialMockzillaHttpResponse copyWith({
    int? statusCode,
    Map<String, String>? headers,
    String? body,
  }) => PartialMockzillaHttpResponse(
    statusCode: statusCode ?? this.statusCode,
    headers: headers ?? this.headers,
    body: body ?? this.body,
  );

  @override
  String toString() =>
      "PartialMockzillaHttpResponse("
      "statusCode=$statusCode, headers=$headers, body=$body"
      ")";
}

/// Definition for a preset response that can be selected in the desktop
/// management app.
class const DashboardOverridePreset({
  required final String name,
  required final String? description,
  required final CommonPartialMockzillaHttpResponse response,
  final DashboardOverridePresetType? type,
}) {
  @override
  int get hashCode => Object.hashAll([name, description, response, type]);

  @override
  bool operator ==(covariant DashboardOverridePreset other) =>
      name == other.name &&
      description == other.description &&
      response == other.response &&
      type == other.type;

  /// Returns a **new instance** of this object with non-null parameters
  /// updated.
  @pragma("vm:prefer-inline")
  DashboardOverridePreset copyWith({
    String? name,
    String? description,
    CommonPartialMockzillaHttpResponse? response,
    DashboardOverridePresetType? type,
  }) => DashboardOverridePreset(
    name: name ?? this.name,
    description: description ?? this.description,
    response: response ?? this.response,
    type: type ?? this.type,
  );

  @override
  String toString() =>
      "DashboardOverridePreset("
      "name=$name, description=$description, response=$response, type=$type"
      ")";
}

/// A collection of preset responses from an endpoint that can be selected in
/// the desktop management app.
class const DashboardOptionsConfig({
  @Deprecated(
    "Success/Error presets are now just one flat list, so use `presets` property",
  )
  final List<DashboardOverridePreset> successPresets = const [],
  @Deprecated("Error Presets will be removed in a future version")
  final List<DashboardOverridePreset> errorPresets = const [],
  final List<DashboardOverridePreset> presets = const [],
}) {
  @override
  int get hashCode => Object.hashAll([
    const DeepCollectionEquality().hash(successPresets),
    const DeepCollectionEquality().hash(errorPresets),
    const DeepCollectionEquality().hash(presets),
  ]);

  @override
  bool operator ==(covariant DashboardOptionsConfig other) =>
      const DeepCollectionEquality().equals(
        successPresets,
        other.successPresets,
      ) &&
      const DeepCollectionEquality().equals(errorPresets, other.errorPresets) &&
      const DeepCollectionEquality().equals(presets, other.presets);

  /// Returns a **new instance** of this object with non-null parameters
  /// updated. Invoke the returned object directly to update
  /// `successPresets`, `errorPresets`, and `presets`.
  CopyDashboardOptionsConfig get copyWith =>
      CopyDashboardOptionsConfig(this, (copy) => copy);

  @override
  String toString() =>
      "DashboardOptionsConfig("
      "successPresets=$successPresets, errorPresets=$errorPresets, "
      "presets=$presets"
      ")";
}

/// {@nodoc}
class CopyDashboardOptionsConfig<T>(
  final DashboardOptionsConfig self,
  final T Function(DashboardOptionsConfig) then,
) {
  /// Returns a **new instance** of `DashboardOptionsConfig` with non-null
  /// parameters updated.
  T call({
    List<DashboardOverridePreset>? successPresets,
    List<DashboardOverridePreset>? errorPresets,
    List<DashboardOverridePreset>? presets,
  }) => then(
    DashboardOptionsConfig(
      successPresets: successPresets ?? self.successPresets,
      errorPresets: errorPresets ?? self.errorPresets,
      presets: presets ?? self.presets,
    ),
  );
}

/// Configuration for an endpoint including how requests should be handled
/// and desktop app presets.
///
/// Please see
/// [Mockzilla endpoint docs](https://mockzilla.apadmi.dev/endpoints/) for more
/// information.
class const EndpointConfig({
  required final String name,

  /// Identifier for this endpoint, defaults to [name].
  final String? customKey,

  /// Whether the Mockzilla server should return an artificial error for a
  /// request to this endpoint. Defaults to [false].
  final bool shouldFail = false,

  /// The artificial delay that Mockzilla should apply to responses
  /// to simulate latency. Defaults to 100ms.
  final Duration delay = const Duration(milliseconds: 100),

  /// Incrementing this will indicate a breaking change has been
  /// made to this endpoint and will invalidate any cached data on the host
  /// device without intervention by the user. Defaults to 1.
  final int versionCode = 1,

  /// Used to determine whether a particular `request` should be evaluated by
  /// this endpoint.
  required final FutureOr<bool> Function(MockzillaHttpRequest request)
  endpointMatcher,

  /// Optional, configures the preset responses for the endpoint in the
  /// Mockzilla dashboard.
  final DashboardOptionsConfig dashboardOptionsConfig =
      const DashboardOptionsConfig(),

  /// This function is called when a network request is made to this endpoint,
  /// note that if an error is being returned due to [shouldFail] then
  /// `errorHandler` is used instead.
  required final FutureOr<MockzillaHttpResponse> Function(
    MockzillaHttpRequest request,
  )
  defaultHandler,

  /// This function is called when, in response to a network request, the
  /// server returns an error due to [shouldFail].
  required final FutureOr<MockzillaHttpResponse> Function(
    MockzillaHttpRequest request,
  )
  errorHandler,
}) {
  String get key => customKey ?? name;

  @override
  int get hashCode => Object.hashAll([
    name,
    customKey,
    shouldFail,
    delay,
    versionCode,
    endpointMatcher,
    dashboardOptionsConfig,
    defaultHandler,
    errorHandler,
  ]);

  @override
  bool operator ==(covariant EndpointConfig other) =>
      name == other.name &&
      customKey == other.customKey &&
      shouldFail == other.shouldFail &&
      delay == other.delay &&
      versionCode == other.versionCode &&
      endpointMatcher == other.endpointMatcher &&
      dashboardOptionsConfig == other.dashboardOptionsConfig &&
      defaultHandler == other.defaultHandler &&
      errorHandler == other.errorHandler;

  /// Returns a **new instance** of this object with non-null parameters
  /// updated. Either invoke the returned object directly to update top-level
  /// fields on this `EndpointConfig` or use the `dashboardOptionsConfig`
  /// accessor to deeply copy fields in the `DashboardOptionsConfig`.
  @pragma("vm:prefer-inline")
  CopyEndpointConfig get copyWith => CopyEndpointConfig(this);

  @override
  String toString() =>
      "EndpointConfig("
      "name=$name, customKey=$customKey, shouldFail=$shouldFail, "
      "delay=$delay, versionCode=$versionCode, "
      "endpointMatcher=$endpointMatcher, "
      "dashboardOptionsConfig=$dashboardOptionsConfig, "
      "defaultHandler=$defaultHandler, errorHandler=$errorHandler"
      ")";
}

/// {@nodoc}
class CopyEndpointConfig(final EndpointConfig self) {
  /// Returns a **new instance** of this object with non-null parameters
  /// updated.
  EndpointConfig call({
    String? name,
    String? customKey,
    bool? shouldFail,
    Duration? delay,
    int? versionCode,
    FutureOr<bool> Function(MockzillaHttpRequest request)? endpointMatcher,
    DashboardOptionsConfig? dashboardOptionsConfig,
    FutureOr<MockzillaHttpResponse> Function(MockzillaHttpRequest request)?
    defaultHandler,
    FutureOr<MockzillaHttpResponse> Function(MockzillaHttpRequest request)?
    errorHandler,
  }) => EndpointConfig(
    name: name ?? self.name,
    customKey: customKey ?? self.customKey,
    shouldFail: shouldFail ?? self.shouldFail,
    delay: delay ?? self.delay,
    versionCode: versionCode ?? self.versionCode,
    endpointMatcher: endpointMatcher ?? self.endpointMatcher,
    dashboardOptionsConfig:
        dashboardOptionsConfig ?? self.dashboardOptionsConfig,
    defaultHandler: defaultHandler ?? self.defaultHandler,
    errorHandler: errorHandler ?? self.errorHandler,
  );

  /// Returns a **new instance** of `EndpointConfig` with an updated
  /// `DashboardOptionsConfig` with the non-null parameters updated.
  CopyDashboardOptionsConfig get dashboardOptionsConfig =>
      CopyDashboardOptionsConfig(
        self.dashboardOptionsConfig,
        (copiedDashboardOptionsConfig) =>
            self.copyWith(dashboardOptionsConfig: copiedDashboardOptionsConfig),
      );
}

abstract class MockzillaLogger {
  void log(LogLevel level, String message, String tag, Exception? exception);
}

class const MockzillaConfig({
  /// The port that the Mockzilla should be available through.
  final int port = 8080,

  /// The list of available mocked endpoints.
  final List<EndpointConfig> endpoints = const [],

  /// Whether Mockzilla server should only be available on the host device.
  final bool localHostOnly = false,

  /// The level of logging that should be used by Mockzilla.
  final LogLevel logLevel = LogLevel.info,

  /// Whether devices running Mockzilla are discoverable on the local network
  /// through the desktop management app.
  final bool isNetworkDiscoveryEnabled = true,

  /// Custom logger implementations for surfacing Mockzilla logs outside of
  /// the Flutter console.
  final List<MockzillaLogger> loggers = const [],
}) {
  @override
  int get hashCode => Object.hashAll([
    port,
    const DeepCollectionEquality().hash(endpoints),
    localHostOnly,
    logLevel,
    isNetworkDiscoveryEnabled,
    const DeepCollectionEquality().hash(loggers),
  ]);

  @override
  bool operator ==(covariant MockzillaConfig other) =>
      port == other.port &&
      const DeepCollectionEquality().equals(endpoints, other.endpoints) &&
      localHostOnly == other.localHostOnly &&
      logLevel == other.logLevel &&
      isNetworkDiscoveryEnabled == other.isNetworkDiscoveryEnabled &&
      const DeepCollectionEquality().equals(loggers, other.loggers);

  /// Returns a **new instance** of this object with non-null parameters
  /// updated. Invoke the returned object directly to update the `port`,
  /// `endpoints`, `localHostOnly`, `logLevel`, `isNetworkDiscoveryEnabled`, or
  /// `loggers` fields.
  @pragma("vm:prefer-inline")
  CopyMockzillaConfig get copyWith =>
      CopyMockzillaConfig._(this, (self) => self);

  @override
  String toString() =>
      "MockzillaConfig("
      "port=$port, endpoints=$endpoints, localHostOnly=$localHostOnly, "
      "logLevel=$logLevel, "
      "isNetworkDiscoveryEnabled=$isNetworkDiscoveryEnabled, "
      "loggers=$loggers"
      ")";
}

/// {@nodoc}
class CopyMockzillaConfig<T>._(
  final MockzillaConfig self,
  final T Function(MockzillaConfig) then,
) {
  /// Returns a **new instance** of this object with non-null parameters
  /// updated.
  T call({
    int? port,
    List<EndpointConfig>? endpoints,
    bool? localHostOnly,
    LogLevel? logLevel,
    bool? isNetworkDiscoveryEnabled,
    List<MockzillaLogger>? loggers,
  }) => then(
    MockzillaConfig(
      port: port ?? self.port,
      endpoints: endpoints ?? self.endpoints,
      localHostOnly: localHostOnly ?? self.localHostOnly,
      logLevel: logLevel ?? self.logLevel,
      isNetworkDiscoveryEnabled:
          isNetworkDiscoveryEnabled ?? self.isNetworkDiscoveryEnabled,
      loggers: loggers ?? self.loggers,
    ),
  );
}

class const MockzillaRuntimeParams({
  required final MockzillaConfig config,
  required final String mockBaseUrl,
  required final String apiBaseUrl,
  required final int port,
}) {
  @override
  int get hashCode => Object.hashAll([config, mockBaseUrl, apiBaseUrl, port]);

  @override
  bool operator ==(covariant MockzillaRuntimeParams other) =>
      config == other.config &&
      mockBaseUrl == other.mockBaseUrl &&
      apiBaseUrl == other.apiBaseUrl &&
      port == other.port;

  /// Returns a **new instance** of this object with non-null parameters
  /// updated. Either invoke the returned object directly to update the
  /// top-level fields of `MockzillaRuntimeParams`, or use the `config`
  /// accessor to obtain a new instance of this object with a new instance of
  /// `MockzillaConfig` with updated fields.
  CopyMockzillaRuntimeParams get copyWith => CopyMockzillaRuntimeParams._(this);

  @override
  String toString() =>
      "MockzillaRuntimeParams("
      "config=$config, mockBaseUrl=$mockBaseUrl, apiBaseUrl=$apiBaseUrl, "
      "port=$port"
      ")";
}

/// {@nodoc}
class CopyMockzillaRuntimeParams._(final MockzillaRuntimeParams self) {
  /// Returns a **new instance** of this object with non-null parameters
  /// updated.
  MockzillaRuntimeParams call({
    MockzillaConfig? config,
    String? mockBaseUrl,
    String? apiBaseUrl,
    int? port,
  }) => MockzillaRuntimeParams(
    config: config ?? self.config,
    mockBaseUrl: mockBaseUrl ?? self.mockBaseUrl,
    apiBaseUrl: apiBaseUrl ?? self.apiBaseUrl,
    port: port ?? self.port,
  );

  /// Returns a **new instance** of `MockzillaRuntimeParams` with non-null
  /// fields of `MockzillaConfig` updated.
  CopyMockzillaConfig get config => CopyMockzillaConfig._(
    self.config,
    (copiedConfig) => self.copyWith(config: copiedConfig),
  );
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

/// Passed to custom Mockzilla logger instances when an exception is thrown in
/// the platform Mockzilla implementation.
class MockzillaPlatformException implements Exception {
  final String message;

  const MockzillaPlatformException(this.message);
}
