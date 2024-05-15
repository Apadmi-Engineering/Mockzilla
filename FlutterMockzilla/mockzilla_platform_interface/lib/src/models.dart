import 'dart:io';

import 'package:freezed_annotation/freezed_annotation.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

part 'models.freezed.dart';

enum HttpMethod {
  get,
  head,
  post,
  put,
  delete,
  options,
  patch;
}

enum LogLevel {
  debug,
  error,
  info,
  verbose,
  warn;
}

@freezed
class MockzillaHttpRequest with _$MockzillaHttpRequest {
  /// A representation of a request to the Mockzilla server; this is passed to
  /// an endpoint handler in order to generate an appropriate response.
  const factory MockzillaHttpRequest({
    required String uri,
    required Map<String, String> headers,
    @Default("") String body,
    required HttpMethod method,
  }) = _MockzillaHttpRequest;
}

@freezed
class MockzillaHttpResponse with _$MockzillaHttpResponse {
  /// Created and returned by an endpoint handler in response to an incoming
  /// HTTP request.
  const factory MockzillaHttpResponse({
    @Default(HttpStatus.ok) int statusCode,
    @Default({}) Map<String, String> headers,
    @Default("") String body,
  }) = _MockzillaHttpResponse;
}

// TODO: Implement builder pattern to create endpoints.
class EndpointConfigBuilder {
  final String name;

  final String key;

  final bool Function(MockzillaHttpRequest) endpointMatcher;

  final MockzillaHttpResponse Function(MockzillaHttpRequest) defaultHandler;

  final MockzillaHttpResponse Function(MockzillaHttpRequest) errorHandler;

  int? failureProbability;
  int? delayMean;
  int? delayVariance;

  EndpointConfigBuilder({
    required this.name,
    required this.key,
    required this.endpointMatcher,
    required this.defaultHandler,
    required this.errorHandler,
  });

  EndpointConfigBuilder setFailureProbability(int failureProbability) {
    this.failureProbability = failureProbability;
    return this;
  }

  EndpointConfigBuilder setMeanDelayMillis(int delayMean) {
    this.delayMean = delayMean;
    return this;
  }

  EndpointConfigBuilder withDelayMean(int delayVariance) {
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
}

@freezed
class EndpointConfig with _$EndpointConfig {
  /// This configuration defines how Mockzilla should deal with a subset of
  /// requests such as configuring the response and meta-data such as the
  /// latency and failure rate.
  ///
  /// Please see [https://apadmi-engineering.github.io/Mockzilla/endpoints/]()
  /// for more information.
  const factory EndpointConfig({
    required String name,
    required String key,

    /// Probability as a percentage that the Mockzilla server should return an
    /// error for any single request to this endpoint.
    int? failureProbability,

    /// Optional, the artificial delay in milliseconds that Mockzilla should use to
    /// simulate latency.
    int? delayMean,

    /// Optional, the variance in milliseconds of the artificial delay applied
    /// by Mockzilla to a response to simulate latency. If not provided, then a
    /// default of 0ms is used to eliminate randomness.
    int? delayVariance,

    /// Used to determine whether a particular `request` should be evaluated by
    /// this endpoint.
    required bool Function(MockzillaHttpRequest request) endpointMatcher,
    MockzillaHttpResponse? webApiDefaultResponse,
    MockzillaHttpResponse? webApiErrorResponse,

    /// This function is called when a network request is made to this endpoint,
    /// note that if an error is being returned due to `failureProbability`
    /// then `errorHandler` is used instead.
    required MockzillaHttpResponse Function(MockzillaHttpRequest request)
        defaultHandler,

    /// This function is called when, in response to a network request, the
    /// server returns an error due to`failureProbability`.
    required MockzillaHttpResponse Function(MockzillaHttpRequest request)
        errorHandler,
  }) = _EndpointConfig;

  static EndpointConfigBuilder builder({
    required String name,
    required String key,
    required bool Function(MockzillaHttpRequest) endpointMatcher,
    required MockzillaHttpResponse Function(MockzillaHttpRequest)
        defaultHandler,
    required MockzillaHttpResponse Function(MockzillaHttpRequest) errorHandler,
  }) {
    return EndpointConfigBuilder(
      name: name,
      key: key,
      endpointMatcher: endpointMatcher,
      defaultHandler: defaultHandler,
      errorHandler: errorHandler,
    );
  }
}

@freezed
class ReleaseModeConfig with _$ReleaseModeConfig {
  /// Rate limiting uses Ktor's implementation, please see
  /// [https://ktor.io/docs/rate-limit.html#configure-rate-limiting]() for more
  /// info.
  const factory ReleaseModeConfig({
    @Default(60) int rateLimit,
    @Default(Duration(seconds: 60)) Duration rateLimitRefillPeriod,
    @Default(Duration(milliseconds: 500)) Duration tokenLifeSpan,
  }) = _ReleaseModeConfig;
}

abstract class MockzillaLogger {
  void log(LogLevel level, String message, String tag, Exception? exception);
}

@freezed
class MockzillaConfig with _$MockzillaConfig {
  const factory MockzillaConfig({
    /// The port that the Mockzilla should be available through.
    required int port,

    /// The list of available mocked endpoints.
    required List<EndpointConfig> endpoints,

    /// Can be used to add rudimentary restrictions to the Mockzilla server
    /// such as rate limiting. See [https://apadmi-engineering.github.io/Mockzilla/additional_config/#release-mode]()
    /// for more information.
    required bool isRelease,

    /// Whether Mockzilla server should only be available on the host device.
    required bool localHostOnly,
    required LogLevel logLevel,
    required ReleaseModeConfig releaseModeConfig,
    required List<MockzillaLogger> additionalLogWriters,
  }) = _MockzillaConfig;
}

// TODO: Implement returning this class from `startMockzilla`.
@freezed
class MockzillaRuntimeParams with _$MockzillaRuntimeParams {
  const factory MockzillaRuntimeParams({
    required MockzillaConfig config,
    required String mockBaseUrl,
    required String apiBaseUrl,
    required int port,
    required AuthHeaderProvider authHeaderProvider,
  }) = _MockzillaRuntimeParams;
}

@freezed
class AuthHeader with _$AuthHeader {
  const factory AuthHeader({
    required String key,
    required String value,
  }) = _AuthHeader;
}

abstract class AuthHeaderProvider {
  Future<AuthHeader> generateHeader();
}
