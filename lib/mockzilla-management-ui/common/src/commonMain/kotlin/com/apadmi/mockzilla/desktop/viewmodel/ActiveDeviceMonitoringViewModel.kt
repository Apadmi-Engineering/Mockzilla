package com.apadmi.mockzilla.desktop.viewmodel

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.Device
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

abstract class ActiveDeviceMonitoringViewModel(
    activeDeviceMonitor: ActiveDeviceMonitor,
    scope: CoroutineScope? = null
) : ViewModel(scope), ActiveDeviceMonitor by activeDeviceMonitor {
    init {
        activeDeviceMonitor.onDeviceSelectionChange.onEach { reloadData() }.launchIn(viewModelScope)

        viewModelScope.launch {
            reloadData()
        }
    }

    internal suspend fun <T> activeDeviceOrDefault(
        default: T,
        block: suspend (Device) -> T
    ): T = activeDevice?.let { block(it) } ?: default

    abstract suspend fun reloadData()
}
