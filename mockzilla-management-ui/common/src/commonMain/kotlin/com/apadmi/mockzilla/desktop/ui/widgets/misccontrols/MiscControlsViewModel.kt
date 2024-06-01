package com.apadmi.mockzilla.desktop.ui.widgets.misccontrols

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.engine.events.EventBus
import com.apadmi.mockzilla.desktop.utils.launchUnit
import com.apadmi.mockzilla.desktop.viewmodel.SelectedDeviceMonitoringViewModel
import com.apadmi.mockzilla.desktop.viewmodel.ViewModel
import com.apadmi.mockzilla.management.MockzillaManagement
import kotlinx.coroutines.CoroutineScope

class MiscControlsViewModel(
    private val eventBus: EventBus,
    activeDevice: ActiveDeviceMonitor,
    private val clearingService: MockzillaManagement.CacheClearingService,
    scope: CoroutineScope? = null
): SelectedDeviceMonitoringViewModel(activeDevice, scope) {

    fun refreshAllData() {
        eventBus.send(EventBus.Event.FullRefresh)
    }

    fun clearAllOverrides() = viewModelScope.launchUnit {
        activeDevice?.let { clearingService.clearAllCaches(it) }
        eventBus.send(EventBus.Event.FullRefresh)
    }

    override suspend fun reloadData(selectedDevice: Device?) {
        /* No-Op */
    }
}