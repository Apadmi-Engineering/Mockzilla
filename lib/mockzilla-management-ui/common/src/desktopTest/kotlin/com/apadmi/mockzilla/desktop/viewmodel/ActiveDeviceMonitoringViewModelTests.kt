package com.apadmi.mockzilla.desktop.viewmodel

import app.cash.turbine.test
import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.engine.device.StatefulDevice
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy

import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import org.junit.Test

import kotlin.test.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

@Suppress("MAGIC_NUMBER")
class ActiveDeviceMonitoringViewModelTests : CoroutineTest() {
    @Mock
    private val activeDeviceMonitorMock = mock(classOf<ActiveDeviceMonitor>())

    @Test
    fun `selectedDevice change fires should call reloadData`() = runBlockingTest {
        /* Setup */
        val numberOfEmits = 10
        val testFlow = MutableStateFlow<StatefulDevice?>(null)
        given(activeDeviceMonitorMock).invocation { selectedDevice }.thenReturn(testFlow)

        testFlow.test {
            val sut = ConcreteDeviceMonitoringViewModel(activeDeviceMonitorMock, this)
            yield()

            /* Run Test */
            repeat(numberOfEmits){
                delay(1)
                testFlow.value = StatefulDevice(Device.dummy(), "", false, "$it")
            }

            /* Verify */
            // Wait for each emission to be processed
            skipItems(numberOfEmits + 1)
            println("waiting dome")
            // Expect an invocation for each emission plus one for the initial load.
            assertEquals(numberOfEmits + 1 , sut.reloadDataInvocationCount)
        }
    }

    @Test
    fun `calls reloadData on launch`() = runBlockingTest {
        /* Setup */
        given(activeDeviceMonitorMock).invocation { selectedDevice }.thenReturn(MutableStateFlow(null))

        /* Run Test */
        val sut = ConcreteDeviceMonitoringViewModel(activeDeviceMonitorMock, this)

        /* Verify */
        yield()
        assertEquals(1 , sut.reloadDataInvocationCount)
    }
}

private class ConcreteDeviceMonitoringViewModel(
    activeDeviceMonitor: ActiveDeviceMonitor,
    scope: CoroutineScope
) : SelectedDeviceMonitoringViewModel(activeDeviceMonitor, scope) {
    var reloadDataInvocationCount = 0
    override suspend fun reloadData(selectedDevice: Device?) {
        reloadDataInvocationCount++
    }
}
