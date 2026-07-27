package com.apadmi.mockzilla.lib.internal.controller

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointPatchItemDto
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.lib.models.PartialMockzillaHttpResponse
import com.apadmi.mockzilla.testutils.fakes.FakeLocalCacheService
import com.apadmi.mockzilla.testutils.fakes.FakeMockServerMonitor

import io.ktor.http.*

import kotlin.test.*
import kotlinx.coroutines.test.runTest

@Suppress("MAGIC_NUMBER", "TOO_LONG_FUNCTION")
class ManagementApiControllerTests {
    private val dummyEndpoints = listOf(EndpointConfiguration.Builder("my-id")
        .setPatternMatcher { uri.endsWith("my-id") }
        .setName("id")
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
                addPreset(
                    MockzillaHttpResponse(
                        statusCode = HttpStatusCode.Created,
                        headers = mapOf("test-header" to "test-value"),
                        body = "my response body"
                    ),
                    name = "p1",
                    description = "p2",
                    type = DashboardOverridePreset.Type.Informational
                )
                addPreset(
                    MockzillaHttpResponse(
                        statusCode = HttpStatusCode.Created,
                        headers = mapOf("test-header" to "test-value"),
                        body = "my response body2"
                    ),
                    name = "Error Preset 1"
                )
                addPreset(
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
        val fakeLocalCacheService = FakeLocalCacheService()
        val sut =
            ManagementApiController(dummyEndpoints, fakeLocalCacheService, FakeMockServerMonitor())

        /* Run Test */
        sut.clearAllCaches()

        /* Verify */
        assertEquals(1, fakeLocalCacheService.clearAllCachesCallCount)
    }

    @Test
    fun `getAllMockDataEntries - replaces cached data - calls through`() = runTest {
        /* Setup */
        val dummyCacheEntry = SerializableEndpointConfig.allNulls("my-id", "id", 0).copy(
            appliedPresetOverride = DashboardOverridePreset(
                "", response = PartialMockzillaHttpResponse(body = "my cahced value"),
                description = "",
                type = null,
            ),
        )

        val fakeLocalCacheService = FakeLocalCacheService(
            mapOf(
                dummyCacheEntry.key to dummyCacheEntry,
                EndpointConfiguration.Key("my-second-id") to null
            )
        )

        val sut =
            ManagementApiController(dummyEndpoints, fakeLocalCacheService, FakeMockServerMonitor())

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
            ManagementApiController(dummyEndpoints, FakeLocalCacheService(), FakeMockServerMonitor())

        /* Run Test & Verify */
        val result = assertFails {
            sut.patchEntries(listOf(SerializableEndpointPatchItemDto.allUnset("random id invalid")))
        }
        assertTrue(result is IllegalStateException)
    }

    @Test
    fun `updateEntry - calls through`() = runTest {
        /* Setup */
        val fakeLocalCacheService = FakeLocalCacheService()
        val sut =
            ManagementApiController(dummyEndpoints, fakeLocalCacheService, FakeMockServerMonitor())

        /* Run Test */
        sut.patchEntries(
            listOf(SerializableEndpointPatchItemDto.allUnset(dummyEndpoints.first().key))
        )

        /* Verify */
        assertEquals(mapOf(
            dummyEndpoints.first() to
                    SerializableEndpointPatchItemDto.allUnset(dummyEndpoints.first().key)
        ), fakeLocalCacheService.patchLocalCachesArgument)
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
        val sut = ManagementApiController(
            dummyEndpoints,
            FakeLocalCacheService(),
            FakeMockServerMonitor(listOf(dummyEvent))
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
            FakeLocalCacheService(),
            FakeMockServerMonitor()
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
            FakeLocalCacheService(),
            FakeMockServerMonitor()
        )

        /* Run Test */
        val result = sut.getDashboardConfig(EndpointConfiguration.Key("my-second-id"))

        /* Verify */
        assertEquals(
            DashboardOptionsConfig(
                presets = listOf(
                    DashboardOverridePreset(
                        response = PartialMockzillaHttpResponse(
                            statusCode = HttpStatusCode.Created,
                            headers = mapOf("test-header" to "test-value"),
                            body = "my response body"
                        ), name = "p1",
                        description = "p2",
                        type = DashboardOverridePreset.Type.Informational
                    ),
                    DashboardOverridePreset(
                        response = PartialMockzillaHttpResponse(
                            statusCode = HttpStatusCode.Created,
                            headers = mapOf("test-header" to "test-value"),
                            body = "my response body2"
                        ),
                        name = "Error Preset 1",
                        description = null,
                        type = null
                    ),
                    DashboardOverridePreset(
                        response = PartialMockzillaHttpResponse(
                            statusCode = HttpStatusCode.Created,
                            headers = mapOf("test-header" to "test-value"),
                            body = "my response body3"
                        ),
                        name = "Preset 3",
                        description = null,
                        type = null
                    )
                )
            ),
            result
        )
    }

    @Test
    fun `getLogsSince - filters out older logs`() = runTest {
        /* Setup */
        val oldEvent = LogEvent(
            timestamp = 1,
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
        val matchingEvent = LogEvent(
            timestamp = 5,
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
        val newerEvent = LogEvent(
            timestamp = 10,
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
        val sut = ManagementApiController(
            dummyEndpoints,
            FakeLocalCacheService(),
            FakeMockServerMonitor(allLogEvents = listOf(oldEvent, matchingEvent, newerEvent))
        )

        /* Run Test */
        val result = sut.getLogsSince(3L)

        /* Verify */
        assertEquals(listOf(matchingEvent, newerEvent), result)
    }

    @Test
    fun `getLogsSince - null since - returns all logs`() = runTest {
        /* Setup */
        val oldEvent = LogEvent(
            timestamp = 1,
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
        val newerEvent = LogEvent(
            timestamp = 5,
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
        val sut = ManagementApiController(
            dummyEndpoints,
            FakeLocalCacheService(),
            FakeMockServerMonitor(allLogEvents = listOf(oldEvent, newerEvent))
        )

        /* Run Test */
        val result = sut.getLogsSince(null)

        /* Verify */
        assertEquals(listOf(oldEvent, newerEvent), result)
    }

    @Test
    fun `getLogsSince - no logs newer than timestamp - returns empty list`() = runTest {
        /* Setup */
        val oldEvent = LogEvent(
            timestamp = 1,
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
        val sut = ManagementApiController(
            dummyEndpoints,
            FakeLocalCacheService(),
            FakeMockServerMonitor(allLogEvents = listOf(oldEvent))
        )

        /* Run Test */
        val result = sut.getLogsSince(Long.MAX_VALUE)

        /* Verify */
        assertEquals(emptyList(), result)
    }

    @Test
    fun `onClientSessionStart - calls through`() = runTest {
        /* Setup */
        val fakeMockServerMonitor = FakeMockServerMonitor()
        val sut = ManagementApiController(
            dummyEndpoints,
            FakeLocalCacheService(),
            fakeMockServerMonitor
        )

        /* Run Test */
        sut.onClientSessionStart(42L)

        /* Verify */
        assertEquals(42L, fakeMockServerMonitor.onClientSessionStartArgument)
    }

    @Test
    fun `getFullBodyLogDetail - truncated event - returns full body from disk`() = runTest {
        /* Setup */
        val logId = "test-log-id"
        val truncatedInMemoryEvent = LogEvent(
            id = logId,
            timestamp = 3,
            url = "url",
            requestBody = "truncated...",
            requestHeaders = mapOf("a" to "b"),
            responseBody = "response body",
            responseHeaders = mapOf("c" to "d"),
            status = HttpStatusCode.BadGateway,
            delay = 4,
            method = "method",
            isIntendedFailure = false,
            isRequestBodyTruncated = true
        )
        val fullEventFromDisk = LogEvent(
            id = logId,
            timestamp = 3,
            url = "url",
            requestBody = "full request body content",
            requestHeaders = mapOf("a" to "b"),
            responseBody = "response body",
            responseHeaders = mapOf("c" to "d"),
            status = HttpStatusCode.BadGateway,
            delay = 4,
            method = "method",
            isIntendedFailure = false
        )
        val sut = ManagementApiController(
            dummyEndpoints,
            FakeLocalCacheService(),
            FakeMockServerMonitor(
                allLogEvents = listOf(truncatedInMemoryEvent),
                diskBodyCache = mapOf(logId to fullEventFromDisk)
            )
        )

        /* Run Test */
        val result = sut.getFullBodyLogDetail(logId)

        /* Verify */
        assertEquals(fullEventFromDisk, result)
    }

    @Test
    fun `getFullBodyLogDetail - not found - returns null`() = runTest {
        /* Setup */
        val sut = ManagementApiController(
            dummyEndpoints,
            FakeLocalCacheService(),
            FakeMockServerMonitor()
        )

        /* Run Test */
        val result = sut.getFullBodyLogDetail("log-id")

        /* Verify */
        assertNull(result)
    }
}
