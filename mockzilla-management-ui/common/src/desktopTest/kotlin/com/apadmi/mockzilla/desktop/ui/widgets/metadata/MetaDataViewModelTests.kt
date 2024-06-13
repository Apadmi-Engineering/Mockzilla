package com.apadmi.mockzilla.desktop.ui.widgets.metadata

import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.engine.device.MetaDataUseCase
import com.apadmi.mockzilla.desktop.engine.device.StatefulDevice
import com.apadmi.mockzilla.desktop.ui.widgets.metadata.MetaDataWidgetViewModel.*
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy

import app.cash.turbine.test
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.mock
import org.junit.Test

import kotlin.test.assertEquals
import kotlinx.coroutines.yield

class MetaDataViewModelTests : CoroutineTest() {
    @Mock
    private val metaDataUseCaseMock = mock(classOf<MetaDataUseCase>())

    @Test
    fun `getMetaData - state=DisplayMetaData`() = runBlockingTest {
        /* Setup */
        coEvery { metaDataUseCaseMock.getMetaData(StatefulDevice.dummy().device, false) }
            .returns(Result.success(MetaData.dummy()))
        val sut = MetaDataWidgetViewModel(Device.dummy(), metaDataUseCaseMock, backgroundScope)

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
        coEvery { metaDataUseCaseMock.getMetaData(Device.dummy()) }.returns(Result.failure(Exception()))

        val sut = MetaDataWidgetViewModel(Device.dummy(), metaDataUseCaseMock, backgroundScope)

        /* Run Test */
        sut.state.test {
            /* Verify */
            assertEquals(State.Loading, awaitItem())
            assertEquals(State.Error, awaitItem())
        }
    }
}
