package com.apadmi.mockzilla.lib.internal.controller

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfigurationPatchRequestDto
import com.apadmi.mockzilla.lib.internal.service.LocalCacheService
import com.apadmi.mockzilla.lib.internal.service.MockServerMonitor
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse

import io.ktor.http.*
import io.mockative.*

import kotlin.test.*
import kotlinx.coroutines.test.runTest

@Suppress("MAGIC_NUMBER")
class ManagementApiControllerTests {
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
        val sut =
            ManagementApiController(dummyEndpoints, localCacheServiceMock, mockServerMonitorMock)

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
        val dummyCacheEntry = SerializableEndpointConfig.allNulls("my-id", "id").copy(
            defaultBody = "my cached value"
        )
        given(localCacheServiceMock).coroutine {
            getLocalCache("my-id")
        }.thenReturn(dummyCacheEntry)
        given(localCacheServiceMock).coroutine {
            getLocalCache("my-second-id")
        }.thenReturn(null)

        val sut =
            ManagementApiController(dummyEndpoints, localCacheServiceMock, mockServerMonitorMock)

        /* Run Test */
        val result = sut.getAllMockDataEntries()

        /* Verify */
        // TODO: Update in next PR with more sophisticated configuration system
        // assertEquals(
        // listOf(dummyCacheEntry, SerializableEndpointConfiguration.allNulls("my-second-id", "my-second-id")),
        // result
        // )
    }

    @Test
    fun `updateEntry - mismatch ids - throws exception`() = runTest {
        /* Setup */
        val sut =
            ManagementApiController(dummyEndpoints, localCacheServiceMock, mockServerMonitorMock)

        /* Run Test & Verify */
        val result = assertFails {
            sut.updateEntry(
                "another id",
                SerializableEndpointConfigurationPatchRequestDto.allUnset("id")
            )
        }
        assertTrue(result is IllegalStateException)
    }

    @Test
    fun `updateEntry - calls through`() = runTest {
        /* Setup */
        val sut =
            ManagementApiController(dummyEndpoints, localCacheServiceMock, mockServerMonitorMock)
        given(localCacheServiceMock).suspendFunction(localCacheServiceMock::updateLocalCache)
            .whenInvokedWith(any(), any()).thenReturn(SerializableEndpointConfig.allNulls("", ""))
        /* Run Test */
        sut.updateEntry(
            dummyEndpoints.first().key,
            SerializableEndpointConfigurationPatchRequestDto.allUnset(dummyEndpoints.first().key)
        )

        /* Verify */
        verify(localCacheServiceMock).coroutine {
            updateLocalCache(
                SerializableEndpointConfigurationPatchRequestDto.allUnset(dummyEndpoints.first().key),
                dummyEndpoints.first()
            )
        }.wasInvoked(1.time)
    }

    @Test
    fun `consumeLogEntries - calls through`() = runTest {
        /* Setup */
        val dummyEvent = LogEvent(
            timestamp = 3,
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
        val sut = ManagementApiController(
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
