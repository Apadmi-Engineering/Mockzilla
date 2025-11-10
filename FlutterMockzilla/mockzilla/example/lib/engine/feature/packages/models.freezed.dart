// GENERATED CODE - DO NOT MODIFY BY HAND
// coverage:ignore-file
// ignore_for_file: type=lint
// ignore_for_file: unused_element, deprecated_member_use, deprecated_member_use_from_same_package, use_function_type_syntax_for_parameters, unnecessary_const, avoid_init_to_null, invalid_override_different_default_values_named, prefer_expression_function_bodies, annotate_overrides, invalid_annotation_target, unnecessary_question_mark

part of 'models.dart';

// **************************************************************************
// FreezedGenerator
// **************************************************************************

// dart format off
T _$identity<T>(T value) => value;

/// @nodoc
mixin _$FetchPackagesRequest {
  String get query;

  /// Create a copy of FetchPackagesRequest
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $FetchPackagesRequestCopyWith<FetchPackagesRequest> get copyWith =>
      _$FetchPackagesRequestCopyWithImpl<FetchPackagesRequest>(
          this as FetchPackagesRequest, _$identity);

  /// Serializes this FetchPackagesRequest to a JSON map.
  Map<String, dynamic> toJson();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is FetchPackagesRequest &&
            (identical(other.query, query) || other.query == query));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(runtimeType, query);

  @override
  String toString() {
    return 'FetchPackagesRequest(query: $query)';
  }
}

/// @nodoc
abstract mixin class $FetchPackagesRequestCopyWith<$Res> {
  factory $FetchPackagesRequestCopyWith(FetchPackagesRequest value,
          $Res Function(FetchPackagesRequest) _then) =
      _$FetchPackagesRequestCopyWithImpl;
  @useResult
  $Res call({String query});
}

/// @nodoc
class _$FetchPackagesRequestCopyWithImpl<$Res>
    implements $FetchPackagesRequestCopyWith<$Res> {
  _$FetchPackagesRequestCopyWithImpl(this._self, this._then);

  final FetchPackagesRequest _self;
  final $Res Function(FetchPackagesRequest) _then;

  /// Create a copy of FetchPackagesRequest
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? query = null,
  }) {
    return _then(_self.copyWith(
      query: null == query
          ? _self.query
          : query // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// Adds pattern-matching-related methods to [FetchPackagesRequest].
extension FetchPackagesRequestPatterns on FetchPackagesRequest {
  /// A variant of `map` that fallback to returning `orElse`.
  ///
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case final Subclass value:
  ///     return ...;
  ///   case _:
  ///     return orElse();
  /// }
  /// ```

  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_FetchPackagesRequest value)? $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _FetchPackagesRequest() when $default != null:
        return $default(_that);
      case _:
        return orElse();
    }
  }

  /// A `switch`-like method, using callbacks.
  ///
  /// Callbacks receives the raw object, upcasted.
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case final Subclass value:
  ///     return ...;
  ///   case final Subclass2 value:
  ///     return ...;
  /// }
  /// ```

  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_FetchPackagesRequest value) $default,
  ) {
    final _that = this;
    switch (_that) {
      case _FetchPackagesRequest():
        return $default(_that);
      case _:
        throw StateError('Unexpected subclass');
    }
  }

  /// A variant of `map` that fallback to returning `null`.
  ///
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case final Subclass value:
  ///     return ...;
  ///   case _:
  ///     return null;
  /// }
  /// ```

  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_FetchPackagesRequest value)? $default,
  ) {
    final _that = this;
    switch (_that) {
      case _FetchPackagesRequest() when $default != null:
        return $default(_that);
      case _:
        return null;
    }
  }

  /// A variant of `when` that fallback to an `orElse` callback.
  ///
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case Subclass(:final field):
  ///     return ...;
  ///   case _:
  ///     return orElse();
  /// }
  /// ```

  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(String query)? $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _FetchPackagesRequest() when $default != null:
        return $default(_that.query);
      case _:
        return orElse();
    }
  }

  /// A `switch`-like method, using callbacks.
  ///
  /// As opposed to `map`, this offers destructuring.
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case Subclass(:final field):
  ///     return ...;
  ///   case Subclass2(:final field2):
  ///     return ...;
  /// }
  /// ```

  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(String query) $default,
  ) {
    final _that = this;
    switch (_that) {
      case _FetchPackagesRequest():
        return $default(_that.query);
      case _:
        throw StateError('Unexpected subclass');
    }
  }

  /// A variant of `when` that fallback to returning `null`
  ///
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case Subclass(:final field):
  ///     return ...;
  ///   case _:
  ///     return null;
  /// }
  /// ```

  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(String query)? $default,
  ) {
    final _that = this;
    switch (_that) {
      case _FetchPackagesRequest() when $default != null:
        return $default(_that.query);
      case _:
        return null;
    }
  }
}

