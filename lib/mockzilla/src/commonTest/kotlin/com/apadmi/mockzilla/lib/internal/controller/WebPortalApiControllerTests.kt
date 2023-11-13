package com.apadmi.mockzilla.lib.internal.controller

import com.apadmi.mockzilla.lib.internal.models.GlobalOverridesDto
import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.models.toMockDataEntryForWeb
import com.apadmi.mockzilla.lib.internal.service.LocalCacheService
import com.apadmi.mockzilla.lib.internal.service.MockServerMonitor
import com.apadmi.mockzilla.lib.internal.utils.toMockDataEntry
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse

import io.ktor.http.*
import io.mockative.*

import kotlin.test.*
import kotlinx.coroutines.test.runTest

@Suppress("MAGIC_NUMBER")
class WebPortalApiControllerTests {
    @Mock
    private val localCacheServiceMock = mock(classOf<LocalCacheService>())

    @Mock
    private val mockServerMonitorMock = mock(classOf<MockServerMonitor>())
    private val dummyEndpoints = listOf(EndpointConfiguration.Builder("my-id")
        .setPatternMatcher { uri.endsWith("my-id") }
        .setDefaultHandler {
            MockzillaHttpResponse(
                statusCode = HttpStatusCode.Created,
                headers = mapOf("test-header" to "test-value"),
                body = "my response body"
            )
        }.build(),
        EndpointConfiguration.Builder("my-second-id")
            .setPatternMatcher { uri.endsWith("my-second-id") }
            .setDefaultHandler {
                MockzillaHttpResponse(
                    statusCode = HttpStatusCode.Created,
                    headers = mapOf("test-header" to "test-value"),
                    body = "my second response body"
                )
            }.build()
    )

    @Test
    fun `clearAllCaches - calls through`() = runTest {
        /* Setup */
        val sut = WebPortalApiController(dummyEndpoints, localCacheServiceMock, mockServerMonitorMock)

        /* Run Test */
        sut.clearAllCaches()

        /* Verify */
        verify(localCacheServiceMock).coroutine {
            clearAllCaches()
        }.wasInvoked(1.time)
    }

    @Test
    fun `getAllMockDataEntries - replaces cached data - calls through`() = runTest {
        /* Setup */
        val dummyCacheEntry = dummyEndpoints.first().toMockDataEntryForWeb().copy(
            defaultBody = "my cached value"
        )
        given(localCacheServiceMock).coroutine {
            getLocalCache("my-id")
        }.thenReturn(dummyCacheEntry)
        given(localCacheServiceMock).coroutine {
            getLocalCache("my-second-id")
        }.thenReturn(null)
        val sut = WebPortalApiController(dummyEndpoints, localCacheServiceMock, mockServerMonitorMock)

        /* Run Test */
        val result = sut.getAllMockDataEntries()

        /* Verify */
        assertEquals(
            listOf(dummyCacheEntry, dummyEndpoints[1].toMockDataEntryForWeb()),
            result
        )
    }

    @Test
    fun `updateEntry - mismatch ids - throws exception`() = runTest {
        /* Setup */
        val sut = WebPortalApiController(dummyEndpoints, localCacheServiceMock, mockServerMonitorMock)

        /* Run Test & Verify */
        val result = assertFails {
            sut.updateEntry(
                "another id",
                dummyEndpoints.first().toMockDataEntryForWeb()
            )
        }
        assertTrue(result is IllegalStateException)
    }

    @Test
    fun `updateEntry - calls through`() = runTest {
        /* Setup */
        val sut = WebPortalApiController(dummyEndpoints, localCacheServiceMock, mockServerMonitorMock)

        /* Run Test */
        sut.updateEntry(
            dummyEndpoints.first().key,
            dummyEndpoints.first().toMockDataEntryForWeb()
        )

        /* Verify */
        verify(localCacheServiceMock).coroutine {
            updateLocalCache(dummyEndpoints.first().toMockDataEntryForWeb())
        }.wasInvoked(1.time)
    }

    @Test
    fun `updateGlobalOverrides - calls through`() = runTest {
        /* Setup */
        val sut = WebPortalApiController(dummyEndpoints, localCacheServiceMock, mockServerMonitorMock)

        /* Run Test */
        sut.updateGlobalOverrides(GlobalOverridesDto(11, 12, 13))

        /* Verify */
        verify(localCacheServiceMock).coroutine {
            updateGlobalOverrides(
                GlobalOverridesDto(11, 12, 13)
            )
        }.wasInvoked(1.time)
    }

    @Test
    fun `getGlobalOverrides - calls through`() = runTest {
        /* Setup */
        given(localCacheServiceMock).coroutine {
            getGlobalOverrides()
        }.thenReturn(GlobalOverridesDto(11, 12, 13))
        val sut = WebPortalApiController(dummyEndpoints, localCacheServiceMock, mockServerMonitorMock)

        /* Run Test */
        val result = sut.getGlobalOverrides()

        /* Verify */
        assertEquals(
            GlobalOverridesDto(11, 12, 13),
            result
        )
    }

    @Test
    fun `getGlobalOverrides - is null - calls through`() = runTest {
        /* Setup */
        given(localCacheServiceMock).coroutine {
            getGlobalOverrides()
        }.thenReturn(null)
        val sut = WebPortalApiController(
            dummyEndpoints,
            localCacheServiceMock,
            mockServerMonitorMock
        )

        /* Run Test */
        val result = sut.getGlobalOverrides()

        /* Verify */
        assertNull(result)
    }

    @Test
    fun `consumeLogEntries - calls through`() = runTest {
        /* Setup */
        val dummyEvent = LogEvent(timestamp = 3,
            url = "url",
            requestBody = "body",
            requestHeaders = mapOf("a" to "b"),
            responseBody = "response body",
            responseHeaders = mapOf("c" to "d"),
            status = HttpStatusCode.BadGateway,
            delay = 4,
            method = "method",
            isIntendedFailure = false
        )
        given(mockServerMonitorMock).coroutine {
            consumeCurrentLogs()
        }.thenReturn(listOf(dummyEvent))
        val sut = WebPortalApiController(
            dummyEndpoints,
            localCacheServiceMock,
            mockServerMonitorMock
        )

        /* Run Test */
        val result = sut.consumeLogEntries()

        /* Verify */
        assertEquals(listOf(dummyEvent), result)
    }
}
