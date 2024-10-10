import 'dart:convert';

import 'package:mockzilla/mockzilla.dart';
import 'package:mockzilla_example/engine/feature/packages/models.dart';

final mockzillaConfig = const MockzillaConfig().addEndpoint(
  () => EndpointConfig(
    name: "Fetch Packages",
    endpointMatcher: (request) =>
        request.uri.endsWith("packages") && request.method == HttpMethod.get,
    defaultHandler: (_) => defaultResponse,
    errorHandler: (_) => errorResponse,
    shouldFail: true,
  ),
);

final defaultResponse = MockzillaHttpResponse(
  statusCode: 200,
  headers: {"Content-type": "application/json"},
  body: jsonEncode(
    const FetchPackagesResponse(
      packages: [
        Package(
          name: "mockzilla",
          description: "A pretty cool network mocking library.",
        ),
        Package(
          name: "mockzilla_platform_interface",
          description: "A common interface for the mockzilla plugin.",
        ),
        Package(
          name: "mockzilla_android",
          description: "The Android implementation for the mockzilla plugin.",
        ),
        Package(
          name: "mockzilla_ios",
          description: "The iOS implementation for the mockzilla plugin.",
        ),
      ],
    ).toJson(),
  ),
);

const errorResponse = MockzillaHttpResponse(
  statusCode: 400,
  headers: {},
  body: "",
);