/// @nodoc
@JsonSerializable()
class _FetchPackagesRequest implements FetchPackagesRequest {
  const _FetchPackagesRequest({required this.query});
  factory _FetchPackagesRequest.fromJson(Map<String, dynamic> json) =>
      _$FetchPackagesRequestFromJson(json);

  @override
  final String query;

  /// Create a copy of FetchPackagesRequest
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$FetchPackagesRequestCopyWith<_FetchPackagesRequest> get copyWith =>
      __$FetchPackagesRequestCopyWithImpl<_FetchPackagesRequest>(
          this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$FetchPackagesRequestToJson(
      this,
    );
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _FetchPackagesRequest &&
            (identical(other.query, query) || other.query == query));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(runtimeType, query);

  @override
  String toString() {
    return 'FetchPackagesRequest(query: $query)';
  }
}

/// @nodoc
abstract mixin class _$FetchPackagesRequestCopyWith<$Res>
    implements $FetchPackagesRequestCopyWith<$Res> {
  factory _$FetchPackagesRequestCopyWith(_FetchPackagesRequest value,
          $Res Function(_FetchPackagesRequest) _then) =
      __$FetchPackagesRequestCopyWithImpl;
  @override
  @useResult
  $Res call({String query});
}

/// @nodoc
class __$FetchPackagesRequestCopyWithImpl<$Res>
    implements _$FetchPackagesRequestCopyWith<$Res> {
  __$FetchPackagesRequestCopyWithImpl(this._self, this._then);

  final _FetchPackagesRequest _self;
  final $Res Function(_FetchPackagesRequest) _then;

  /// Create a copy of FetchPackagesRequest
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? query = null,
  }) {
    return _then(_FetchPackagesRequest(
      query: null == query
          ? _self.query
          : query // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// @nodoc
mixin _$FetchPackagesResponse {
  List<Package> get packages;

  /// Create a copy of FetchPackagesResponse
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $FetchPackagesResponseCopyWith<FetchPackagesResponse> get copyWith =>
      _$FetchPackagesResponseCopyWithImpl<FetchPackagesResponse>(
          this as FetchPackagesResponse, _$identity);

  /// Serializes this FetchPackagesResponse to a JSON map.
  Map<String, dynamic> toJson();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is FetchPackagesResponse &&
            const DeepCollectionEquality().equals(other.packages, packages));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode =>
      Object.hash(runtimeType, const DeepCollectionEquality().hash(packages));

  @override
  String toString() {
    return 'FetchPackagesResponse(packages: $packages)';
  }
}

/// @nodoc
abstract mixin class $FetchPackagesResponseCopyWith<$Res> {
  factory $FetchPackagesResponseCopyWith(FetchPackagesResponse value,
          $Res Function(FetchPackagesResponse) _then) =
      _$FetchPackagesResponseCopyWithImpl;
  @useResult
  $Res call({List<Package> packages});
}

/// @nodoc
class _$FetchPackagesResponseCopyWithImpl<$Res>
    implements $FetchPackagesResponseCopyWith<$Res> {
  _$FetchPackagesResponseCopyWithImpl(this._self, this._then);

  final FetchPackagesResponse _self;
  final $Res Function(FetchPackagesResponse) _then;

  /// Create a copy of FetchPackagesResponse
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? packages = null,
  }) {
    return _then(_self.copyWith(
      packages: null == packages
          ? _self.packages
          : packages // ignore: cast_nullable_to_non_nullable
              as List<Package>,
    ));
  }
}

