package com.apadmi.mockzilla.desktop.ui.widgets.devicetabs

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceSelector
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn

class DeviceTabsViewModel(
    private val activeDeviceMonitor: ActiveDeviceMonitor,
    private val activeDeviceSelector: ActiveDeviceSelector,
    scope: CoroutineScope? = null
) : ViewModel(scope) {
    val state = MutableStateFlow(State(emptyList()))

    init {
        combine(
            activeDeviceMonitor.onDeviceConnectionStateChange,
            activeDeviceMonitor.selectedDevice
        ) { _, device ->
            reloadData(device?.device)
        }.launchIn(viewModelScope)
        reloadData(activeDeviceMonitor.selectedDevice.value?.device)
    }

    fun onChangeDevice(entry: State.DeviceTabEntry) {
        activeDeviceSelector.updateSelectedDevice(entry.underlyingDevice)
    }

    fun addNewDevice() {
        // No devices being active => User sees the screen to add a new device
        activeDeviceSelector.clearSelectedDevice()
    }

    private fun reloadData(selectedDevice: Device?) {
        state.value = State(devices = activeDeviceMonitor.allDevices.map {
            State.DeviceTabEntry(it.name, it.device == selectedDevice, it.isConnected, it.device)
        })
    }

    /**
     * @property devices
     */
    data class State(val devices: List<DeviceTabEntry>) {
        /**
         * @property name
         * @property isActive
         * @property isConnected
         * @property underlyingDevice
         */
        data class DeviceTabEntry(
            val name: String,
            val isActive: Boolean,
            val isConnected: Boolean,
            val underlyingDevice: Device
        ) {
            companion object
        }
    }
}
