package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.wrapper

import androidx.compose.runtime.Immutable
import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.viewmodel.SelectedDeviceMonitoringViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

class MiddlePaneWrapperViewModel(
    activeDeviceMonitor: ActiveDeviceMonitor,
    scope: CoroutineScope? = null
) : SelectedDeviceMonitoringViewModel(activeDeviceMonitor, scope) {
    val state = MutableStateFlow(State.NewDeviceConnection)

    override suspend fun reloadData(selectedDevice: Device?) {
        state.value = selectedDevice?.let {
            State.Endpoints
        } ?: State.NewDeviceConnection
    }

    @Immutable
    enum class State {
        Endpoints,
        NewDeviceConnection,
        ;
    }
}
