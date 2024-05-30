package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.endpoints

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.viewmodel.SelectedDeviceMonitoringViewModel
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.management.MockzillaManagement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

class EndpointsViewModel(
    private val endpointsService: MockzillaManagement.EndpointsService,
    activeDeviceMonitor: ActiveDeviceMonitor,
    scope: CoroutineScope? = null
) : SelectedDeviceMonitoringViewModel(activeDeviceMonitor, scope) {
    val state = MutableStateFlow<State>(State.Empty)
    private val checkboxStates = mutableMapOf<Device, MutableList<EndpointConfiguration.Key>>()
    override suspend fun reloadData(selectedDevice: Device?) {
        val device = selectedDevice ?: return run {
            state.value = State.Empty
        }

        state.value = endpointsService.fetchAllEndpointConfigs(device).fold(
            onSuccess = { State.EndpointsList(it.toConfig(checkboxStates[device])) },
            onFailure = { State.Empty }
        )
    }

    private fun List<SerializableEndpointConfig>.toConfig(
        tickedCheckboxes: List<EndpointConfiguration.Key>?
    ) = map {
        State.EndpointConfig(
            it.key,
            it.name,
            it.shouldFail,
            tickedCheckboxes?.contains(it.key) == true
        )
    }

    fun onCheckboxChanged(key: EndpointConfiguration.Key, value: Boolean) {
        val currentState = state.value as? State.EndpointsList ?: return
        val device = activeDevice ?: return
        checkboxStates.getOrPut(device) { mutableListOf() }.apply {
            if (value) {
                add(key)
            } else {
                remove(key)
            }
        }
        state.value = currentState.copy(
            endpoints = currentState.endpoints.map {
                it.copy(isCheckboxTicked = if (it.key == key) value else it.isCheckboxTicked)
            }
        )
    }

    sealed class State {
        data class EndpointConfig(
            val key: EndpointConfiguration.Key,
            val name: String,
            val fail: Boolean?,
            val isCheckboxTicked: Boolean
        )

        data object Empty : State()

        /**
         * @property endpoints
         */
        data class EndpointsList(val endpoints: List<EndpointConfig>) : State()
    }
}
