// dart format width=80
// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'models.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$MockzillaHttpRequest {
  String get uri;
  Map<String, String> get headers;
  String get body;
  HttpMethod get method;

  /// Create a copy of MockzillaHttpRequest
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $MockzillaHttpRequestCopyWith<MockzillaHttpRequest> get copyWith =>
      _$MockzillaHttpRequestCopyWithImpl<MockzillaHttpRequest>(
          this as MockzillaHttpRequest, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is MockzillaHttpRequest &&
            (identical(other.uri, uri) || other.uri == uri) &&
            const DeepCollectionEquality().equals(other.headers, headers) &&
            (identical(other.body, body) || other.body == body) &&
            (identical(other.method, method) || other.method == method));
  }

  @override
  int get hashCode => Object.hash(runtimeType, uri,
      const DeepCollectionEquality().hash(headers), body, method);

  @override
  String toString() {
    return 'MockzillaHttpRequest(uri: $uri, headers: $headers, body: $body, method: $method)';
  }
}

/// @nodoc
abstract mixin class $MockzillaHttpRequestCopyWith<$Res> {
  factory $MockzillaHttpRequestCopyWith(MockzillaHttpRequest value,
          $Res Function(MockzillaHttpRequest) _then) =
      _$MockzillaHttpRequestCopyWithImpl;
  @useResult
  $Res call(
      {String uri,
      Map<String, String> headers,
      String body,
      HttpMethod method});
}

/// @nodoc
class _$MockzillaHttpRequestCopyWithImpl<$Res>
    implements $MockzillaHttpRequestCopyWith<$Res> {
  _$MockzillaHttpRequestCopyWithImpl(this._self, this._then);

  final MockzillaHttpRequest _self;
  final $Res Function(MockzillaHttpRequest) _then;

  /// Create a copy of MockzillaHttpRequest
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? uri = null,
    Object? headers = null,
    Object? body = null,
    Object? method = null,
  }) {
    return _then(_self.copyWith(
      uri: null == uri
          ? _self.uri
          : uri // ignore: cast_nullable_to_non_nullable
              as String,
      headers: null == headers
          ? _self.headers
          : headers // ignore: cast_nullable_to_non_nullable
              as Map<String, String>,
      body: null == body
          ? _self.body
          : body // ignore: cast_nullable_to_non_nullable
              as String,
      method: null == method
          ? _self.method
          : method // ignore: cast_nullable_to_non_nullable
              as HttpMethod,
    ));
  }
}

/// @nodoc

class _MockzillaHttpRequest implements MockzillaHttpRequest {
  const _MockzillaHttpRequest(
      {required this.uri,
      final Map<String, String> headers = const {},
      this.body = "",
      required this.method})
      : _headers = headers;

  @override
  final String uri;
  final Map<String, String> _headers;
  @override
  @JsonKey()
  Map<String, String> get headers {
    if (_headers is EqualUnmodifiableMapView) return _headers;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableMapView(_headers);
  }

  @override
  @JsonKey()
  final String body;
  @override
  final HttpMethod method;

  /// Create a copy of MockzillaHttpRequest
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$MockzillaHttpRequestCopyWith<_MockzillaHttpRequest> get copyWith =>
      __$MockzillaHttpRequestCopyWithImpl<_MockzillaHttpRequest>(
          this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _MockzillaHttpRequest &&
            (identical(other.uri, uri) || other.uri == uri) &&
            const DeepCollectionEquality().equals(other._headers, _headers) &&
            (identical(other.body, body) || other.body == body) &&
            (identical(other.method, method) || other.method == method));
  }

  @override
  int get hashCode => Object.hash(runtimeType, uri,
      const DeepCollectionEquality().hash(_headers), body, method);

  @override
  String toString() {
    return 'MockzillaHttpRequest(uri: $uri, headers: $headers, body: $body, method: $method)';
  }
}

/// @nodoc
abstract mixin class _$MockzillaHttpRequestCopyWith<$Res>
    implements $MockzillaHttpRequestCopyWith<$Res> {
  factory _$MockzillaHttpRequestCopyWith(_MockzillaHttpRequest value,
          $Res Function(_MockzillaHttpRequest) _then) =
      __$MockzillaHttpRequestCopyWithImpl;
  @override
  @useResult
  $Res call(
      {String uri,
      Map<String, String> headers,
      String body,
      HttpMethod method});
}

/// @nodoc
class __$MockzillaHttpRequestCopyWithImpl<$Res>
    implements _$MockzillaHttpRequestCopyWith<$Res> {
  __$MockzillaHttpRequestCopyWithImpl(this._self, this._then);

  final _MockzillaHttpRequest _self;
  final $Res Function(_MockzillaHttpRequest) _then;

  /// Create a copy of MockzillaHttpRequest
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? uri = null,
    Object? headers = null,
    Object? body = null,
    Object? method = null,
  }) {
    return _then(_MockzillaHttpRequest(
      uri: null == uri
          ? _self.uri
          : uri // ignore: cast_nullable_to_non_nullable
              as String,
      headers: null == headers
          ? _self._headers
          : headers // ignore: cast_nullable_to_non_nullable
              as Map<String, String>,
      body: null == body
          ? _self.body
          : body // ignore: cast_nullable_to_non_nullable
              as String,
      method: null == method
          ? _self.method
          : method // ignore: cast_nullable_to_non_nullable
              as HttpMethod,
    ));
  }
}

/// @nodoc
mixin _$MockzillaHttpResponse {
  /// The HTTP status to use for the response, defaults to 200 - OK.
  int get statusCode;

  /// The response headers, defaults a single `Content-Type` header with a
  /// value of `application/json`.
  Map<String, String> get headers;
  String get body;

  /// Create a copy of MockzillaHttpResponse
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $MockzillaHttpResponseCopyWith<MockzillaHttpResponse> get copyWith =>
      _$MockzillaHttpResponseCopyWithImpl<MockzillaHttpResponse>(
          this as MockzillaHttpResponse, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is MockzillaHttpResponse &&
            (identical(other.statusCode, statusCode) ||
                other.statusCode == statusCode) &&
            const DeepCollectionEquality().equals(other.headers, headers) &&
            (identical(other.body, body) || other.body == body));
  }

  @override
  int get hashCode => Object.hash(runtimeType, statusCode,
      const DeepCollectionEquality().hash(headers), body);

  @override
  String toString() {
    return 'MockzillaHttpResponse(statusCode: $statusCode, headers: $headers, body: $body)';
  }
}

