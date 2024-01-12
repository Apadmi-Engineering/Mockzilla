package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.details

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.viewmodel.SelectedDeviceMonitoringViewModel
import kotlinx.coroutines.CoroutineScope

class EndpointDetailsViewModel(
    activeDeviceMonitor: ActiveDeviceMonitor,
    scope: CoroutineScope? = null
) : SelectedDeviceMonitoringViewModel(activeDeviceMonitor, scope) {
    override suspend fun reloadData(selectedDevice: Device?) {
        TODO("Implement class")
    }
}
