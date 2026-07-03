@file:NoKDoc

package com.apadmi.mockzilla.ui.ui.common

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.management.internal.ktor.FailedHttpResponseException
import com.apadmi.mockzilla.ui.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.device.StatefulDevice
import com.apadmi.mockzilla.ui.engine.events.EventBus
import com.apadmi.mockzilla.ui.engine.events.EventBus.*
import com.apadmi.mockzilla.ui.engine.events.GenericErrorableOperation
import com.apadmi.mockzilla.ui.internal.NoKDoc
import com.apadmi.mockzilla.ui.internal.viewmodel.ViewModel
import io.ktor.http.HttpStatusCode

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@InternalMockzillaApi
public class DeviceRootViewModel(
    private val device: Device,
    private val eventBus: EventBus,
    private val activeDeviceMonitor: ActiveDeviceMonitor,
) : ViewModel() {
    public val state: MutableStateFlow<State> = MutableStateFlow(
        activeDeviceMonitor.allDevices.find { it.device == device }.let { initial ->
            when {
                initial == null || !initial.isCompatibleMockzillaVersion -> State.UnsupportedDeviceMockzillaVersion
                else -> State.Connected(activeDevice = initial, error = null, selectedEndpoint = null)
            }
        }
    )

    init {
        activeDeviceMonitor.onDeviceConnectionStateChange.onEach {
            val myDevice = activeDeviceMonitor.allDevices.find { it.device == device }
            val error = State.Connected.ErrorBannerState.ConnectionLost.takeUnless {
                myDevice?.isConnected == true
            }
            state.value = when {
                myDevice == null || !myDevice.isCompatibleMockzillaVersion -> State.UnsupportedDeviceMockzillaVersion
                else -> State.Connected(
                    activeDevice = myDevice,
                    error = error,
                    selectedEndpoint = (state.value as? State.Connected)?.selectedEndpoint
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

    private fun EventBus.handleNewErrorEvents() = events
        .filterIsInstance<Event.GenericError>()
        .filter {
            (state.value as? State.Connected)?.activeDevice?.isConnected == true
        }
        .onEach {
            val apiError = it.error as? FailedHttpResponseException
            state.value = (state.value as? State.Connected)?.copy(
                error = State.Connected.ErrorBannerState.ApiError(
                    status = apiError?.statusCode,
                    rawError = apiError?.body ?: it.error.toString(),
                    operation = it.operation
                )
            ) ?: state.value
        }
        .launchIn(viewModelScope)

    public fun setSelectedEndpoint(key: EndpointConfiguration.Key?) {
        val currentState = state.value as? State.Connected ?: return
        state.value = currentState.copy(selectedEndpoint = key)
    }

    public fun refreshAll() {
        eventBus.send(Event.FullRefresh)
    }

    public fun dismissError() {
        val currentState = state.value as? State.Connected ?: return
        state.value = currentState.copy(error = null)
    }

    @InternalMockzillaApi
    public sealed class State {
        @InternalMockzillaApi
        public data object UnsupportedDeviceMockzillaVersion : State()

        @InternalMockzillaApi
        public data class Connected(
            val activeDevice: StatefulDevice,
            val selectedEndpoint: EndpointConfiguration.Key?,
            val error: ErrorBannerState? = null
        ) : State() {
            @InternalMockzillaApi
            public sealed class ErrorBannerState {
                @InternalMockzillaApi
                public data object ConnectionLost : ErrorBannerState()
                @InternalMockzillaApi
                public data class ApiError(
                    val status: HttpStatusCode?,
                    val rawError: String?,
                    val operation: GenericErrorableOperation?
                ) : ErrorBannerState()
            }
        }
    }
}