/// @nodoc
abstract mixin class $MockzillaHttpResponseCopyWith<$Res> {
  factory $MockzillaHttpResponseCopyWith(MockzillaHttpResponse value,
          $Res Function(MockzillaHttpResponse) _then) =
      _$MockzillaHttpResponseCopyWithImpl;
  @useResult
  $Res call({int statusCode, Map<String, String> headers, String body});
}

/// @nodoc
class _$MockzillaHttpResponseCopyWithImpl<$Res>
    implements $MockzillaHttpResponseCopyWith<$Res> {
  _$MockzillaHttpResponseCopyWithImpl(this._self, this._then);

  final MockzillaHttpResponse _self;
  final $Res Function(MockzillaHttpResponse) _then;

  /// Create a copy of MockzillaHttpResponse
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? statusCode = null,
    Object? headers = null,
    Object? body = null,
  }) {
    return _then(_self.copyWith(
      statusCode: null == statusCode
          ? _self.statusCode
          : statusCode // ignore: cast_nullable_to_non_nullable
              as int,
      headers: null == headers
          ? _self.headers
          : headers // ignore: cast_nullable_to_non_nullable
              as Map<String, String>,
      body: null == body
          ? _self.body
          : body // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _MockzillaHttpResponse implements MockzillaHttpResponse {
  const _MockzillaHttpResponse(
      {this.statusCode = HttpStatus.ok,
      final Map<String, String> headers = const {
        "Content-Type": "application/json"
      },
      this.body = ""})
      : _headers = headers;

  /// The HTTP status to use for the response, defaults to 200 - OK.
  @override
  @JsonKey()
  final int statusCode;

  /// The response headers, defaults a single `Content-Type` header with a
  /// value of `application/json`.
  final Map<String, String> _headers;

  /// The response headers, defaults a single `Content-Type` header with a
  /// value of `application/json`.
  @override
  @JsonKey()
  Map<String, String> get headers {
    if (_headers is EqualUnmodifiableMapView) return _headers;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableMapView(_headers);
  }

  @override
  @JsonKey()
  final String body;

  /// Create a copy of MockzillaHttpResponse
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$MockzillaHttpResponseCopyWith<_MockzillaHttpResponse> get copyWith =>
      __$MockzillaHttpResponseCopyWithImpl<_MockzillaHttpResponse>(
          this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _MockzillaHttpResponse &&
            (identical(other.statusCode, statusCode) ||
                other.statusCode == statusCode) &&
            const DeepCollectionEquality().equals(other._headers, _headers) &&
            (identical(other.body, body) || other.body == body));
  }

  @override
  int get hashCode => Object.hash(runtimeType, statusCode,
      const DeepCollectionEquality().hash(_headers), body);

  @override
  String toString() {
    return 'MockzillaHttpResponse(statusCode: $statusCode, headers: $headers, body: $body)';
  }
}

/// @nodoc
abstract mixin class _$MockzillaHttpResponseCopyWith<$Res>
    implements $MockzillaHttpResponseCopyWith<$Res> {
  factory _$MockzillaHttpResponseCopyWith(_MockzillaHttpResponse value,
          $Res Function(_MockzillaHttpResponse) _then) =
      __$MockzillaHttpResponseCopyWithImpl;
  @override
  @useResult
  $Res call({int statusCode, Map<String, String> headers, String body});
}

/// @nodoc
class __$MockzillaHttpResponseCopyWithImpl<$Res>
    implements _$MockzillaHttpResponseCopyWith<$Res> {
  __$MockzillaHttpResponseCopyWithImpl(this._self, this._then);

  final _MockzillaHttpResponse _self;
  final $Res Function(_MockzillaHttpResponse) _then;

  /// Create a copy of MockzillaHttpResponse
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? statusCode = null,
    Object? headers = null,
    Object? body = null,
  }) {
    return _then(_MockzillaHttpResponse(
      statusCode: null == statusCode
          ? _self.statusCode
          : statusCode // ignore: cast_nullable_to_non_nullable
              as int,
      headers: null == headers
          ? _self._headers
          : headers // ignore: cast_nullable_to_non_nullable
              as Map<String, String>,
      body: null == body
          ? _self.body
          : body // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc
mixin _$DashboardOverridePreset {
  String get name;
  String? get description;
  MockzillaHttpResponse get response;

  /// Create a copy of DashboardOverridePreset
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $DashboardOverridePresetCopyWith<DashboardOverridePreset> get copyWith =>
      _$DashboardOverridePresetCopyWithImpl<DashboardOverridePreset>(
          this as DashboardOverridePreset, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is DashboardOverridePreset &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.description, description) ||
                other.description == description) &&
            (identical(other.response, response) ||
                other.response == response));
  }

  @override
  int get hashCode => Object.hash(runtimeType, name, description, response);

  @override
  String toString() {
    return 'DashboardOverridePreset(name: $name, description: $description, response: $response)';
  }
}

/// @nodoc
abstract mixin class $DashboardOverridePresetCopyWith<$Res> {
  factory $DashboardOverridePresetCopyWith(DashboardOverridePreset value,
          $Res Function(DashboardOverridePreset) _then) =
      _$DashboardOverridePresetCopyWithImpl;
  @useResult
  $Res call({String name, String? description, MockzillaHttpResponse response});

  $MockzillaHttpResponseCopyWith<$Res> get response;
}

/// @nodoc
class _$DashboardOverridePresetCopyWithImpl<$Res>
    implements $DashboardOverridePresetCopyWith<$Res> {
  _$DashboardOverridePresetCopyWithImpl(this._self, this._then);

  final DashboardOverridePreset _self;
  final $Res Function(DashboardOverridePreset) _then;

  /// Create a copy of DashboardOverridePreset
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? description = freezed,
    Object? response = null,
  }) {
    return _then(_self.copyWith(
      name: null == name
          ? _self.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      description: freezed == description
          ? _self.description
          : description // ignore: cast_nullable_to_non_nullable
              as String?,
      response: null == response
          ? _self.response
          : response // ignore: cast_nullable_to_non_nullable
              as MockzillaHttpResponse,
    ));
  }

  /// Create a copy of DashboardOverridePreset
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $MockzillaHttpResponseCopyWith<$Res> get response {
    return $MockzillaHttpResponseCopyWith<$Res>(_self.response, (value) {
      return _then(_self.copyWith(response: value));
    });
  }
}

