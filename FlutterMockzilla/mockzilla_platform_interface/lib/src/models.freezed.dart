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

  @JsonKey(ignore: true)
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
      required final Map<String, String> headers,
      this.body = "",
      required this.method})
      : _headers = headers;

  @override
  final String uri;
  final Map<String, String> _headers;
  @override
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

  @JsonKey(ignore: true)
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
      required final Map<String, String> headers,
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
  @override
  @JsonKey(ignore: true)
  _$$MockzillaHttpRequestImplCopyWith<_$MockzillaHttpRequestImpl>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
mixin _$MockzillaHttpResponse {
  int get statusCode => throw _privateConstructorUsedError;
  Map<String, String> get headers => throw _privateConstructorUsedError;
  String get body => throw _privateConstructorUsedError;

  @JsonKey(ignore: true)
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
      final Map<String, String> headers = const {},
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

  @JsonKey(ignore: true)
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
  @override
  @JsonKey(ignore: true)
  _$$MockzillaHttpResponseImplCopyWith<_$MockzillaHttpResponseImpl>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
mixin _$EndpointConfig {
  String get name => throw _privateConstructorUsedError;
  String? get customKey => throw _privateConstructorUsedError;

  /// Probability as a percentage that the Mockzilla server should return an
  /// error for any single request to this endpoint.
  int get failureProbability => throw _privateConstructorUsedError;

  /// Optional, the artificial delay in milliseconds that Mockzilla should use to
  /// simulate latency.
  int get delayMean => throw _privateConstructorUsedError;

  /// Optional, the variance in milliseconds of the artificial delay applied
  /// by Mockzilla to a response to simulate latency. If not provided, then a
  /// default of 0ms is used to eliminate randomness.
  int get delayVariance => throw _privateConstructorUsedError;

  /// Used to determine whether a particular `request` should be evaluated by
  /// this endpoint.
  bool Function(MockzillaHttpRequest) get endpointMatcher =>
      throw _privateConstructorUsedError;
  MockzillaHttpResponse? get webApiDefaultResponse =>
      throw _privateConstructorUsedError;
  MockzillaHttpResponse? get webApiErrorResponse =>
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

  @JsonKey(ignore: true)
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
      int failureProbability,
      int delayMean,
      int delayVariance,
      bool Function(MockzillaHttpRequest) endpointMatcher,
      MockzillaHttpResponse? webApiDefaultResponse,
      MockzillaHttpResponse? webApiErrorResponse,
      MockzillaHttpResponse Function(MockzillaHttpRequest) defaultHandler,
      MockzillaHttpResponse Function(MockzillaHttpRequest) errorHandler});

  $MockzillaHttpResponseCopyWith<$Res>? get webApiDefaultResponse;
  $MockzillaHttpResponseCopyWith<$Res>? get webApiErrorResponse;
}

