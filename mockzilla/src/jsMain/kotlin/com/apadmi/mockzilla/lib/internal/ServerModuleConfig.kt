package com.apadmi.mockzilla.lib.internal

import com.apadmi.mockzilla.lib.internal.di.DependencyInjector
import com.apadmi.mockzilla.lib.internal.models.ClearCachesRequestDto
import com.apadmi.mockzilla.lib.internal.models.MockDataResponseDto
import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfigPatchRequestDto
import com.apadmi.mockzilla.lib.internal.msw.Msw
import com.apadmi.mockzilla.lib.internal.msw.RestHandler
import com.apadmi.mockzilla.lib.internal.utils.CorsUtils
import com.apadmi.mockzilla.lib.internal.utils.JsMockzillaRequest
import com.apadmi.mockzilla.lib.internal.utils.JsonProvider
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse

import co.touchlab.kermit.Logger
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.decodeURLPart
import org.w3c.fetch.Headers
import org.w3c.fetch.Request as JsRequest
import org.w3c.fetch.Response as JsResponse
import org.w3c.fetch.ResponseInit

import kotlin.js.Promise
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asPromise
import kotlinx.coroutines.async
import kotlinx.coroutines.await

private val jsonHeader = mapOf(HttpHeaders.ContentType to "application/json")

// JS response throws an error if the body isn't null for these response codes
private val nullBodyStatuses = listOf(
    HttpStatusCode.SwitchingProtocols,
    HttpStatusCode.NoContent,
    HttpStatusCode.ResetContent,
    HttpStatusCode.NotModified
)

@OptIn(ExperimentalWasmJsInterop::class)
private fun MockzillaHttpResponse.toJsResponse() = JsResponse(
    body.takeUnless { it.isBlank() && statusCode in nullBodyStatuses } as? JsAny, ResponseInit(
        status = statusCode.value.toShort(),
        statusText = statusCode.description,
        Headers().also {
            headers.forEach { (key, value) ->
                it.append(key, value)
            }
            it.append("Connection", "close")
        }
    ))

private fun JsRequest.safeResponse(
    logger: Logger,
    scope: CoroutineScope,
    block: suspend (JsRequest) -> MockzillaHttpResponse,
): Promise<JsResponse> = scope.async {
    try {
        block(this@safeResponse).toJsResponse()
    } catch (e: Exception) {
        logger.e("Unexpected exception", e)

        MockzillaHttpResponse(
            statusCode = HttpStatusCode.InternalServerError,
            body = e.stackTraceToString(),
        ).toJsResponse()
    }
}.asPromise()
@Suppress("TOO_LONG_FUNCTION")
internal fun configureEndpoints(
    di: DependencyInjector,
    baseUrl: String,
    scope: CoroutineScope
): Array<out RestHandler> = arrayOf(
    Msw.http.all("$baseUrl/local-mock/*") { info ->
        di.logger.i { "Responding to ${info.request.method}: ${info.request.url}" }

        if (info.request.method.lowercase() == "options") {
            return@all Promise.resolve(
                MockzillaHttpResponse(
                    headers = CorsUtils.allowAllHeaders
                ).toJsResponse()
            )
        }

        info.request.safeResponse(di.logger, scope) { request ->
            di.localMockController.handleRequest(JsMockzillaRequest(request))
        }
    },

    Msw.http.options("$baseUrl/api/*") {
        di.logger.v { "Handling OPTIONS request: ${it.request.url}" }
        Promise.resolve(MockzillaHttpResponse(headers = CorsUtils.allowAllHeaders).toJsResponse())
    },
    Msw.http.get("$baseUrl/api/meta") { info ->
        di.logger.v { "Handling GET meta: ${info.request.url}\n" }
        info.request.safeResponse(di.logger, scope) { request ->
            MockzillaHttpResponse(
                headers = CorsUtils.allowAllHeaders + jsonHeader,
                body = JsonProvider.json.encodeToString(di.metaData)
            )
        }
    },
    Msw.http.get("$baseUrl/api/mock-data") { info ->
        di.logger.v { "Handling GET mock-data: ${info.request.url}" }
        info.request.safeResponse(di.logger, scope) { request ->
            MockzillaHttpResponse(
                headers = CorsUtils.allowAllHeaders + jsonHeader,
                body = JsonProvider.json.encodeToString(
                    MockDataResponseDto(
                        di.managementApiController.getAllMockDataEntries()
                    )
                )
            )
        }
    },
    Msw.http.get("$baseUrl/api/mock-data/*/dashboard-config") { info ->
        di.logger.v { "Handling GET mock-data presets: ${info.request.url}" }
        val keyRegex = ".*/mock-data/(.*)/dashboard-config".toRegex()
        val key = keyRegex.matchEntire(info.request.url)?.groupValues?.last()
            ?.decodeURLPart()
            ?: throw IllegalStateException("Missing key in url ${info.request.url}")
        info.request.safeResponse(di.logger, scope) { request ->
            MockzillaHttpResponse(
                body = JsonProvider.json.encodeToString(
                    di.managementApiController.getDashboardConfig(EndpointConfiguration.Key(key))
                ),
                headers = CorsUtils.allowAllHeaders + jsonHeader
            )
        }
    },
    Msw.http.patch("$baseUrl/api/mock-data") { info ->
        di.logger.v { "Handling POST mock-data: ${info.request.url}" }
        info.request.safeResponse(di.logger, scope) { request ->
            val patches =
                JsonProvider.json.decodeFromString<SerializableEndpointConfigPatchRequestDto>(
                    request.text().await()
                ).entries
            di.managementApiController.patchEntries(patches)
            MockzillaHttpResponse(
                statusCode = HttpStatusCode.Created,
                headers = CorsUtils.allowAllHeaders + jsonHeader
            )
        }
    },
    Msw.http.delete("$baseUrl/api/mock-data/all") { info ->
        di.logger.v { "Handling DELETE mock-data: ${info.request.url}" }
        info.request.safeResponse(di.logger, scope) { call ->
            di.managementApiController.clearAllCaches()
            MockzillaHttpResponse(
                statusCode = HttpStatusCode.NoContent,
                headers = CorsUtils.allowAllHeaders + jsonHeader
            )
        }
    },
    Msw.http.delete("$baseUrl/api/mock-data") { info ->
        di.logger.v { "Handling DELETE mock-data: ${info.request.url}" }
        info.request.safeResponse(di.logger, scope) { request ->
            di.managementApiController.clearCache(
                JsonProvider.json.decodeFromString<ClearCachesRequestDto>(
                    request.text().await()
                ).keys
            )
            MockzillaHttpResponse(
                statusCode = HttpStatusCode.NoContent,
                headers = CorsUtils.allowAllHeaders
            )
        }
    },
    Msw.http.get("$baseUrl/api/monitor-logs") { info ->
        info.request.safeResponse(di.logger, scope) { request ->
            MockzillaHttpResponse(
                headers = CorsUtils.allowAllHeaders + jsonHeader,
                body = JsonProvider.json.encodeToString(
                    MonitorLogsResponse(
                        di.metaData.appPackage, di.managementApiController.consumeLogEntries()
                    )
                )
            )
        }
    }
)
