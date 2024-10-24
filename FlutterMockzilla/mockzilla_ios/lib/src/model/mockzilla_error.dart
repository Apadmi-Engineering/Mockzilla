sealed class MockzillaError implements Error {}

class EndpointNotFoundError extends MockzillaError {
  String key;
  @override
  StackTrace stackTrace;

  EndpointNotFoundError(this.key, this.stackTrace);

  @override
  String toString() => "Mockzilla tried to find an endpoint with key $key but "
      "it doesn't exist. If you have added an endpoint with key $key since "
      "starting Mockzilla you may need to restart the server.";
}
