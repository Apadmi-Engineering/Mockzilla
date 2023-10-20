package com.apadmi.mockzilla.desktop.ui.widgets.devicetabs

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceSelector
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.engine.device.StatefulDevice
import com.apadmi.mockzilla.desktop.ui.widgets.devicetabs.DeviceTabsViewModel.*
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy

import app.cash.turbine.test
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.verify
import org.junit.Test

import kotlin.test.assertEquals
import kotlinx.coroutines.flow.flowOf

class DeviceTabsViewModelTests : CoroutineTest() {
    @Mock
    private val activeDeviceMonitorMock = mock(classOf<ActiveDeviceMonitor>())

    @Mock
    private val activeDeviceSelectorMock = mock(classOf<ActiveDeviceSelector>())

    private fun createSut() = DeviceTabsViewModel(activeDeviceMonitorMock.also {
        given(it).invocation { onDeviceConnectionStateChange }.thenReturn(flowOf())
        given(it).invocation { onDeviceSelectionChange }.thenReturn(flowOf())
    }, activeDeviceSelectorMock, testScope)

    @Test
    fun `onChangeDevice - calls through`() = runBlockingTest {
        /* Setup */
        given(activeDeviceMonitorMock).invocation { allDevices }.thenReturn(emptyList())
        val sut = createSut()

        /* Run Test */
        sut.onChangeDevice(State.DeviceTabEntry.dummy().copy(underlyingDevice = Device.dummy()))

        /* Verify */
        verify(activeDeviceSelectorMock).invocation { updateActiveDevice(Device.dummy()) }
            .wasInvoked()
    }

    @Test
    fun `addNewDevice - clears active device`() = runBlockingTest {
        /* Setup */
        given(activeDeviceMonitorMock).invocation { allDevices }.thenReturn(emptyList())
        val sut = createSut()

        /* Run Test */
        sut.addNewDevice()

        /* Verify */
        verify(activeDeviceSelectorMock).invocation { clearActiveDevice() }.wasInvoked()
    }

    @Suppress("TOO_LONG_FUNCTION")
    @Test
    fun `reloadData - pulls latest data from monitor - updates state`() = runBlockingTest {
        /* Setup */
        val dummyActiveDevice = Device.dummy().copy(ip = "device1")
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
        given(activeDeviceMonitorMock).invocation { activeDevice }.thenReturn(dummyActiveDevice)

        val sut = createSut()
        sut.state.test {
            skipItems(1)

            /* Run Test */
            sut.reloadData()

            /* Verify */
            assertEquals(
                State(
                    listOf(
                        State.DeviceTabEntry(
                            "Device Name 1",
                            isActive = true,
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
