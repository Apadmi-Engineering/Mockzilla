package com.apadmi.mockzilla.desktop.ui.widgets

import com.apadmi.mockzilla.desktop.ui.widgets.DeviceConnectionViewModel.State
import com.apadmi.mockzilla.desktop.utils.setStateWithYield
import com.apadmi.mockzilla.desktop.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class DeviceConnectionViewModel : ViewModel() {

    val state = MutableStateFlow(State())

    fun onIpAndPortChanged(newValue: String) {
        state.setStateWithYield(viewModelScope, State(ipAndPort = newValue, State.ConnectionState.Connecting))
    }
    data class State(
        val ipAndPort: String = "",
        val connectionState: ConnectionState = ConnectionState.Disconnected
    ) {
        enum class ConnectionState {
            Connected,
            Connecting,
            Disconnected,
            ;
        }
    }
}
