package com.apadmi.mockzilla.lib.integration

import com.apadmi.mockzilla.lib.internal.models.*
import com.apadmi.mockzilla.lib.internal.utils.epochMillis
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.lib.service.MockzillaWeb
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
    @OptIn(MockzillaWeb::class)
    @Test
    fun `GET mock-data - returns as expected`() = runIntegrationTest(
        MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .setFailureProbabilityPercentage(50)
            .setMeanDelayMillis(100)
            .setDelayVarianceMillis(60)
            .addEndpoint(EndpointConfiguration.Builder("my-id")
                .setWebApiDefaultResponse(
                    MockzillaHttpResponse(
                        statusCode = HttpStatusCode.Created,
                        body = "my web api body"
                    )
                )
                .setWebApiErrorResponse(
                    MockzillaHttpResponse(
                        body = "my web api error body",
                        statusCode = HttpStatusCode.NotAcceptable
                    )
                )
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
            Json.encodeToString(listOf(MockDataEntryDto(
                name = "my-id",
                key = "my-id",
                failProbability = 50,
                delayMean = 100,
                delayVariance = 60,
                headers = emptyMap(),
                defaultBody = "my web api body",
                errorBody = "my web api error body",
                errorStatus = HttpStatusCode.NotAcceptable,
                defaultStatus = HttpStatusCode.Created
            ))),
            response.bodyAsText()
        )
    }

    @Test
    fun `DELETE mock-data - clears caches as expected`() = runIntegrationTest(
        MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .addEndpoint(EndpointConfiguration.Builder("id"))
            .build(),
        setup = { cacheService ->
            cacheService.updateLocalCache(
                EndpointConfiguration.Builder("id").build().toMockDataEntryForWeb()
            )
            cacheService.updateGlobalOverrides(GlobalOverridesDto(null, null, null))
        }
    ) { params, cacheService ->
        /* Run Test */
        val response = HttpClient(CIO).delete("${params.apiBaseUrl}/mock-data")

        /* Verify */
        assertNull(cacheService.getLocalCache("id"))
        assertNull(cacheService.getGlobalOverrides())
        assertEquals(
            HttpStatusCode.NoContent,
            response.status
        )
    }

    @Test
    fun `POST mock-data - updates cache as expected`() =
        runIntegrationTest(MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .addEndpoint(EndpointConfiguration.Builder("id"))
            .build()
        ) { params, cacheService ->
            /* Run Test */
            val response = HttpClient(CIO).post(
                "${params.apiBaseUrl}/mock-data/id") {
                contentType(ContentType.Application.Json)
                setBody(
                    Json.encodeToString(
                        EndpointConfiguration.Builder("id").setDefaultHandler {
                            MockzillaHttpResponse(
                                body = "hello",
                                statusCode = HttpStatusCode.NoContent,
                                headers = mapOf("Content-Type" to "application/json")
                            )
                        }.build()
                            .toMockDataEntryForWeb()
                    )
                )
            }

            /* Verify */
            assertEquals(
                EndpointConfiguration.Builder("id").setDefaultHandler {
                    MockzillaHttpResponse(
                        body = "hello",
                        statusCode = HttpStatusCode.NoContent,
                        headers = mapOf("Content-Type" to "application/json")
                    )
                }.build()
                    .toMockDataEntryForWeb(),
                cacheService.getLocalCache("id")
            )
            assertEquals(
                HttpStatusCode.NoContent,
                response.status
            )
        }

    @Test
    fun `GET global - returns as expected`() = runIntegrationTest(
        MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .addEndpoint(EndpointConfiguration.Builder("my-id").build())
            .build(),
        setup = { cacheService ->
            cacheService.updateGlobalOverrides(GlobalOverridesDto(11, 12, 13))
        }
    ) { params, _ ->
        /* Run Test */
        val response = HttpClient(CIO).get("${params.apiBaseUrl}/global")

        /* Verify */
        assertEquals(
            HttpStatusCode.OK,
            response.status
        )
        assertEquals(
            Json.encodeToString(GlobalOverridesDto(11, 12, 13)),
            response.bodyAsText()
        )
    }

    @Test
    fun `POST global - returns as expected`() = runIntegrationTest(
        MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .addEndpoint(EndpointConfiguration.Builder("my-id").build())
            .build()
    ) { params, cacheService ->
        /* Run Test */
        val response = HttpClient(CIO).post(
            "${params.apiBaseUrl}/global") {
            contentType(ContentType.Application.Json)
            setBody(
                Json.encodeToString(
                    GlobalOverridesDto(11, 12, 13)
                )
            )
        }

        /* Verify */
        assertEquals(
            HttpStatusCode.NoContent,
            response.status
        )
        assertEquals(
            cacheService.getGlobalOverrides(),
            GlobalOverridesDto(11, 12, 13)
        )
    }

    @Test
    fun `GET monitor-logs - returns as expected`() = runIntegrationTest(
        MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .setFailureProbabilityPercentage(0)
            .setMeanDelayMillis(24)
            .setDelayVarianceMillis(0)
            .addEndpoint(EndpointConfiguration.Builder("my-id")
                .setDefaultHandler {
                    MockzillaHttpResponse(
                        statusCode = HttpStatusCode.Created,
                        headers = mapOf("test-header" to "test-value"),
                        body = "my response body"
                    )
                }.build())
            .build()
    ) { params, _ ->
        // Make a call to the mock server to create a log entry
        val timestamp = epochMillis()
        HttpClient(CIO).get("${params.mockBaseUrl}/my-id")

        /* Run Test */
        val response = HttpClient(CIO).get("${params.apiBaseUrl}/monitor-logs")
        val responseBody: MonitorLogsResponse = Json.decodeFromString(response.bodyAsText())

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
            listOf(LogEvent(timestamp = 0,
                url = "/local-mock/my-id",
                requestBody = "",
                requestHeaders = emptyMap(),
                responseBody = "my response body",
                responseHeaders = mapOf("test-header" to "test-value"),
                status = HttpStatusCode.Created,
                delay = 24,
                method = "GET",
                isIntendedFailure = false
            )),
            responseBody.logs.map { it.copy(timestamp = 0, requestHeaders = emptyMap()) }
        )
    }
}
