package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.utils.createFileIoforTesting

import io.ktor.http.HttpStatusCode

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest

@Suppress("MAGIC_NUMBER")
class LogBodyCacheServiceTests {
    private fun makeEvent(
        id: String = "test-id",
        timestamp: Long = 1000L,
        requestBody: String = "request body",
        responseBody: String = "response body",
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

    @Test
    fun `storeFullEntry and fetchFullEntry - stores and retrieves full event`() = runTest {
        val sut = LocalBodyCacheService(createFileIoforTesting())
        val event = makeEvent()
        sut.storeFullEntry(event)
        val retrieved = sut.fetchFullEntry(event.id)
        assertNotNull(retrieved)
        assertEquals(event.requestBody, retrieved.requestBody)
        assertEquals(event.responseBody, retrieved.responseBody)
        assertEquals(event.url, retrieved.url)
    }

    @Test
    fun `fetchFullEntry - not stored - returns null`() = runTest {
        val sut = LocalBodyCacheService(createFileIoforTesting())
        assertNull(sut.fetchFullEntry("doesNotExist"))
    }

    @Test
    fun `deleteOldFullEntries - removes entries older than threshold`() = runTest {
        val sut = LocalBodyCacheService(createFileIoforTesting())
        val old = makeEvent(id = "old", timestamp = 1000L)
        val recent = makeEvent(id = "recent", timestamp = 5000L)
        sut.storeFullEntry(old)
        sut.storeFullEntry(recent)
        sut.deleteOldFullEntries(olderThan = 3000L)
        assertNull(sut.fetchFullEntry("old"))
        assertNotNull(sut.fetchFullEntry("recent"))
    }

    @Test
    fun `deleteOldFullEntries - threshold equals timestamp - does not delete entry at threshold`() = runTest {
        val sut = LocalBodyCacheService(createFileIoforTesting())
        val event = makeEvent(id = "exact", timestamp = 3000L)
        sut.storeFullEntry(event)
        sut.deleteOldFullEntries(olderThan = 3000L)
        assertNotNull(sut.fetchFullEntry("exact"))
    }

    @Test
    fun `clearAll - removes all stored entries`() = runTest {
        val sut = LocalBodyCacheService(createFileIoforTesting())
        sut.storeFullEntry(makeEvent(id = "a"))
        sut.storeFullEntry(makeEvent(id = "b"))
        sut.clearAll()
        assertNull(sut.fetchFullEntry("a"))
        assertNull(sut.fetchFullEntry("b"))
    }
}
