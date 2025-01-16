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

FetchPackagesRequest _$FetchPackagesRequestFromJson(Map<String, dynamic> json) {
  return _FetchPackagesRequest.fromJson(json);
}

/// @nodoc
mixin _$FetchPackagesRequest {
  String get query => throw _privateConstructorUsedError;

  /// Serializes this FetchPackagesRequest to a JSON map.
  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;

  /// Create a copy of FetchPackagesRequest
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $FetchPackagesRequestCopyWith<FetchPackagesRequest> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $FetchPackagesRequestCopyWith<$Res> {
  factory $FetchPackagesRequestCopyWith(FetchPackagesRequest value,
          $Res Function(FetchPackagesRequest) then) =
      _$FetchPackagesRequestCopyWithImpl<$Res, FetchPackagesRequest>;
  @useResult
  $Res call({String query});
}

/// @nodoc
class _$FetchPackagesRequestCopyWithImpl<$Res,
        $Val extends FetchPackagesRequest>
    implements $FetchPackagesRequestCopyWith<$Res> {
  _$FetchPackagesRequestCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of FetchPackagesRequest
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? query = null,
  }) {
    return _then(_value.copyWith(
      query: null == query
          ? _value.query
          : query // ignore: cast_nullable_to_non_nullable
              as String,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$FetchPackagesRequestImplCopyWith<$Res>
    implements $FetchPackagesRequestCopyWith<$Res> {
  factory _$$FetchPackagesRequestImplCopyWith(_$FetchPackagesRequestImpl value,
          $Res Function(_$FetchPackagesRequestImpl) then) =
      __$$FetchPackagesRequestImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({String query});
}

/// @nodoc
class __$$FetchPackagesRequestImplCopyWithImpl<$Res>
    extends _$FetchPackagesRequestCopyWithImpl<$Res, _$FetchPackagesRequestImpl>
    implements _$$FetchPackagesRequestImplCopyWith<$Res> {
  __$$FetchPackagesRequestImplCopyWithImpl(_$FetchPackagesRequestImpl _value,
      $Res Function(_$FetchPackagesRequestImpl) _then)
      : super(_value, _then);

  /// Create a copy of FetchPackagesRequest
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? query = null,
  }) {
    return _then(_$FetchPackagesRequestImpl(
      query: null == query
          ? _value.query
          : query // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$FetchPackagesRequestImpl implements _FetchPackagesRequest {
  const _$FetchPackagesRequestImpl({required this.query});

  factory _$FetchPackagesRequestImpl.fromJson(Map<String, dynamic> json) =>
      _$$FetchPackagesRequestImplFromJson(json);

  @override
  final String query;

  @override
  String toString() {
    return 'FetchPackagesRequest(query: $query)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$FetchPackagesRequestImpl &&
            (identical(other.query, query) || other.query == query));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(runtimeType, query);

  /// Create a copy of FetchPackagesRequest
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$FetchPackagesRequestImplCopyWith<_$FetchPackagesRequestImpl>
      get copyWith =>
          __$$FetchPackagesRequestImplCopyWithImpl<_$FetchPackagesRequestImpl>(
              this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$FetchPackagesRequestImplToJson(
      this,
    );
  }
}

abstract class _FetchPackagesRequest implements FetchPackagesRequest {
  const factory _FetchPackagesRequest({required final String query}) =
      _$FetchPackagesRequestImpl;

  factory _FetchPackagesRequest.fromJson(Map<String, dynamic> json) =
      _$FetchPackagesRequestImpl.fromJson;

  @override
  String get query;

  /// Create a copy of FetchPackagesRequest
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$FetchPackagesRequestImplCopyWith<_$FetchPackagesRequestImpl>
      get copyWith => throw _privateConstructorUsedError;
}

FetchPackagesResponse _$FetchPackagesResponseFromJson(
    Map<String, dynamic> json) {
  return _FetchPackagesResponse.fromJson(json);
}

/// @nodoc
mixin _$FetchPackagesResponse {
  List<Package> get packages => throw _privateConstructorUsedError;

  /// Serializes this FetchPackagesResponse to a JSON map.
  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;

  /// Create a copy of FetchPackagesResponse
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $FetchPackagesResponseCopyWith<FetchPackagesResponse> get copyWith =>
      throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $FetchPackagesResponseCopyWith<$Res> {
  factory $FetchPackagesResponseCopyWith(FetchPackagesResponse value,
          $Res Function(FetchPackagesResponse) then) =
      _$FetchPackagesResponseCopyWithImpl<$Res, FetchPackagesResponse>;
  @useResult
  $Res call({List<Package> packages});
}

/// @nodoc
class _$FetchPackagesResponseCopyWithImpl<$Res,
        $Val extends FetchPackagesResponse>
    implements $FetchPackagesResponseCopyWith<$Res> {
  _$FetchPackagesResponseCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of FetchPackagesResponse
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? packages = null,
  }) {
    return _then(_value.copyWith(
      packages: null == packages
          ? _value.packages
          : packages // ignore: cast_nullable_to_non_nullable
              as List<Package>,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$FetchPackagesResponseImplCopyWith<$Res>
    implements $FetchPackagesResponseCopyWith<$Res> {
  factory _$$FetchPackagesResponseImplCopyWith(
          _$FetchPackagesResponseImpl value,
          $Res Function(_$FetchPackagesResponseImpl) then) =
      __$$FetchPackagesResponseImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({List<Package> packages});
}

/// @nodoc
class __$$FetchPackagesResponseImplCopyWithImpl<$Res>
    extends _$FetchPackagesResponseCopyWithImpl<$Res,
        _$FetchPackagesResponseImpl>
    implements _$$FetchPackagesResponseImplCopyWith<$Res> {
  __$$FetchPackagesResponseImplCopyWithImpl(_$FetchPackagesResponseImpl _value,
      $Res Function(_$FetchPackagesResponseImpl) _then)
      : super(_value, _then);

  /// Create a copy of FetchPackagesResponse
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? packages = null,
  }) {
    return _then(_$FetchPackagesResponseImpl(
      packages: null == packages
          ? _value._packages
          : packages // ignore: cast_nullable_to_non_nullable
              as List<Package>,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$FetchPackagesResponseImpl implements _FetchPackagesResponse {
  const _$FetchPackagesResponseImpl({required final List<Package> packages})
      : _packages = packages;

  factory _$FetchPackagesResponseImpl.fromJson(Map<String, dynamic> json) =>
      _$$FetchPackagesResponseImplFromJson(json);

  final List<Package> _packages;
  @override
  List<Package> get packages {
    if (_packages is EqualUnmodifiableListView) return _packages;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_packages);
  }

  @override
  String toString() {
    return 'FetchPackagesResponse(packages: $packages)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$FetchPackagesResponseImpl &&
            const DeepCollectionEquality().equals(other._packages, _packages));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode =>
      Object.hash(runtimeType, const DeepCollectionEquality().hash(_packages));

  /// Create a copy of FetchPackagesResponse
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$FetchPackagesResponseImplCopyWith<_$FetchPackagesResponseImpl>
      get copyWith => __$$FetchPackagesResponseImplCopyWithImpl<
          _$FetchPackagesResponseImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$FetchPackagesResponseImplToJson(
      this,
    );
  }
}

abstract class _FetchPackagesResponse implements FetchPackagesResponse {
  const factory _FetchPackagesResponse(
      {required final List<Package> packages}) = _$FetchPackagesResponseImpl;

  factory _FetchPackagesResponse.fromJson(Map<String, dynamic> json) =
      _$FetchPackagesResponseImpl.fromJson;

  @override
  List<Package> get packages;

  /// Create a copy of FetchPackagesResponse
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$FetchPackagesResponseImplCopyWith<_$FetchPackagesResponseImpl>
      get copyWith => throw _privateConstructorUsedError;
}

Package _$PackageFromJson(Map<String, dynamic> json) {
  return _Package.fromJson(json);
}

/// @nodoc
mixin _$Package {
  String get name => throw _privateConstructorUsedError;
  String get description => throw _privateConstructorUsedError;

  /// Serializes this Package to a JSON map.
  Map<String, dynamic> toJson() => throw _privateConstructorUsedError;

  /// Create a copy of Package
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  $PackageCopyWith<Package> get copyWith => throw _privateConstructorUsedError;
}

/// @nodoc
abstract class $PackageCopyWith<$Res> {
  factory $PackageCopyWith(Package value, $Res Function(Package) then) =
      _$PackageCopyWithImpl<$Res, Package>;
  @useResult
  $Res call({String name, String description});
}

/// @nodoc
class _$PackageCopyWithImpl<$Res, $Val extends Package>
    implements $PackageCopyWith<$Res> {
  _$PackageCopyWithImpl(this._value, this._then);

  // ignore: unused_field
  final $Val _value;
  // ignore: unused_field
  final $Res Function($Val) _then;

  /// Create a copy of Package
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? description = null,
  }) {
    return _then(_value.copyWith(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      description: null == description
          ? _value.description
          : description // ignore: cast_nullable_to_non_nullable
              as String,
    ) as $Val);
  }
}

/// @nodoc
abstract class _$$PackageImplCopyWith<$Res> implements $PackageCopyWith<$Res> {
  factory _$$PackageImplCopyWith(
          _$PackageImpl value, $Res Function(_$PackageImpl) then) =
      __$$PackageImplCopyWithImpl<$Res>;
  @override
  @useResult
  $Res call({String name, String description});
}

/// @nodoc
class __$$PackageImplCopyWithImpl<$Res>
    extends _$PackageCopyWithImpl<$Res, _$PackageImpl>
    implements _$$PackageImplCopyWith<$Res> {
  __$$PackageImplCopyWithImpl(
      _$PackageImpl _value, $Res Function(_$PackageImpl) _then)
      : super(_value, _then);

  /// Create a copy of Package
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? description = null,
  }) {
    return _then(_$PackageImpl(
      name: null == name
          ? _value.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      description: null == description
          ? _value.description
          : description // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc
@JsonSerializable()
class _$PackageImpl implements _Package {
  const _$PackageImpl({required this.name, required this.description});

  factory _$PackageImpl.fromJson(Map<String, dynamic> json) =>
      _$$PackageImplFromJson(json);

  @override
  final String name;
  @override
  final String description;

  @override
  String toString() {
    return 'Package(name: $name, description: $description)';
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _$PackageImpl &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.description, description) ||
                other.description == description));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(runtimeType, name, description);

  /// Create a copy of Package
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  @pragma('vm:prefer-inline')
  _$$PackageImplCopyWith<_$PackageImpl> get copyWith =>
      __$$PackageImplCopyWithImpl<_$PackageImpl>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$$PackageImplToJson(
      this,
    );
  }
}

abstract class _Package implements Package {
  const factory _Package(
      {required final String name,
      required final String description}) = _$PackageImpl;

  factory _Package.fromJson(Map<String, dynamic> json) = _$PackageImpl.fromJson;

  @override
  String get name;
  @override
  String get description;

  /// Create a copy of Package
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  _$$PackageImplCopyWith<_$PackageImpl> get copyWith =>
      throw _privateConstructorUsedError;
}
