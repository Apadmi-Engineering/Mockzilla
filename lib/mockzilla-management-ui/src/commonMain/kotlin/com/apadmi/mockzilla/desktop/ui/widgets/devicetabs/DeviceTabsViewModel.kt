package com.apadmi.mockzilla.desktop.ui.widgets.devicetabs

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


import androidx.compose.runtime.Immutable
import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.MetaDataUseCase
import com.apadmi.mockzilla.desktop.viewmodel.ActiveDeviceMonitoringViewModel
import com.apadmi.mockzilla.lib.models.MetaData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

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

    data class State(val devices: List<DeviceTabEntry>) {
        data class DeviceTabEntry(val name: String, val isActive: Boolean, val isConnected: Boolean)
    }
}
