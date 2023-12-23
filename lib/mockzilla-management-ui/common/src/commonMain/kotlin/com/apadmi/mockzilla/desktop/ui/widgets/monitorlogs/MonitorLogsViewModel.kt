package com.apadmi.mockzilla.desktop.ui.widgets.monitorlogs

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.viewmodel.ActiveDeviceMonitoringViewModel
import kotlinx.coroutines.CoroutineScope

class MonitorLogsViewModel(
    activeDeviceMonitor: ActiveDeviceMonitor,
    scope: CoroutineScope? = null
) : ActiveDeviceMonitoringViewModel(activeDeviceMonitor, scope) {
    override suspend fun reloadData() {
        TODO("Implement class")
    }
}
