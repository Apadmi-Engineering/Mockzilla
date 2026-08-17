package com.apadmi.mockzilla.lib.integration

import com.apadmi.mockzilla.lib.internal.models.*
import com.apadmi.mockzilla.lib.internal.utils.JsonProvider
import com.apadmi.mockzilla.lib.internal.utils.epochMillis
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.lib.models.PartialMockzillaHttpResponse
import com.apadmi.mockzilla.testutils.dummy
import com.apadmi.mockzilla.testutils.runIntegrationTest

import io.ktor.client.*
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
class ApiIntegrationTests {
    @Test
    fun `GET mock-data - returns as expected`() = runIntegrationTest(
        MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .setDelayMillis(100)
            .addEndpoint(EndpointConfiguration.Builder("my-id")
                .setName("My first endpoint")
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
        val response = HttpClient().get("${params.apiBaseUrl}/mock-data")

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
                            name = "My first endpoint",
                            key = "my-id",
                            versionCode = Int.MIN_VALUE
                        )
                    )
                )
            ),
            response.bodyAsText()
        )
    }

    @Test
    fun `GET dashboard-config presets - returns as expected`() = runIntegrationTest(
        MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .setDelayMillis(100)
            .addEndpoint(EndpointConfiguration.Builder("my-id")
                .configureDashboardOverrides {
                    addPreset(
                        MockzillaHttpResponse(
                            HttpStatusCode.Created,
                            emptyMap(),
                            "my body"
                        ), name = "Preset name",
                        description = "Preset description",
                        type = DashboardOverridePreset.Type.Informational
                    )
                }
                .build()
            )
            .build()
    ) { params, _ ->
        /* Run Test */
        val response = HttpClient().get("${params.apiBaseUrl}/mock-data/my-id/dashboard-config")

        /* Verify */
        assertEquals(
            HttpStatusCode.OK,
            response.status
        )
        assertEquals(
            DashboardOptionsConfig(
                presets = listOf(
                    DashboardOverridePreset(
                        response = PartialMockzillaHttpResponse(
                            HttpStatusCode.Created,
                            emptyMap(),
                            "my body"
                        ), name = "Preset name",
                        description = "Preset description",
                        type = DashboardOverridePreset.Type.Informational
                    )
                ),
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
        /* Setup */
        cacheService.patchLocalCaches(
            mapOf(
                EndpointConfiguration.Builder("id").build() to SerializableEndpointPatchItemDto(
                    EndpointConfiguration.Key("id"), delayMs = SetOrDont.Set(1442)
                )
            )
        )
        check(cacheService.getLocalCache(EndpointConfiguration.Key("id")) != null)

        /* Run Test */
        val response = HttpClient().delete("${params.apiBaseUrl}/mock-data/all")

        /* Verify */
        assertNull(cacheService.getLocalCache(EndpointConfiguration.Key("id")))
        assertEquals(
            HttpStatusCode.NoContent,
            response.status
        )
    }

    @Test
    fun `DELETE mock-data specific key - clears caches as expected`() = runIntegrationTest(
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
        /* Setup */
        cacheService.patchLocalCaches(
            mapOf(
                EndpointConfiguration.Builder("id").build() to SerializableEndpointPatchItemDto(
                    EndpointConfiguration.Key("id"), delayMs = SetOrDont.Set(1442)
                )
            )
        )
        check(cacheService.getLocalCache(EndpointConfiguration.Key("id")) != null)

        /* Run Test */
        val response = HttpClient().delete("${params.apiBaseUrl}/mock-data") {
            contentType(ContentType.Application.Json)
            setBody(
                Json.encodeToString(ClearCachesRequestDto(listOf(EndpointConfiguration.Key("id"))))
            )
        }

        /* Verify */
        assertNull(cacheService.getLocalCache(EndpointConfiguration.Key("id")))
        assertEquals(
            HttpStatusCode.NoContent,
            response.status
        )
    }

    @Test
    fun `PATCH mock-data - updates cache as expected`() =
        runIntegrationTest(
            MockzillaConfig.Builder()
                .setPort(0)  // Port determined at runtime
                .addEndpoint(EndpointConfiguration.Builder("id"))
                .build()
        ) { params, cacheService ->
            /* Run Test */
            val response = HttpClient().patch(
                "${params.apiBaseUrl}/mock-data"
            ) {
                contentType(ContentType.Application.Json)
                setBody(
                    Json.encodeToString(
                        SerializableEndpointConfigPatchRequestDto(
                            SerializableEndpointPatchItemDto.allUnset("id").copy(
                                appliedPresetOverride = SetOrDont.Set(
                                    DashboardOverridePreset.dummy(
                                        body = "hello",
                                        statusCode = HttpStatusCode.NoContent,
                                        headers = mapOf("Content-Type" to "application/json")
                                    )
                                ),
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
                SerializableEndpointConfig.allNulls("id", "id", Int.MIN_VALUE).copy(
                    appliedPresetOverride = DashboardOverridePreset.dummy(
                        body = "hello",
                        statusCode = HttpStatusCode.NoContent,
                        headers = mapOf("Content-Type" to "application/json")
                    )
                ),
                cacheService.getLocalCache(EndpointConfiguration.Key("id"))
            )
        }

    @Test
    fun `PUT mock-data by key - applies preset by name - updates cache as expected`() =
        runIntegrationTest(
            MockzillaConfig.Builder()
                .setPort(0)  // Port determined at runtime
                .addEndpoint(EndpointConfiguration.Builder("id")
                    .configureDashboardOverrides {
                        addPreset(
                            MockzillaHttpResponse(
                                HttpStatusCode.Created,
                                emptyMap(),
                                "preset body"
                            ),
                            name = "Preset name",
                            description = "Preset description",
                            type = DashboardOverridePreset.Type.Informational
                        )
                    }
                    .build())
                .build()
        ) { params, cacheService ->
            /* Run Test */
            val response = HttpClient().put(
                "${params.apiBaseUrl}/mock-data/id"
            ) {
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(ApplyPresetRequestDto("Preset name")))
            }

            /* Verify */
            val expectedPreset = DashboardOverridePreset(
                name = "Preset name",
                description = "Preset description",
                type = DashboardOverridePreset.Type.Informational,
                response = PartialMockzillaHttpResponse(
                    HttpStatusCode.Created,
                    emptyMap(),
                    "preset body"
                )
            )
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(
                SerializableEndpointConfig.allNulls("id", "id", Int.MIN_VALUE).copy(
                    appliedPresetOverride = expectedPreset
                ),
                JsonProvider.json.decodeFromString<SerializableEndpointConfig>(response.bodyAsText())
            )
            assertEquals(
                SerializableEndpointConfig.allNulls("id", "id", Int.MIN_VALUE).copy(
                    appliedPresetOverride = expectedPreset
                ),
                cacheService.getLocalCache(EndpointConfiguration.Key("id"))
            )
        }

    @Test
    fun `PUT mock-data by key - unknown preset name - returns 404`() =
        runIntegrationTest(
            MockzillaConfig.Builder()
                .setPort(0)  // Port determined at runtime
                .addEndpoint(EndpointConfiguration.Builder("id")
                    .configureDashboardOverrides {
                        addPreset(MockzillaHttpResponse(HttpStatusCode.Created, emptyMap(), "preset body"))
                    }
                    .build())
                .build()
        ) { params, cacheService ->
            /* Run Test */
            val response = HttpClient().put(
                "${params.apiBaseUrl}/mock-data/id"
            ) {
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(ApplyPresetRequestDto("no such preset")))
            }

            /* Verify */
            assertEquals(HttpStatusCode.NotFound, response.status)
            assertNull(cacheService.getLocalCache(EndpointConfiguration.Key("id")))
        }

    @Test
    fun `PUT mock-data by key - unknown endpoint key - returns 404`() =
        runIntegrationTest(
            MockzillaConfig.Builder()
                .setPort(0)  // Port determined at runtime
                .addEndpoint(EndpointConfiguration.Builder("id"))
                .build()
        ) { params, _ ->
            /* Run Test */
            val response = HttpClient().put(
                "${params.apiBaseUrl}/mock-data/does-not-exist"
            ) {
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(ApplyPresetRequestDto("whatever")))
            }

            /* Verify */
            assertEquals(HttpStatusCode.NotFound, response.status)
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
                            body = "resp"
                        )
                    }.build()
            )
            .build()
    ) { params, _ ->
        // Make a call to the mock server to create a log entry
        val timestamp = epochMillis()
        HttpClient().get("${params.mockBaseUrl}/my-id")

        /* Run Test */
        val response = HttpClient().get("${params.apiBaseUrl}/monitor-logs")
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
        // Check entry is correct ignoring the timestamp, id, and request headers
        assertEquals(
            listOf(
                LogEvent(
                    id = "",
                    timestamp = 0,
                    url = "/local-mock/my-id",
                    requestBody = "",
                    requestHeaders = emptyMap(),
                    responseBody = "resp",
                    responseHeaders = mapOf("test-header" to "test-value"),
                    status = HttpStatusCode.Created,
                    delay = 24,
                    method = "GET",
                    isIntendedFailure = false
                )
            ),
            responseBody.logs.map {
                it.copy(
                    id = "",
                    timestamp = 0,
                    requestHeaders = emptyMap(),
                    requestSizeBytes = null,
                    responseSizeBytes = null,
                )
            }
        )
    }

    @Test
    fun `GET monitor-logs poll - returns logs since timestamp`() = runIntegrationTest(
        MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .setDelayMillis(24)
            .addEndpoint(
                EndpointConfiguration.Builder("my-id")
                    .setDefaultHandler {
                        MockzillaHttpResponse(
                            statusCode = HttpStatusCode.Created,
                            headers = mapOf("test-header" to "test-value"),
                            body = "resp"
                        )
                    }.build()
            )
            .build()
    ) { params, _ ->
        // Record a timestamp then make a call to generate a log entry after it
        val since = epochMillis()
        HttpClient().get("${params.mockBaseUrl}/my-id")

        /* Run Test */
        val response = HttpClient().get("${params.apiBaseUrl}/monitor-logs/poll?since=$since")
        val responseBody: MonitorLogsResponse =
            JsonProvider.json.decodeFromString(response.bodyAsText())

        /* Verify */
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(1, responseBody.logs.size)
        assertTrue(responseBody.logs.first().timestamp > since)
    }

    @Test
    fun `GET monitor-logs full-body - returns log detail`() = runIntegrationTest(
        MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .setDelayMillis(24)
            .addEndpoint(
                EndpointConfiguration.Builder("my-id")
                    .setDefaultHandler {
                        MockzillaHttpResponse(
                            statusCode = HttpStatusCode.Created,
                            headers = mapOf("test-header" to "test-value"),
                            body = "resp"
                        )
                    }.build()
            )
            .build()
    ) { params, _ ->
        // Make a call to the mock server to create a log entry
        HttpClient().get("${params.mockBaseUrl}/my-id")

        // Use poll (non-destructive) to retrieve the log id
        val pollResponse: MonitorLogsResponse = JsonProvider.json.decodeFromString(
            HttpClient().get("${params.apiBaseUrl}/monitor-logs/poll").bodyAsText()
        )
        val logId = pollResponse.logs.first().id

        /* Run Test */
        val response = HttpClient().get("${params.apiBaseUrl}/monitor-logs/$logId/full-body")
        val responseBody: LogEvent = JsonProvider.json.decodeFromString(response.bodyAsText())

        /* Verify */
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(logId, responseBody.id)
    }
}
