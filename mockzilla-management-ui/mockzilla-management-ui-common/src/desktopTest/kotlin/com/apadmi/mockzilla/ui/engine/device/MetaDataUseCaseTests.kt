package com.apadmi.mockzilla.ui.engine.device

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MetaDataUseCaseTests : CoroutineTest() {
    @RelaxedMockK
    lateinit var serviceMock: MockzillaManagement.MetaDataService
    private fun createSut() = MetaDataUseCaseImpl(serviceMock)

    @Test
    fun `getMetaData - fails - returns failure`() = runBlockingTest {
        /* Setup */
        coEvery { serviceMock.fetchMetaData(Device.dummy(), hideFromLogs = false) }.returns(
            Result.failure(Exception())
        )
        val sut = createSut()

        /* Run Test */
        val result = sut.getMetaData(Device.dummy())

        /* Verify */
        assertTrue(result.isFailure)
    }

    @Test
    fun `getMetaData - succeeds - returns and sets cache`() = runBlockingTest {
        /* Setup */
        coEvery { serviceMock.fetchMetaData(Device.dummy(), hideFromLogs = false) }.returns(
            Result.success(MetaData.dummy())
        )
        val sut = createSut()

        /* Run Test */
        val result = sut.getMetaData(Device.dummy())
        val result2 = sut.getMetaData(Device.dummy())  // Should hit cache

        /* Verify */
        assertEquals(Result.success(MetaData.dummy()), result)
        assertEquals(result, result2)
        coVerify(exactly = 1) { serviceMock.fetchMetaData(Device.dummy(), false) }
    }

    @Test
    fun `getMetaData - after invalidate - re-fetches from network`() = runBlockingTest {
        /* Setup */
        coEvery { serviceMock.fetchMetaData(Device.dummy(), hideFromLogs = false) }.returns(
            Result.success(MetaData.dummy())
        )
        val sut = createSut()

        /* Run Test */
        sut.getMetaData(Device.dummy())
        sut.invalidate(Device.dummy())
        sut.getMetaData(Device.dummy())  // Should cache-miss after invalidation

        /* Verify */
        coVerify(exactly = 2) { serviceMock.fetchMetaData(Device.dummy(), false) }
    }
}