/// Adds pattern-matching-related methods to [FetchPackagesResponse].
extension FetchPackagesResponsePatterns on FetchPackagesResponse {
  /// A variant of `map` that fallback to returning `orElse`.
  ///
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case final Subclass value:
  ///     return ...;
  ///   case _:
  ///     return orElse();
  /// }
  /// ```

  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_FetchPackagesResponse value)? $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _FetchPackagesResponse() when $default != null:
        return $default(_that);
      case _:
        return orElse();
    }
  }

  /// A `switch`-like method, using callbacks.
  ///
  /// Callbacks receives the raw object, upcasted.
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case final Subclass value:
  ///     return ...;
  ///   case final Subclass2 value:
  ///     return ...;
  /// }
  /// ```

  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_FetchPackagesResponse value) $default,
  ) {
    final _that = this;
    switch (_that) {
      case _FetchPackagesResponse():
        return $default(_that);
      case _:
        throw StateError('Unexpected subclass');
    }
  }

  /// A variant of `map` that fallback to returning `null`.
  ///
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case final Subclass value:
  ///     return ...;
  ///   case _:
  ///     return null;
  /// }
  /// ```

  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_FetchPackagesResponse value)? $default,
  ) {
    final _that = this;
    switch (_that) {
      case _FetchPackagesResponse() when $default != null:
        return $default(_that);
      case _:
        return null;
    }
  }

  /// A variant of `when` that fallback to an `orElse` callback.
  ///
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case Subclass(:final field):
  ///     return ...;
  ///   case _:
  ///     return orElse();
  /// }
  /// ```

  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(List<Package> packages)? $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _FetchPackagesResponse() when $default != null:
        return $default(_that.packages);
      case _:
        return orElse();
    }
  }

  /// A `switch`-like method, using callbacks.
  ///
  /// As opposed to `map`, this offers destructuring.
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case Subclass(:final field):
  ///     return ...;
  ///   case Subclass2(:final field2):
  ///     return ...;
  /// }
  /// ```

  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(List<Package> packages) $default,
  ) {
    final _that = this;
    switch (_that) {
      case _FetchPackagesResponse():
        return $default(_that.packages);
      case _:
        throw StateError('Unexpected subclass');
    }
  }

  /// A variant of `when` that fallback to returning `null`
  ///
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case Subclass(:final field):
  ///     return ...;
  ///   case _:
  ///     return null;
  /// }
  /// ```

  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(List<Package> packages)? $default,
  ) {
    final _that = this;
    switch (_that) {
      case _FetchPackagesResponse() when $default != null:
        return $default(_that.packages);
      case _:
        return null;
    }
  }
}

/// @nodoc
@JsonSerializable()
class _FetchPackagesResponse implements FetchPackagesResponse {
  const _FetchPackagesResponse({required final List<Package> packages})
      : _packages = packages;
  factory _FetchPackagesResponse.fromJson(Map<String, dynamic> json) =>
      _$FetchPackagesResponseFromJson(json);

  final List<Package> _packages;
  @override
  List<Package> get packages {
    if (_packages is EqualUnmodifiableListView) return _packages;
    // ignore: implicit_dynamic_type
    return EqualUnmodifiableListView(_packages);
  }

  /// Create a copy of FetchPackagesResponse
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$FetchPackagesResponseCopyWith<_FetchPackagesResponse> get copyWith =>
      __$FetchPackagesResponseCopyWithImpl<_FetchPackagesResponse>(
          this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$FetchPackagesResponseToJson(
      this,
    );
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _FetchPackagesResponse &&
            const DeepCollectionEquality().equals(other._packages, _packages));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode =>
      Object.hash(runtimeType, const DeepCollectionEquality().hash(_packages));

