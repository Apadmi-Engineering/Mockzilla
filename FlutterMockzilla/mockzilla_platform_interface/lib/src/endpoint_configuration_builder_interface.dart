import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

class EndpointConfigBuilder {
  final String name;

  final String key;

  bool Function(MockzillaHttpRequest) endpointMatcher = (_) => false;

  MockzillaHttpResponse Function(MockzillaHttpRequest) defaultHandler =
      (_) => const MockzillaHttpResponse();

  MockzillaHttpResponse Function(MockzillaHttpRequest) errorHandler =
      (_) => const MockzillaHttpResponse();

  int? failureProbability;
  int? delayMean;
  int? delayVariance;

  EndpointConfigBuilder({
    required this.name,
    required this.key,
  });

  EndpointConfigBuilder setFailureProbability(int failureProbability) {
    this.failureProbability = failureProbability;
    return this;
  }

  EndpointConfigBuilder setMeanDelayMillis(int delayMean) {
    this.delayMean = delayMean;
    return this;
  }

  EndpointConfigBuilder setDelayMean(int delayVariance) {
    this.delayVariance = delayVariance;
    return this;
  }

  EndpointConfig build() {
    return EndpointConfig(
      key: key,
      name: name,
      endpointMatcher: endpointMatcher,
      defaultHandler: defaultHandler,
      errorHandler: errorHandler,
    );
  }

  EndpointConfigBuilder setDefaultHandler(
      MockzillaHttpResponse Function(MockzillaHttpRequest) defaultHandler) {
    this.defaultHandler = defaultHandler;
    return this;
  }

  EndpointConfigBuilder setEndpointMatcher(
      bool Function(MockzillaHttpRequest) endpointMatcher) {
    this.endpointMatcher = endpointMatcher;
    return this;
  }

  EndpointConfigBuilder setErrorHandler(
      MockzillaHttpResponse Function(MockzillaHttpRequest) errorHandler) {
    this.errorHandler = errorHandler;
    return this;
  }
}
