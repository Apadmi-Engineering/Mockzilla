package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.internal.models.GlobalOverridesDto
import com.apadmi.mockzilla.lib.internal.models.toMockDataEntry
import com.apadmi.mockzilla.lib.internal.utils.createFileIoforTesting
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import com.apadmi.mockzilla.lib.service.MockzillaWeb

import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

@Suppress("MAGIC_NUMBER")
class LocalCacheServiceTests {
    @Test
    fun `getLocalCache - doesnt exist - returns null`() = runTest {
        /* Setup */
        val sut = LocalCacheServiceImpl(createFileIoforTesting(), Logger(StaticConfig()))

        /* Run Test */
        val result = sut.getLocalCache("I do not exist")

        /* Verify */
        assertNull(result)

        /* Cleanup */
        sut.clearAllCaches()
    }

    @OptIn(MockzillaWeb::class)
    @Test
    fun `updateLocalCache and getLocalCache - returns value`() = runTest {
        /* Setup */
        val entryDummy = EndpointConfiguration.Builder("id")
            .setFailureProbability(40)
            .setWebApiDefaultResponse(MockzillaHttpResponse(body = "hello"))
            .build()
            .toMockDataEntry()
        val sut = LocalCacheServiceImpl(createFileIoforTesting(), Logger(StaticConfig()))

        /* Run Test */
        sut.updateLocalCache(entryDummy)
        val result = sut.getLocalCache("id")

        /* Verify */
        assertEquals(result, entryDummy)

        /* Cleanup */
        sut.clearAllCaches()
    }

    @Test
    fun `getLocalCache - invalid - throws exception`() = runTest {
        /* Setup */
        val fileIo = createFileIoforTesting()
        val sut = LocalCacheServiceImpl(fileIo, Logger(StaticConfig()))

        /* Run Test */
        fileIo.saveToCache("invalid.json", "{,")
        val result = runCatching { sut.getLocalCache("invalid") }

        /* Verify */
        assertTrue(result.exceptionOrNull() is IllegalStateException)

        /* Cleanup */
        sut.clearAllCaches()
    }

    @Test
    fun `getGlobalOverrides - doesnt exist - returns null`() = runTest {
        /* Setup */
        val sut = LocalCacheServiceImpl(createFileIoforTesting(), Logger(StaticConfig()))

        /* Run Test */
        val result = sut.getGlobalOverrides()

        /* Verify */
        assertNull(result)

        /* Cleanup */
        sut.clearAllCaches()
    }

    @Test
    fun `updateGlobalOverrides and getGlobalOverrides - exists - returns null`() = runTest {
        /* Setup */
        val overridesDummy = GlobalOverridesDto(
            67,
            1,
            1
        )
        val sut = LocalCacheServiceImpl(createFileIoforTesting(), Logger(StaticConfig()))

        /* Run Test */
        sut.updateGlobalOverrides(overridesDummy)
        val result = sut.getGlobalOverrides()

        /* Verify */
        assertEquals(result, overridesDummy)

        /* Cleanup */
        sut.clearAllCaches()
    }

    @Test
    fun `getGlobalOverrides - invalid - throws exception`() = runTest {
        /* Setup */
        val fileIo = createFileIoforTesting()
        val sut = LocalCacheServiceImpl(fileIo, Logger(StaticConfig()))

        /* Run Test */
        fileIo.saveToCache(LocalCacheServiceImpl.globalOverridesFileName, "{,")
        val result = runCatching { sut.getGlobalOverrides() }

        /* Verify */
        assertTrue(result.exceptionOrNull() is IllegalStateException)

        /* Cleanup */
        sut.clearAllCaches()
    }
}