/// @nodoc

class _DashboardOverridePreset implements DashboardOverridePreset {
  const _DashboardOverridePreset(
      {required this.name, required this.description, required this.response});

  @override
  final String name;
  @override
  final String? description;
  @override
  final MockzillaHttpResponse response;

  /// Create a copy of DashboardOverridePreset
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$DashboardOverridePresetCopyWith<_DashboardOverridePreset> get copyWith =>
      __$DashboardOverridePresetCopyWithImpl<_DashboardOverridePreset>(
          this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _DashboardOverridePreset &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.description, description) ||
                other.description == description) &&
            (identical(other.response, response) ||
                other.response == response));
  }

  @override
  int get hashCode => Object.hash(runtimeType, name, description, response);

  @override
  String toString() {
    return 'DashboardOverridePreset(name: $name, description: $description, response: $response)';
  }
}

/// @nodoc
abstract mixin class _$DashboardOverridePresetCopyWith<$Res>
    implements $DashboardOverridePresetCopyWith<$Res> {
  factory _$DashboardOverridePresetCopyWith(_DashboardOverridePreset value,
          $Res Function(_DashboardOverridePreset) _then) =
      __$DashboardOverridePresetCopyWithImpl;
  @override
  @useResult
  $Res call({String name, String? description, MockzillaHttpResponse response});

  @override
  $MockzillaHttpResponseCopyWith<$Res> get response;
}

/// @nodoc
class __$DashboardOverridePresetCopyWithImpl<$Res>
    implements _$DashboardOverridePresetCopyWith<$Res> {
  __$DashboardOverridePresetCopyWithImpl(this._self, this._then);

  final _DashboardOverridePreset _self;
  final $Res Function(_DashboardOverridePreset) _then;

  /// Create a copy of DashboardOverridePreset
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? name = null,
    Object? description = freezed,
    Object? response = null,
  }) {
    return _then(_DashboardOverridePreset(
      name: null == name
          ? _self.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      description: freezed == description
          ? _self.description
          : description // ignore: cast_nullable_to_non_nullable
              as String?,
      response: null == response
          ? _self.response
          : response // ignore: cast_nullable_to_non_nullable
              as MockzillaHttpResponse,
    ));
  }

  /// Create a copy of DashboardOverridePreset
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $MockzillaHttpResponseCopyWith<$Res> get response {
    return $MockzillaHttpResponseCopyWith<$Res>(_self.response, (value) {
      return _then(_self.copyWith(response: value));
    });
  }
}

/// @nodoc
mixin _$DashboardOptionsConfig {
  List<DashboardOverridePreset> get successPresets;
  List<DashboardOverridePreset> get errorPresets;

  /// Create a copy of DashboardOptionsConfig
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $DashboardOptionsConfigCopyWith<DashboardOptionsConfig> get copyWith =>
      _$DashboardOptionsConfigCopyWithImpl<DashboardOptionsConfig>(
          this as DashboardOptionsConfig, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is DashboardOptionsConfig &&
            const DeepCollectionEquality()
                .equals(other.successPresets, successPresets) &&
            const DeepCollectionEquality()
                .equals(other.errorPresets, errorPresets));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType,
      const DeepCollectionEquality().hash(successPresets),
      const DeepCollectionEquality().hash(errorPresets));

  @override
  String toString() {
    return 'DashboardOptionsConfig(successPresets: $successPresets, errorPresets: $errorPresets)';
  }
}

/// @nodoc
abstract mixin class $DashboardOptionsConfigCopyWith<$Res> {
  factory $DashboardOptionsConfigCopyWith(DashboardOptionsConfig value,
          $Res Function(DashboardOptionsConfig) _then) =
      _$DashboardOptionsConfigCopyWithImpl;
  @useResult
  $Res call(
      {List<DashboardOverridePreset> successPresets,
      List<DashboardOverridePreset> errorPresets});
}

/// @nodoc
class _$DashboardOptionsConfigCopyWithImpl<$Res>
    implements $DashboardOptionsConfigCopyWith<$Res> {
  _$DashboardOptionsConfigCopyWithImpl(this._self, this._then);

  final DashboardOptionsConfig _self;
  final $Res Function(DashboardOptionsConfig) _then;

  /// Create a copy of DashboardOptionsConfig
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? successPresets = null,
    Object? errorPresets = null,
  }) {
    return _then(_self.copyWith(
      successPresets: null == successPresets
          ? _self.successPresets
          : successPresets // ignore: cast_nullable_to_non_nullable
              as List<DashboardOverridePreset>,
      errorPresets: null == errorPresets
          ? _self.errorPresets
          : errorPresets // ignore: cast_nullable_to_non_nullable
              as List<DashboardOverridePreset>,
    ));
  }
}

/// @nodoc

class _DashboardOptionsConfig implements DashboardOptionsConfig {
  const _DashboardOptionsConfig(
      {final List<DashboardOverridePreset> successPresets = const [],
      final List<DashboardOverridePreset> errorPresets = const []})
      : _successPresets = successPresets,
        _errorPresets = errorPresets;

  final List<DashboardOverridePreset> _successPresets;
  @override
  @JsonKey()
  List<DashboardOverridePreset> get successPresets {
    if (_successPresets is EqualUnmodifiableListView) return _successPresets;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_successPresets);
  }

  final List<DashboardOverridePreset> _errorPresets;
  @override
  @JsonKey()
  List<DashboardOverridePreset> get errorPresets {
    if (_errorPresets is EqualUnmodifiableListView) return _errorPresets;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_errorPresets);
  }

  /// Create a copy of DashboardOptionsConfig
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$DashboardOptionsConfigCopyWith<_DashboardOptionsConfig> get copyWith =>
      __$DashboardOptionsConfigCopyWithImpl<_DashboardOptionsConfig>(
          this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _DashboardOptionsConfig &&
            const DeepCollectionEquality()
                .equals(other._successPresets, _successPresets) &&
            const DeepCollectionEquality()
                .equals(other._errorPresets, _errorPresets));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType,
      const DeepCollectionEquality().hash(_successPresets),
      const DeepCollectionEquality().hash(_errorPresets));

  @override
  String toString() {
    return 'DashboardOptionsConfig(successPresets: $successPresets, errorPresets: $errorPresets)';
  }
}

