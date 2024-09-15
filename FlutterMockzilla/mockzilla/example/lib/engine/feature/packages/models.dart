import 'package:freezed_annotation/freezed_annotation.dart';

part 'models.freezed.dart';

part 'models.g.dart';

@freezed
class FetchPackagesRequest with _$FetchPackagesRequest {
  const factory FetchPackagesRequest({
    required String query,
  }) = _FetchPackagesRequest;

  factory FetchPackagesRequest.fromJson(Map<String, dynamic> json) =>
      _$FetchPackagesRequestFromJson(json);
}

@freezed
class FetchPackagesResponse with _$FetchPackagesResponse {
  const factory FetchPackagesResponse({
    required List<Package> packages,
  }) = _FetchPackagesResponse;

  factory FetchPackagesResponse.fromJson(Map<String, dynamic> json) =>
      _$FetchPackagesResponseFromJson(json);
}

@freezed
class Package with _$Package {
  const factory Package({
    required String name,
    required String description,
  }) = _Package;

  factory Package.fromJson(Map<String, dynamic> json) =>
      _$PackageFromJson(json);
}
