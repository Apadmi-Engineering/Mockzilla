package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.utils.createFileIoforTesting

import io.ktor.http.HttpStatusCode

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

@Suppress("MAGIC_NUMBER")
class MockServerMonitorImplTests {
    private fun makeEvent(
        id: String = "test-id",
        timestamp: Long = 1L,
        requestBody: String = "",
        responseBody: String = "",
    ) = LogEvent(
        id = id,
        timestamp = timestamp,
        url = "https://example.com",
        requestBody = requestBody,
        requestHeaders = emptyMap(),
        responseHeaders = emptyMap(),
        responseBody = responseBody,
        status = HttpStatusCode.OK,
        delay = 0,
        method = "GET",
        isIntendedFailure = false,
    )

    private fun createBodyCacheService() = LocalBodyCacheService(createFileIoforTesting())
    private fun createSut(store: LocalBodyCacheService = createBodyCacheService()) = MockServerMonitorImpl(store)

    @Test
    fun `log - small body - stored in memory without truncation`() = runTest {
        val sut = createSut()
        val event = makeEvent(requestBody = "small", responseBody = "small")
        sut.log(event)
        val logs = sut.getLogsSince(null)
        assertEquals(1, logs.size)
        assertFalse(logs[0].isRequestBodyTruncated)
        assertFalse(logs[0].isResponseBodyTruncated)
        assertEquals("small", logs[0].requestBody)
    }

    @Test
    fun `log - request body exceeds limit - truncated in memory and stored on disk`() = runTest {
        val sut = createSut()
        val largeBody = "x".repeat(MockServerMonitorImpl.maxUntruncatedBodySizeBytes + 1)
        val event = makeEvent(requestBody = largeBody)
        sut.log(event)
        val logs = sut.getLogsSince(null)
        assertEquals(1, logs.size)
        assertTrue(logs[0].isRequestBodyTruncated)
        assertEquals(MockServerMonitorImpl.maxUntruncatedBodySizeBytes, logs[0].requestBody.length)
    }

    @Test
    fun `log - response body exceeds limit - truncated in memory and stored on disk`() = runTest {
        val sut = createSut()
        val largeBody = "y".repeat(MockServerMonitorImpl.maxUntruncatedBodySizeBytes + 1)
        val event = makeEvent(responseBody = largeBody)
        sut.log(event)
        val logs = sut.getLogsSince(null)
        assertEquals(1, logs.size)
        assertTrue(logs[0].isResponseBodyTruncated)
        assertEquals(MockServerMonitorImpl.maxUntruncatedBodySizeBytes, logs[0].responseBody.length)
    }

    @Test
    fun `getLogsSince - null - returns all events`() = runTest {
        val sut = createSut()
        sut.log(makeEvent(id = "a", timestamp = 1))
        sut.log(makeEvent(id = "b", timestamp = 2))
        val logs = sut.getLogsSince(null)
        assertEquals(2, logs.size)
    }

    @Test
    fun `getLogsSince - with timestamp - returns only newer events`() = runTest {
        val sut = createSut()
        sut.log(makeEvent(id = "a", timestamp = 1))
        sut.log(makeEvent(id = "b", timestamp = 5))
        sut.log(makeEvent(id = "c", timestamp = 10))
        val logs = sut.getLogsSince(5)
        assertEquals(1, logs.size)
        assertEquals("c", logs[0].id)
    }

    @Test
    fun `clearAllLogs - empties in-memory buffer`() = runTest {
        val sut = createSut()
        sut.log(makeEvent(id = "a"))
        sut.log(makeEvent(id = "b"))
        sut.clearAllLogs()
        assertEquals(0, sut.getLogsSince(null).size)
    }

    @Test
    fun `clearAllLogs - clears full-entry files from disk`() = runTest {
        val store = createBodyCacheService()
        val sut = createSut(store)
        val largeBody = "z".repeat(MockServerMonitorImpl.maxUntruncatedBodySizeBytes + 1)
        val event = makeEvent(id = "disk-event", responseBody = largeBody)
        sut.log(event)
        sut.clearAllLogs()
        // After clear, full-entry file should be gone
        assertNull(store.fetchFullEntry("disk-event"))
    }
}
