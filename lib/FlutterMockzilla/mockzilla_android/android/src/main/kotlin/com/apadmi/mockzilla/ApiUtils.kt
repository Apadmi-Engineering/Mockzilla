package com.apadmi.mockzilla

import ApiEndpointConfig
import ApiHttpMethod
import ApiLogLevel
import ApiMockzillaConfig
import ApiMockzillaHttpRequest
import ApiMockzillaHttpResponse
import ApiMockzillaRuntimeParams
import ApiReleaseModeConfig
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpRequest
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlin.time.Duration.Companion.milliseconds

fun ApiHttpMethod.toNative() = when (this) {
    ApiHttpMethod.GET -> HttpMethod.Get
    ApiHttpMethod.HEAD -> HttpMethod.Head
    ApiHttpMethod.POST -> HttpMethod.Post
    ApiHttpMethod.PUT -> HttpMethod.Put
    ApiHttpMethod.DELETE -> HttpMethod.Delete
    ApiHttpMethod.OPTIONS -> HttpMethod.Options
    ApiHttpMethod.PATCH -> HttpMethod.Patch
}

fun ApiHttpMethod.Companion.fromNative(
    data: HttpMethod
) = when (data) {
    HttpMethod.Get -> ApiHttpMethod.GET
    HttpMethod.Head -> ApiHttpMethod.HEAD
    HttpMethod.Post -> ApiHttpMethod.POST
    HttpMethod.Put -> ApiHttpMethod.PUT
    HttpMethod.Delete -> ApiHttpMethod.DELETE
    HttpMethod.Options -> ApiHttpMethod.OPTIONS
    HttpMethod.Patch -> ApiHttpMethod.PATCH
    else -> throw NotImplementedError()
}

fun ApiLogLevel.toNative() = when (this) {
    ApiLogLevel.DEBUG -> MockzillaConfig.LogLevel.Debug
    ApiLogLevel.ERROR -> MockzillaConfig.LogLevel.Error
    ApiLogLevel.INFO -> MockzillaConfig.LogLevel.Info
    ApiLogLevel.VERBOSE -> MockzillaConfig.LogLevel.Verbose
    ApiLogLevel.WARN -> MockzillaConfig.LogLevel.Warn
}

fun ApiLogLevel.Companion.fromNative(
    data: MockzillaConfig.LogLevel
) = when (data) {
    MockzillaConfig.LogLevel.Debug -> ApiLogLevel.DEBUG
    MockzillaConfig.LogLevel.Error -> ApiLogLevel.ERROR
    MockzillaConfig.LogLevel.Info -> ApiLogLevel.INFO
    MockzillaConfig.LogLevel.Verbose -> ApiLogLevel.VERBOSE
    MockzillaConfig.LogLevel.Warn -> ApiLogLevel.WARN
    MockzillaConfig.LogLevel.Assert -> throw NotImplementedError()
}

fun ApiMockzillaHttpRequest.toNative() = MockzillaHttpRequest(
    this.uri,
    this.headers.filter { entry -> entry.key != null && entry.value != null } as? Map<String, String>
        ?: emptyMap(),
    this.body,
    this.method.toNative(),
)

fun ApiMockzillaHttpRequest.Companion.fromNative(
    data: MockzillaHttpRequest
) = ApiMockzillaHttpRequest(
    data.uri,
    data.headers as Map<String?, String?>,
    data.body,
    ApiHttpMethod.fromNative(data.method)
)

fun ApiMockzillaHttpResponse.toNative() = MockzillaHttpResponse(
    HttpStatusCode.fromValue(this.statusCode.toInt()),
    this.headers.filter { entry -> entry.key != null && entry.value != null } as? Map<String, String>
        ?: emptyMap(),
    this.body,
)

fun ApiMockzillaHttpResponse.Companion.fromNative(
    data: MockzillaHttpResponse
) = ApiMockzillaHttpResponse(
    data.statusCode.value.toLong(),
    data.headers as Map<String?, String?>,
    data.body,
)

fun ApiEndpointConfig.toNative(
    endpointMatcher: MockzillaHttpRequest.(key: String) -> Boolean,
    defaultHandler: MockzillaHttpRequest.(key: String) -> MockzillaHttpResponse,
    errorHandler: MockzillaHttpRequest.(key: String) -> MockzillaHttpResponse,
) = EndpointConfiguration(
    this.name,
    this.key,
    this.failureProbability?.toInt(),
    this.delayMean?.toInt(),
    this.delayVariance?.toInt(),
    { endpointMatcher(this, key) },
    this.webApiDefaultResponse?.toNative(),
    this.webApiErrorResponse?.toNative(),
    { defaultHandler(this, key) },
    { errorHandler(this, key) },
)

fun ApiEndpointConfig.Companion.fromNative(
    data: EndpointConfiguration
) = ApiEndpointConfig(
    data.name,
    data.key,
    data.failureProbability?.toLong(),
    data.delayMean?.toLong(),
    data.delayVariance?.toLong(),
    data.webApiDefaultResponse?.let { ApiMockzillaHttpResponse.fromNative(it) },
    data.webApiErrorResponse?.let { ApiMockzillaHttpResponse.fromNative(it) },
)

fun ApiReleaseModeConfig.toNative() = MockzillaConfig.ReleaseModeConfig(
    this.rateLimit.toInt(),
    this.rateLimitRefillPeriodMillis.milliseconds,
    this.tokenLifeSpanMillis.milliseconds,
)

fun ApiReleaseModeConfig.Companion.fromNative(
    data: MockzillaConfig.ReleaseModeConfig
) = ApiReleaseModeConfig(
    data.rateLimit.toLong(),
    data.rateLimitRefillPeriod.inWholeMilliseconds,
    data.tokenLifeSpan.inWholeMilliseconds
)

fun ApiMockzillaConfig.toNative(
    endpointMatcher: MockzillaHttpRequest.(key: String) -> Boolean,
    defaultHandler: MockzillaHttpRequest.(key: String) -> MockzillaHttpResponse,
    errorHandler: MockzillaHttpRequest.(key: String) -> MockzillaHttpResponse,
) = MockzillaConfig(
    this.port.toInt(),
    this.endpoints.filterNotNull().map {
        it.toNative(endpointMatcher, defaultHandler, errorHandler)
    },
    this.isRelease,
    this.localHostOnly,
    this.logLevel.toNative(),
    this.releaseModeConfig.toNative(),
    emptyList()
)


fun ApiMockzillaConfig.Companion.fromNative(
    data: MockzillaConfig
) = ApiMockzillaConfig(
    data.port.toLong(),
    data.endpoints.map { ApiEndpointConfig.fromNative(it) },
    data.isRelease,
    data.localhostOnly,
    ApiLogLevel.fromNative(data.logLevel),
    ApiReleaseModeConfig.fromNative(data.releaseModeConfig),
)

fun ApiMockzillaRuntimeParams.Companion.fromNative(
    data: MockzillaRuntimeParams
) = ApiMockzillaRuntimeParams(
    ApiMockzillaConfig.fromNative(data.config),
    data.mockBaseUrl,
    data.apiBaseUrl,
    data.port.toLong()
)