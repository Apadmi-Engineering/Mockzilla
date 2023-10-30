package com.apadmi.mockzilla.desktop.ui.widgets.metadata

import com.apadmi.mockzilla.desktop.engine.device.MetaDataUseCase
import com.apadmi.mockzilla.desktop.ui.widgets.metadata.MetaDataWidgetViewModel.*
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.testutils.SelectedDeviceMonitoringViewModelBaseTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy

import app.cash.turbine.test
import com.apadmi.mockzilla.desktop.engine.device.StatefulDevice
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import org.junit.Test

import kotlin.test.assertEquals
import kotlinx.coroutines.yield

class MetaDataViewModelTests : SelectedDeviceMonitoringViewModelBaseTest() {
    @Mock
    private val metaDataUseCaseMock = mock(classOf<MetaDataUseCase>())

    @Test
    fun `getMetaData - no active device - state=NoDeviceConnected`() = runBlockingTest {
        /* Setup */
        selectedDeviceMock.value = null
        val sut = MetaDataWidgetViewModel(metaDataUseCaseMock, activeDeviceMonitorMock, this)

        /* Run Test */
        sut.state.test {
            assertEquals(State.NoDeviceConnected, awaitItem())
        }
    }

    @Test
    fun `getMetaData - state=DisplayMetaData`() = runBlockingTest {
        /* Setup */
        selectedDeviceMock.value = StatefulDevice.dummy()
        given(metaDataUseCaseMock).coroutine {
            getMetaData(StatefulDevice.dummy().device)
        }.thenReturn(Result.success(MetaData.dummy()))
        val sut = MetaDataWidgetViewModel(metaDataUseCaseMock, activeDeviceMonitorMock, this)

        /* Run Test */
        sut.state.test {
            yield()
            skipItems(1)
            assertEquals(State.DisplayMetaData(MetaData.dummy()), awaitItem())
        }
    }

    @Test
    fun `getMetaData - network call fails - state=NoDeviceConnected`() = runBlockingTest {
        /* Setup */
        selectedDeviceMock.value = StatefulDevice.dummy()
        given(metaDataUseCaseMock).coroutine {
            getMetaData(StatefulDevice.dummy().device)
        }.thenReturn(Result.failure(Exception()))
        val sut = MetaDataWidgetViewModel(metaDataUseCaseMock, activeDeviceMonitorMock, this)

        /* Run Test */
        sut.state.test {
            assertEquals(State.NoDeviceConnected, awaitItem())
        }
    }
}
