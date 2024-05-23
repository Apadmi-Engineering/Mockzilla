package com.apadmi.mockzilla.lib.internal.controller

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointPatchItemDto
import com.apadmi.mockzilla.lib.internal.service.LocalCacheService
import com.apadmi.mockzilla.lib.internal.service.MockServerMonitor
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse

import io.ktor.http.*
import io.mockative.*

import kotlin.test.*
import kotlinx.coroutines.test.runTest

@Suppress("MAGIC_NUMBER", "TOO_LONG_FUNCTION")
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
            .configureDashboardOverrides {
                addSuccessPreset(
                    MockzillaHttpResponse(
                        statusCode = HttpStatusCode.Created,
                        headers = mapOf("test-header" to "test-value"),
                        body = "my response body"
                    ), name = "p1", description = "p2"
                )
                addErrorPreset(
                    MockzillaHttpResponse(
                        statusCode = HttpStatusCode.Created,
                        headers = mapOf("test-header" to "test-value"),
                        body = "my response body2"
                    )
                )
                addSuccessPreset(
                    MockzillaHttpResponse(
                        statusCode = HttpStatusCode.Created,
                        headers = mapOf("test-header" to "test-value"),
                        body = "my response body3"
                    )
                )
            }
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
        val dummyCacheEntry = SerializableEndpointConfig.allNulls("my-id", "id", 0).copy(
            defaultBody = "my cached value"
        )
        given(localCacheServiceMock).coroutine {
            getLocalCache(dummyCacheEntry.key)
        }.thenReturn(dummyCacheEntry)
        given(localCacheServiceMock).coroutine {
            getLocalCache(EndpointConfiguration.Key("my-second-id"))
        }.thenReturn(null)

        val sut =
            ManagementApiController(dummyEndpoints, localCacheServiceMock, mockServerMonitorMock)

        /* Run Test */
        val result = sut.getAllMockDataEntries()

        /* Verify */
        assertEquals(
            listOf(
                dummyCacheEntry,
                SerializableEndpointConfig.allNulls("my-second-id", "my-second-id", Int.MIN_VALUE)
            ),
            result
        )
    }

    @Test
    fun `updateEntry - mismatch ids - throws exception`() = runTest {
        /* Setup */
        val sut =
            ManagementApiController(dummyEndpoints, localCacheServiceMock, mockServerMonitorMock)

        /* Run Test & Verify */
        val result = assertFails {
            sut.patchEntries(listOf(SerializableEndpointPatchItemDto.allUnset("random id invalid")))
        }
        assertTrue(result is IllegalStateException)
    }

    @Test
    fun `updateEntry - calls through`() = runTest {
        /* Setup */
        val sut =
            ManagementApiController(dummyEndpoints, localCacheServiceMock, mockServerMonitorMock)
        given(localCacheServiceMock).suspendFunction(localCacheServiceMock::patchLocalCaches)
            .whenInvokedWith(any()).thenReturn(Unit)
        /* Run Test */
        sut.patchEntries(
            listOf(SerializableEndpointPatchItemDto.allUnset(dummyEndpoints.first().key))
        )

        /* Verify */
        verify(localCacheServiceMock).coroutine {
            patchLocalCaches(
                mapOf(dummyEndpoints.first() to
                        SerializableEndpointPatchItemDto.allUnset(dummyEndpoints.first().key))
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

    @Test
    fun `getDashboardConfig - invalid key - throws exception`() {
        /* Setup */
        val sut = ManagementApiController(
            dummyEndpoints,
            localCacheServiceMock,
            mockServerMonitorMock
        )

        /* Run test & Verify */
        assertFails {
            sut.getDashboardConfig(EndpointConfiguration.Key("random key"))
        }
    }

    @Test
    fun `getDashboardConfig - valid key - returns correct presets`() {
        /* Setup */
        val sut = ManagementApiController(
            dummyEndpoints,
            localCacheServiceMock,
            mockServerMonitorMock
        )

        /* Run Test */
        val result = sut.getDashboardConfig(EndpointConfiguration.Key("my-second-id"))

        /* Verify */
        assertEquals(
            DashboardOptionsConfig(
                errorPresets = listOf(
                    DashboardOverridePreset(
                        response = MockzillaHttpResponse(
                            statusCode = HttpStatusCode.Created,
                            headers = mapOf("test-header" to "test-value"),
                            body = "my response body2"
                        ), name = "Error Preset 1",
                        description = null
                    )
                ),
                successPresets = listOf(
                    DashboardOverridePreset(
                        response = MockzillaHttpResponse(
                            statusCode = HttpStatusCode.Created,
                            headers = mapOf("test-header" to "test-value"),
                            body = "my response body"
                        ), name = "p1", description = "p2"
                    ), DashboardOverridePreset(
                        response = MockzillaHttpResponse(
                            statusCode = HttpStatusCode.Created,
                            headers = mapOf("test-header" to "test-value"),
                            body = "my response body3"
                        ),
                        name = "Preset 2",
                        description = null
                    )
                )
            ),
            result
        )
    }
}
