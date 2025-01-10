part of 'callback_provider_test.dart';

final _bridgeRequestFixture = BridgeMockzillaHttpRequest(
  uri: "http://localhost:8080/local-mock/endpoint",
  headers: {},
  body: "",
  method: BridgeHttpMethod.get,
);

const _requestFixture = MockzillaHttpRequest(
  uri: "http://localhost:8080/local-mock/endpoint",
  method: HttpMethod.get,
);

const _responseFixture = MockzillaHttpResponse(
  body: "{ 'response': 'success' }",
);
