package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.wrapper

import androidx.compose.runtime.Immutable
import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.viewmodel.ActiveDeviceMonitoringViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

class MiddlePaneWrapperViewModel(
    activeDeviceMonitor: ActiveDeviceMonitor,
    scope: CoroutineScope? = null
) : ActiveDeviceMonitoringViewModel(activeDeviceMonitor, scope) {
    val state = MutableStateFlow(State.NewDeviceConnection)

    override suspend fun reloadData() {
        state.value = activeDevice?.let {
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
