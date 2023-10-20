package com.apadmi.mockzilla.lib.integration

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.testutils.runIntegrationTest
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress(
    "TOO_LONG_FUNCTION",
    "MAGIC_NUMBER",
    "TOO_MANY_LINES_IN_LAMBDA"
)
class LocalMockIntegrationTests {
    @Test
    fun `GET local-mock - no endpoint match - returns as expected`() = runIntegrationTest(
        MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .setMeanDelayMillis(0)
            .setDelayVarianceMillis(0)
            .addEndpoint(EndpointConfiguration.Builder("id"))
            .build()
    ) { params, _ ->
        /* Run Test */
        val response = HttpClient(CIO).get("${params.mockBaseUrl}/does-not-exist")

        /* Verify */
        assertEquals(
            "Could not find endpoint for request /local-mock/does-not-exist",
            response.bodyAsText()
        )
    }

    @Test
    fun `GET local-mock - trailing slash - returns as expected`() =
        runIntegrationTest(MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .setMeanDelayMillis(0)
            .setDelayVarianceMillis(0)
            .addEndpoint(EndpointConfiguration.Builder("my-id")
                .setPatternMatcher { uri.endsWith("test/my-id/") }
                .setDefaultHandler {
                    MockzillaHttpResponse(
                        statusCode = HttpStatusCode.Created,
                        body = "my response body"
                    )
                }
            )
            .build()
        ) { params, _ ->
            /* Run Test */
            val response = HttpClient(CIO).get("${params.mockBaseUrl}/test/my-id/")

            /* Verify */
            assertEquals(
                HttpStatusCode.Created,
                response.status
            )
            assertEquals(
                "my response body",
                response.bodyAsText()
            )
        }

    @Test
    fun `GET local-mock - endpoint match - returns as expected`() =
        runIntegrationTest(MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .setMeanDelayMillis(0)
            .setDelayVarianceMillis(0)
            .addEndpoint(EndpointConfiguration.Builder("my-id")
                .setPatternMatcher { uri.endsWith("test/my-id") }
                .setDefaultHandler {
                    MockzillaHttpResponse(
                        statusCode = HttpStatusCode.Created,
                        headers = mapOf("test-header" to "test-value"),
                        body = body
                    )
                }
            )
            .build()
        ) { params, _ ->
            /* Run Test */
            val response = HttpClient(CIO).post("${params.mockBaseUrl}/test/my-id") {
                setBody("Body from request")
            }

            /* Verify */
            assertEquals(
                HttpStatusCode.Created,
                response.status
            )
            assertEquals(
                "Body from request",
                response.bodyAsText()
            )
            assertEquals(
                "test-value",
                response.headers["test-header"]
            )
            assertEquals(
                "close",
                response.headers["Connection"]
            )
        }

    @Test
    fun `GET local-mock - GET with Content-Type header but no body - returns as expected`() =
        runIntegrationTest(MockzillaConfig.Builder()
            .setPort(0)  // Port determined at runtime
            .setMeanDelayMillis(0)
            .setDelayVarianceMillis(0)
            .addEndpoint(EndpointConfiguration.Builder("my-id")
                .setPatternMatcher { uri.endsWith("test/my-id") }
                .setDefaultHandler {
                    MockzillaHttpResponse(
                        statusCode = HttpStatusCode.Created,
                        body = "my response body"
                    )
                }
            )
            .build()
        ) { params, _ ->
            /* Run Test */
            val response = HttpClient(CIO).get("${params.mockBaseUrl}/test/my-id") {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }

            /* Verify */
            assertEquals(
                HttpStatusCode.Created,
                response.status
            )
        }
}