/// @nodoc
class _$EndpointConfigCopyWithImpl<$Res, $Val extends EndpointConfig>
    implements $EndpointConfigCopyWith<$Res> {
  _$EndpointConfigCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? customKey = freezed,
    Object? failureProbability = null,
    Object? delayMean = null,
    Object? delayVariance = null,
    Object? endpointMatcher = null,
    Object? webApiDefaultResponse = freezed,
    Object? webApiErrorResponse = freezed,
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
      failureProbability: null == failureProbability
          ? _value.failureProbability
          : failureProbability // ignore: cast_nullable_to_non_nullable
              as int,
      delayMean: null == delayMean
          ? _value.delayMean
          : delayMean // ignore: cast_nullable_to_non_nullable
              as int,
      delayVariance: null == delayVariance
          ? _value.delayVariance
          : delayVariance // ignore: cast_nullable_to_non_nullable
              as int,
      endpointMatcher: null == endpointMatcher
          ? _value.endpointMatcher
          : endpointMatcher // ignore: cast_nullable_to_non_nullable
              as bool Function(MockzillaHttpRequest),
      webApiDefaultResponse: freezed == webApiDefaultResponse
          ? _value.webApiDefaultResponse
          : webApiDefaultResponse // ignore: cast_nullable_to_non_nullable
              as MockzillaHttpResponse?,
      webApiErrorResponse: freezed == webApiErrorResponse
          ? _value.webApiErrorResponse
          : webApiErrorResponse // ignore: cast_nullable_to_non_nullable
              as MockzillaHttpResponse?,
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

  @override
  @pragma('vm:prefer-inline')
  $MockzillaHttpResponseCopyWith<$Res>? get webApiDefaultResponse {
    if (_value.webApiDefaultResponse == null) {
      return null;
    }

    return $MockzillaHttpResponseCopyWith<$Res>(_value.webApiDefaultResponse!,
        (value) {
      return _then(_value.copyWith(webApiDefaultResponse: value) as $Val);
    });
  }

  @override
  @pragma('vm:prefer-inline')
  $MockzillaHttpResponseCopyWith<$Res>? get webApiErrorResponse {
    if (_value.webApiErrorResponse == null) {
      return null;
    }

    return $MockzillaHttpResponseCopyWith<$Res>(_value.webApiErrorResponse!,
        (value) {
      return _then(_value.copyWith(webApiErrorResponse: value) as $Val);
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
      int failureProbability,
      int delayMean,
      int delayVariance,
      bool Function(MockzillaHttpRequest) endpointMatcher,
      MockzillaHttpResponse? webApiDefaultResponse,
      MockzillaHttpResponse? webApiErrorResponse,
      MockzillaHttpResponse Function(MockzillaHttpRequest) defaultHandler,
      MockzillaHttpResponse Function(MockzillaHttpRequest) errorHandler});

  @override
  $MockzillaHttpResponseCopyWith<$Res>? get webApiDefaultResponse;
  @override
  $MockzillaHttpResponseCopyWith<$Res>? get webApiErrorResponse;
}

/// @nodoc
class __$$EndpointConfigImplCopyWithImpl<$Res>
    extends _$EndpointConfigCopyWithImpl<$Res, _$EndpointConfigImpl>
    implements _$$EndpointConfigImplCopyWith<$Res> {
  __$$EndpointConfigImplCopyWithImpl(
      _$EndpointConfigImpl _value, $Res Function(_$EndpointConfigImpl) _then)
      : super(_value, _then);

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? customKey = freezed,
    Object? failureProbability = null,
    Object? delayMean = null,
    Object? delayVariance = null,
    Object? endpointMatcher = null,
    Object? webApiDefaultResponse = freezed,
    Object? webApiErrorResponse = freezed,
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
      failureProbability: null == failureProbability
          ? _value.failureProbability
          : failureProbability // ignore: cast_nullable_to_non_nullable
              as int,
      delayMean: null == delayMean
          ? _value.delayMean
          : delayMean // ignore: cast_nullable_to_non_nullable
              as int,
      delayVariance: null == delayVariance
          ? _value.delayVariance
          : delayVariance // ignore: cast_nullable_to_non_nullable
              as int,
      endpointMatcher: null == endpointMatcher
          ? _value.endpointMatcher
          : endpointMatcher // ignore: cast_nullable_to_non_nullable
              as bool Function(MockzillaHttpRequest),
      webApiDefaultResponse: freezed == webApiDefaultResponse
          ? _value.webApiDefaultResponse
          : webApiDefaultResponse // ignore: cast_nullable_to_non_nullable
              as MockzillaHttpResponse?,
      webApiErrorResponse: freezed == webApiErrorResponse
          ? _value.webApiErrorResponse
          : webApiErrorResponse // ignore: cast_nullable_to_non_nullable
              as MockzillaHttpResponse?,
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
      this.failureProbability = 0,
      this.delayMean = 100,
      this.delayVariance = 20,
      required this.endpointMatcher,
      this.webApiDefaultResponse,
      this.webApiErrorResponse,
      required this.defaultHandler,
      required this.errorHandler})
      : super._();

  @override
  final String name;
  @override
  final String? customKey;

  /// Probability as a percentage that the Mockzilla server should return an
  /// error for any single request to this endpoint.
  @override
  @JsonKey()
  final int failureProbability;

  /// Optional, the artificial delay in milliseconds that Mockzilla should use to
  /// simulate latency.
  @override
  @JsonKey()
  final int delayMean;

  /// Optional, the variance in milliseconds of the artificial delay applied
  /// by Mockzilla to a response to simulate latency. If not provided, then a
  /// default of 0ms is used to eliminate randomness.
  @override
  @JsonKey()
  final int delayVariance;

  /// Used to determine whether a particular `request` should be evaluated by
  /// this endpoint.
  @override
  final bool Function(MockzillaHttpRequest) endpointMatcher;
  @override
  final MockzillaHttpResponse? webApiDefaultResponse;
  @override
  final MockzillaHttpResponse? webApiErrorResponse;

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
    return 'EndpointConfig(name: $name, customKey: $customKey, failureProbability: $failureProbability, delayMean: $delayMean, delayVariance: $delayVariance, endpointMatcher: $endpointMatcher, webApiDefaultResponse: $webApiDefaultResponse, webApiErrorResponse: $webApiErrorResponse, defaultHandler: $defaultHandler, errorHandler: $errorHandler)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$EndpointConfigImpl &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.customKey, customKey) ||
                other.customKey == customKey) &&
            (identical(other.failureProbability, failureProbability) ||
                other.failureProbability == failureProbability) &&
            (identical(other.delayMean, delayMean) ||
                other.delayMean == delayMean) &&
            (identical(other.delayVariance, delayVariance) ||
                other.delayVariance == delayVariance) &&
            (identical(other.endpointMatcher, endpointMatcher) ||
                other.endpointMatcher == endpointMatcher) &&
            (identical(other.webApiDefaultResponse, webApiDefaultResponse) ||
                other.webApiDefaultResponse == webApiDefaultResponse) &&
            (identical(other.webApiErrorResponse, webApiErrorResponse) ||
                other.webApiErrorResponse == webApiErrorResponse) &&
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
      failureProbability,
      delayMean,
      delayVariance,
      endpointMatcher,
      webApiDefaultResponse,
      webApiErrorResponse,
      defaultHandler,
      errorHandler);

  @JsonKey(ignore: true)
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
      final int failureProbability,
      final int delayMean,
      final int delayVariance,
      required final bool Function(MockzillaHttpRequest) endpointMatcher,
      final MockzillaHttpResponse? webApiDefaultResponse,
      final MockzillaHttpResponse? webApiErrorResponse,
      required final MockzillaHttpResponse Function(MockzillaHttpRequest)
          defaultHandler,
      required final MockzillaHttpResponse Function(MockzillaHttpRequest)
          errorHandler}) = _$EndpointConfigImpl;
  const _EndpointConfig._() : super._();

  @override
  String get name;
  @override
  String? get customKey;
  @override

  /// Probability as a percentage that the Mockzilla server should return an
  /// error for any single request to this endpoint.
  int get failureProbability;
  @override

  /// Optional, the artificial delay in milliseconds that Mockzilla should use to
  /// simulate latency.
  int get delayMean;
  @override

  /// Optional, the variance in milliseconds of the artificial delay applied
  /// by Mockzilla to a response to simulate latency. If not provided, then a
  /// default of 0ms is used to eliminate randomness.
  int get delayVariance;
  @override

  /// Used to determine whether a particular `request` should be evaluated by
  /// this endpoint.
  bool Function(MockzillaHttpRequest) get endpointMatcher;
  @override
  MockzillaHttpResponse? get webApiDefaultResponse;
  @override
  MockzillaHttpResponse? get webApiErrorResponse;
  @override

  /// This function is called when a network request is made to this endpoint,
  /// note that if an error is being returned due to `failureProbability`
  /// then `errorHandler` is used instead.
  MockzillaHttpResponse Function(MockzillaHttpRequest) get defaultHandler;
  @override

  /// This function is called when, in response to a network request, the
  /// server returns an error due to`failureProbability`.
  MockzillaHttpResponse Function(MockzillaHttpRequest) get errorHandler;
  @override
  @JsonKey(ignore: true)
  _$$EndpointConfigImplCopyWith<_$EndpointConfigImpl> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
