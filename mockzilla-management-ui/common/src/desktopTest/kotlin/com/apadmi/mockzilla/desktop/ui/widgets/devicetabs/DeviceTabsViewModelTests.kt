package com.apadmi.mockzilla.desktop.ui.widgets.devicetabs

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceSelector
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.engine.device.StatefulDevice
import com.apadmi.mockzilla.desktop.ui.widgets.devicetabs.DeviceTabsViewModel.*
import com.apadmi.mockzilla.testutils.dummymodels.dummy

import app.cash.turbine.test
import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.testutils.CoroutineTest
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.thenDoNothing
import io.mockative.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test

import kotlin.test.assertEquals
import kotlinx.coroutines.flow.flowOf

class DeviceTabsViewModelTests : CoroutineTest() {
    @Mock
    private val activeDeviceSelectorMock = mock(classOf<ActiveDeviceSelector>())

    @Mock
    private val activeDeviceMonitorMock = mock(classOf<ActiveDeviceMonitor>())

    private fun createSut() = DeviceTabsViewModel(activeDeviceMonitorMock.also {
        given(it).invocation { onDeviceConnectionStateChange }.thenReturn(flowOf())
    }, activeDeviceSelectorMock, testScope.backgroundScope)

    @Test
    fun `onChangeDevice - calls through`() = runBlockingTest {
        /* Setup */
        given(activeDeviceMonitorMock).invocation { selectedDevice }.thenReturn(MutableStateFlow(null))
        given(activeDeviceMonitorMock).invocation { allDevices }.thenReturn(emptyList())
        given(activeDeviceSelectorMock).invocation { updateSelectedDevice(Device.dummy()) }.thenDoNothing()
        val sut = createSut()
        sut.state.test {
            /* Run Test */
            sut.onChangeDevice(State.DeviceTabEntry.dummy().copy(underlyingDevice = Device.dummy()))

            /* Verify */
            verify(activeDeviceSelectorMock).invocation { updateSelectedDevice(Device.dummy()) }
                .wasInvoked()
            assertEquals(State(emptyList()), awaitItem())
        }
    }

    @Test
    fun `addNewDevice - clears active device`() = runBlockingTest {
        /* Setup */
        given(activeDeviceMonitorMock).invocation { selectedDevice }.thenReturn(MutableStateFlow(null))
        given(activeDeviceMonitorMock).invocation { allDevices }.thenReturn(emptyList())
        given(activeDeviceSelectorMock).invocation { clearSelectedDevice() }.thenDoNothing()
        val sut = createSut()

        /* Run Test */
        sut.addNewDevice()

        /* Verify */
        verify(activeDeviceSelectorMock).invocation { clearSelectedDevice() }.wasInvoked()
    }

    @Suppress("TOO_LONG_FUNCTION")
    @Test
    fun `init - pulls latest data from monitor - updates state`() = runBlockingTest {
        /* Setup */
        val dummyActiveDevice = Device.dummy().copy(ip = "device1")
        given(activeDeviceMonitorMock).invocation { selectedDevice }.thenReturn(MutableStateFlow(null))
        given(activeDeviceMonitorMock).invocation { allDevices }.thenReturn(
            listOf(
                StatefulDevice(
                    dummyActiveDevice,
                    "Device Name 1",
                    isConnected = false,
                    connectedAppPackage = "package"
                ),
                StatefulDevice(
                    Device.dummy(),
                    "Device Name 2",
                    isConnected = true,
                    connectedAppPackage = "package"
                ),
            )
        )

        val sut = createSut()
        sut.state.test {

            /* Verify */
            assertEquals(
                State(
                    listOf(
                        State.DeviceTabEntry(
                            "Device Name 1",
                            isActive = false,
                            isConnected = false,
                            dummyActiveDevice
                        ),
                        State.DeviceTabEntry(
                            "Device Name 2",
                            isActive = false,
                            isConnected = true,
                            Device.dummy()
                        )
                    )
                ),
                awaitItem()
            )
        }
    }
}
