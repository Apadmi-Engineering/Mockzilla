package com.apadmi.mockzilla.desktop.ui.widgets.devicetabs

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceSelector
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.engine.device.StatefulDevice
import com.apadmi.mockzilla.desktop.ui.widgets.devicetabs.DeviceTabsViewModel.*
import com.apadmi.mockzilla.testutils.CoroutineTest
import com.apadmi.mockzilla.testutils.dummymodels.dummy

import app.cash.turbine.test

import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Test

import kotlin.test.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class DeviceTabsViewModelTests : CoroutineTest() {
    @RelaxedMockK
    lateinit var activeDeviceSelectorMock: ActiveDeviceSelector

    @RelaxedMockK
    lateinit var activeDeviceMonitorMock: ActiveDeviceMonitor

    private fun createSut() = DeviceTabsViewModel(activeDeviceMonitorMock.also {
        every { it.onDeviceConnectionStateChange }.returns(flowOf())
    }, activeDeviceSelectorMock, testScope.backgroundScope)

    @Test
    fun `onChangeDevice - calls through`() = runBlockingTest {
        /* Setup */
        every { activeDeviceMonitorMock.selectedDevice }.returns(MutableStateFlow(null))
        every { activeDeviceMonitorMock.allDevices }.returns(emptyList())
        val sut = createSut()
        sut.state.test {
            /* Run Test */
            sut.onChangeDevice(State.DeviceTabEntry.dummy().copy(underlyingDevice = Device.dummy()))

            /* Verify */
            verify(exactly = 1) { activeDeviceSelectorMock.updateSelectedDevice(Device.dummy()) }
            assertEquals(State(emptyList()), awaitItem())
        }
    }

    @Test
    fun `addNewDevice - clears active device`() = runBlockingTest {
        /* Setup */
        every { activeDeviceMonitorMock.selectedDevice }.returns(MutableStateFlow(null))
        every { activeDeviceMonitorMock.allDevices }.returns(emptyList())
        val sut = createSut()

        /* Run Test */
        sut.addNewDevice()

        /* Verify */
        verify(exactly = 1) { activeDeviceSelectorMock.clearSelectedDevice() }
    }

    @Suppress("TOO_LONG_FUNCTION")
    @Test
    fun `init - pulls latest data from monitor - updates state`() = runBlockingTest {
        /* Setup */
        val dummyActiveDevice = Device.dummy().copy(ip = "device1")
        every { activeDeviceMonitorMock.selectedDevice }.returns(MutableStateFlow(null))
        every { activeDeviceMonitorMock.allDevices }.returns(
            listOf(
                StatefulDevice(
                    dummyActiveDevice,
                    "Device Name 1",
                    isConnected = false,
                    connectedAppPackage = "package",
                    true
                ),
                StatefulDevice(
                    Device.dummy(),
                    "Device Name 2",
                    isConnected = true,
                    connectedAppPackage = "package",
                    true
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
