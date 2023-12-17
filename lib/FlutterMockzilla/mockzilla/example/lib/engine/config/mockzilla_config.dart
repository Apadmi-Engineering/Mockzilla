import 'dart:convert';

import 'package:example/engine/feature/packages/models.dart';
import 'package:mockzilla/mockzilla.dart';

final mockzillaConfig = MockzillaConfig(
  port: 8080,
  endpoints: [
    EndpointConfig(
      name: "Fetch Packages",
      key: "fetch-packages",
      endpointMatcher: (request) =>
          RegExp(r"/packages").hasMatch(request.uri) &&
          request.method == HttpMethod.get,
      defaultHandler: (_) => defaultResponse,
      errorHandler: (_) => errorResponse,
      failureProbability: 0,
      delayMean: 100,
      delayVariance: 0,
    ),
  ],
  isRelease: false,
  localHostOnly: false,
  logLevel: LogLevel.debug,
  releaseModeConfig: const ReleaseModeConfig(),
  additionalLogWriters: [],
);

final defaultResponse = MockzillaHttpResponse(
  statusCode: 200,
  headers: {
    "Content-type": "application/json"
  },
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
