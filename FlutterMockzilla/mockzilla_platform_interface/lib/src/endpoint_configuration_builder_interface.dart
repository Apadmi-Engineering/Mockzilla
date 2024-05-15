import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

abstract class EndpointBuilderDefaultHandlerStep {
  EndpointBuilderMatcherStep setDefaultHandler(
      MockzillaHttpResponse Function(MockzillaHttpRequest) defaultHandler);
}

abstract class EndpointBuilderMatcherStep {
  EndpointBuilderErrorHandlerStep setEndpointMatcher(
      bool Function(MockzillaHttpRequest) endpointMatcher);
}

abstract class EndpointBuilderErrorHandlerStep {
  EndpointBuilderOptionalsStep setErrorHandler(
      MockzillaHttpResponse Function(MockzillaHttpRequest) errorHandler);
}

abstract class EndpointBuilderOptionalsStep {
  EndpointBuilderBuildStep setFailureProbability(int failureProbability);

  EndpointBuilderBuildStep setMeanDelayMillis(int delayMean);

  EndpointBuilderBuildStep setDelayMean(int delayVariance);
}

abstract class EndpointBuilderBuildStep
    implements EndpointBuilderOptionalsStep {
  EndpointConfig build();
}

class EndpointConfigBuilder
    implements
        EndpointBuilderDefaultHandlerStep,
        EndpointBuilderMatcherStep,
        EndpointBuilderErrorHandlerStep,
        EndpointBuilderOptionalsStep,
        EndpointBuilderBuildStep {
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

  @override
  EndpointConfigBuilder setFailureProbability(int failureProbability) {
    this.failureProbability = failureProbability;
    return this;
  }

  @override
  EndpointConfigBuilder setMeanDelayMillis(int delayMean) {
    this.delayMean = delayMean;
    return this;
  }

  @override
  EndpointConfigBuilder setDelayMean(int delayVariance) {
    this.delayVariance = delayVariance;
    return this;
  }

  @override
  EndpointConfig build() {
    return EndpointConfig(
      key: key,
      name: name,
      endpointMatcher: endpointMatcher,
      defaultHandler: defaultHandler,
      errorHandler: errorHandler,
    );
  }

  @override
  EndpointBuilderMatcherStep setDefaultHandler(
      MockzillaHttpResponse Function(MockzillaHttpRequest) defaultHandler) {
    this.defaultHandler = defaultHandler;
    return this;
  }

  @override
  EndpointBuilderErrorHandlerStep setEndpointMatcher(
      bool Function(MockzillaHttpRequest) endpointMatcher) {
    this.endpointMatcher = endpointMatcher;
    return this;
  }

  @override
  EndpointBuilderOptionalsStep setErrorHandler(
      MockzillaHttpResponse Function(MockzillaHttpRequest) errorHandler) {
    this.errorHandler = errorHandler;
    return this;
  }
}
