package com.apadmi.mockzilla.desktop.ui

import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.StatefulDevice
import com.apadmi.mockzilla.desktop.engine.events.EventBus
import com.apadmi.mockzilla.desktop.engine.events.EventBus.*
import com.apadmi.mockzilla.desktop.ui.AppRootViewModel.State.Connected.ErrorBannerState
import com.apadmi.mockzilla.desktop.viewmodel.ViewModel
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AppRootViewModel(
    private val eventBus: EventBus,
    activeDeviceMonitor: ActiveDeviceMonitor
) : ViewModel() {
    val state = MutableStateFlow<State>(State.NewDeviceConnection)

    init {
        activeDeviceMonitor.selectedDevice.onEach { device ->
            state.value = when {
                device == null -> State.NewDeviceConnection
                !device.isCompatibleMockzillaVersion -> State.UnsupportedDeviceMockzillaVersion
                else -> State.Connected(
                    activeDevice = device,
                    error = ErrorBannerState.ConnectionLost.takeUnless { device.isConnected },
                    selectedEndpoint = null
                )
            }
        }.launchIn(viewModelScope)

        eventBus.handleNewErrorEvents()
        eventBus.handleClearingErrors()
    }

    private fun EventBus.handleClearingErrors() = events.filter {
        it is Event.EndpointDataChanged || it is Event.FullRefresh
    }.onEach {
        state.value = (state.value as? State.Connected)?.copy(error = null) ?: state.value
    }.launchIn(viewModelScope)

    private fun EventBus.handleNewErrorEvents() = events.filter {
        it is Event.GenericError && (state.value as? State.Connected)?.activeDevice?.isConnected == true
    }.onEach {
        state.value = (state.value as? State.Connected)?.copy(error = ErrorBannerState.UnknownError) ?: state.value
    }.launchIn(viewModelScope)

    fun setSelectedEndpoint(key: EndpointConfiguration.Key) {
        val currentState = state.value as? State.Connected ?: return
        state.value = currentState.copy(selectedEndpoint = key)
    }

    fun refreshAll() {
        eventBus.send(Event.FullRefresh)
    }

    sealed class State {
        data object NewDeviceConnection : State()
        data object UnsupportedDeviceMockzillaVersion : State()

        /**
         * @property activeDevice
         * @property selectedEndpoint
         * @property error
         */
        data class Connected(
            val activeDevice: StatefulDevice,
            val selectedEndpoint: EndpointConfiguration.Key?,
            val error: ErrorBannerState? = null
        ) : State() {
            enum class ErrorBannerState {
                ConnectionLost,
                UnknownError,
                ;
            }
        }
    }
}
