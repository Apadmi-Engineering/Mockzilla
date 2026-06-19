package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.internal.utils.createFileIoforTesting

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest

class LogBodyCacheServiceTests {
    @Test
    fun `storeAndFetchRequestBody - stores and retrieves body`() = runTest {
        val sut = LocalBodyCacheService(createFileIoforTesting())
        sut.storeRequestBody("id1", "request body content")
        assertEquals("request body content", sut.fetchRequestBody("id1"))
    }

    @Test
    fun `storeAndFetchResponseBody - stores and retrieves body`() = runTest {
        val sut = LocalBodyCacheService(createFileIoforTesting())
        sut.storeResponseBody("id1", "response body content")
        assertEquals("response body content", sut.fetchResponseBody("id1"))
    }

    @Test
    fun `fetchRequestBody - not stored - returns null`() = runTest {
        val sut = LocalBodyCacheService(createFileIoforTesting())
        assertNull(sut.fetchRequestBody("doesNotExist"))
    }

    @Test
    fun `fetchResponseBody - not stored - returns null`() = runTest {
        val sut = LocalBodyCacheService(createFileIoforTesting())
        assertNull(sut.fetchResponseBody("doesNotExist"))
    }

    @Test
    fun `evict - removes both body files`() = runTest {
        val sut = LocalBodyCacheService(createFileIoforTesting())
        sut.storeRequestBody("id1", "req")
        sut.storeResponseBody("id1", "res")
        sut.evict("id1")
        assertNull(sut.fetchRequestBody("id1"))
        assertNull(sut.fetchResponseBody("id1"))
    }

    @Test
    fun `evict - called when no files exist - does not throw`() = runTest {
        val sut = LocalBodyCacheService(createFileIoforTesting())
        sut.evict("nonExistent")
    }

    @Test
    fun `clearAll - removes all stored bodies`() = runTest {
        val sut = LocalBodyCacheService(createFileIoforTesting())
        sut.storeRequestBody("a", "req-a")
        sut.storeResponseBody("a", "res-a")
        sut.storeRequestBody("b", "req-b")
        sut.clearAll()
        assertNull(sut.fetchRequestBody("a"))
        assertNull(sut.fetchResponseBody("a"))
        assertNull(sut.fetchRequestBody("b"))
    }
}