/// @nodoc
abstract mixin class _$DashboardOptionsConfigCopyWith<$Res>
    implements $DashboardOptionsConfigCopyWith<$Res> {
  factory _$DashboardOptionsConfigCopyWith(_DashboardOptionsConfig value,
          $Res Function(_DashboardOptionsConfig) _then) =
      __$DashboardOptionsConfigCopyWithImpl;
  @override
  @useResult
  $Res call(
      {List<DashboardOverridePreset> successPresets,
      List<DashboardOverridePreset> errorPresets});
}

/// @nodoc
class __$DashboardOptionsConfigCopyWithImpl<$Res>
    implements _$DashboardOptionsConfigCopyWith<$Res> {
  __$DashboardOptionsConfigCopyWithImpl(this._self, this._then);

  final _DashboardOptionsConfig _self;
  final $Res Function(_DashboardOptionsConfig) _then;

  /// Create a copy of DashboardOptionsConfig
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? successPresets = null,
    Object? errorPresets = null,
  }) {
    return _then(_DashboardOptionsConfig(
      successPresets: null == successPresets
          ? _self._successPresets
          : successPresets // ignore: cast_nullable_to_non_nullable
              as List<DashboardOverridePreset>,
      errorPresets: null == errorPresets
          ? _self._errorPresets
          : errorPresets // ignore: cast_nullable_to_non_nullable
              as List<DashboardOverridePreset>,
    ));
  }
}

/// @nodoc
mixin _$EndpointConfig {
  String get name;

  /// Identifier for this endpoint, defaults to [name].
  String? get customKey;

  /// Whether the Mockzilla server should return an artificial error for a
  /// request to this endpoint. Defaults to [false].
  bool get shouldFail;

  /// The artificial delay that Mockzilla should apply to responses
  /// to simulate latency. Defaults to 100ms.
  Duration get delay;

  /// Incrementing this will indicate a breaking change has been
  /// made to this endpoint and will invalidate any cached data on the host
  /// device without intervention by the user. Defaults to 1.
  int get versionCode;

  /// Used to determine whether a particular `request` should be evaluated by
  /// this endpoint.
  bool Function(MockzillaHttpRequest request) get endpointMatcher;

  /// Optional, configures the preset responses for the endpoint in the
  /// Mockzilla dashboard.
  DashboardOptionsConfig get dashboardOptionsConfig;

  /// This function is called when a network request is made to this endpoint,
  /// note that if an error is being returned due to [shouldFail] then
  /// `errorHandler` is used instead.
  MockzillaHttpResponse Function(MockzillaHttpRequest request)
      get defaultHandler;

  /// This function is called when, in response to a network request, the
  /// server returns an error due to [shouldFail].
  MockzillaHttpResponse Function(MockzillaHttpRequest request) get errorHandler;

  /// Create a copy of EndpointConfig
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $EndpointConfigCopyWith<EndpointConfig> get copyWith =>
      _$EndpointConfigCopyWithImpl<EndpointConfig>(
          this as EndpointConfig, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is EndpointConfig &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.customKey, customKey) ||
                other.customKey == customKey) &&
            (identical(other.shouldFail, shouldFail) ||
                other.shouldFail == shouldFail) &&
            (identical(other.delay, delay) || other.delay == delay) &&
            (identical(other.versionCode, versionCode) ||
                other.versionCode == versionCode) &&
            (identical(other.endpointMatcher, endpointMatcher) ||
                other.endpointMatcher == endpointMatcher) &&
            (identical(other.dashboardOptionsConfig, dashboardOptionsConfig) ||
                other.dashboardOptionsConfig == dashboardOptionsConfig) &&
            (identical(other.defaultHandler, defaultHandler) ||
                other.defaultHandler == defaultHandler) &&
            (identical(other.errorHandler, errorHandler) ||
                other.errorHandler == errorHandler));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType,
      name,
      customKey,
      shouldFail,
      delay,
      versionCode,
      endpointMatcher,
      dashboardOptionsConfig,
      defaultHandler,
      errorHandler);

  @override
  String toString() {
    return 'EndpointConfig(name: $name, customKey: $customKey, shouldFail: $shouldFail, delay: $delay, versionCode: $versionCode, endpointMatcher: $endpointMatcher, dashboardOptionsConfig: $dashboardOptionsConfig, defaultHandler: $defaultHandler, errorHandler: $errorHandler)';
  }
}

/// @nodoc
abstract mixin class $EndpointConfigCopyWith<$Res> {
  factory $EndpointConfigCopyWith(
          EndpointConfig value, $Res Function(EndpointConfig) _then) =
      _$EndpointConfigCopyWithImpl;
  @useResult
  $Res call(
      {String name,
      String? customKey,
      bool shouldFail,
      Duration delay,
      int versionCode,
      bool Function(MockzillaHttpRequest request) endpointMatcher,
      DashboardOptionsConfig dashboardOptionsConfig,
      MockzillaHttpResponse Function(MockzillaHttpRequest request)
          defaultHandler,
      MockzillaHttpResponse Function(MockzillaHttpRequest request)
          errorHandler});

  $DashboardOptionsConfigCopyWith<$Res> get dashboardOptionsConfig;
}

