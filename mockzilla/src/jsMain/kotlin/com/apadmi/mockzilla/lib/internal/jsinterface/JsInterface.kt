@file:OptIn(ExperimentalJsExport::class, ExperimentalWasmJsInterop::class)

package com.apadmi.mockzilla.lib.internal.jsinterface

import com.apadmi.mockzilla.lib.internal.stopServer
import com.apadmi.mockzilla.lib.internal.utils.JsonProvider
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpRequest
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams
import com.apadmi.mockzilla.lib.models.PartialMockzillaHttpResponse
import com.apadmi.mockzilla.lib.startMockzilla
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.await
import kotlinx.coroutines.promise
import kotlin.js.Promise

@JsExport
@Suppress("unused")
object JsLogLevels {
    const val Assert = "Assert"
    const val Debug = "Debug"
    const val Error = "Error"
    const val Info = "Info"
    const val Verbose = "Verbose"
    const val Warn = "Warn"
}

@JsExport
@Suppress("unused")
object JsPresetType {
    const val ClientError = "ClientError"
    const val Informational = "Informational"
    const val Other = "Other"
    const val Redirect = "Redirect"
    const val ServerError = "ServerError"
    const val Success = "Success"
}

@JsExport
data class JsMockzillaHttpRequest(
    val uri: String,
    val headers: String, // JSON Encoded
    val method: String,
    val bodyAsBytes: Promise<ByteArray>,
    val bodyAsString: Promise<String>
)

fun MockzillaHttpRequest.toJs() = JsMockzillaHttpRequest(
    uri = uri,
    headers = JsonProvider.json.encodeToString(headers),
    method = method.value,
    bodyAsBytes = GlobalScope.promise { bodyAsBytes() },
    bodyAsString = GlobalScope.promise { bodyAsString() },
)

@JsExport
data class JsEndpointConfiguration(
    val name: String,
    val key: String,
    val shouldFail: Boolean,
    val delay: Int?,
    val dashboardOptionsConfig: JsDashboardOptionsConfig,
    val versionCode: Int,
    // Need to use dynamic since the JSExport annotations isn't smart enough to export the declaration
    // correctly
    val endpointMatcher: (dynamic /* JsMockzillaHttpRequest */) -> Promise<Boolean>,
    val defaultHandler: (dynamic /* JsMockzillaHttpRequest */) -> Promise<JsMockzillaHttpResponse>,
    val errorHandler: (dynamic /* JsMockzillaHttpRequest */) -> Promise<JsMockzillaHttpResponse>,
) {
    internal fun fromJs(): EndpointConfiguration {
        return EndpointConfiguration(
            name = name,
            key = EndpointConfiguration.Key(key),
            shouldFail = shouldFail,
            delay = delay,
            dashboardOptionsConfig = dashboardOptionsConfig.fromJs(),
            versionCode = versionCode,
            endpointMatcher = { endpointMatcher(toJs()).await() },
            defaultHandler = { defaultHandler(this.toJs()).await().fromJs() },
            errorHandler = { errorHandler(this.toJs()).await().fromJs() },
        )
    }
}

@JsExport
data class JsDashboardOptionsConfig(
    val presets: JsArray<JsDashboardOverridePreset>,
) {
    internal fun fromJs() = DashboardOptionsConfig(
        errorPresets = emptyList(),
        successPresets = presets.toList().map { js ->
            DashboardOverridePreset(
                name = js.name,
                description = js.description,
                response = js.response.fromJs(),
                type = js.type?.let {
                    when(it) {
                        "ClientError" -> DashboardOverridePreset.Type.ClientError
                        "Informational" -> DashboardOverridePreset.Type.Informational
                        "Other" -> DashboardOverridePreset.Type.Other
                        "Redirect" -> DashboardOverridePreset.Type.Redirect
                        "ServerError" -> DashboardOverridePreset.Type.ServerError
                        "Success" -> DashboardOverridePreset.Type.Success
                        else -> null
                    }
                }
            )
        }
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class.js != other::class.js) return false

        other as JsDashboardOptionsConfig

        if (!presets.contentEquals(other.presets)) return false

        return true
    }

    override fun hashCode(): Int {
        return presets.contentHashCode()
    }
}

