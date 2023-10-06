package com.apadmi.mockzilla.desktop.ui.widgets

import com.apadmi.mockzilla.desktop.ui.viewmodel.ViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.DeviceConnectionViewModel.State
import com.apadmi.mockzilla.desktop.utils.setStateWithYield
import kotlinx.coroutines.flow.MutableStateFlow

class DeviceConnectionViewModel : ViewModel<State>() {
    override val state = MutableStateFlow(State())

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
