package com.apadmi.mockzilla.ui.engine.device

import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy

import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppIconUseCaseTests : CoroutineTest() {
    @RelaxedMockK
    lateinit var appIconServiceMock: MockzillaManagement.AppIconService

    private fun createSut() = AppIconUseCaseImpl(appIconServiceMock)

    @Test
    fun `getAppIcon - success with bytes - returns bytes`() = runBlockingTest {
        /* Setup */
        val bytes = byteArrayOf(1, 2, 3)
        coEvery { appIconServiceMock.fetchAppIcon(Device.dummy()) }
            .returns(Result.success(bytes))
        val sut = createSut()

        /* Run Test */
        val result = sut.getAppIcon(Device.dummy())

        /* Verify */
        assertEquals(Result.success(bytes), result)
    }

    @Test
    fun `getAppIcon - success with null (no icon) - returns null`() = runBlockingTest {
        /* Setup */
        coEvery { appIconServiceMock.fetchAppIcon(Device.dummy()) }
            .returns(Result.success(null))
        val sut = createSut()

        /* Run Test */
        val result = sut.getAppIcon(Device.dummy())

        /* Verify */
        assertEquals(Result.success(null), result)
    }

    @Test
    fun `getAppIcon - network failure - returns failure`() = runBlockingTest {
        /* Setup */
        coEvery { appIconServiceMock.fetchAppIcon(Device.dummy()) }
            .returns(Result.failure(Exception()))
        val sut = createSut()

        /* Run Test */
        val result = sut.getAppIcon(Device.dummy())

        /* Verify */
        assertTrue(result.isFailure)
    }
}
