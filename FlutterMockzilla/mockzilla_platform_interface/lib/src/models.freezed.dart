// coverage:ignore-file
// GENERATED CODE - DO NOT MODIFY BY HAND
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'models.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

T _$identity<T>(T value) => value;

final _privateConstructorUsedError = UnsupportedError(
    'It seems like you constructed your class using `MyClass._()`. This constructor is only meant to be used by freezed and you are not supposed to need it nor use it.\nPlease check the documentation here for more information: https://github.com/rrousselGit/freezed#adding-getters-and-methods-to-our-models');

/// @nodoc
mixin _$MockzillaHttpRequest {
  String get uri => throw _privateConstructorUsedError;
  Map<String, String> get headers => throw _privateConstructorUsedError;
  String get body => throw _privateConstructorUsedError;
  HttpMethod get method => throw _privateConstructorUsedError;

  /// Create a copy of MockzillaHttpRequest
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $MockzillaHttpRequestCopyWith<MockzillaHttpRequest> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $MockzillaHttpRequestCopyWith<$Res> {
  factory $MockzillaHttpRequestCopyWith(MockzillaHttpRequest value,
          $Res Function(MockzillaHttpRequest) then) =
      _$MockzillaHttpRequestCopyWithImpl<$Res, MockzillaHttpRequest>;
  @useResult
  $Res call(
      {String uri,
      Map<String, String> headers,
      String body,
      HttpMethod method});
}

/// @nodoc
class _$MockzillaHttpRequestCopyWithImpl<$Res,
        $Val extends MockzillaHttpRequest>
    implements $MockzillaHttpRequestCopyWith<$Res> {
  _$MockzillaHttpRequestCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

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
    return _then(_value.copyWith(
      uri: null == uri
          ? _value.uri
          : uri // ignore: cast_nullable_to_non_nullable
              as String,
      headers: null == headers
          ? _value.headers
          : headers // ignore: cast_nullable_to_non_nullable
              as Map<String, String>,
      body: null == body
          ? _value.body
          : body // ignore: cast_nullable_to_non_nullable
              as String,
      method: null == method
          ? _value.method
          : method // ignore: cast_nullable_to_non_nullable
              as HttpMethod,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$MockzillaHttpRequestImplCopyWith<$Res>
    implements $MockzillaHttpRequestCopyWith<$Res> {
  factory _$$MockzillaHttpRequestImplCopyWith(_$MockzillaHttpRequestImpl value,
          $Res Function(_$MockzillaHttpRequestImpl) then) =
      __$$MockzillaHttpRequestImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {String uri,
      Map<String, String> headers,
      String body,
      HttpMethod method});
}

/// @nodoc
class __$$MockzillaHttpRequestImplCopyWithImpl<$Res>
    extends _$MockzillaHttpRequestCopyWithImpl<$Res, _$MockzillaHttpRequestImpl>
    implements _$$MockzillaHttpRequestImplCopyWith<$Res> {
  __$$MockzillaHttpRequestImplCopyWithImpl(_$MockzillaHttpRequestImpl _value,
      $Res Function(_$MockzillaHttpRequestImpl) _then)
      : super(_value, _then);

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
    return _then(_$MockzillaHttpRequestImpl(
      uri: null == uri
          ? _value.uri
          : uri // ignore: cast_nullable_to_non_nullable
              as String,
      headers: null == headers
          ? _value._headers
          : headers // ignore: cast_nullable_to_non_nullable
              as Map<String, String>,
      body: null == body
          ? _value.body
          : body // ignore: cast_nullable_to_non_nullable
              as String,
      method: null == method
          ? _value.method
          : method // ignore: cast_nullable_to_non_nullable
              as HttpMethod,
    ));
  }
}

/// @nodoc

class _$MockzillaHttpRequestImpl implements _MockzillaHttpRequest {
  const _$MockzillaHttpRequestImpl(
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

  @override
  String toString() {
    return 'MockzillaHttpRequest(uri: $uri, headers: $headers, body: $body, method: $method)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$MockzillaHttpRequestImpl &&
            (identical(other.uri, uri) || other.uri == uri) &&
            const DeepCollectionEquality().equals(other._headers, _headers) &&
            (identical(other.body, body) || other.body == body) &&
            (identical(other.method, method) || other.method == method));
  }

  @override
  int get hashCode => Object.hash(runtimeType, uri,
      const DeepCollectionEquality().hash(_headers), body, method);

  /// Create a copy of MockzillaHttpRequest
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$MockzillaHttpRequestImplCopyWith<_$MockzillaHttpRequestImpl>
      get copyWith =>
          __$$MockzillaHttpRequestImplCopyWithImpl<_$MockzillaHttpRequestImpl>(
              this, _$identity);
}

abstract class _MockzillaHttpRequest implements MockzillaHttpRequest {
  const factory _MockzillaHttpRequest(
      {required final String uri,
      final Map<String, String> headers,
      final String body,
      required final HttpMethod method}) = _$MockzillaHttpRequestImpl;

  @override
  String get uri;
  @override
  Map<String, String> get headers;
  @override
  String get body;
  @override
  HttpMethod get method;

  /// Create a copy of MockzillaHttpRequest
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$MockzillaHttpRequestImplCopyWith<_$MockzillaHttpRequestImpl>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
mixin _$MockzillaHttpResponse {
  int get statusCode => throw _privateConstructorUsedError;
  Map<String, String> get headers => throw _privateConstructorUsedError;
  String get body => throw _privateConstructorUsedError;

  /// Create a copy of MockzillaHttpResponse
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $MockzillaHttpResponseCopyWith<MockzillaHttpResponse> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $MockzillaHttpResponseCopyWith<$Res> {
  factory $MockzillaHttpResponseCopyWith(MockzillaHttpResponse value,
          $Res Function(MockzillaHttpResponse) then) =
      _$MockzillaHttpResponseCopyWithImpl<$Res, MockzillaHttpResponse>;
  @useResult
  $Res call({int statusCode, Map<String, String> headers, String body});
}

/// @nodoc
class _$MockzillaHttpResponseCopyWithImpl<$Res,
        $Val extends MockzillaHttpResponse>
    implements $MockzillaHttpResponseCopyWith<$Res> {
  _$MockzillaHttpResponseCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of MockzillaHttpResponse
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? statusCode = null,
    Object? headers = null,
    Object? body = null,
  }) {
    return _then(_value.copyWith(
      statusCode: null == statusCode
          ? _value.statusCode
          : statusCode // ignore: cast_nullable_to_non_nullable
              as int,
      headers: null == headers
          ? _value.headers
          : headers // ignore: cast_nullable_to_non_nullable
              as Map<String, String>,
      body: null == body
          ? _value.body
          : body // ignore: cast_nullable_to_non_nullable
              as String,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$MockzillaHttpResponseImplCopyWith<$Res>
    implements $MockzillaHttpResponseCopyWith<$Res> {
  factory _$$MockzillaHttpResponseImplCopyWith(
          _$MockzillaHttpResponseImpl value,
          $Res Function(_$MockzillaHttpResponseImpl) then) =
      __$$MockzillaHttpResponseImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({int statusCode, Map<String, String> headers, String body});
}

/// @nodoc
class __$$MockzillaHttpResponseImplCopyWithImpl<$Res>
    extends _$MockzillaHttpResponseCopyWithImpl<$Res,
        _$MockzillaHttpResponseImpl>
    implements _$$MockzillaHttpResponseImplCopyWith<$Res> {
  __$$MockzillaHttpResponseImplCopyWithImpl(_$MockzillaHttpResponseImpl _value,
      $Res Function(_$MockzillaHttpResponseImpl) _then)
      : super(_value, _then);

  /// Create a copy of MockzillaHttpResponse
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? statusCode = null,
    Object? headers = null,
    Object? body = null,
  }) {
    return _then(_$MockzillaHttpResponseImpl(
      statusCode: null == statusCode
          ? _value.statusCode
          : statusCode // ignore: cast_nullable_to_non_nullable
              as int,
      headers: null == headers
          ? _value._headers
          : headers // ignore: cast_nullable_to_non_nullable
              as Map<String, String>,
      body: null == body
          ? _value.body
          : body // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$MockzillaHttpResponseImpl implements _MockzillaHttpResponse {
  const _$MockzillaHttpResponseImpl(
      {this.statusCode = HttpStatus.ok,
      final Map<String, String> headers = const {
        "Content-Type": "application/json"
      },
      this.body = ""})
      : _headers = headers;

  @override
  @JsonKey()
  final int statusCode;
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
  String toString() {
    return 'MockzillaHttpResponse(statusCode: $statusCode, headers: $headers, body: $body)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$MockzillaHttpResponseImpl &&
            (identical(other.statusCode, statusCode) ||
                other.statusCode == statusCode) &&
            const DeepCollectionEquality().equals(other._headers, _headers) &&
            (identical(other.body, body) || other.body == body));
  }

  @override
  int get hashCode => Object.hash(runtimeType, statusCode,
      const DeepCollectionEquality().hash(_headers), body);

  /// Create a copy of MockzillaHttpResponse
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$MockzillaHttpResponseImplCopyWith<_$MockzillaHttpResponseImpl>
      get copyWith => __$$MockzillaHttpResponseImplCopyWithImpl<
          _$MockzillaHttpResponseImpl>(this, _$identity);
}

abstract class _MockzillaHttpResponse implements MockzillaHttpResponse {
  const factory _MockzillaHttpResponse(
      {final int statusCode,
      final Map<String, String> headers,
      final String body}) = _$MockzillaHttpResponseImpl;

  @override
  int get statusCode;
  @override
  Map<String, String> get headers;
  @override
  String get body;

  /// Create a copy of MockzillaHttpResponse
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$MockzillaHttpResponseImplCopyWith<_$MockzillaHttpResponseImpl>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
mixin _$DashboardOverridePreset {
  String get name => throw _privateConstructorUsedError;
  String? get description => throw _privateConstructorUsedError;
  MockzillaHttpResponse get response => throw _privateConstructorUsedError;

  /// Create a copy of DashboardOverridePreset
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $DashboardOverridePresetCopyWith<DashboardOverridePreset> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $DashboardOverridePresetCopyWith<$Res> {
  factory $DashboardOverridePresetCopyWith(DashboardOverridePreset value,
          $Res Function(DashboardOverridePreset) then) =
      _$DashboardOverridePresetCopyWithImpl<$Res, DashboardOverridePreset>;
  @useResult
  $Res call({String name, String? description, MockzillaHttpResponse response});

  $MockzillaHttpResponseCopyWith<$Res> get response;
}

/// @nodoc
class _$DashboardOverridePresetCopyWithImpl<$Res,
        $Val extends DashboardOverridePreset>
    implements $DashboardOverridePresetCopyWith<$Res> {
  _$DashboardOverridePresetCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of DashboardOverridePreset
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? description = freezed,
    Object? response = null,
  }) {
    return _then(_value.copyWith(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      description: freezed == description
          ? _value.description
          : description // ignore: cast_nullable_to_non_nullable
              as String?,
      response: null == response
          ? _value.response
          : response // ignore: cast_nullable_to_non_nullable
              as MockzillaHttpResponse,
    ) as $Val);
  }

  /// Create a copy of DashboardOverridePreset
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $MockzillaHttpResponseCopyWith<$Res> get response {
    return $MockzillaHttpResponseCopyWith<$Res>(_value.response, (value) {
      return _then(_value.copyWith(response: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$DashboardOverridePresetImplCopyWith<$Res>
    implements $DashboardOverridePresetCopyWith<$Res> {
  factory _$$DashboardOverridePresetImplCopyWith(
          _$DashboardOverridePresetImpl value,
          $Res Function(_$DashboardOverridePresetImpl) then) =
      __$$DashboardOverridePresetImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({String name, String? description, MockzillaHttpResponse response});

  @override
  $MockzillaHttpResponseCopyWith<$Res> get response;
}

/// @nodoc
class __$$DashboardOverridePresetImplCopyWithImpl<$Res>
    extends _$DashboardOverridePresetCopyWithImpl<$Res,
        _$DashboardOverridePresetImpl>
    implements _$$DashboardOverridePresetImplCopyWith<$Res> {
  __$$DashboardOverridePresetImplCopyWithImpl(
      _$DashboardOverridePresetImpl _value,
      $Res Function(_$DashboardOverridePresetImpl) _then)
      : super(_value, _then);

  /// Create a copy of DashboardOverridePreset
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? description = freezed,
    Object? response = null,
  }) {
    return _then(_$DashboardOverridePresetImpl(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      description: freezed == description
          ? _value.description
          : description // ignore: cast_nullable_to_non_nullable
              as String?,
      response: null == response
          ? _value.response
          : response // ignore: cast_nullable_to_non_nullable
              as MockzillaHttpResponse,
    ));
  }
}

/// @nodoc

class _$DashboardOverridePresetImpl implements _DashboardOverridePreset {
  const _$DashboardOverridePresetImpl(
      {required this.name, required this.description, required this.response});

  @override
  final String name;
  @override
  final String? description;
  @override
  final MockzillaHttpResponse response;

  @override
  String toString() {
    return 'DashboardOverridePreset(name: $name, description: $description, response: $response)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$DashboardOverridePresetImpl &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.description, description) ||
                other.description == description) &&
            (identical(other.response, response) ||
                other.response == response));
  }

  @override
  int get hashCode => Object.hash(runtimeType, name, description, response);

  /// Create a copy of DashboardOverridePreset
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$DashboardOverridePresetImplCopyWith<_$DashboardOverridePresetImpl>
      get copyWith => __$$DashboardOverridePresetImplCopyWithImpl<
          _$DashboardOverridePresetImpl>(this, _$identity);
}

abstract class _DashboardOverridePreset implements DashboardOverridePreset {
  const factory _DashboardOverridePreset(
          {required final String name,
          required final String? description,
          required final MockzillaHttpResponse response}) =
      _$DashboardOverridePresetImpl;

  @override
  String get name;
  @override
  String? get description;
  @override
  MockzillaHttpResponse get response;

  /// Create a copy of DashboardOverridePreset
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$DashboardOverridePresetImplCopyWith<_$DashboardOverridePresetImpl>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
mixin _$DashboardOptionsConfig {
  List<DashboardOverridePreset> get successPresets =>
      throw _privateConstructorUsedError;
  List<DashboardOverridePreset> get errorPresets =>
      throw _privateConstructorUsedError;

  /// Create a copy of DashboardOptionsConfig
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $DashboardOptionsConfigCopyWith<DashboardOptionsConfig> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $DashboardOptionsConfigCopyWith<$Res> {
  factory $DashboardOptionsConfigCopyWith(DashboardOptionsConfig value,
          $Res Function(DashboardOptionsConfig) then) =
      _$DashboardOptionsConfigCopyWithImpl<$Res, DashboardOptionsConfig>;
  @useResult
  $Res call(
      {List<DashboardOverridePreset> successPresets,
      List<DashboardOverridePreset> errorPresets});
}

/// @nodoc
class _$DashboardOptionsConfigCopyWithImpl<$Res,
        $Val extends DashboardOptionsConfig>
    implements $DashboardOptionsConfigCopyWith<$Res> {
  _$DashboardOptionsConfigCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of DashboardOptionsConfig
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? successPresets = null,
    Object? errorPresets = null,
  }) {
    return _then(_value.copyWith(
      successPresets: null == successPresets
          ? _value.successPresets
          : successPresets // ignore: cast_nullable_to_non_nullable
              as List<DashboardOverridePreset>,
      errorPresets: null == errorPresets
          ? _value.errorPresets
          : errorPresets // ignore: cast_nullable_to_non_nullable
              as List<DashboardOverridePreset>,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$DashboardOptionsConfigImplCopyWith<$Res>
    implements $DashboardOptionsConfigCopyWith<$Res> {
  factory _$$DashboardOptionsConfigImplCopyWith(
          _$DashboardOptionsConfigImpl value,
          $Res Function(_$DashboardOptionsConfigImpl) then) =
      __$$DashboardOptionsConfigImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {List<DashboardOverridePreset> successPresets,
      List<DashboardOverridePreset> errorPresets});
}

/// @nodoc
class __$$DashboardOptionsConfigImplCopyWithImpl<$Res>
    extends _$DashboardOptionsConfigCopyWithImpl<$Res,
        _$DashboardOptionsConfigImpl>
    implements _$$DashboardOptionsConfigImplCopyWith<$Res> {
  __$$DashboardOptionsConfigImplCopyWithImpl(
      _$DashboardOptionsConfigImpl _value,
      $Res Function(_$DashboardOptionsConfigImpl) _then)
      : super(_value, _then);

  /// Create a copy of DashboardOptionsConfig
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? successPresets = null,
    Object? errorPresets = null,
  }) {
    return _then(_$DashboardOptionsConfigImpl(
      successPresets: null == successPresets
          ? _value._successPresets
          : successPresets // ignore: cast_nullable_to_non_nullable
              as List<DashboardOverridePreset>,
      errorPresets: null == errorPresets
          ? _value._errorPresets
          : errorPresets // ignore: cast_nullable_to_non_nullable
              as List<DashboardOverridePreset>,
    ));
  }
}

/// @nodoc

class _$DashboardOptionsConfigImpl implements _DashboardOptionsConfig {
  const _$DashboardOptionsConfigImpl(
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

  @override
  String toString() {
    return 'DashboardOptionsConfig(successPresets: $successPresets, errorPresets: $errorPresets)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$DashboardOptionsConfigImpl &&
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

  /// Create a copy of DashboardOptionsConfig
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$DashboardOptionsConfigImplCopyWith<_$DashboardOptionsConfigImpl>
      get copyWith => __$$DashboardOptionsConfigImplCopyWithImpl<
          _$DashboardOptionsConfigImpl>(this, _$identity);
}

abstract class _DashboardOptionsConfig implements DashboardOptionsConfig {
  const factory _DashboardOptionsConfig(
          {final List<DashboardOverridePreset> successPresets,
          final List<DashboardOverridePreset> errorPresets}) =
      _$DashboardOptionsConfigImpl;

  @override
  List<DashboardOverridePreset> get successPresets;
  @override
  List<DashboardOverridePreset> get errorPresets;

  /// Create a copy of DashboardOptionsConfig
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$DashboardOptionsConfigImplCopyWith<_$DashboardOptionsConfigImpl>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
mixin _$EndpointConfig {
  String get name => throw _privateConstructorUsedError;
  String? get customKey => throw _privateConstructorUsedError;

  /// Whether the Mockzilla server should return an artificial error for a
  /// request to this endpoint.
  bool get shouldFail => throw _privateConstructorUsedError;

  /// Optional, the artificial delay that Mockzilla should apply to responses
  /// to simulate latency.
  Duration get delay => throw _privateConstructorUsedError;
  int get versionCode => throw _privateConstructorUsedError;

  /// Used to determine whether a particular `request` should be evaluated by
  /// this endpoint.
  bool Function(MockzillaHttpRequest) get endpointMatcher =>
      throw _privateConstructorUsedError;

  /// Optional, configures the preset responses for the endpoint in the
  /// Mockzilla dashboard.
  DashboardOptionsConfig get dashboardOptionsConfig =>
      throw _privateConstructorUsedError;

  /// This function is called when a network request is made to this endpoint,
  /// note that if an error is being returned due to `failureProbability`
  /// then `errorHandler` is used instead.
  MockzillaHttpResponse Function(MockzillaHttpRequest) get defaultHandler =>
      throw _privateConstructorUsedError;

  /// This function is called when, in response to a network request, the
  /// server returns an error due to`failureProbability`.
  MockzillaHttpResponse Function(MockzillaHttpRequest) get errorHandler =>
      throw _privateConstructorUsedError;

  /// Create a copy of EndpointConfig
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $EndpointConfigCopyWith<EndpointConfig> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $EndpointConfigCopyWith<$Res> {
  factory $EndpointConfigCopyWith(
          EndpointConfig value, $Res Function(EndpointConfig) then) =
      _$EndpointConfigCopyWithImpl<$Res, EndpointConfig>;
  @useResult
  $Res call(
      {String name,
      String? customKey,
      bool shouldFail,
      Duration delay,
      int versionCode,
      bool Function(MockzillaHttpRequest) endpointMatcher,
      DashboardOptionsConfig dashboardOptionsConfig,
      MockzillaHttpResponse Function(MockzillaHttpRequest) defaultHandler,
      MockzillaHttpResponse Function(MockzillaHttpRequest) errorHandler});

  $DashboardOptionsConfigCopyWith<$Res> get dashboardOptionsConfig;
}

/// @nodoc
class _$EndpointConfigCopyWithImpl<$Res, $Val extends EndpointConfig>
    implements $EndpointConfigCopyWith<$Res> {
  _$EndpointConfigCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

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
    return _then(_value.copyWith(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      customKey: freezed == customKey
          ? _value.customKey
          : customKey // ignore: cast_nullable_to_non_nullable
              as String?,
      shouldFail: null == shouldFail
          ? _value.shouldFail
          : shouldFail // ignore: cast_nullable_to_non_nullable
              as bool,
      delay: null == delay
          ? _value.delay
          : delay // ignore: cast_nullable_to_non_nullable
              as Duration,
      versionCode: null == versionCode
          ? _value.versionCode
          : versionCode // ignore: cast_nullable_to_non_nullable
              as int,
      endpointMatcher: null == endpointMatcher
          ? _value.endpointMatcher
          : endpointMatcher // ignore: cast_nullable_to_non_nullable
              as bool Function(MockzillaHttpRequest),
      dashboardOptionsConfig: null == dashboardOptionsConfig
          ? _value.dashboardOptionsConfig
          : dashboardOptionsConfig // ignore: cast_nullable_to_non_nullable
              as DashboardOptionsConfig,
      defaultHandler: null == defaultHandler
          ? _value.defaultHandler
          : defaultHandler // ignore: cast_nullable_to_non_nullable
              as MockzillaHttpResponse Function(MockzillaHttpRequest),
      errorHandler: null == errorHandler
          ? _value.errorHandler
          : errorHandler // ignore: cast_nullable_to_non_nullable
              as MockzillaHttpResponse Function(MockzillaHttpRequest),
    ) as $Val);
  }

  /// Create a copy of EndpointConfig
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $DashboardOptionsConfigCopyWith<$Res> get dashboardOptionsConfig {
    return $DashboardOptionsConfigCopyWith<$Res>(_value.dashboardOptionsConfig,
        (value) {
      return _then(_value.copyWith(dashboardOptionsConfig: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$EndpointConfigImplCopyWith<$Res>
    implements $EndpointConfigCopyWith<$Res> {
  factory _$$EndpointConfigImplCopyWith(_$EndpointConfigImpl value,
          $Res Function(_$EndpointConfigImpl) then) =
      __$$EndpointConfigImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {String name,
      String? customKey,
      bool shouldFail,
      Duration delay,
      int versionCode,
      bool Function(MockzillaHttpRequest) endpointMatcher,
      DashboardOptionsConfig dashboardOptionsConfig,
      MockzillaHttpResponse Function(MockzillaHttpRequest) defaultHandler,
      MockzillaHttpResponse Function(MockzillaHttpRequest) errorHandler});

  @override
  $DashboardOptionsConfigCopyWith<$Res> get dashboardOptionsConfig;
}

/// @nodoc
class __$$EndpointConfigImplCopyWithImpl<$Res>
    extends _$EndpointConfigCopyWithImpl<$Res, _$EndpointConfigImpl>
    implements _$$EndpointConfigImplCopyWith<$Res> {
  __$$EndpointConfigImplCopyWithImpl(
      _$EndpointConfigImpl _value, $Res Function(_$EndpointConfigImpl) _then)
      : super(_value, _then);

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
    return _then(_$EndpointConfigImpl(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      customKey: freezed == customKey
          ? _value.customKey
          : customKey // ignore: cast_nullable_to_non_nullable
              as String?,
      shouldFail: null == shouldFail
          ? _value.shouldFail
          : shouldFail // ignore: cast_nullable_to_non_nullable
              as bool,
      delay: null == delay
          ? _value.delay
          : delay // ignore: cast_nullable_to_non_nullable
              as Duration,
      versionCode: null == versionCode
          ? _value.versionCode
          : versionCode // ignore: cast_nullable_to_non_nullable
              as int,
      endpointMatcher: null == endpointMatcher
          ? _value.endpointMatcher
          : endpointMatcher // ignore: cast_nullable_to_non_nullable
              as bool Function(MockzillaHttpRequest),
      dashboardOptionsConfig: null == dashboardOptionsConfig
          ? _value.dashboardOptionsConfig
          : dashboardOptionsConfig // ignore: cast_nullable_to_non_nullable
              as DashboardOptionsConfig,
      defaultHandler: null == defaultHandler
          ? _value.defaultHandler
          : defaultHandler // ignore: cast_nullable_to_non_nullable
              as MockzillaHttpResponse Function(MockzillaHttpRequest),
      errorHandler: null == errorHandler
          ? _value.errorHandler
          : errorHandler // ignore: cast_nullable_to_non_nullable
              as MockzillaHttpResponse Function(MockzillaHttpRequest),
    ));
  }
}

/// @nodoc

class _$EndpointConfigImpl extends _EndpointConfig {
  const _$EndpointConfigImpl(
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
  @override
  final String? customKey;

  /// Whether the Mockzilla server should return an artificial error for a
  /// request to this endpoint.
  @override
  @JsonKey()
  final bool shouldFail;

  /// Optional, the artificial delay that Mockzilla should apply to responses
  /// to simulate latency.
  @override
  @JsonKey()
  final Duration delay;
  @override
  @JsonKey()
  final int versionCode;

  /// Used to determine whether a particular `request` should be evaluated by
  /// this endpoint.
  @override
  final bool Function(MockzillaHttpRequest) endpointMatcher;

  /// Optional, configures the preset responses for the endpoint in the
  /// Mockzilla dashboard.
  @override
  @JsonKey()
  final DashboardOptionsConfig dashboardOptionsConfig;

  /// This function is called when a network request is made to this endpoint,
  /// note that if an error is being returned due to `failureProbability`
  /// then `errorHandler` is used instead.
  @override
  final MockzillaHttpResponse Function(MockzillaHttpRequest) defaultHandler;

  /// This function is called when, in response to a network request, the
  /// server returns an error due to`failureProbability`.
  @override
  final MockzillaHttpResponse Function(MockzillaHttpRequest) errorHandler;

  @override
  String toString() {
    return 'EndpointConfig(name: $name, customKey: $customKey, shouldFail: $shouldFail, delay: $delay, versionCode: $versionCode, endpointMatcher: $endpointMatcher, dashboardOptionsConfig: $dashboardOptionsConfig, defaultHandler: $defaultHandler, errorHandler: $errorHandler)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$EndpointConfigImpl &&
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

  /// Create a copy of EndpointConfig
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$EndpointConfigImplCopyWith<_$EndpointConfigImpl> get copyWith =>
      __$$EndpointConfigImplCopyWithImpl<_$EndpointConfigImpl>(
          this, _$identity);
}

abstract class _EndpointConfig extends EndpointConfig {
  const factory _EndpointConfig(
      {required final String name,
      final String? customKey,
      final bool shouldFail,
      final Duration delay,
      final int versionCode,
      required final bool Function(MockzillaHttpRequest) endpointMatcher,
      final DashboardOptionsConfig dashboardOptionsConfig,
      required final MockzillaHttpResponse Function(MockzillaHttpRequest)
          defaultHandler,
      required final MockzillaHttpResponse Function(MockzillaHttpRequest)
          errorHandler}) = _$EndpointConfigImpl;
  const _EndpointConfig._() : super._();

  @override
  String get name;
  @override
  String? get customKey;

  /// Whether the Mockzilla server should return an artificial error for a
  /// request to this endpoint.
  @override
  bool get shouldFail;

  /// Optional, the artificial delay that Mockzilla should apply to responses
  /// to simulate latency.
  @override
  Duration get delay;
  @override
  int get versionCode;

  /// Used to determine whether a particular `request` should be evaluated by
  /// this endpoint.
  @override
  bool Function(MockzillaHttpRequest) get endpointMatcher;

  /// Optional, configures the preset responses for the endpoint in the
  /// Mockzilla dashboard.
  @override
  DashboardOptionsConfig get dashboardOptionsConfig;

  /// This function is called when a network request is made to this endpoint,
  /// note that if an error is being returned due to `failureProbability`
  /// then `errorHandler` is used instead.
  @override
  MockzillaHttpResponse Function(MockzillaHttpRequest) get defaultHandler;

  /// This function is called when, in response to a network request, the
  /// server returns an error due to`failureProbability`.
  @override
  MockzillaHttpResponse Function(MockzillaHttpRequest) get errorHandler;

  /// Create a copy of EndpointConfig
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$EndpointConfigImplCopyWith<_$EndpointConfigImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
mixin _$ReleaseModeConfig {
  int get rateLimit => throw _privateConstructorUsedError;
  Duration get rateLimitRefillPeriod => throw _privateConstructorUsedError;
  Duration get tokenLifeSpan => throw _privateConstructorUsedError;

  /// Create a copy of ReleaseModeConfig
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $ReleaseModeConfigCopyWith<ReleaseModeConfig> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $ReleaseModeConfigCopyWith<$Res> {
  factory $ReleaseModeConfigCopyWith(
          ReleaseModeConfig value, $Res Function(ReleaseModeConfig) then) =
      _$ReleaseModeConfigCopyWithImpl<$Res, ReleaseModeConfig>;
  @useResult
  $Res call(
      {int rateLimit, Duration rateLimitRefillPeriod, Duration tokenLifeSpan});
}

/// @nodoc
class _$ReleaseModeConfigCopyWithImpl<$Res, $Val extends ReleaseModeConfig>
    implements $ReleaseModeConfigCopyWith<$Res> {
  _$ReleaseModeConfigCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of ReleaseModeConfig
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? rateLimit = null,
    Object? rateLimitRefillPeriod = null,
    Object? tokenLifeSpan = null,
  }) {
    return _then(_value.copyWith(
      rateLimit: null == rateLimit
          ? _value.rateLimit
          : rateLimit // ignore: cast_nullable_to_non_nullable
              as int,
      rateLimitRefillPeriod: null == rateLimitRefillPeriod
          ? _value.rateLimitRefillPeriod
          : rateLimitRefillPeriod // ignore: cast_nullable_to_non_nullable
              as Duration,
      tokenLifeSpan: null == tokenLifeSpan
          ? _value.tokenLifeSpan
          : tokenLifeSpan // ignore: cast_nullable_to_non_nullable
              as Duration,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$ReleaseModeConfigImplCopyWith<$Res>
    implements $ReleaseModeConfigCopyWith<$Res> {
  factory _$$ReleaseModeConfigImplCopyWith(_$ReleaseModeConfigImpl value,
          $Res Function(_$ReleaseModeConfigImpl) then) =
      __$$ReleaseModeConfigImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {int rateLimit, Duration rateLimitRefillPeriod, Duration tokenLifeSpan});
}

/// @nodoc
class __$$ReleaseModeConfigImplCopyWithImpl<$Res>
    extends _$ReleaseModeConfigCopyWithImpl<$Res, _$ReleaseModeConfigImpl>
    implements _$$ReleaseModeConfigImplCopyWith<$Res> {
  __$$ReleaseModeConfigImplCopyWithImpl(_$ReleaseModeConfigImpl _value,
      $Res Function(_$ReleaseModeConfigImpl) _then)
      : super(_value, _then);

  /// Create a copy of ReleaseModeConfig
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? rateLimit = null,
    Object? rateLimitRefillPeriod = null,
    Object? tokenLifeSpan = null,
  }) {
    return _then(_$ReleaseModeConfigImpl(
      rateLimit: null == rateLimit
          ? _value.rateLimit
          : rateLimit // ignore: cast_nullable_to_non_nullable
              as int,
      rateLimitRefillPeriod: null == rateLimitRefillPeriod
          ? _value.rateLimitRefillPeriod
          : rateLimitRefillPeriod // ignore: cast_nullable_to_non_nullable
              as Duration,
      tokenLifeSpan: null == tokenLifeSpan
          ? _value.tokenLifeSpan
          : tokenLifeSpan // ignore: cast_nullable_to_non_nullable
              as Duration,
    ));
  }
}

/// @nodoc

class _$ReleaseModeConfigImpl implements _ReleaseModeConfig {
  const _$ReleaseModeConfigImpl(
      {this.rateLimit = 60,
      this.rateLimitRefillPeriod = const Duration(seconds: 60),
      this.tokenLifeSpan = const Duration(milliseconds: 500)});

  @override
  @JsonKey()
  final int rateLimit;
  @override
  @JsonKey()
  final Duration rateLimitRefillPeriod;
  @override
  @JsonKey()
  final Duration tokenLifeSpan;

  @override
  String toString() {
    return 'ReleaseModeConfig(rateLimit: $rateLimit, rateLimitRefillPeriod: $rateLimitRefillPeriod, tokenLifeSpan: $tokenLifeSpan)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$ReleaseModeConfigImpl &&
            (identical(other.rateLimit, rateLimit) ||
                other.rateLimit == rateLimit) &&
            (identical(other.rateLimitRefillPeriod, rateLimitRefillPeriod) ||
                other.rateLimitRefillPeriod == rateLimitRefillPeriod) &&
            (identical(other.tokenLifeSpan, tokenLifeSpan) ||
                other.tokenLifeSpan == tokenLifeSpan));
  }

  @override
  int get hashCode =>
      Object.hash(runtimeType, rateLimit, rateLimitRefillPeriod, tokenLifeSpan);

  /// Create a copy of ReleaseModeConfig
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$ReleaseModeConfigImplCopyWith<_$ReleaseModeConfigImpl> get copyWith =>
      __$$ReleaseModeConfigImplCopyWithImpl<_$ReleaseModeConfigImpl>(
          this, _$identity);
}

abstract class _ReleaseModeConfig implements ReleaseModeConfig {
  const factory _ReleaseModeConfig(
      {final int rateLimit,
      final Duration rateLimitRefillPeriod,
      final Duration tokenLifeSpan}) = _$ReleaseModeConfigImpl;

  @override
  int get rateLimit;
  @override
  Duration get rateLimitRefillPeriod;
  @override
  Duration get tokenLifeSpan;

  /// Create a copy of ReleaseModeConfig
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$ReleaseModeConfigImplCopyWith<_$ReleaseModeConfigImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
mixin _$MockzillaConfig {
  /// The port that the Mockzilla should be available through.
  int get port => throw _privateConstructorUsedError;

  /// The list of available mocked endpoints.
  List<EndpointConfig> get endpoints => throw _privateConstructorUsedError;

  /// Can be used to add rudimentary restrictions to the Mockzilla server
  /// such as rate limiting. See [https://apadmi-engineering.github.io/Mockzilla/additional_config/#release-mode]()
  /// for more information.
  bool get isRelease => throw _privateConstructorUsedError;

  /// Whether Mockzilla server should only be available on the host device.
  bool get localHostOnly => throw _privateConstructorUsedError;

  /// The level of logging that should be used by Mockzilla.
  LogLevel get logLevel => throw _privateConstructorUsedError;

  /// Used for additional configuration when [isRelease] is [true].
  ReleaseModeConfig get releaseModeConfig => throw _privateConstructorUsedError;

  /// Whether devices running Mockzilla are discoverable through the desktop
  /// management UI.
  bool get isNetworkDiscoveryEnabled => throw _privateConstructorUsedError;

  /// Create a copy of MockzillaConfig
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $MockzillaConfigCopyWith<MockzillaConfig> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $MockzillaConfigCopyWith<$Res> {
  factory $MockzillaConfigCopyWith(
          MockzillaConfig value, $Res Function(MockzillaConfig) then) =
      _$MockzillaConfigCopyWithImpl<$Res, MockzillaConfig>;
  @useResult
  $Res call(
      {int port,
      List<EndpointConfig> endpoints,
      bool isRelease,
      bool localHostOnly,
      LogLevel logLevel,
      ReleaseModeConfig releaseModeConfig,
      bool isNetworkDiscoveryEnabled});

  $ReleaseModeConfigCopyWith<$Res> get releaseModeConfig;
}

/// @nodoc
class _$MockzillaConfigCopyWithImpl<$Res, $Val extends MockzillaConfig>
    implements $MockzillaConfigCopyWith<$Res> {
  _$MockzillaConfigCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of MockzillaConfig
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? port = null,
    Object? endpoints = null,
    Object? isRelease = null,
    Object? localHostOnly = null,
    Object? logLevel = null,
    Object? releaseModeConfig = null,
    Object? isNetworkDiscoveryEnabled = null,
  }) {
    return _then(_value.copyWith(
      port: null == port
          ? _value.port
          : port // ignore: cast_nullable_to_non_nullable
              as int,
      endpoints: null == endpoints
          ? _value.endpoints
          : endpoints // ignore: cast_nullable_to_non_nullable
              as List<EndpointConfig>,
      isRelease: null == isRelease
          ? _value.isRelease
          : isRelease // ignore: cast_nullable_to_non_nullable
              as bool,
      localHostOnly: null == localHostOnly
          ? _value.localHostOnly
          : localHostOnly // ignore: cast_nullable_to_non_nullable
              as bool,
      logLevel: null == logLevel
          ? _value.logLevel
          : logLevel // ignore: cast_nullable_to_non_nullable
              as LogLevel,
      releaseModeConfig: null == releaseModeConfig
          ? _value.releaseModeConfig
          : releaseModeConfig // ignore: cast_nullable_to_non_nullable
              as ReleaseModeConfig,
      isNetworkDiscoveryEnabled: null == isNetworkDiscoveryEnabled
          ? _value.isNetworkDiscoveryEnabled
          : isNetworkDiscoveryEnabled // ignore: cast_nullable_to_non_nullable
              as bool,
    ) as $Val);
  }

  /// Create a copy of MockzillaConfig
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $ReleaseModeConfigCopyWith<$Res> get releaseModeConfig {
    return $ReleaseModeConfigCopyWith<$Res>(_value.releaseModeConfig, (value) {
      return _then(_value.copyWith(releaseModeConfig: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$MockzillaConfigImplCopyWith<$Res>
    implements $MockzillaConfigCopyWith<$Res> {
  factory _$$MockzillaConfigImplCopyWith(_$MockzillaConfigImpl value,
          $Res Function(_$MockzillaConfigImpl) then) =
      __$$MockzillaConfigImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {int port,
      List<EndpointConfig> endpoints,
      bool isRelease,
      bool localHostOnly,
      LogLevel logLevel,
      ReleaseModeConfig releaseModeConfig,
      bool isNetworkDiscoveryEnabled});

  @override
  $ReleaseModeConfigCopyWith<$Res> get releaseModeConfig;
}

/// @nodoc
class __$$MockzillaConfigImplCopyWithImpl<$Res>
    extends _$MockzillaConfigCopyWithImpl<$Res, _$MockzillaConfigImpl>
    implements _$$MockzillaConfigImplCopyWith<$Res> {
  __$$MockzillaConfigImplCopyWithImpl(
      _$MockzillaConfigImpl _value, $Res Function(_$MockzillaConfigImpl) _then)
      : super(_value, _then);

  /// Create a copy of MockzillaConfig
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? port = null,
    Object? endpoints = null,
    Object? isRelease = null,
    Object? localHostOnly = null,
    Object? logLevel = null,
    Object? releaseModeConfig = null,
    Object? isNetworkDiscoveryEnabled = null,
  }) {
    return _then(_$MockzillaConfigImpl(
      port: null == port
          ? _value.port
          : port // ignore: cast_nullable_to_non_nullable
              as int,
      endpoints: null == endpoints
          ? _value._endpoints
          : endpoints // ignore: cast_nullable_to_non_nullable
              as List<EndpointConfig>,
      isRelease: null == isRelease
          ? _value.isRelease
          : isRelease // ignore: cast_nullable_to_non_nullable
              as bool,
      localHostOnly: null == localHostOnly
          ? _value.localHostOnly
          : localHostOnly // ignore: cast_nullable_to_non_nullable
              as bool,
      logLevel: null == logLevel
          ? _value.logLevel
          : logLevel // ignore: cast_nullable_to_non_nullable
              as LogLevel,
      releaseModeConfig: null == releaseModeConfig
          ? _value.releaseModeConfig
          : releaseModeConfig // ignore: cast_nullable_to_non_nullable
              as ReleaseModeConfig,
      isNetworkDiscoveryEnabled: null == isNetworkDiscoveryEnabled
          ? _value.isNetworkDiscoveryEnabled
          : isNetworkDiscoveryEnabled // ignore: cast_nullable_to_non_nullable
              as bool,
    ));
  }
}

/// @nodoc

class _$MockzillaConfigImpl implements _MockzillaConfig {
  const _$MockzillaConfigImpl(
      {this.port = 8080,
      final List<EndpointConfig> endpoints = const [],
      this.isRelease = false,
      this.localHostOnly = false,
      this.logLevel = LogLevel.info,
      this.releaseModeConfig = const ReleaseModeConfig(),
      this.isNetworkDiscoveryEnabled = true})
      : _endpoints = endpoints;

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

  /// Can be used to add rudimentary restrictions to the Mockzilla server
  /// such as rate limiting. See [https://apadmi-engineering.github.io/Mockzilla/additional_config/#release-mode]()
  /// for more information.
  @override
  @JsonKey()
  final bool isRelease;

  /// Whether Mockzilla server should only be available on the host device.
  @override
  @JsonKey()
  final bool localHostOnly;

  /// The level of logging that should be used by Mockzilla.
  @override
  @JsonKey()
  final LogLevel logLevel;

  /// Used for additional configuration when [isRelease] is [true].
  @override
  @JsonKey()
  final ReleaseModeConfig releaseModeConfig;

  /// Whether devices running Mockzilla are discoverable through the desktop
  /// management UI.
  @override
  @JsonKey()
  final bool isNetworkDiscoveryEnabled;

  @override
  String toString() {
    return 'MockzillaConfig(port: $port, endpoints: $endpoints, isRelease: $isRelease, localHostOnly: $localHostOnly, logLevel: $logLevel, releaseModeConfig: $releaseModeConfig, isNetworkDiscoveryEnabled: $isNetworkDiscoveryEnabled)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$MockzillaConfigImpl &&
            (identical(other.port, port) || other.port == port) &&
            const DeepCollectionEquality()
                .equals(other._endpoints, _endpoints) &&
            (identical(other.isRelease, isRelease) ||
                other.isRelease == isRelease) &&
            (identical(other.localHostOnly, localHostOnly) ||
                other.localHostOnly == localHostOnly) &&
            (identical(other.logLevel, logLevel) ||
                other.logLevel == logLevel) &&
            (identical(other.releaseModeConfig, releaseModeConfig) ||
                other.releaseModeConfig == releaseModeConfig) &&
            (identical(other.isNetworkDiscoveryEnabled,
                    isNetworkDiscoveryEnabled) ||
                other.isNetworkDiscoveryEnabled == isNetworkDiscoveryEnabled));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType,
      port,
      const DeepCollectionEquality().hash(_endpoints),
      isRelease,
      localHostOnly,
      logLevel,
      releaseModeConfig,
      isNetworkDiscoveryEnabled);

  /// Create a copy of MockzillaConfig
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$MockzillaConfigImplCopyWith<_$MockzillaConfigImpl> get copyWith =>
      __$$MockzillaConfigImplCopyWithImpl<_$MockzillaConfigImpl>(
          this, _$identity);
}

abstract class _MockzillaConfig implements MockzillaConfig {
  const factory _MockzillaConfig(
      {final int port,
      final List<EndpointConfig> endpoints,
      final bool isRelease,
      final bool localHostOnly,
      final LogLevel logLevel,
      final ReleaseModeConfig releaseModeConfig,
      final bool isNetworkDiscoveryEnabled}) = _$MockzillaConfigImpl;

  /// The port that the Mockzilla should be available through.
  @override
  int get port;

  /// The list of available mocked endpoints.
  @override
  List<EndpointConfig> get endpoints;

  /// Can be used to add rudimentary restrictions to the Mockzilla server
  /// such as rate limiting. See [https://apadmi-engineering.github.io/Mockzilla/additional_config/#release-mode]()
  /// for more information.
  @override
  bool get isRelease;

  /// Whether Mockzilla server should only be available on the host device.
  @override
  bool get localHostOnly;

  /// The level of logging that should be used by Mockzilla.
  @override
  LogLevel get logLevel;

  /// Used for additional configuration when [isRelease] is [true].
  @override
  ReleaseModeConfig get releaseModeConfig;

  /// Whether devices running Mockzilla are discoverable through the desktop
  /// management UI.
  @override
  bool get isNetworkDiscoveryEnabled;

  /// Create a copy of MockzillaConfig
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$MockzillaConfigImplCopyWith<_$MockzillaConfigImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
mixin _$MockzillaRuntimeParams {
  MockzillaConfig get config => throw _privateConstructorUsedError;
  String get mockBaseUrl => throw _privateConstructorUsedError;
  String get apiBaseUrl => throw _privateConstructorUsedError;
  int get port => throw _privateConstructorUsedError;
  AuthHeaderProvider get authHeaderProvider =>
      throw _privateConstructorUsedError;

  /// Create a copy of MockzillaRuntimeParams
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $MockzillaRuntimeParamsCopyWith<MockzillaRuntimeParams> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $MockzillaRuntimeParamsCopyWith<$Res> {
  factory $MockzillaRuntimeParamsCopyWith(MockzillaRuntimeParams value,
          $Res Function(MockzillaRuntimeParams) then) =
      _$MockzillaRuntimeParamsCopyWithImpl<$Res, MockzillaRuntimeParams>;
  @useResult
  $Res call(
      {MockzillaConfig config,
      String mockBaseUrl,
      String apiBaseUrl,
      int port,
      AuthHeaderProvider authHeaderProvider});

  $MockzillaConfigCopyWith<$Res> get config;
}

/// @nodoc
class _$MockzillaRuntimeParamsCopyWithImpl<$Res,
        $Val extends MockzillaRuntimeParams>
    implements $MockzillaRuntimeParamsCopyWith<$Res> {
  _$MockzillaRuntimeParamsCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of MockzillaRuntimeParams
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? config = null,
    Object? mockBaseUrl = null,
    Object? apiBaseUrl = null,
    Object? port = null,
    Object? authHeaderProvider = null,
  }) {
    return _then(_value.copyWith(
      config: null == config
          ? _value.config
          : config // ignore: cast_nullable_to_non_nullable
              as MockzillaConfig,
      mockBaseUrl: null == mockBaseUrl
          ? _value.mockBaseUrl
          : mockBaseUrl // ignore: cast_nullable_to_non_nullable
              as String,
      apiBaseUrl: null == apiBaseUrl
          ? _value.apiBaseUrl
          : apiBaseUrl // ignore: cast_nullable_to_non_nullable
              as String,
      port: null == port
          ? _value.port
          : port // ignore: cast_nullable_to_non_nullable
              as int,
      authHeaderProvider: null == authHeaderProvider
          ? _value.authHeaderProvider
          : authHeaderProvider // ignore: cast_nullable_to_non_nullable
              as AuthHeaderProvider,
    ) as $Val);
  }

  /// Create a copy of MockzillaRuntimeParams
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $MockzillaConfigCopyWith<$Res> get config {
    return $MockzillaConfigCopyWith<$Res>(_value.config, (value) {
      return _then(_value.copyWith(config: value) as $Val);
    });
  }
}

/// @nodoc
abstract class _$$MockzillaRuntimeParamsImplCopyWith<$Res>
    implements $MockzillaRuntimeParamsCopyWith<$Res> {
  factory _$$MockzillaRuntimeParamsImplCopyWith(
          _$MockzillaRuntimeParamsImpl value,
          $Res Function(_$MockzillaRuntimeParamsImpl) then) =
      __$$MockzillaRuntimeParamsImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call(
      {MockzillaConfig config,
      String mockBaseUrl,
      String apiBaseUrl,
      int port,
      AuthHeaderProvider authHeaderProvider});

  @override
  $MockzillaConfigCopyWith<$Res> get config;
}

/// @nodoc
class __$$MockzillaRuntimeParamsImplCopyWithImpl<$Res>
    extends _$MockzillaRuntimeParamsCopyWithImpl<$Res,
        _$MockzillaRuntimeParamsImpl>
    implements _$$MockzillaRuntimeParamsImplCopyWith<$Res> {
  __$$MockzillaRuntimeParamsImplCopyWithImpl(
      _$MockzillaRuntimeParamsImpl _value,
      $Res Function(_$MockzillaRuntimeParamsImpl) _then)
      : super(_value, _then);

  /// Create a copy of MockzillaRuntimeParams
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? config = null,
    Object? mockBaseUrl = null,
    Object? apiBaseUrl = null,
    Object? port = null,
    Object? authHeaderProvider = null,
  }) {
    return _then(_$MockzillaRuntimeParamsImpl(
      config: null == config
          ? _value.config
          : config // ignore: cast_nullable_to_non_nullable
              as MockzillaConfig,
      mockBaseUrl: null == mockBaseUrl
          ? _value.mockBaseUrl
          : mockBaseUrl // ignore: cast_nullable_to_non_nullable
              as String,
      apiBaseUrl: null == apiBaseUrl
          ? _value.apiBaseUrl
          : apiBaseUrl // ignore: cast_nullable_to_non_nullable
              as String,
      port: null == port
          ? _value.port
          : port // ignore: cast_nullable_to_non_nullable
              as int,
      authHeaderProvider: null == authHeaderProvider
          ? _value.authHeaderProvider
          : authHeaderProvider // ignore: cast_nullable_to_non_nullable
              as AuthHeaderProvider,
    ));
  }
}

/// @nodoc

class _$MockzillaRuntimeParamsImpl implements _MockzillaRuntimeParams {
  const _$MockzillaRuntimeParamsImpl(
      {required this.config,
      required this.mockBaseUrl,
      required this.apiBaseUrl,
      required this.port,
      required this.authHeaderProvider});

  @override
  final MockzillaConfig config;
  @override
  final String mockBaseUrl;
  @override
  final String apiBaseUrl;
  @override
  final int port;
  @override
  final AuthHeaderProvider authHeaderProvider;

  @override
  String toString() {
    return 'MockzillaRuntimeParams(config: $config, mockBaseUrl: $mockBaseUrl, apiBaseUrl: $apiBaseUrl, port: $port, authHeaderProvider: $authHeaderProvider)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$MockzillaRuntimeParamsImpl &&
            (identical(other.config, config) || other.config == config) &&
            (identical(other.mockBaseUrl, mockBaseUrl) ||
                other.mockBaseUrl == mockBaseUrl) &&
            (identical(other.apiBaseUrl, apiBaseUrl) ||
                other.apiBaseUrl == apiBaseUrl) &&
            (identical(other.port, port) || other.port == port) &&
            (identical(other.authHeaderProvider, authHeaderProvider) ||
                other.authHeaderProvider == authHeaderProvider));
  }

  @override
  int get hashCode => Object.hash(
      runtimeType, config, mockBaseUrl, apiBaseUrl, port, authHeaderProvider);

  /// Create a copy of MockzillaRuntimeParams
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$MockzillaRuntimeParamsImplCopyWith<_$MockzillaRuntimeParamsImpl>
      get copyWith => __$$MockzillaRuntimeParamsImplCopyWithImpl<
          _$MockzillaRuntimeParamsImpl>(this, _$identity);
}

abstract class _MockzillaRuntimeParams implements MockzillaRuntimeParams {
  const factory _MockzillaRuntimeParams(
          {required final MockzillaConfig config,
          required final String mockBaseUrl,
          required final String apiBaseUrl,
          required final int port,
          required final AuthHeaderProvider authHeaderProvider}) =
      _$MockzillaRuntimeParamsImpl;

  @override
  MockzillaConfig get config;
  @override
  String get mockBaseUrl;
  @override
  String get apiBaseUrl;
  @override
  int get port;
  @override
  AuthHeaderProvider get authHeaderProvider;

  /// Create a copy of MockzillaRuntimeParams
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$MockzillaRuntimeParamsImplCopyWith<_$MockzillaRuntimeParamsImpl>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
mixin _$AuthHeader {
  String get key => throw _privateConstructorUsedError;
  String get value => throw _privateConstructorUsedError;

  /// Create a copy of AuthHeader
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $AuthHeaderCopyWith<AuthHeader> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $AuthHeaderCopyWith<$Res> {
  factory $AuthHeaderCopyWith(
          AuthHeader value, $Res Function(AuthHeader) then) =
      _$AuthHeaderCopyWithImpl<$Res, AuthHeader>;
  @useResult
  $Res call({String key, String value});
}

/// @nodoc
class _$AuthHeaderCopyWithImpl<$Res, $Val extends AuthHeader>
    implements $AuthHeaderCopyWith<$Res> {
  _$AuthHeaderCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of AuthHeader
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? key = null,
    Object? value = null,
  }) {
    return _then(_value.copyWith(
      key: null == key
          ? _value.key
          : key // ignore: cast_nullable_to_non_nullable
              as String,
      value: null == value
          ? _value.value
          : value // ignore: cast_nullable_to_non_nullable
              as String,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$AuthHeaderImplCopyWith<$Res>
    implements $AuthHeaderCopyWith<$Res> {
  factory _$$AuthHeaderImplCopyWith(
          _$AuthHeaderImpl value, $Res Function(_$AuthHeaderImpl) then) =
      __$$AuthHeaderImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({String key, String value});
}

/// @nodoc
class __$$AuthHeaderImplCopyWithImpl<$Res>
    extends _$AuthHeaderCopyWithImpl<$Res, _$AuthHeaderImpl>
    implements _$$AuthHeaderImplCopyWith<$Res> {
  __$$AuthHeaderImplCopyWithImpl(
      _$AuthHeaderImpl _value, $Res Function(_$AuthHeaderImpl) _then)
      : super(_value, _then);

  /// Create a copy of AuthHeader
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? key = null,
    Object? value = null,
  }) {
    return _then(_$AuthHeaderImpl(
      key: null == key
          ? _value.key
          : key // ignore: cast_nullable_to_non_nullable
              as String,
      value: null == value
          ? _value.value
          : value // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc

class _$AuthHeaderImpl implements _AuthHeader {
  const _$AuthHeaderImpl({required this.key, required this.value});

  @override
  final String key;
  @override
  final String value;

  @override
  String toString() {
    return 'AuthHeader(key: $key, value: $value)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$AuthHeaderImpl &&
            (identical(other.key, key) || other.key == key) &&
            (identical(other.value, value) || other.value == value));
  }

  @override
  int get hashCode => Object.hash(runtimeType, key, value);

  /// Create a copy of AuthHeader
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$AuthHeaderImplCopyWith<_$AuthHeaderImpl> get copyWith =>
      __$$AuthHeaderImplCopyWithImpl<_$AuthHeaderImpl>(this, _$identity);
}

abstract class _AuthHeader implements AuthHeader {
  const factory _AuthHeader(
      {required final String key,
      required final String value}) = _$AuthHeaderImpl;

  @override
  String get key;
  @override
  String get value;

  /// Create a copy of AuthHeader
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$AuthHeaderImplCopyWith<_$AuthHeaderImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
