package com.apadmi.mockzilla.desktop.engine.device

import com.apadmi.mockzilla.desktop.utils.TimeStampAccessor
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy

import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.time
import io.mockative.verify
import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.test.runBlockingTest

class MetaDataUseCaseTests : CoroutineTest() {
    @Mock
    private val serviceMock = mock(classOf<MockzillaManagement.MetaDataService>())
    private fun createSut(
        timeStampAccessor: TimeStampAccessor = System::currentTimeMillis
    ) = MetaDataUseCaseImpl(serviceMock, timeStampAccessor)
    
    @Test
    fun `getMetaData - fails - returns failure`() = runBlockingTest {
        /* Setup */
        given(serviceMock).coroutine {
            fetchMetaData(Device.dummy())
        }.thenReturn(Result.failure(Exception()))
        val sut = createSut()

        /* Run Test */
        val result = sut.getMetaData(Device.dummy())

        /* Verify */
        assertTrue(result.isFailure)
    }

    @Test
    fun `getMetaData - succeeds - returns and sets cache`() = runBlockingTest {
        /* Setup */
        given(serviceMock).coroutine {
            fetchMetaData(Device.dummy())
        }.thenReturn(Result.success(MetaData.dummy()))
        val sut = createSut()

        /* Run Test */
        val result = sut.getMetaData(Device.dummy())
        val result2 = sut.getMetaData(Device.dummy())  // Should hit cache

        /* Verify */
        assertEquals(Result.success(MetaData.dummy()), result)
        assertEquals(result, result2)
        verify(serviceMock).coroutine { fetchMetaData(Device.dummy()) }
            .wasInvoked(1.time)
    }

    @Test
    fun `getMetaData - refreshes after cache expires`() = runBlockingTest {
        /* Setup */
        given(serviceMock).coroutine {
            fetchMetaData(Device.dummy())
        }.thenReturn(Result.success(MetaData.dummy()))
        var currentTimeStamp = System.currentTimeMillis()
        val sut = createSut { currentTimeStamp }

        /* Run Test */
        val result = sut.getMetaData(Device.dummy())
        // Mimic time advancing
        currentTimeStamp += 0.6.seconds.inWholeMilliseconds

        val result2 = sut.getMetaData(Device.dummy())  // Should cache-miss

        /* Verify */
        assertEquals(Result.success(MetaData.dummy()), result)
        assertEquals(result, result2)
        verify(serviceMock).coroutine { fetchMetaData(Device.dummy()) }
            .wasInvoked(2.time)
    }
}