/// @nodoc
class _$EndpointConfigCopyWithImpl<$Res>
    implements $EndpointConfigCopyWith<$Res> {
  _$EndpointConfigCopyWithImpl(this._self, this._then);

  final EndpointConfig _self;
  final $Res Function(EndpointConfig) _then;

  /// Create a copy of EndpointConfig
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? customKey = freezed,
    Object? shouldFail = null,
    Object? delay = null,
    Object? versionCode = null,
    Object? endpointMatcher = null,
    Object? dashboardOptionsConfig = null,
    Object? defaultHandler = null,
    Object? errorHandler = null,
  }) {
    return _then(_self.copyWith(
      name: null == name
          ? _self.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      customKey: freezed == customKey
          ? _self.customKey
          : customKey // ignore: cast_nullable_to_non_nullable
              as String?,
      shouldFail: null == shouldFail
          ? _self.shouldFail
          : shouldFail // ignore: cast_nullable_to_non_nullable
              as bool,
      delay: null == delay
          ? _self.delay
          : delay // ignore: cast_nullable_to_non_nullable
              as Duration,
      versionCode: null == versionCode
          ? _self.versionCode
          : versionCode // ignore: cast_nullable_to_non_nullable
              as int,
      endpointMatcher: null == endpointMatcher
          ? _self.endpointMatcher
          : endpointMatcher // ignore: cast_nullable_to_non_nullable
              as bool Function(MockzillaHttpRequest request),
      dashboardOptionsConfig: null == dashboardOptionsConfig
          ? _self.dashboardOptionsConfig
          : dashboardOptionsConfig // ignore: cast_nullable_to_non_nullable
              as DashboardOptionsConfig,
      defaultHandler: null == defaultHandler
          ? _self.defaultHandler
          : defaultHandler // ignore: cast_nullable_to_non_nullable
              as MockzillaHttpResponse Function(MockzillaHttpRequest request),
      errorHandler: null == errorHandler
          ? _self.errorHandler
          : errorHandler // ignore: cast_nullable_to_non_nullable
              as MockzillaHttpResponse Function(MockzillaHttpRequest request),
    ));
  }

  /// Create a copy of EndpointConfig
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $DashboardOptionsConfigCopyWith<$Res> get dashboardOptionsConfig {
    return $DashboardOptionsConfigCopyWith<$Res>(_self.dashboardOptionsConfig,
        (value) {
      return _then(_self.copyWith(dashboardOptionsConfig: value));
    });
  }
}

/// @nodoc

class _EndpointConfig extends EndpointConfig {
  const _EndpointConfig(
      {required this.name,
      this.customKey,
      this.shouldFail = false,
      this.delay = const Duration(milliseconds: 100),
      this.versionCode = 1,
      required this.endpointMatcher,
      this.dashboardOptionsConfig = const DashboardOptionsConfig(),
      required this.defaultHandler,
      required this.errorHandler})
      : super._();

  @override
  final String name;

  /// Identifier for this endpoint, defaults to [name].
  @override
  final String? customKey;

  /// Whether the Mockzilla server should return an artificial error for a
  /// request to this endpoint. Defaults to [false].
  @override
  @JsonKey()
  final bool shouldFail;

  /// The artificial delay that Mockzilla should apply to responses
  /// to simulate latency. Defaults to 100ms.
  @override
  @JsonKey()
  final Duration delay;

  /// Incrementing this will indicate a breaking change has been
  /// made to this endpoint and will invalidate any cached data on the host
  /// device without intervention by the user. Defaults to 1.
  @override
  @JsonKey()
  final int versionCode;

  /// Used to determine whether a particular `request` should be evaluated by
  /// this endpoint.
  @override
  final bool Function(MockzillaHttpRequest request) endpointMatcher;

  /// Optional, configures the preset responses for the endpoint in the
  /// Mockzilla dashboard.
  @override
  @JsonKey()
  final DashboardOptionsConfig dashboardOptionsConfig;

  /// This function is called when a network request is made to this endpoint,
  /// note that if an error is being returned due to [shouldFail] then
  /// `errorHandler` is used instead.
  @override
  final MockzillaHttpResponse Function(MockzillaHttpRequest request)
      defaultHandler;

  /// This function is called when, in response to a network request, the
  /// server returns an error due to [shouldFail].
  @override
  final MockzillaHttpResponse Function(MockzillaHttpRequest request)
      errorHandler;

  /// Create a copy of EndpointConfig
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$EndpointConfigCopyWith<_EndpointConfig> get copyWith =>
      __$EndpointConfigCopyWithImpl<_EndpointConfig>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _EndpointConfig &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.customKey, customKey) ||
                other.customKey == customKey) &&
            (identical(other.shouldFail, shouldFail) ||
                other.shouldFail == shouldFail) &&
            (identical(other.delay, delay) || other.delay == delay) &&
            (identical(other.versionCode, versionCode) ||
                other.versionCode == versionCode) &&
            (identical(other.endpointMatcher, endpointMatcher) ||
                other.endpointMatcher == endpointMatcher) &&
            (identical(other.dashboardOptionsConfig, dashboardOptionsConfig) ||
                other.dashboardOptionsConfig == dashboardOptionsConfig) &&
            (identical(other.defaultHandler, defaultHandler) ||
                other.defaultHandler == defaultHandler) &&
            (identical(other.errorHandler, errorHandler) ||
                other.errorHandler == errorHandler));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType,
      name,
      customKey,
      shouldFail,
      delay,
      versionCode,
      endpointMatcher,
      dashboardOptionsConfig,
      defaultHandler,
      errorHandler);

  @override
  String toString() {
    return 'EndpointConfig(name: $name, customKey: $customKey, shouldFail: $shouldFail, delay: $delay, versionCode: $versionCode, endpointMatcher: $endpointMatcher, dashboardOptionsConfig: $dashboardOptionsConfig, defaultHandler: $defaultHandler, errorHandler: $errorHandler)';
  }
}

/// @nodoc
abstract mixin class _$EndpointConfigCopyWith<$Res>
    implements $EndpointConfigCopyWith<$Res> {
  factory _$EndpointConfigCopyWith(
          _EndpointConfig value, $Res Function(_EndpointConfig) _then) =
      __$EndpointConfigCopyWithImpl;
  @override
  @useResult
  $Res call(
      {String name,
      String? customKey,
      bool shouldFail,
      Duration delay,
      int versionCode,
      bool Function(MockzillaHttpRequest request) endpointMatcher,
      DashboardOptionsConfig dashboardOptionsConfig,
      MockzillaHttpResponse Function(MockzillaHttpRequest request)
          defaultHandler,
      MockzillaHttpResponse Function(MockzillaHttpRequest request)
          errorHandler});

  @override
  $DashboardOptionsConfigCopyWith<$Res> get dashboardOptionsConfig;
}

