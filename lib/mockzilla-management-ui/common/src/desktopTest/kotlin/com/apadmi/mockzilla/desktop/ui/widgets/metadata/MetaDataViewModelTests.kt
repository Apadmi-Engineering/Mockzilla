package com.apadmi.mockzilla.desktop.ui.widgets.metadata

import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.engine.device.MetaDataUseCase
import com.apadmi.mockzilla.desktop.ui.widgets.metadata.MetaDataWidgetViewModel.*
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.testutils.ActiveDeviceMonitoringViewModelBaseTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy

import app.cash.turbine.test
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import org.junit.Test

import kotlin.test.assertEquals
import kotlinx.coroutines.yield

class MetaDataViewModelTests : ActiveDeviceMonitoringViewModelBaseTest() {
    @Mock
    private val metaDataUseCaseMock = mock(classOf<MetaDataUseCase>())

    @Test
    fun `getMetaData - no active device - state=NoDeviceConnected`() = runBlockingTest {
        /* Setup */
        activeDeviceMock = null
        val sut = MetaDataWidgetViewModel(metaDataUseCaseMock, activeDeviceMonitorMock, this)

        /* Run Test */
        sut.state.test {
            assertEquals(State.NoDeviceConnected, awaitItem())
        }
    }

    @Test
    fun `getMetaData - active device - state=DisplayMetaData`() = runBlockingTest {
        /* Setup */
        activeDeviceMock = Device.dummy()
        given(metaDataUseCaseMock).coroutine {
            getMetaData(activeDeviceMock!!)
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
    fun `getMetaData - active device - network call fails - state=NoDeviceConnected`() = runBlockingTest {
        /* Setup */
        activeDeviceMock = Device.dummy()
        given(metaDataUseCaseMock).coroutine {
            getMetaData(activeDeviceMock!!)
        }.thenReturn(Result.failure(Exception()))
        val sut = MetaDataWidgetViewModel(metaDataUseCaseMock, activeDeviceMonitorMock, this)

        /* Run Test */
        sut.state.test {
            assertEquals(State.NoDeviceConnected, awaitItem())
        }
    }
}
