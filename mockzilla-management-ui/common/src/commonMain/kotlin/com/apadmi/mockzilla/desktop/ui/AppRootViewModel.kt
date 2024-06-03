package com.apadmi.mockzilla.desktop.ui

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.StatefulDevice
import com.apadmi.mockzilla.desktop.viewmodel.ViewModel
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AppRootViewModel(
    activeDeviceMonitor: ActiveDeviceMonitor
) : ViewModel() {
    val state = MutableStateFlow<State>(State.NewDeviceConnection)

    init {
        activeDeviceMonitor.selectedDevice.onEach { device ->
            when {
                device == null -> state.value = State.NewDeviceConnection
                !device.isCompatibleMockzillaVersion -> state.value = State.UnsupportedDeviceMockzillaVersion
                else -> state.value = State.Connected(activeDevice = device, selectedEndpoint = null)
            }
        }.launchIn(viewModelScope)
    }

    fun setSelectedEndpoint(key: EndpointConfiguration.Key) {
        val currentState = state.value as? State.Connected ?: return
        state.value = currentState.copy(selectedEndpoint = key)
    }
    sealed class State {
        data object NewDeviceConnection : State()
        data object UnsupportedDeviceMockzillaVersion : State()
        /**
         * @property activeDevice
         * @property selectedEndpoint
         */
        data class Connected(val activeDevice: StatefulDevice, val selectedEndpoint: EndpointConfiguration.Key?) : State()
    }
}