/// @nodoc
class __$EndpointConfigCopyWithImpl<$Res>
    implements _$EndpointConfigCopyWith<$Res> {
  __$EndpointConfigCopyWithImpl(this._self, this._then);

  final _EndpointConfig _self;
  final $Res Function(_EndpointConfig) _then;

  /// Create a copy of EndpointConfig
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? name = null,
    Object? customKey = freezed,
    Object? shouldFail = null,
    Object? delay = null,
    Object? versionCode = null,
    Object? endpointMatcher = null,
    Object? dashboardOptionsConfig = null,
    Object? defaultHandler = null,
    Object? errorHandler = null,
  }) {
    return _then(_EndpointConfig(
      name: null == name
          ? _self.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      customKey: freezed == customKey
          ? _self.customKey
          : customKey // ignore: cast_nullable_to_non_nullable
              as String?,
      shouldFail: null == shouldFail
          ? _self.shouldFail
          : shouldFail // ignore: cast_nullable_to_non_nullable
              as bool,
      delay: null == delay
          ? _self.delay
          : delay // ignore: cast_nullable_to_non_nullable
              as Duration,
      versionCode: null == versionCode
          ? _self.versionCode
          : versionCode // ignore: cast_nullable_to_non_nullable
              as int,
      endpointMatcher: null == endpointMatcher
          ? _self.endpointMatcher
          : endpointMatcher // ignore: cast_nullable_to_non_nullable
              as bool Function(MockzillaHttpRequest request),
      dashboardOptionsConfig: null == dashboardOptionsConfig
          ? _self.dashboardOptionsConfig
          : dashboardOptionsConfig // ignore: cast_nullable_to_non_nullable
              as DashboardOptionsConfig,
      defaultHandler: null == defaultHandler
          ? _self.defaultHandler
          : defaultHandler // ignore: cast_nullable_to_non_nullable
              as MockzillaHttpResponse Function(MockzillaHttpRequest request),
      errorHandler: null == errorHandler
          ? _self.errorHandler
          : errorHandler // ignore: cast_nullable_to_non_nullable
              as MockzillaHttpResponse Function(MockzillaHttpRequest request),
    ));
  }

  /// Create a copy of EndpointConfig
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $DashboardOptionsConfigCopyWith<$Res> get dashboardOptionsConfig {
    return $DashboardOptionsConfigCopyWith<$Res>(_self.dashboardOptionsConfig,
        (value) {
      return _then(_self.copyWith(dashboardOptionsConfig: value));
    });
  }
}

/// @nodoc
mixin _$MockzillaConfig {
  /// The port that the Mockzilla should be available through.
  int get port;

  /// The list of available mocked endpoints.
  List<EndpointConfig> get endpoints;

  /// Whether Mockzilla server should only be available on the host device.
  bool get localHostOnly;

  /// The level of logging that should be used by Mockzilla.
  LogLevel get logLevel;

  /// Whether devices running Mockzilla are discoverable on the local network
  /// through the desktop management app.
  bool get isNetworkDiscoveryEnabled;

  /// Custom logger implementations for surfacing Mockzilla logs outside of
  /// the Flutter console.
  List<MockzillaLogger> get loggers;

  /// Create a copy of MockzillaConfig
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $MockzillaConfigCopyWith<MockzillaConfig> get copyWith =>
      _$MockzillaConfigCopyWithImpl<MockzillaConfig>(
          this as MockzillaConfig, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is MockzillaConfig &&
            (identical(other.port, port) || other.port == port) &&
            const DeepCollectionEquality().equals(other.endpoints, endpoints) &&
            (identical(other.localHostOnly, localHostOnly) ||
                other.localHostOnly == localHostOnly) &&
            (identical(other.logLevel, logLevel) ||
                other.logLevel == logLevel) &&
            (identical(other.isNetworkDiscoveryEnabled,
                    isNetworkDiscoveryEnabled) ||
                other.isNetworkDiscoveryEnabled == isNetworkDiscoveryEnabled) &&
            const DeepCollectionEquality().equals(other.loggers, loggers));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType,
      port,
      const DeepCollectionEquality().hash(endpoints),
      localHostOnly,
      logLevel,
      isNetworkDiscoveryEnabled,
      const DeepCollectionEquality().hash(loggers));

  @override
  String toString() {
    return 'MockzillaConfig(port: $port, endpoints: $endpoints, localHostOnly: $localHostOnly, logLevel: $logLevel, isNetworkDiscoveryEnabled: $isNetworkDiscoveryEnabled, loggers: $loggers)';
  }
}

/// @nodoc
abstract mixin class $MockzillaConfigCopyWith<$Res> {
  factory $MockzillaConfigCopyWith(
          MockzillaConfig value, $Res Function(MockzillaConfig) _then) =
      _$MockzillaConfigCopyWithImpl;
  @useResult
  $Res call(
      {int port,
      List<EndpointConfig> endpoints,
      bool localHostOnly,
      LogLevel logLevel,
      bool isNetworkDiscoveryEnabled,
      List<MockzillaLogger> loggers});
}

/// @nodoc
class _$MockzillaConfigCopyWithImpl<$Res>
    implements $MockzillaConfigCopyWith<$Res> {
  _$MockzillaConfigCopyWithImpl(this._self, this._then);

  final MockzillaConfig _self;
  final $Res Function(MockzillaConfig) _then;

  /// Create a copy of MockzillaConfig
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? port = null,
    Object? endpoints = null,
    Object? localHostOnly = null,
    Object? logLevel = null,
    Object? isNetworkDiscoveryEnabled = null,
    Object? loggers = null,
  }) {
    return _then(_self.copyWith(
      port: null == port
          ? _self.port
          : port // ignore: cast_nullable_to_non_nullable
              as int,
      endpoints: null == endpoints
          ? _self.endpoints
          : endpoints // ignore: cast_nullable_to_non_nullable
              as List<EndpointConfig>,
      localHostOnly: null == localHostOnly
          ? _self.localHostOnly
          : localHostOnly // ignore: cast_nullable_to_non_nullable
              as bool,
      logLevel: null == logLevel
          ? _self.logLevel
          : logLevel // ignore: cast_nullable_to_non_nullable
              as LogLevel,
      isNetworkDiscoveryEnabled: null == isNetworkDiscoveryEnabled
          ? _self.isNetworkDiscoveryEnabled
          : isNetworkDiscoveryEnabled // ignore: cast_nullable_to_non_nullable
              as bool,
      loggers: null == loggers
          ? _self.loggers
          : loggers // ignore: cast_nullable_to_non_nullable
              as List<MockzillaLogger>,
    ));
  }
}

