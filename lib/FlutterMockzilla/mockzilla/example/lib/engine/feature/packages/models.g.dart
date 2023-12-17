// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'models.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_$FetchPackagesResponseImpl _$$FetchPackagesResponseImplFromJson(
        Map<String, dynamic> json) =>
    _$FetchPackagesResponseImpl(
      packages: (json['packages'] as List<dynamic>)
          .map((e) => Package.fromJson(e as Map<String, dynamic>))
          .toList(),
    );

Map<String, dynamic> _$$FetchPackagesResponseImplToJson(
        _$FetchPackagesResponseImpl instance) =>
    <String, dynamic>{
      'packages': instance.packages,
    };

_$PackageImpl _$$PackageImplFromJson(Map<String, dynamic> json) =>
    _$PackageImpl(
      name: json['name'] as String,
      description: json['description'] as String,
    );

Map<String, dynamic> _$$PackageImplToJson(_$PackageImpl instance) =>
    <String, dynamic>{
      'name': instance.name,
      'description': instance.description,
    };
