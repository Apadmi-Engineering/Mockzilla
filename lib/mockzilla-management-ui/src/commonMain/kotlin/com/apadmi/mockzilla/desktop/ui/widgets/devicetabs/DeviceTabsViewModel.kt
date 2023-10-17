package com.apadmi.mockzilla.desktop.ui.widgets.devicetabs

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.viewmodel.ActiveDeviceMonitoringViewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DeviceTabsViewModel(
    private val activeDeviceMonitor: ActiveDeviceMonitor,
    scope: CoroutineScope? = null
) : ActiveDeviceMonitoringViewModel(activeDeviceMonitor, scope) {
    val state = MutableStateFlow<State>(State(emptyList()))

    init {
        activeDeviceMonitor.onDeviceConnectionStateChange.onEach { reloadData() }
            .launchIn(viewModelScope)
    }

    override suspend fun reloadData() {
        state.value = State(devices = activeDeviceMonitor.allDevices.map {
            State.DeviceTabEntry(it.name, it.device == activeDevice, it.isConnected)
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
         */
        data class DeviceTabEntry(
            val name: String,
            val isActive: Boolean,
            val isConnected: Boolean
        )
    }
}