  @override
  String toString() {
    return 'FetchPackagesResponse(packages: $packages)';
  }
}

/// @nodoc
abstract mixin class _$FetchPackagesResponseCopyWith<$Res>
    implements $FetchPackagesResponseCopyWith<$Res> {
  factory _$FetchPackagesResponseCopyWith(_FetchPackagesResponse value,
          $Res Function(_FetchPackagesResponse) _then) =
      __$FetchPackagesResponseCopyWithImpl;
  @override
  @useResult
  $Res call({List<Package> packages});
}

/// @nodoc
class __$FetchPackagesResponseCopyWithImpl<$Res>
    implements _$FetchPackagesResponseCopyWith<$Res> {
  __$FetchPackagesResponseCopyWithImpl(this._self, this._then);

  final _FetchPackagesResponse _self;
  final $Res Function(_FetchPackagesResponse) _then;

  /// Create a copy of FetchPackagesResponse
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? packages = null,
  }) {
    return _then(_FetchPackagesResponse(
      packages: null == packages
          ? _self._packages
          : packages // ignore: cast_nullable_to_non_nullable
              as List<Package>,
    ));
  }
}

/// @nodoc
mixin _$Package {
  String get name;
  String get description;

  /// Create a copy of Package
  /// with the given fields replaced by the non-null parameter values.
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  $PackageCopyWith<Package> get copyWith =>
      _$PackageCopyWithImpl<Package>(this as Package, _$identity);

  /// Serializes this Package to a JSON map.
  Map<String, dynamic> toJson();

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is Package &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.description, description) ||
                other.description == description));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(runtimeType, name, description);

  @override
  String toString() {
    return 'Package(name: $name, description: $description)';
  }
}

/// @nodoc
abstract mixin class $PackageCopyWith<$Res> {
  factory $PackageCopyWith(Package value, $Res Function(Package) _then) =
      _$PackageCopyWithImpl;
  @useResult
  $Res call({String name, String description});
}

/// @nodoc
class _$PackageCopyWithImpl<$Res> implements $PackageCopyWith<$Res> {
  _$PackageCopyWithImpl(this._self, this._then);

  final Package _self;
  final $Res Function(Package) _then;

