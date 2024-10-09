package com.apadmi.mockzilla.lib.internal.controller

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.testutils.TestMockzillaHttpRequest
import com.apadmi.mockzilla.testutils.fakes.FakeLocalCacheService
import com.apadmi.mockzilla.testutils.fakes.FakeMockServerMonitor

import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig
import io.ktor.http.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

@Suppress("MAGIC_NUMBER", "TOO_LONG_FUNCTION")
class LocalMockControllerTests {
    private val dummyEndpoints = listOf(
        EndpointConfiguration.Builder("my-id")
            .setPatternMatcher { uri.endsWith("my-id") }
            .setDefaultHandler {
                MockzillaHttpResponse(
                    statusCode = HttpStatusCode.Created,
                    headers = mapOf("test-header" to "test-value"),
                    body = "my response body"
                )
            }.build(),
    )

    @Test
    fun `GET response - no caches - 0 failure probability - succeeds`() = runTest {
        /* Setup */
        val sut = LocalMockController(
            FakeLocalCacheService(null),
            FakeMockServerMonitor(),
            dummyEndpoints,
            Logger(StaticConfig())
        )

        /* Run Test */
        val response = sut.handleRequest(
            TestMockzillaHttpRequest(
                uri = "http://example.com/local-mock/my-id",
                headers = emptyMap(),
                method = HttpMethod.Get
            )
        )

        /* Verify */
        assertEquals(
            MockzillaHttpResponse(
                statusCode = HttpStatusCode.Created,
                headers = mapOf("test-header" to "test-value"),
                body = "my response body"
            ), response
        )
    }

    @Test
    fun `GET response - shouldFail=true cached - fails`() = runTest {
        /* Setup */
        val localCacheValue = SerializableEndpointConfig(
            key = EndpointConfiguration.Key("my-id"),
            name = "my-id",
            versionCode = 0,
            shouldFail = true,
            delayMs = 0,
            defaultHeaders = emptyMap(),
            defaultBody = "",
            defaultStatus = HttpStatusCode.OK,
            errorBody = "",
            errorHeaders = emptyMap(),
            errorStatus = HttpStatusCode.InternalServerError,
        )

        val sut = LocalMockController(
            FakeLocalCacheService(mapOf(localCacheValue.key to localCacheValue)),
            FakeMockServerMonitor(),
            dummyEndpoints,
            Logger(StaticConfig())
        )

        /* Run Test */
        val response = sut.handleRequest(
            TestMockzillaHttpRequest(
                uri = "http://example.com/local-mock/my-id",
                headers = emptyMap(),
                method = HttpMethod.Get
            )
        )

        /* Verify */
        assertEquals(
            MockzillaHttpResponse(
                statusCode = HttpStatusCode.InternalServerError
            ), response
        )
    }
}