@JsExport
data class JsDashboardOverridePreset(
    val name: String,
    val description: String?,
    val response: JsPartialMockzillaHttpResponse,
    val type: String? // JsPresetType
)

fun entriesOf(jsObject: dynamic): List<Pair<String, String>> =
    (js("Object.entries") as (dynamic) -> Array<Array<Any?>>)
        .invoke(jsObject)
        .map { entry -> entry[0].toString() to entry[1].toString() }

fun mapFrom(jsObject: dynamic): Map<String, String> =
    entriesOf(jsObject).toMap()

@JsExport
data class JsMockzillaHttpResponse(
    val statusCode: Int,
    val headers: dynamic,
    val body: String = "",
) {
    internal fun fromJs() = MockzillaHttpResponse(
        HttpStatusCode.fromValue(statusCode),
        mapFrom(headers),
        body
    )
}

@JsExport
data class JsPartialMockzillaHttpResponse(
    val statusCode: Int?,
    val headers: dynamic?,
    val body: String?,
) {
    internal fun fromJs() = PartialMockzillaHttpResponse(
        statusCode?.let { HttpStatusCode.fromValue(statusCode) },
        headers?.let { mapFrom(headers) },
        body
    )
}

@OptIn(ExperimentalWasmJsInterop::class)
@JsExport
data class JsMockzillaConfig(
    val endpoints: JsArray<JsEndpointConfiguration>,
    val logLevel: String,
) {

    @OptIn(ExperimentalWasmJsInterop::class)
    internal fun fromJs() = MockzillaConfig(
        port = MockzillaConfig.Builder.defaultPort,
        endpoints = endpoints.map { it.fromJs() },
        isRelease = false,
        localhostOnly = false,
        logLevel = when (logLevel) {
            "Assert" -> MockzillaConfig.LogLevel.Assert
            "Debug" -> MockzillaConfig.LogLevel.Debug
            "Error" -> MockzillaConfig.LogLevel.Error
            "Info" -> MockzillaConfig.LogLevel.Info
            "Verbose" -> MockzillaConfig.LogLevel.Verbose
            "Warn" -> MockzillaConfig.LogLevel.Warn
            else -> MockzillaConfig.LogLevel.Verbose
        },
        releaseModeConfig = MockzillaConfig.ReleaseModeConfig(),
        isNetworkDiscoveryEnabled = false,
        additionalLogWriters = emptyList()
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class.js != other::class.js) return false

        other as JsMockzillaConfig

        if (!endpoints.contentEquals(other.endpoints)) return false
        if (logLevel != other.logLevel) return false

        return true
    }

    override fun hashCode(): Int {
        var result = endpoints.contentHashCode()
        result = 31 * result + logLevel.hashCode()
        return result
    }
}

@JsExport
data class JsMockzillaRuntimeParams(
    val config: JsMockzillaConfig,
    val ip: String,
    val mockBaseUrl: String,
    val apiBaseUrl: String,
    val port: Int,
    val mockzillaVersion: String
)

fun MockzillaRuntimeParams.toJs(config: JsMockzillaConfig) = JsMockzillaRuntimeParams(
    config = config,
    ip = ip,
    mockBaseUrl = mockBaseUrl,
    apiBaseUrl = apiBaseUrl,
    port = port,
    mockzillaVersion = mockzillaVersion
)

@OptIn(DelicateCoroutinesApi::class)
@JsExport
fun startMockzillaJs(
    appName: String,
    appVersion: String,
    config: JsMockzillaConfig,
): Promise<JsMockzillaRuntimeParams> = GlobalScope.promise {
    startMockzilla(appName, appVersion, config.fromJs()).toJs(config)
}

@JsExport
fun stopMockzilla() = GlobalScope.promise {
    stopServer()
}
