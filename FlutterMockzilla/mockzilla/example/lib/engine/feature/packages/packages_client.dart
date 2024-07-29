import 'package:example/engine/feature/packages/models.dart';
import 'package:retrofit/http.dart';
import 'package:dio/dio.dart';

part 'packages_client.g.dart';

@RestApi(baseUrl: "http://localhost:8080/local-mock/")
abstract class PackagesClient {
  factory PackagesClient(Dio dio, {String baseUrl}) = _PackagesClient;

  @GET("/packages")
  Future<FetchPackagesResponse> fetchPackages(
      @Body() FetchPackagesRequest request);
}
