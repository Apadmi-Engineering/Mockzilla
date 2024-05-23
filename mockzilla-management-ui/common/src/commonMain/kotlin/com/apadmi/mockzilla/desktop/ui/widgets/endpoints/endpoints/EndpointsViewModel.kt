package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.endpoints

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.viewmodel.SelectedDeviceMonitoringViewModel
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.management.MockzillaManagement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

class EndpointsViewModel(
    private val endpointsService: MockzillaManagement.EndpointsService,
    activeDeviceMonitor: ActiveDeviceMonitor,
    scope: CoroutineScope? = null
) : SelectedDeviceMonitoringViewModel(activeDeviceMonitor, scope) {
    val state = MutableStateFlow<State>(State.Empty)
    override suspend fun reloadData(selectedDevice: Device?) {
        val device = selectedDevice ?: return run {
            state.value = State.Empty
        }

        state.value = endpointsService.fetchAllEndpointConfigs(device).fold(
            onSuccess = { State.EndpointsList(it) },
            onFailure = { State.Empty }
        )
    }

    sealed class State {
        data object Empty : State()
        /**
         * @property endpoints
         */
        data class EndpointsList(val endpoints: List<SerializableEndpointConfig>) : State()
    }
}
