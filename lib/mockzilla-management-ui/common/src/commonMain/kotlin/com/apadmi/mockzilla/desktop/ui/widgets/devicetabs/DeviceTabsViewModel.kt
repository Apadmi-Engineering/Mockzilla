package com.apadmi.mockzilla.desktop.ui.widgets.devicetabs

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceSelector
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.viewmodel.ActiveDeviceMonitoringViewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DeviceTabsViewModel(
    private val activeDeviceMonitor: ActiveDeviceMonitor,
    private val activeDeviceSelector: ActiveDeviceSelector,
    scope: CoroutineScope? = null
) : ActiveDeviceMonitoringViewModel(activeDeviceMonitor, scope) {
    val state = MutableStateFlow(State(emptyList()))

    init {
        activeDeviceMonitor
            .onDeviceConnectionStateChange
            .onEach { reloadData() }
            .launchIn(viewModelScope)
    }

    fun onChangeDevice(entry: State.DeviceTabEntry) {
        activeDeviceSelector.updateActiveDevice(entry.underlyingDevice)
    }

    fun addNewDevice() {
        // No devices being active => User sees the screen to add a new device
        activeDeviceSelector.clearActiveDevice()
    }

    override suspend fun reloadData() {
        state.value = State(devices = activeDeviceMonitor.allDevices.map {
            State.DeviceTabEntry(it.name, it.device == activeDevice, it.isConnected, it.device)
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
