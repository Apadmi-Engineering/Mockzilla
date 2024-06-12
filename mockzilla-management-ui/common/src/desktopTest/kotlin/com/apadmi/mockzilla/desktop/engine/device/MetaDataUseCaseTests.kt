package com.apadmi.mockzilla.desktop.engine.device

import com.apadmi.mockzilla.desktop.utils.TimeStampAccessor
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy

import io.mockative.Mock
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.coVerify
import io.mockative.given
import io.mockative.mock
import io.mockative.time
import io.mockative.verify
import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

class MetaDataUseCaseTests : CoroutineTest() {
    @Mock
    private val serviceMock = mock(classOf<MockzillaManagement.MetaDataService>())
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
        coVerify { serviceMock.fetchMetaData(Device.dummy(), true) }
            .wasInvoked(1.time)
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
        coVerify { serviceMock.fetchMetaData(Device.dummy(), false) }
            .wasInvoked(2.time)
    }
}
