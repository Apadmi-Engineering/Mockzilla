package com.apadmi.mockzilla

import BridgeDashboardOptionsConfig
import BridgeDashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpRequest
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams

import BridgeEndpointConfig
import BridgeHttpMethod
import BridgeLogLevel
import BridgeMockzillaConfig
import BridgeMockzillaHttpRequest
import BridgeMockzillaHttpResponse
import BridgeMockzillaRuntimeParams
import BridgeReleaseModeConfig
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode

import kotlin.time.Duration.Companion.milliseconds

fun BridgeHttpMethod.toNative() = when (this) {
    BridgeHttpMethod.GET -> HttpMethod.Get
    BridgeHttpMethod.HEAD -> HttpMethod.Head
    BridgeHttpMethod.POST -> HttpMethod.Post
    BridgeHttpMethod.PUT -> HttpMethod.Put
    BridgeHttpMethod.DELETE -> HttpMethod.Delete
    BridgeHttpMethod.OPTIONS -> HttpMethod.Options
    BridgeHttpMethod.PATCH -> HttpMethod.Patch
}

fun BridgeHttpMethod.Companion.fromNative(
    data: HttpMethod
) = when (data) {
    HttpMethod.Get -> BridgeHttpMethod.GET
    HttpMethod.Head -> BridgeHttpMethod.HEAD
    HttpMethod.Post -> BridgeHttpMethod.POST
    HttpMethod.Put -> BridgeHttpMethod.PUT
    HttpMethod.Delete -> BridgeHttpMethod.DELETE
    HttpMethod.Options -> BridgeHttpMethod.OPTIONS
    HttpMethod.Patch -> BridgeHttpMethod.PATCH
    else -> throw NotImplementedError()
}

fun BridgeLogLevel.toNative() = when (this) {
    BridgeLogLevel.DEBUG -> MockzillaConfig.LogLevel.Debug
    BridgeLogLevel.ERROR -> MockzillaConfig.LogLevel.Error
    BridgeLogLevel.INFO -> MockzillaConfig.LogLevel.Info
    BridgeLogLevel.VERBOSE -> MockzillaConfig.LogLevel.Verbose
    BridgeLogLevel.WARN -> MockzillaConfig.LogLevel.Warn
    BridgeLogLevel.ASSERTION -> MockzillaConfig.LogLevel.Assert
}

fun BridgeLogLevel.Companion.fromNative(
    data: MockzillaConfig.LogLevel
) = when (data) {
    MockzillaConfig.LogLevel.Debug -> BridgeLogLevel.DEBUG
    MockzillaConfig.LogLevel.Error -> BridgeLogLevel.ERROR
    MockzillaConfig.LogLevel.Info -> BridgeLogLevel.INFO
    MockzillaConfig.LogLevel.Verbose -> BridgeLogLevel.VERBOSE
    MockzillaConfig.LogLevel.Warn -> BridgeLogLevel.WARN
    MockzillaConfig.LogLevel.Assert -> BridgeLogLevel.ASSERTION
}

fun BridgeMockzillaHttpRequest.Companion.fromNative(
    data: MockzillaHttpRequest
) = BridgeMockzillaHttpRequest(
    data.uri,
    data.headers as Map<String?, String?>,
    data.body,
    BridgeHttpMethod.fromNative(data.method)
)

fun BridgeMockzillaHttpResponse.toNative() = MockzillaHttpResponse(
    HttpStatusCode.fromValue(this.statusCode.toInt()),
    this.headers.filter { entry -> entry.key != null && entry.value != null } as? Map<String, String>
        ?: emptyMap(),
    this.body,
)

fun BridgeMockzillaHttpResponse.Companion.fromNative(
    data: MockzillaHttpResponse
) = BridgeMockzillaHttpResponse(
    data.statusCode.value.toLong(),
    data.headers as Map<String?, String?>,
    data.body,
)

fun BridgeDashboardOverridePreset.Companion.fromNative(data: DashboardOverridePreset) =
    BridgeDashboardOverridePreset(
        data.name,
        data.description,
        BridgeMockzillaHttpResponse.fromNative(data.response),
    )

fun BridgeDashboardOverridePreset.toNative() = DashboardOverridePreset(
    this.name,
    this.description,
    this.response.toNative(),
)

fun BridgeDashboardOptionsConfig.toNative() = DashboardOptionsConfig(
    successPresets = successPresets.mapNotNull { it?.toNative() },
    errorPresets = errorPresets.mapNotNull { it?.toNative() }
)

fun BridgeDashboardOptionsConfig.Companion.fromNative(data: DashboardOptionsConfig) = BridgeDashboardOptionsConfig(
    successPresets = data.successPresets.map { BridgeDashboardOverridePreset.fromNative(it) },
    errorPresets = data.errorPresets.map { BridgeDashboardOverridePreset.fromNative(it) }
)

fun BridgeEndpointConfig.toNative(
    endpointMatcher: MockzillaHttpRequest.(key: String) -> Boolean,
    defaultHandler: MockzillaHttpRequest.(key: String) -> MockzillaHttpResponse,
    errorHandler: MockzillaHttpRequest.(key: String) -> MockzillaHttpResponse,
): EndpointConfiguration = EndpointConfiguration(
    this.name,
    EndpointConfiguration.Key(this.key),
    this.shouldFail,
    this.delayMs.toInt(),
    this.config.toNative(),
    this.versionCode.toInt(),
    { endpointMatcher(this, key) },
    { defaultHandler(this, key) },
    { errorHandler(this, key) },
)

fun BridgeEndpointConfig.Companion.fromNative(
    data: EndpointConfiguration
): BridgeEndpointConfig = BridgeEndpointConfig(
    data.name,
    data.key.raw,
    data.shouldFail,
    data.delay?.toLong() ?: 100,
    data.versionCode.toLong(),
    BridgeDashboardOptionsConfig.fromNative(data.dashboardOptionsConfig)
)

fun BridgeReleaseModeConfig.toNative() = MockzillaConfig.ReleaseModeConfig(
    this.rateLimit.toInt(),
    this.rateLimitRefillPeriodMillis.milliseconds,
    this.tokenLifeSpanMillis.milliseconds,
)

fun BridgeReleaseModeConfig.Companion.fromNative(
    data: MockzillaConfig.ReleaseModeConfig
) = BridgeReleaseModeConfig(
    data.rateLimit.toLong(),
    data.rateLimitRefillPeriod.inWholeMilliseconds,
    data.tokenLifeSpan.inWholeMilliseconds
)

fun BridgeMockzillaConfig.toNative(
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
    this.isNetworkDiscoveryEnabled,
    emptyList()
)

fun BridgeMockzillaConfig.Companion.fromNative(
    data: MockzillaConfig
) = BridgeMockzillaConfig(
    data.port.toLong(),
    data.endpoints.map { BridgeEndpointConfig.fromNative(it) },
    data.isRelease,
    data.localhostOnly,
    BridgeLogLevel.fromNative(data.logLevel),
    BridgeReleaseModeConfig.fromNative(data.releaseModeConfig),
    data.isNetworkDiscoveryEnabled,
)

fun BridgeMockzillaRuntimeParams.Companion.fromNative(
    data: MockzillaRuntimeParams
) = BridgeMockzillaRuntimeParams(
    BridgeMockzillaConfig.fromNative(data.config),
    data.mockBaseUrl,
    data.apiBaseUrl,
    data.port.toLong()
)
