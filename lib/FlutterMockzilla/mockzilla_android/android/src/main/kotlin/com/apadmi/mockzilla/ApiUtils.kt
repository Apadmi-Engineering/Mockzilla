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

fun ApiHttpMethod.toModel() = when (this) {
    ApiHttpMethod.GET -> HttpMethod.Get
    ApiHttpMethod.HEAD -> HttpMethod.Head
    ApiHttpMethod.POST -> HttpMethod.Post
    ApiHttpMethod.PUT -> HttpMethod.Put
    ApiHttpMethod.DELETE -> HttpMethod.Delete
    ApiHttpMethod.OPTIONS -> HttpMethod.Options
    ApiHttpMethod.PATCH -> HttpMethod.Patch
}

fun ApiHttpMethod.Companion.fromModel(
    data: HttpMethod
) = when(data) {
    HttpMethod.Get -> ApiHttpMethod.GET
    HttpMethod.Head -> ApiHttpMethod.HEAD
    HttpMethod.Post -> ApiHttpMethod.POST
    HttpMethod.Put -> ApiHttpMethod.PUT
    HttpMethod.Delete -> ApiHttpMethod.DELETE
    HttpMethod.Options -> ApiHttpMethod.OPTIONS
    HttpMethod.Patch -> ApiHttpMethod.PATCH
    else -> throw NotImplementedError()
}

fun ApiLogLevel.toModel() = when (this) {
    ApiLogLevel.DEBUG -> MockzillaConfig.LogLevel.Debug
    ApiLogLevel.ERROR -> MockzillaConfig.LogLevel.Error
    ApiLogLevel.INFO -> MockzillaConfig.LogLevel.Info
    ApiLogLevel.VERBOSE -> MockzillaConfig.LogLevel.Verbose
    ApiLogLevel.WARN -> MockzillaConfig.LogLevel.Warn
}

fun ApiLogLevel.Companion.fromModel(
    data: MockzillaConfig.LogLevel
) = when(data) {
    MockzillaConfig.LogLevel.Debug -> ApiLogLevel.DEBUG
    MockzillaConfig.LogLevel.Error -> ApiLogLevel.ERROR
    MockzillaConfig.LogLevel.Info -> ApiLogLevel.INFO
    MockzillaConfig.LogLevel.Verbose -> ApiLogLevel.VERBOSE
    MockzillaConfig.LogLevel.Warn -> ApiLogLevel.WARN
    MockzillaConfig.LogLevel.Assert -> throw NotImplementedError()
}

fun ApiMockzillaHttpRequest.toModel() = MockzillaHttpRequest(
    this.uri,
    this.headers.filter { entry -> entry.key != null && entry.value != null } as? Map<String, String>
        ?: emptyMap(),
    this.body,
    this.method.toModel(),
)

fun ApiMockzillaHttpRequest.Companion.fromModel(
    data: MockzillaHttpRequest
) = ApiMockzillaHttpRequest(
    data.uri,
    data.headers as Map<String?, String?>,
    data.body,
    ApiHttpMethod.fromModel(data.method)
)

fun ApiMockzillaHttpResponse.toModel() = MockzillaHttpResponse(
    HttpStatusCode.fromValue(this.statusCode.toInt()),
    this.headers.filter { entry -> entry.key != null && entry.value != null } as? Map<String, String>
        ?: emptyMap(),
    this.body,
)

fun ApiMockzillaHttpResponse.Companion.fromModel(
    data: MockzillaHttpResponse
) = ApiMockzillaHttpResponse(
    data.statusCode.value.toLong(),
    data.headers as Map<String?, String?>,
    data.body,
)

fun ApiEndpointConfig.toModel(
    endpointMatcher: MockzillaHttpRequest.(key: String) -> Boolean,
    defaultHandler: MockzillaHttpRequest.() -> MockzillaHttpResponse,
    errorHandler: MockzillaHttpRequest.() -> MockzillaHttpResponse,
) = EndpointConfiguration(
    this.name,
    this.key,
    this.failureProbability?.toInt(),
    this.delayMean?.toInt(),
    this.delayVariance?.toInt(),
    { endpointMatcher(this, key) },
    this.webApiDefaultResponse?.toModel(),
    this.webApiErrorResponse?.toModel(),
    defaultHandler, errorHandler,
)

fun ApiEndpointConfig.Companion.fromModel(
    data: EndpointConfiguration
) = ApiEndpointConfig(
    data.name,
    data.key,
    data.failureProbability?.toLong(),
    data.delayMean?.toLong(),
    data.delayVariance?.toLong(),
    data.webApiDefaultResponse?.let { ApiMockzillaHttpResponse.fromModel(it) },
    data.webApiErrorResponse?.let { ApiMockzillaHttpResponse.fromModel(it) },
)

fun ApiReleaseModeConfig.toModel() = MockzillaConfig.ReleaseModeConfig(
    this.rateLimit.toInt(),
    this.rateLimitRefillPeriodMillis.milliseconds,
    this.tokenLifeSpanMillis.milliseconds,
)

fun ApiReleaseModeConfig.Companion.fromModel(
    data: MockzillaConfig.ReleaseModeConfig
) = ApiReleaseModeConfig(
    data.rateLimit.toLong(),
    data.rateLimitRefillPeriod.inWholeMilliseconds,
    data.tokenLifeSpan.inWholeMilliseconds
)

fun ApiMockzillaConfig.toModel(
    endpointMatcher: MockzillaHttpRequest.(key: String) -> Boolean,
    defaultHandler: MockzillaHttpRequest.() -> MockzillaHttpResponse,
    errorHandler: MockzillaHttpRequest.() -> MockzillaHttpResponse,
) = MockzillaConfig(
    this.port.toInt(),
    this.endpoints.filterNotNull().map {
        it.toModel(endpointMatcher, defaultHandler, errorHandler)
    },
    this.isRelease,
    this.localHostOnly,
    this.logLevel.toModel(),
    this.releaseModeConfig.toModel(),
    emptyList()
)


fun ApiMockzillaConfig.Companion.fromModel(
    data: MockzillaConfig
) = ApiMockzillaConfig(
    data.port.toLong(),
    data.endpoints.map { ApiEndpointConfig.fromModel(it) },
    data.isRelease,
    data.localhostOnly,
    ApiLogLevel.fromModel(data.logLevel),
    ApiReleaseModeConfig.fromModel(data.releaseModeConfig),
)
fun ApiMockzillaRuntimeParams.Companion.fromModel(
    data: MockzillaRuntimeParams
) = ApiMockzillaRuntimeParams(
    ApiMockzillaConfig.fromModel(data.config),
    data.mockBaseUrl,
    data.apiBaseUrl,
    data.port.toLong()
)