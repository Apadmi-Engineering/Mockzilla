package com.apadmi.mockzilla.desktop.engine.device

import com.apadmi.mockzilla.desktop.utils.TimeStampAccessor
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
import kotlin.time.Duration.Companion.seconds

class MetaDataUseCaseTests : CoroutineTest() {
    @RelaxedMockK
    lateinit var serviceMock: MockzillaManagement.MetaDataService
    private fun createSut(
        timeStampAccessor: TimeStampAccessor = System::currentTimeMillis
    ) = MetaDataUseCaseImpl(serviceMock, timeStampAccessor)
    
    @Test
    fun `getMetaData - fails - returns failure`() = runBlockingTest {
        /* Setup */
        coEvery { serviceMock.fetchMetaData(Device.dummy(), hideFromLogs = true) }.returns(
            Result.failure(Exception())
        )
        val sut = createSut()

        /* Run Test */
        val result = sut.getMetaData(Device.dummy(), true)

        /* Verify */
        assertTrue(result.isFailure)
    }

    @Test
    fun `getMetaData - succeeds - returns and sets cache`() = runBlockingTest {
        /* Setup */
        coEvery { serviceMock.fetchMetaData(Device.dummy(), hideFromLogs = true) }.returns(
            Result.success(MetaData.dummy())
        )
        val sut = createSut()

        /* Run Test */
        val result = sut.getMetaData(Device.dummy(), true)
        val result2 = sut.getMetaData(Device.dummy(), true)  // Should hit cache

        /* Verify */
        assertEquals(Result.success(MetaData.dummy()), result)
        assertEquals(result, result2)
        coVerify(exactly = 1) { serviceMock.fetchMetaData(Device.dummy(), true) }
    }

    @Test
    fun `getMetaData - refreshes after cache expires`() = runBlockingTest {
        /* Setup */
        coEvery { serviceMock.fetchMetaData(Device.dummy(), hideFromLogs = false) }.returns(
            Result.success(MetaData.dummy())
        )
        var currentTimeStamp = System.currentTimeMillis()
        val sut = createSut { currentTimeStamp }

        /* Run Test */
        val result = sut.getMetaData(Device.dummy(), false)
        // Mimic time advancing
        currentTimeStamp += 0.6.seconds.inWholeMilliseconds

        val result2 = sut.getMetaData(Device.dummy(), false)  // Should cache-miss

        /* Verify */
        assertEquals(Result.success(MetaData.dummy()), result)
        assertEquals(result, result2)
        coVerify(exactly = 2) { serviceMock.fetchMetaData(Device.dummy(), false) }
    }
}