mixin _$ReleaseModeConfig {
  int get rateLimit => throw _privateConstructorUsedError;
  Duration get rateLimitRefillPeriod => throw _privateConstructorUsedError;
  Duration get tokenLifeSpan => throw _privateConstructorUsedError;

  @JsonKey(ignore: true)
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

  @JsonKey(ignore: true)
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
  @override
  @JsonKey(ignore: true)
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

  /// The configuration for rate limiting.
  /// Rate limiting uses Ktor's implementation, please see
  /// [https://ktor.io/docs/rate-limit.html#configure-rate-limiting]() for more
  /// info.
  ReleaseModeConfig get releaseModeConfig => throw _privateConstructorUsedError;

  /// The list of additional log writers that should be used by Mockzilla.
  List<MockzillaLogger> get additionalLogWriters =>
      throw _privateConstructorUsedError;

  @JsonKey(ignore: true)
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
      List<MockzillaLogger> additionalLogWriters});

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

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? port = null,
    Object? endpoints = null,
    Object? isRelease = null,
    Object? localHostOnly = null,
    Object? logLevel = null,
    Object? releaseModeConfig = null,
    Object? additionalLogWriters = null,
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
      additionalLogWriters: null == additionalLogWriters
          ? _value.additionalLogWriters
          : additionalLogWriters // ignore: cast_nullable_to_non_nullable
              as List<MockzillaLogger>,
    ) as $Val);
  }

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
      List<MockzillaLogger> additionalLogWriters});

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

  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? port = null,
    Object? endpoints = null,
    Object? isRelease = null,
    Object? localHostOnly = null,
    Object? logLevel = null,
    Object? releaseModeConfig = null,
    Object? additionalLogWriters = null,
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
      additionalLogWriters: null == additionalLogWriters
          ? _value._additionalLogWriters
          : additionalLogWriters // ignore: cast_nullable_to_non_nullable
              as List<MockzillaLogger>,
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
      required final List<MockzillaLogger> additionalLogWriters})
      : _endpoints = endpoints,
        _additionalLogWriters = additionalLogWriters;

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

  /// The configuration for rate limiting.
  /// Rate limiting uses Ktor's implementation, please see
  /// [https://ktor.io/docs/rate-limit.html#configure-rate-limiting]() for more
  /// info.
  @override
  @JsonKey()
  final ReleaseModeConfig releaseModeConfig;

  /// The list of additional log writers that should be used by Mockzilla.
  final List<MockzillaLogger> _additionalLogWriters;

  /// The list of additional log writers that should be used by Mockzilla.
  @override
  List<MockzillaLogger> get additionalLogWriters {
    if (_additionalLogWriters is EqualUnmodifiableListView)
      return _additionalLogWriters;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_additionalLogWriters);
  }

  @override
  String toString() {
    return 'MockzillaConfig(port: $port, endpoints: $endpoints, isRelease: $isRelease, localHostOnly: $localHostOnly, logLevel: $logLevel, releaseModeConfig: $releaseModeConfig, additionalLogWriters: $additionalLogWriters)';
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
            const DeepCollectionEquality()
                .equals(other._additionalLogWriters, _additionalLogWriters));
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
      const DeepCollectionEquality().hash(_additionalLogWriters));

  @JsonKey(ignore: true)
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
          required final List<MockzillaLogger> additionalLogWriters}) =
      _$MockzillaConfigImpl;

  @override

  /// The port that the Mockzilla should be available through.
  int get port;
  @override

  /// The list of available mocked endpoints.
  List<EndpointConfig> get endpoints;
  @override

  /// Can be used to add rudimentary restrictions to the Mockzilla server
  /// such as rate limiting. See [https://apadmi-engineering.github.io/Mockzilla/additional_config/#release-mode]()
  /// for more information.
  bool get isRelease;
  @override

  /// Whether Mockzilla server should only be available on the host device.
  bool get localHostOnly;
  @override

  /// The level of logging that should be used by Mockzilla.
  LogLevel get logLevel;
  @override

  /// The configuration for rate limiting.
  /// Rate limiting uses Ktor's implementation, please see
  /// [https://ktor.io/docs/rate-limit.html#configure-rate-limiting]() for more
  /// info.
  ReleaseModeConfig get releaseModeConfig;
  @override

  /// The list of additional log writers that should be used by Mockzilla.
  List<MockzillaLogger> get additionalLogWriters;
  @override
  @JsonKey(ignore: true)
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

  @JsonKey(ignore: true)
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

  @JsonKey(ignore: true)
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
  @override
  @JsonKey(ignore: true)
  _$$MockzillaRuntimeParamsImplCopyWith<_$MockzillaRuntimeParamsImpl>
      get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
mixin _$AuthHeader {
  String get key => throw _privateConstructorUsedError;
  String get value => throw _privateConstructorUsedError;

  @JsonKey(ignore: true)
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

  @JsonKey(ignore: true)
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
  @override
  @JsonKey(ignore: true)
  _$$AuthHeaderImplCopyWith<_$AuthHeaderImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