/// @nodoc

class _MockzillaConfig implements MockzillaConfig {
  const _MockzillaConfig(
      {this.port = 8080,
      final List<EndpointConfig> endpoints = const [],
      this.localHostOnly = false,
      this.logLevel = LogLevel.info,
      this.isNetworkDiscoveryEnabled = true,
      final List<MockzillaLogger> loggers = const []})
      : _endpoints = endpoints,
        _loggers = loggers;

  /// The port that the Mockzilla should be available through.
  @override
  @JsonKey()
  final int port;

  /// The list of available mocked endpoints.
  final List<EndpointConfig> _endpoints;

  /// The list of available mocked endpoints.
  @override
  @JsonKey()
  List<EndpointConfig> get endpoints {
    if (_endpoints is EqualUnmodifiableListView) return _endpoints;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_endpoints);
  }

  /// Whether Mockzilla server should only be available on the host device.
  @override
  @JsonKey()
  final bool localHostOnly;

  /// The level of logging that should be used by Mockzilla.
  @override
  @JsonKey()
  final LogLevel logLevel;

  /// Whether devices running Mockzilla are discoverable on the local network
  /// through the desktop management app.
  @override
  @JsonKey()
  final bool isNetworkDiscoveryEnabled;

  /// Custom logger implementations for surfacing Mockzilla logs outside of
  /// the Flutter console.
  final List<MockzillaLogger> _loggers;

  /// Custom logger implementations for surfacing Mockzilla logs outside of
  /// the Flutter console.
  @override
  @JsonKey()
  List<MockzillaLogger> get loggers {
    if (_loggers is EqualUnmodifiableListView) return _loggers;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_loggers);
  }

  /// Create a copy of MockzillaConfig
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$MockzillaConfigCopyWith<_MockzillaConfig> get copyWith =>
      __$MockzillaConfigCopyWithImpl<_MockzillaConfig>(this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _MockzillaConfig &&
            (identical(other.port, port) || other.port == port) &&
            const DeepCollectionEquality()
                .equals(other._endpoints, _endpoints) &&
            (identical(other.localHostOnly, localHostOnly) ||
                other.localHostOnly == localHostOnly) &&
            (identical(other.logLevel, logLevel) ||
                other.logLevel == logLevel) &&
            (identical(other.isNetworkDiscoveryEnabled,
                    isNetworkDiscoveryEnabled) ||
                other.isNetworkDiscoveryEnabled == isNetworkDiscoveryEnabled) &&
            const DeepCollectionEquality().equals(other._loggers, _loggers));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType,
      port,
      const DeepCollectionEquality().hash(_endpoints),
      localHostOnly,
      logLevel,
      isNetworkDiscoveryEnabled,
      const DeepCollectionEquality().hash(_loggers));

  @override
  String toString() {
    return 'MockzillaConfig(port: $port, endpoints: $endpoints, localHostOnly: $localHostOnly, logLevel: $logLevel, isNetworkDiscoveryEnabled: $isNetworkDiscoveryEnabled, loggers: $loggers)';
  }
}

/// @nodoc
abstract mixin class _$MockzillaConfigCopyWith<$Res>
    implements $MockzillaConfigCopyWith<$Res> {
  factory _$MockzillaConfigCopyWith(
          _MockzillaConfig value, $Res Function(_MockzillaConfig) _then) =
      __$MockzillaConfigCopyWithImpl;
  @override
  @useResult
  $Res call(
      {int port,
      List<EndpointConfig> endpoints,
      bool localHostOnly,
      LogLevel logLevel,
      bool isNetworkDiscoveryEnabled,
      List<MockzillaLogger> loggers});
}

/// @nodoc
class __$MockzillaConfigCopyWithImpl<$Res>
    implements _$MockzillaConfigCopyWith<$Res> {
  __$MockzillaConfigCopyWithImpl(this._self, this._then);

  final _MockzillaConfig _self;
  final $Res Function(_MockzillaConfig) _then;

  /// Create a copy of MockzillaConfig
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? port = null,
    Object? endpoints = null,
    Object? localHostOnly = null,
    Object? logLevel = null,
    Object? isNetworkDiscoveryEnabled = null,
    Object? loggers = null,
  }) {
    return _then(_MockzillaConfig(
      port: null == port
          ? _self.port
          : port // ignore: cast_nullable_to_non_nullable
              as int,
      endpoints: null == endpoints
          ? _self._endpoints
          : endpoints // ignore: cast_nullable_to_non_nullable
              as List<EndpointConfig>,
      localHostOnly: null == localHostOnly
          ? _self.localHostOnly
          : localHostOnly // ignore: cast_nullable_to_non_nullable
              as bool,
      logLevel: null == logLevel
          ? _self.logLevel
          : logLevel // ignore: cast_nullable_to_non_nullable
              as LogLevel,
      isNetworkDiscoveryEnabled: null == isNetworkDiscoveryEnabled
          ? _self.isNetworkDiscoveryEnabled
          : isNetworkDiscoveryEnabled // ignore: cast_nullable_to_non_nullable
              as bool,
      loggers: null == loggers
          ? _self._loggers
          : loggers // ignore: cast_nullable_to_non_nullable
              as List<MockzillaLogger>,
    ));
  }
}

