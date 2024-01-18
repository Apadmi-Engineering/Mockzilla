package com.apadmi.mockzilla.lib.internal.controller

import com.apadmi.mockzilla.lib.internal.models.MockDataEntryDto
import com.apadmi.mockzilla.lib.internal.models.SetOrDont
import com.apadmi.mockzilla.lib.internal.service.LocalCacheService
import com.apadmi.mockzilla.lib.internal.service.MockServerMonitor
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.testutils.TestMockzillaHttpRequest

import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig
import io.ktor.http.*
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

@Suppress("MAGIC_NUMBER", "TOO_LONG_FUNCTION")
class LocalMockControllerTests {
    @Mock
    private val localCacheServiceMock = mock(classOf<LocalCacheService>())

    @Mock
    private val mockServerMonitorMock = mock(classOf<MockServerMonitor>())
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
            localCacheServiceMock,
            mockServerMonitorMock,
            dummyEndpoints,
            Logger(StaticConfig())
        )
        given(localCacheServiceMock).coroutine { getLocalCache("my-id") }.thenReturn(null)

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
        val sut = LocalMockController(
            localCacheServiceMock,
            mockServerMonitorMock,
            dummyEndpoints,
            Logger(StaticConfig())
        )
        given(localCacheServiceMock).coroutine { getLocalCache("my-id") }.thenReturn(
            MockDataEntryDto(
                key = "my-id",
                name = "my-id",
                shouldFail = SetOrDont.Set(true),
                delayMs = SetOrDont.Set(0),
                headers = SetOrDont.Set(emptyMap()),
                defaultBody = SetOrDont.Set(""),
                defaultStatus = SetOrDont.Set(HttpStatusCode.OK),
                errorBody = SetOrDont.Set(""),
                errorStatus = SetOrDont.Set(HttpStatusCode.InternalServerError),
            )
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
