import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
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

  EndpointConfigBuilder setFailureProbability(int failureProbability) {
    _failureProbability = failureProbability;
    return this;
  }

  EndpointConfigBuilder setMeanDelayMillis(int delayMean) {
    _delayMean = delayMean;
    return this;
  }

  EndpointConfigBuilder setDelayMean(int delayVariance) {
    _delayVariance = delayVariance;
    return this;
  }

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

  EndpointConfigBuilder setDefaultHandler(
      MockzillaHttpResponse Function(MockzillaHttpRequest) defaultHandler) {
    _defaultHandler = defaultHandler;
    return this;
  }

  EndpointConfigBuilder setEndpointMatcher(
      bool Function(MockzillaHttpRequest) endpointMatcher) {
    _endpointMatcher = endpointMatcher;
    return this;
  }

  EndpointConfigBuilder setErrorHandler(
      MockzillaHttpResponse Function(MockzillaHttpRequest) errorHandler) {
    _errorHandler = errorHandler;
    return this;
  }
}
