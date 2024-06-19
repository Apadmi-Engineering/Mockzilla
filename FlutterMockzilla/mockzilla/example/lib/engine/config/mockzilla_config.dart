import 'dart:convert';

import 'package:example/engine/feature/packages/models.dart';
import 'package:mockzilla/mockzilla.dart';

final mockzillaConfig = const MockzillaConfig().addEndpoint(
  () => EndpointConfig(
    name: "Fetch Packages",
    endpointMatcher: (request) =>
        request.uri.endsWith("packages") && request.method == HttpMethod.get,
    defaultHandler: (_) => defaultResponse,
    errorHandler: (_) => errorResponse,
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
          name: "freezed",
          description: "Code generation for immutable classes.",
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
