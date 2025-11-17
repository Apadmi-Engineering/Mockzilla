// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'models.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

_FetchPackagesRequest _$FetchPackagesRequestFromJson(
        Map<String, dynamic> json) =>
    _FetchPackagesRequest(
      query: json['query'] as String,
    );

Map<String, dynamic> _$FetchPackagesRequestToJson(
        _FetchPackagesRequest instance) =>
    <String, dynamic>{
      'query': instance.query,
    };

_FetchPackagesResponse _$FetchPackagesResponseFromJson(
        Map<String, dynamic> json) =>
    _FetchPackagesResponse(
      packages: (json['packages'] as List<dynamic>)
          .map((e) => Package.fromJson(e as Map<String, dynamic>))
          .toList(),
    );

Map<String, dynamic> _$FetchPackagesResponseToJson(
        _FetchPackagesResponse instance) =>
    <String, dynamic>{
      'packages': instance.packages,
    };

_Package _$PackageFromJson(Map<String, dynamic> json) => _Package(
      name: json['name'] as String,
      description: json['description'] as String,
    );

Map<String, dynamic> _$PackageToJson(_Package instance) => <String, dynamic>{
      'name': instance.name,
      'description': instance.description,
    };
