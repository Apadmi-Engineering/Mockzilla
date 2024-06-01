package com.apadmi.mockzilla.desktop.ui.widgets.monitorlogs.details

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope

class MonitorLogDetailsViewModel(
    private val device: Device,
    scope: CoroutineScope? = null
) : ViewModel(scope) {

}
