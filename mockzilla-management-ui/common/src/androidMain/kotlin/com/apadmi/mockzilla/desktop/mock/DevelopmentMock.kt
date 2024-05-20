@file:Suppress("MAGIC_NUMBER")

package com.apadmi.mockzilla.desktop.mock

import android.content.Context
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams
import com.apadmi.mockzilla.lib.startMockzilla
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.http.appendPathSegments
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

val endpointWithPresets = EndpointConfiguration.Builder(
    "endpoint-with-presets"
)
    .setDefaultHandler {
        MockzillaHttpResponse(headers = mapOf("Content-Type" to "application/json"), body = "{}")
    }
    .configureDashboardOverrides {
        addSuccessPreset(MockzillaHttpResponse(HttpStatusCode.Created, headers = emptyMap(), body = ""))
        addSuccessPreset(MockzillaHttpResponse(body = """{ "hello": "world" }"""))

        addErrorPreset(MockzillaHttpResponse(HttpStatusCode.ServiceUnavailable, body = "Go away"))
    }
    .build()

val responseCodesToCycleThrough = listOf(
    HttpStatusCode.OK,
    HttpStatusCode.Created,
    HttpStatusCode.Gone,
    HttpStatusCode.BadRequest,
    HttpStatusCode.Unauthorized,
    HttpStatusCode.Forbidden,
    HttpStatusCode(418, "I'm a teapot"),
    HttpStatusCode.TooEarly,
    HttpStatusCode.BadGateway,
    HttpStatusCode.InternalServerError,
)
val endpointThatCyclesThroughResponseCodes = EndpointConfiguration.Builder(
    "endpoint-that-cycles-through-response-codes"
).setDefaultHandler {
    val code = responseCodesToCycleThrough[currentResponseCode++ % responseCodesToCycleThrough.size]
    MockzillaHttpResponse(
        statusCode = code,
        headers = mapOf("Content-Type" to "application/json"),
        body = code.description
    )
}.build()
var currentResponseCode = 0

private suspend fun HttpClient.get(
    runtimeParams: MockzillaRuntimeParams,
    endpoint: EndpointConfiguration
) =
    get(runtimeParams.mockBaseUrl) {
        url { appendPathSegments(endpoint.key.raw) }
    }

// This is confusing because it gets a bit meta, but on Android we actually start an instance of the
// Mockzilla server within the Mockzilla UI so that we don't have to have it running separately for development
fun startDevelopmentMockzillaServer(context: Context) = startMockzilla(
    MockzillaConfig.Builder()
        .setPort(8080)
        .addEndpoint(endpointWithPresets)
        .addEndpoint(endpointThatCyclesThroughResponseCodes)
        .build(),
    context = context
)

suspend fun simulateUseOfDevelopmentServer(runtimeParams: MockzillaRuntimeParams) =
    withContext(Dispatchers.IO) {
        val client = HttpClient()

        client.get(runtimeParams, endpointWithPresets)
        repeat(responseCodesToCycleThrough.size * 2) {
            delay(it * 90L)
            client.get(runtimeParams, endpointThatCyclesThroughResponseCodes)
        }
    }
