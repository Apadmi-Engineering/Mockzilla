import 'package:mockzilla/mockzilla.dart';

class EndpointConfigBuilder {
  final String _name;

  bool Function(MockzillaHttpRequest) _endpointMatcher = (_) => false;

  MockzillaHttpResponse Function(MockzillaHttpRequest) _defaultHandler =
      (_) => const MockzillaHttpResponse();

  MockzillaHttpResponse Function(MockzillaHttpRequest) _errorHandler =
      (_) => const MockzillaHttpResponse();

  int? _failureProbability;

  int? _delayMean;

  int? _delayVariance;

  EndpointConfigBuilder({
    required String name,
  }) : _name = name;

  /// Sets the [failureProbability] as a percentage that an error is returned from this [EndpointConfig].
  EndpointConfigBuilder setFailureProbability(int failureProbability) {
    _failureProbability = failureProbability;
    return this;
  }

  /// Set the artificial latency in milliseconds for this [EndpointConfig].
  EndpointConfigBuilder setMeanDelayMillis(int delayMeanMillis) {
    _delayMean = delayMeanMillis;
    return this;
  }

  /// Sets the delay variance in milliseconds for this [EndpointConfig], simulating latency.
  /// Defaults to 0ms.
  EndpointConfigBuilder setDelayVariance(int delayVarianceMillis) {
    _delayVariance = delayVarianceMillis;
    return this;
  }

  /// Build this [EndpointConfig] with applied options.
  EndpointConfig build() {
    return EndpointConfig(
      key: _name,
      name: _name,
      endpointMatcher: _endpointMatcher,
      defaultHandler: _defaultHandler,
      errorHandler: _errorHandler,
      failureProbability: _failureProbability,
      delayMean: _delayMean,
      delayVariance: _delayVariance,
    );
  }

  /// Sets the default handler for this [EndpointConfig].
  /// i.e. What this endpoint should return.
  EndpointConfigBuilder setDefaultHandler(
      MockzillaHttpResponse Function(MockzillaHttpRequest) defaultHandler) {
    _defaultHandler = defaultHandler;
    return this;
  }

  /// Sets the endpoint matcher for this [EndpointConfig].
  /// Evaluates if some [MockzillaHttpRequest] should be handled by this endpoint.
  EndpointConfigBuilder setEndpointMatcher(
      bool Function(MockzillaHttpRequest) endpointMatcher) {
    _endpointMatcher = endpointMatcher;
    return this;
  }

  /// Sets the error handler for this [EndpointConfig].
  /// Given an error is simulated, call the [errorHandler] and respond with your designated [MockzillaHttpResponse].
  EndpointConfigBuilder setErrorHandler(
      MockzillaHttpResponse Function(MockzillaHttpRequest) errorHandler) {
    _errorHandler = errorHandler;
    return this;
  }
}