  /// Create a copy of Package
  /// with the given fields replaced by the non-null parameter values.
  @pragma('vm:prefer-inline')
  @override
  $Res call({
    Object? name = null,
    Object? description = null,
  }) {
    return _then(_self.copyWith(
      name: null == name
          ? _self.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      description: null == description
          ? _self.description
          : description // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

/// Adds pattern-matching-related methods to [Package].
extension PackagePatterns on Package {
  /// A variant of `map` that fallback to returning `orElse`.
  ///
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case final Subclass value:
  ///     return ...;
  ///   case _:
  ///     return orElse();
  /// }
  /// ```

  @optionalTypeArgs
  TResult maybeMap<TResult extends Object?>(
    TResult Function(_Package value)? $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _Package() when $default != null:
        return $default(_that);
      case _:
        return orElse();
    }
  }

  /// A `switch`-like method, using callbacks.
  ///
  /// Callbacks receives the raw object, upcasted.
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case final Subclass value:
  ///     return ...;
  ///   case final Subclass2 value:
  ///     return ...;
  /// }
  /// ```

  @optionalTypeArgs
  TResult map<TResult extends Object?>(
    TResult Function(_Package value) $default,
  ) {
    final _that = this;
    switch (_that) {
      case _Package():
        return $default(_that);
      case _:
        throw StateError('Unexpected subclass');
    }
  }

  /// A variant of `map` that fallback to returning `null`.
  ///
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case final Subclass value:
  ///     return ...;
  ///   case _:
  ///     return null;
  /// }
  /// ```

  @optionalTypeArgs
  TResult? mapOrNull<TResult extends Object?>(
    TResult? Function(_Package value)? $default,
  ) {
    final _that = this;
    switch (_that) {
      case _Package() when $default != null:
        return $default(_that);
      case _:
        return null;
    }
  }

  /// A variant of `when` that fallback to an `orElse` callback.
  ///
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case Subclass(:final field):
  ///     return ...;
  ///   case _:
  ///     return orElse();
  /// }
  /// ```

  @optionalTypeArgs
  TResult maybeWhen<TResult extends Object?>(
    TResult Function(String name, String description)? $default, {
    required TResult orElse(),
  }) {
    final _that = this;
    switch (_that) {
      case _Package() when $default != null:
        return $default(_that.name, _that.description);
      case _:
        return orElse();
    }
  }

  /// A `switch`-like method, using callbacks.
  ///
  /// As opposed to `map`, this offers destructuring.
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case Subclass(:final field):
  ///     return ...;
  ///   case Subclass2(:final field2):
  ///     return ...;
  /// }
  /// ```

  @optionalTypeArgs
  TResult when<TResult extends Object?>(
    TResult Function(String name, String description) $default,
  ) {
    final _that = this;
    switch (_that) {
      case _Package():
        return $default(_that.name, _that.description);
      case _:
        throw StateError('Unexpected subclass');
    }
  }

  /// A variant of `when` that fallback to returning `null`
  ///
  /// It is equivalent to doing:
  /// ```dart
  /// switch (sealedClass) {
  ///   case Subclass(:final field):
  ///     return ...;
  ///   case _:
  ///     return null;
  /// }
  /// ```

  @optionalTypeArgs
  TResult? whenOrNull<TResult extends Object?>(
    TResult? Function(String name, String description)? $default,
  ) {
    final _that = this;
    switch (_that) {
      case _Package() when $default != null:
        return $default(_that.name, _that.description);
      case _:
        return null;
    }
  }
}

/// @nodoc
@JsonSerializable()
class _Package implements Package {
  const _Package({required this.name, required this.description});
  factory _Package.fromJson(Map<String, dynamic> json) =>
      _$PackageFromJson(json);

  @override
  final String name;
  @override
  final String description;

  /// Create a copy of Package
  /// with the given fields replaced by the non-null parameter values.
  @override
  @JsonKey(includeFromJson: false, includeToJson: false)
  @pragma('vm:prefer-inline')
  _$PackageCopyWith<_Package> get copyWith =>
      __$PackageCopyWithImpl<_Package>(this, _$identity);

  @override
  Map<String, dynamic> toJson() {
    return _$PackageToJson(
      this,
    );
  }

  @override
  bool operator ==(Object other) {
    return identical(this, other) ||
        (other.runtimeType == runtimeType &&
            other is _Package &&
            (identical(other.name, name) || other.name == name) &&
            (identical(other.description, description) ||
                other.description == description));
  }

  @JsonKey(includeFromJson: false, includeToJson: false)
  @override
  int get hashCode => Object.hash(runtimeType, name, description);

  @override
  String toString() {
    return 'Package(name: $name, description: $description)';
  }
}

/// @nodoc
abstract mixin class _$PackageCopyWith<$Res> implements $PackageCopyWith<$Res> {
  factory _$PackageCopyWith(_Package value, $Res Function(_Package) _then) =
      __$PackageCopyWithImpl;
  @override
  @useResult
  $Res call({String name, String description});
}

/// @nodoc
class __$PackageCopyWithImpl<$Res> implements _$PackageCopyWith<$Res> {
  __$PackageCopyWithImpl(this._self, this._then);

  final _Package _self;
  final $Res Function(_Package) _then;

  /// Create a copy of Package
  /// with the given fields replaced by the non-null parameter values.
  @override
  @pragma('vm:prefer-inline')
  $Res call({
    Object? name = null,
    Object? description = null,
  }) {
    return _then(_Package(
      name: null == name
          ? _self.name
          : name // ignore: cast_nullable_to_non_nullable
              as String,
      description: null == description
          ? _self.description
          : description // ignore: cast_nullable_to_non_nullable
              as String,
    ));
  }
}

// dart format on
