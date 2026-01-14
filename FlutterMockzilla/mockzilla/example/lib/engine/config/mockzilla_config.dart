import 'dart:convert';

import 'package:mockzilla/mockzilla.dart';
import 'package:mockzilla_example/engine/feature/packages/models.dart';

final mockzillaConfig =
    const MockzillaConfig(logLevel: LogLevel.verbose).addEndpoint(
  () => EndpointConfig(
      name: "Fetch Packages",
      endpointMatcher: (request) async =>
          request.uri.endsWith("packages") && request.method == HttpMethod.post,
      defaultHandler: (_) async => defaultResponse,
      errorHandler: (_) async => errorResponse,
      shouldFail: false,
      dashboardOptionsConfig: DashboardOptionsConfig(presets: [
        DashboardOverridePreset(
            name: "Error Preset",
            description: "Error Preset Example",
            response: MockzillaHttpResponse(
              statusCode: 404,
              headers: {},
              body: "No body",
            ))
      ])),
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