/// @nodoc
mixin _$MockzillaRuntimeParams {
  MockzillaConfig get config;
  String get mockBaseUrl;
  String get apiBaseUrl;
  int get port;

  /// Create a copy of MockzillaRuntimeParams
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $MockzillaRuntimeParamsCopyWith<MockzillaRuntimeParams> get copyWith =>
      _$MockzillaRuntimeParamsCopyWithImpl<MockzillaRuntimeParams>(
          this as MockzillaRuntimeParams, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is MockzillaRuntimeParams &&
            (identical(other.config, config) || other.config == config) &&
            (identical(other.mockBaseUrl, mockBaseUrl) ||
                other.mockBaseUrl == mockBaseUrl) &&
            (identical(other.apiBaseUrl, apiBaseUrl) ||
                other.apiBaseUrl == apiBaseUrl) &&
            (identical(other.port, port) || other.port == port));
  }

  @override
  int get hashCode =>
      Object.hash(runtimeType, config, mockBaseUrl, apiBaseUrl, port);

  @override
  String toString() {
    return 'MockzillaRuntimeParams(config: $config, mockBaseUrl: $mockBaseUrl, apiBaseUrl: $apiBaseUrl, port: $port)';
  }
}

/// @nodoc
abstract mixin class $MockzillaRuntimeParamsCopyWith<$Res> {
  factory $MockzillaRuntimeParamsCopyWith(MockzillaRuntimeParams value,
          $Res Function(MockzillaRuntimeParams) _then) =
      _$MockzillaRuntimeParamsCopyWithImpl;
  @useResult
  $Res call(
      {MockzillaConfig config,
      String mockBaseUrl,
      String apiBaseUrl,
      int port});

  $MockzillaConfigCopyWith<$Res> get config;
}

/// @nodoc
class _$MockzillaRuntimeParamsCopyWithImpl<$Res>
    implements $MockzillaRuntimeParamsCopyWith<$Res> {
  _$MockzillaRuntimeParamsCopyWithImpl(this._self, this._then);

  final MockzillaRuntimeParams _self;
  final $Res Function(MockzillaRuntimeParams) _then;

  /// Create a copy of MockzillaRuntimeParams
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? config = null,
    Object? mockBaseUrl = null,
    Object? apiBaseUrl = null,
    Object? port = null,
  }) {
    return _then(_self.copyWith(
      config: null == config
          ? _self.config
          : config // ignore: cast_nullable_to_non_nullable
              as MockzillaConfig,
      mockBaseUrl: null == mockBaseUrl
          ? _self.mockBaseUrl
          : mockBaseUrl // ignore: cast_nullable_to_non_nullable
              as String,
      apiBaseUrl: null == apiBaseUrl
          ? _self.apiBaseUrl
          : apiBaseUrl // ignore: cast_nullable_to_non_nullable
              as String,
      port: null == port
          ? _self.port
          : port // ignore: cast_nullable_to_non_nullable
              as int,
    ));
  }

  /// Create a copy of MockzillaRuntimeParams
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $MockzillaConfigCopyWith<$Res> get config {
    return $MockzillaConfigCopyWith<$Res>(_self.config, (value) {
      return _then(_self.copyWith(config: value));
    });
  }
}

/// @nodoc

class _MockzillaRuntimeParams implements MockzillaRuntimeParams {
  const _MockzillaRuntimeParams(
      {required this.config,
      required this.mockBaseUrl,
      required this.apiBaseUrl,
      required this.port});

  @override
  final MockzillaConfig config;
  @override
  final String mockBaseUrl;
  @override
  final String apiBaseUrl;
  @override
  final int port;

  /// Create a copy of MockzillaRuntimeParams
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$MockzillaRuntimeParamsCopyWith<_MockzillaRuntimeParams> get copyWith =>
      __$MockzillaRuntimeParamsCopyWithImpl<_MockzillaRuntimeParams>(
          this, _$identity);

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _MockzillaRuntimeParams &&
            (identical(other.config, config) || other.config == config) &&
            (identical(other.mockBaseUrl, mockBaseUrl) ||
                other.mockBaseUrl == mockBaseUrl) &&
            (identical(other.apiBaseUrl, apiBaseUrl) ||
                other.apiBaseUrl == apiBaseUrl) &&
            (identical(other.port, port) || other.port == port));
  }

  @override
  int get hashCode =>
      Object.hash(runtimeType, config, mockBaseUrl, apiBaseUrl, port);

  @override
  String toString() {
    return 'MockzillaRuntimeParams(config: $config, mockBaseUrl: $mockBaseUrl, apiBaseUrl: $apiBaseUrl, port: $port)';
  }
}

/// @nodoc
abstract mixin class _$MockzillaRuntimeParamsCopyWith<$Res>
    implements $MockzillaRuntimeParamsCopyWith<$Res> {
  factory _$MockzillaRuntimeParamsCopyWith(_MockzillaRuntimeParams value,
          $Res Function(_MockzillaRuntimeParams) _then) =
      __$MockzillaRuntimeParamsCopyWithImpl;
  @override
  @useResult
  $Res call(
      {MockzillaConfig config,
      String mockBaseUrl,
      String apiBaseUrl,
      int port});

  @override
  $MockzillaConfigCopyWith<$Res> get config;
}

/// @nodoc
class __$MockzillaRuntimeParamsCopyWithImpl<$Res>
    implements _$MockzillaRuntimeParamsCopyWith<$Res> {
  __$MockzillaRuntimeParamsCopyWithImpl(this._self, this._then);

  final _MockzillaRuntimeParams _self;
  final $Res Function(_MockzillaRuntimeParams) _then;

  /// Create a copy of MockzillaRuntimeParams
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? config = null,
    Object? mockBaseUrl = null,
    Object? apiBaseUrl = null,
    Object? port = null,
  }) {
    return _then(_MockzillaRuntimeParams(
      config: null == config
          ? _self.config
          : config // ignore: cast_nullable_to_non_nullable
              as MockzillaConfig,
      mockBaseUrl: null == mockBaseUrl
          ? _self.mockBaseUrl
          : mockBaseUrl // ignore: cast_nullable_to_non_nullable
              as String,
      apiBaseUrl: null == apiBaseUrl
          ? _self.apiBaseUrl
          : apiBaseUrl // ignore: cast_nullable_to_non_nullable
              as String,
      port: null == port
          ? _self.port
          : port // ignore: cast_nullable_to_non_nullable
              as int,
    ));
  }

  /// Create a copy of MockzillaRuntimeParams
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $MockzillaConfigCopyWith<$Res> get config {
    return $MockzillaConfigCopyWith<$Res>(_self.config, (value) {
      return _then(_self.copyWith(config: value));
    });
  }
}

// dart format on
