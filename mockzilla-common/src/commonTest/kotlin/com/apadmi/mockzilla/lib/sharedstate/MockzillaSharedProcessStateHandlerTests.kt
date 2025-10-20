@file:Suppress("MAGIC_NUMBER")

package com.apadmi.mockzilla.lib.sharedstate

import com.apadmi.mockzilla.lib.internal.utils.createFileIoforTesting

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

class MockzillaSharedProcessStateHandlerTests {
    @Test
    fun `read and write - hits caches - works correctly`() = runBlocking {
        /* Setup */
        val sut = MockzillaSharedProcessStateHandler(createFileIoforTesting())
        val dummy = MockzillaSharedProcessState("this is a test ip", 98_249_283)

        /* Run Test */
        sut.setSharedProcessState(dummy)

        /* Verify */
        assertEquals(dummy, sut.getSharedProcessState())
    }

    @Test
    fun `read and write - without caches - works correctly`() = runBlocking {
        /* Setup */
        val fileIo = createFileIoforTesting()

        // Two SUTs to ensure caching is backed onto disk
        val sut = MockzillaSharedProcessStateHandler(fileIo)
        val sut2 = MockzillaSharedProcessStateHandler(fileIo)
        val dummy = MockzillaSharedProcessState("this is a test ip", 98_249_283)

        /* Run Test */
        sut.setSharedProcessState(dummy)

        /* Verify */
        assertEquals(dummy, sut2.getSharedProcessState())
    }

    @Test
    fun `read - nothing written - returns null`() = runBlocking {
        /* Setup */
        val fileIo = createFileIoforTesting()
        val sut = MockzillaSharedProcessStateHandler(fileIo)

        /* Run Test & Verify */
        assertEquals(null, sut.getSharedProcessState())
    }
}
