package com.apadmi.mockzilla.lib.integration

import com.apadmi.mockzilla.lib.internal.models.*
import com.apadmi.mockzilla.lib.internal.utils.JsonProvider
import com.apadmi.mockzilla.lib.internal.utils.epochMillis
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.testutils.runIntegrationTest

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

import kotlin.math.abs
import kotlin.test.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Suppress(
    "TOO_LONG_FUNCTION",
    "MAGIC_NUMBER",
    "TOO_MANY_LINES_IN_LAMBDA"
)
class WebPortalApiIntegrationTests {
    @Test
    fun `GET mock-data - returns as expected`() = runIntegrationTest(
        MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .setDelayMillis(100)
            .addEndpoint(EndpointConfiguration.Builder("my-id")
                .setDefaultHandler {
                    MockzillaHttpResponse(
                        HttpStatusCode.Created,
                        emptyMap(),
                        "my body"
                    )
                }
                .setErrorHandler {
                    MockzillaHttpResponse(
                        HttpStatusCode.NotAcceptable,
                        emptyMap(),
                        "my error body"
                    )
                }
                .build()
            )
            .build()
    ) { params, _ ->
        /* Run Test */
        val response = HttpClient(CIO).get("${params.apiBaseUrl}/mock-data")

        /* Verify */
        assertEquals(
            HttpStatusCode.OK,
            response.status
        )
        assertEquals(
            JsonProvider.json.encodeToString(
                MockDataResponseDto(
                    listOf(
                        SerializableEndpointConfig.allNulls(
                            name = "my-id",
                            key = "my-id",
                        )
                    )
                )
            ),
            response.bodyAsText()
        )
    }

    @Test
    fun `GET mock-data presets - returns as expected`() = runIntegrationTest(
        MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .setDelayMillis(100)
            .addEndpoint(EndpointConfiguration.Builder("my-id")
                .configureDashboardOverrides {
                    addSuccessPreset(
                        MockzillaHttpResponse(
                            HttpStatusCode.Created,
                            emptyMap(),
                            "my body"
                        ), name = "Preset name", description = "Preset description"
                    )
                }
                .build()
            )
            .build()
    ) { params, _ ->
        /* Run Test */
        val response = HttpClient(CIO).get("${params.apiBaseUrl}/mock-data/my-id/presets")

        /* Verify */
        assertEquals(
            HttpStatusCode.OK,
            response.status
        )
        assertEquals(
            DashboardOptionsConfig(
                successPresets = listOf(
                    DashboardOverridePreset(
                        response = MockzillaHttpResponse(
                            HttpStatusCode.Created,
                            emptyMap(),
                            "my body"
                        ), name = "Preset name", description = "Preset description"
                    )
                ),
                errorPresets = emptyList()
            ),
            JsonProvider.json.decodeFromString<DashboardOptionsConfig>(response.bodyAsText())
        )
    }

    @Test
    fun `DELETE mock-data - clears caches as expected`() = runIntegrationTest(
        MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .addEndpoint(EndpointConfiguration.Builder("id"))
            .build(),
        setup = { cacheService ->
            cacheService.patchLocalCaches(
                mapOf(
                    EndpointConfiguration.Builder("id").build() to
                            SerializableEndpointPatchItemDto.allUnset("id")
                )
            )
        }
    ) { params, cacheService ->
        /* Run Test */
        val response = HttpClient(CIO).delete("${params.apiBaseUrl}/mock-data")

        /* Verify */
        assertNull(cacheService.getLocalCache("id"))
        assertEquals(
            HttpStatusCode.NoContent,
            response.status
        )
    }

    @Test
    fun `POST mock-data - updates cache as expected`() =
        runIntegrationTest(
            MockzillaConfig.Builder()
                .setPort(0)  // Port determined at runtime
                .addEndpoint(EndpointConfiguration.Builder("id"))
                .build()
        ) { params, cacheService ->
            /* Run Test */
            val response = HttpClient(CIO).patch(
                "${params.apiBaseUrl}/mock-data"
            ) {
                contentType(ContentType.Application.Json)
                setBody(
                    Json.encodeToString(
                        SerializableEndpointConfigPatchRequestDto(
                            SerializableEndpointPatchItemDto.allUnset("id").copy(
                                defaultBody = SetOrDont.Set("hello"),
                                defaultStatus = SetOrDont.Set(HttpStatusCode.NoContent),
                                headers = SetOrDont.Set(mapOf("Content-Type" to "application/json"))
                            )
                        )
                    )
                )
            }

            /* Verify */
            assertEquals(
                HttpStatusCode.Created,
                response.status
            )
            assertEquals(
                SerializableEndpointConfig.allNulls("id", "id").copy(
                    defaultBody = "hello",
                    defaultStatus = HttpStatusCode.NoContent,
                    defaultHeaders = mapOf("Content-Type" to "application/json")
                ),
                cacheService.getLocalCache("id")
            )
        }

    @Test
    fun `GET monitor-logs - returns as expected`() = runIntegrationTest(
        MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .setDelayMillis(24)
            .addEndpoint(
                EndpointConfiguration.Builder("my-id")
                    .setDefaultHandler {
                        MockzillaHttpResponse(
                            statusCode = HttpStatusCode.Created,
                            headers = mapOf("test-header" to "test-value"),
                            body = "my response body"
                        )
                    }.build()
            )
            .build()
    ) { params, _ ->
        // Make a call to the mock server to create a log entry
        val timestamp = epochMillis()
        HttpClient(CIO).get("${params.mockBaseUrl}/my-id")

        /* Run Test */
        val response = HttpClient(CIO).get("${params.apiBaseUrl}/monitor-logs")
        val responseBody: MonitorLogsResponse =
            JsonProvider.json.decodeFromString(response.bodyAsText())

        /* Verify */
        assertEquals(
            HttpStatusCode.OK,
            response.status
        )
        assertEquals(1, responseBody.logs.size)

        assertTrue(responseBody.logs
            .map { it.timestamp }
            .all { abs(it - timestamp) <= 300 })
        // Check entry is correct ignoring the timestamp and request headers
        assertEquals(
            listOf(
                LogEvent(
                    timestamp = 0,
                    url = "/local-mock/my-id",
                    requestBody = "",
                    requestHeaders = emptyMap(),
                    responseBody = "my response body",
                    responseHeaders = mapOf("test-header" to "test-value"),
                    status = HttpStatusCode.Created,
                    delay = 24,
                    method = "GET",
                    isIntendedFailure = false
                )
            ),
            responseBody.logs.map { it.copy(timestamp = 0, requestHeaders = emptyMap()) }
        )
    }
}
