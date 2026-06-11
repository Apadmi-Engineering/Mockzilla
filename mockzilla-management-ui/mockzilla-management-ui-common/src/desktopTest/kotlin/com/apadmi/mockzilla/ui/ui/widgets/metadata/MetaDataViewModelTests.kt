@file:Suppress("MAGIC_NUMBER")

package com.apadmi.mockzilla.ui.ui.widgets.metadata

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy
import com.apadmi.mockzilla.ui.engine.device.AppIconUseCase
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.device.MetaDataUseCase
import com.apadmi.mockzilla.ui.engine.device.MonitorLogsUseCase
import com.apadmi.mockzilla.ui.engine.device.StatefulDevice
import com.apadmi.mockzilla.ui.ui.common.widgets.metadata.MetaDataWidgetViewModel
import com.apadmi.mockzilla.ui.ui.common.widgets.metadata.MetaDataWidgetViewModel.*

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.yield

internal class MetaDataViewModelTests : CoroutineTest() {
    @RelaxedMockK
    lateinit var metaDataUseCaseMock: MetaDataUseCase

    @RelaxedMockK
    lateinit var monitorLogsUseCaseMock: MonitorLogsUseCase

    @RelaxedMockK
    lateinit var appIconUseCaseMock: AppIconUseCase

    private fun createSut() = MetaDataWidgetViewModel(
        Device.dummy(),
        metaDataUseCaseMock,
        monitorLogsUseCaseMock,
        appIconUseCaseMock,
        testScope.backgroundScope
    )

    @Test
    fun `getMetaData - state=DisplayMetaData`() = runBlockingTest {
        /* Setup */
        coEvery { metaDataUseCaseMock.getMetaData(StatefulDevice.dummy().device, false) }
            .returns(Result.success(MetaData.dummy()))
        coEvery { monitorLogsUseCaseMock.getMonitorLogs(Device.dummy()) } returns Result.failure(Exception())
        coEvery { appIconUseCaseMock.getAppIcon(Device.dummy()) } returns Result.success(null)

        /* Run Test */
        createSut().state.test {
            yield()
            skipItems(1)
            assertEquals(State.DisplayMetaData(MetaData.dummy(), appIconBytes = null), awaitItem())
        }
    }

    @Test
    fun `getMetaData - network call fails - state=Error`() = runBlockingTest {
        /* Setup */
        coEvery { metaDataUseCaseMock.getMetaData(Device.dummy()) }.returns(Result.failure(Exception()))
        coEvery { monitorLogsUseCaseMock.getMonitorLogs(Device.dummy()) } returns Result.failure(Exception())
        coEvery { appIconUseCaseMock.getAppIcon(Device.dummy()) } returns Result.success(null)

        /* Run Test */
        createSut().state.test {
            /* Verify */
            assertEquals(State.Loading, awaitItem())
            assertEquals(State.Error, awaitItem())
        }
    }

    @Test
    fun `getAppIcon - success with bytes - appIconBytes set in DisplayMetaData state`() = runBlockingTest {
        /* Setup */
        val bytes = byteArrayOf(1, 2, 3)
        coEvery { metaDataUseCaseMock.getMetaData(StatefulDevice.dummy().device, false) }
            .returns(Result.success(MetaData.dummy()))
        coEvery { monitorLogsUseCaseMock.getMonitorLogs(Device.dummy()) } returns Result.failure(Exception())
        coEvery { appIconUseCaseMock.getAppIcon(Device.dummy()) } returns Result.success(bytes)

        /* Run Test */
        createSut().state.test {
            yield()
            skipItems(1)
            val displayState = awaitItem() as State.DisplayMetaData
            assertTrue(displayState.appIconBytes.contentEquals(bytes))
        }
    }

    @Test
    fun `getAppIcon - failure - appIconBytes null, state still DisplayMetaData`() = runBlockingTest {
        /* Setup */
        coEvery { metaDataUseCaseMock.getMetaData(StatefulDevice.dummy().device, false) }
            .returns(Result.success(MetaData.dummy()))
        coEvery { monitorLogsUseCaseMock.getMonitorLogs(Device.dummy()) } returns Result.failure(Exception())
        coEvery { appIconUseCaseMock.getAppIcon(Device.dummy()) } returns Result.failure(Exception())

        /* Run Test */
        createSut().state.test {
            yield()
            skipItems(1)
            val displayState = awaitItem() as State.DisplayMetaData
            assertNull(displayState.appIconBytes)
        }
    }

    @Test
    fun `getAppIcon - returns null (no icon) - appIconBytes null in state`() = runBlockingTest {
        /* Setup */
        coEvery { metaDataUseCaseMock.getMetaData(StatefulDevice.dummy().device, false) }
            .returns(Result.success(MetaData.dummy()))
        coEvery { monitorLogsUseCaseMock.getMonitorLogs(Device.dummy()) } returns Result.failure(Exception())
        coEvery { appIconUseCaseMock.getAppIcon(Device.dummy()) } returns Result.success(null)

        /* Run Test */
        createSut().state.test {
            yield()
            skipItems(1)
            val displayState = awaitItem() as State.DisplayMetaData
            assertNull(displayState.appIconBytes)
        }
    }
}
