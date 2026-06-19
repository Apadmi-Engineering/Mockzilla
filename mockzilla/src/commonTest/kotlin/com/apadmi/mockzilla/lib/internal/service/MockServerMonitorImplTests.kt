package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.utils.createFileIoforTesting

import io.ktor.http.HttpStatusCode

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
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

    private fun createSut() = MockServerMonitorImpl(LocalBodyCacheService(createFileIoforTesting()))

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
        val largeBody = "x".repeat(LocalBodyCacheService.bodySizeLimit + 1)
        val event = makeEvent(requestBody = largeBody)
        sut.log(event)
        val logs = sut.getLogsSince(null)
        assertEquals(1, logs.size)
        assertTrue(logs[0].isRequestBodyTruncated)
        assertEquals(LocalBodyCacheService.bodySizeLimit, logs[0].requestBody.length)
    }

    @Test
    fun `log - response body exceeds limit - truncated in memory and stored on disk`() = runTest {
        val sut = createSut()
        val largeBody = "y".repeat(LocalBodyCacheService.bodySizeLimit + 1)
        val event = makeEvent(responseBody = largeBody)
        sut.log(event)
        val logs = sut.getLogsSince(null)
        assertEquals(1, logs.size)
        assertTrue(logs[0].isResponseBodyTruncated)
        assertEquals(LocalBodyCacheService.bodySizeLimit, logs[0].responseBody.length)
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
    fun `getLogDetail - non-truncated event - returns event directly without disk read`() = runTest {
        val sut = createSut()
        val event = makeEvent(requestBody = "req", responseBody = "res")
        sut.log(event)
        val detail = sut.getLogDetail(event.id)
        assertNotNull(detail)
        assertFalse(detail.isRequestBodyTruncated)
        assertEquals("req", detail.requestBody)
    }

    @Test
    fun `getLogDetail - truncated event - returns enriched event from disk`() = runTest {
        val sut = createSut()
        val largeReq = "r".repeat(LocalBodyCacheService.bodySizeLimit + 1)
        val largeRes = "s".repeat(LocalBodyCacheService.bodySizeLimit + 1)
        val event = makeEvent(id = "enrich-me", requestBody = largeReq, responseBody = largeRes)
        sut.log(event)
        val detail = sut.getLogDetail("enrich-me")
        assertNotNull(detail)
        assertFalse(detail.isRequestBodyTruncated)
        assertFalse(detail.isResponseBodyTruncated)
        assertEquals(largeReq, detail.requestBody)
        assertEquals(largeRes, detail.responseBody)
    }

    @Test
    fun `getLogDetail - unknown id - returns null`() = runTest {
        val sut = createSut()
        assertNull(sut.getLogDetail("unknown"))
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
    fun `clearAllLogs - clears body files from disk`() = runTest {
        val fileIo = createFileIoforTesting()
        val store = LocalBodyCacheService(fileIo)
        val sut = MockServerMonitorImpl(store)
        val largeBody = "z".repeat(LocalBodyCacheService.bodySizeLimit + 1)
        val event = makeEvent(id = "disk-event", responseBody = largeBody)
        sut.log(event)
        sut.clearAllLogs()
        // After clear, disk files should be gone (fetching returns null)
        assertNull(store.fetchResponseBody("disk-event"))
    }
}
