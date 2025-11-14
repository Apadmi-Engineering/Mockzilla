package com.apadmi.mockzilla.ui.ui.common.widgets.globalcontrols

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.events.EventBus
import com.apadmi.mockzilla.ui.ui.common.components.ForceFailureBannerState
import com.apadmi.mockzilla.ui.ui.common.utils.withDebounce
import com.apadmi.mockzilla.ui.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class GlobalControlsViewModel(
    private val device: Device,
    private val endpointsService: MockzillaManagement.EndpointsService,
    private val updateService: MockzillaManagement.UpdateService,
    private val clearingService: MockzillaManagement.CacheClearingService,
    private val eventBus: EventBus,
    scope: CoroutineScope? = null,
) : ViewModel(scope) {
    val state = MutableStateFlow<State>(State.Loading)
    var latencyDebounceJob: Job? = null

    init {
        eventBus.events.filter { it is EventBus.Event.EndpointDataChanged || it is EventBus.Event.FullRefresh }
            .onEach { reloadData() }
            .launchIn(viewModelScope)

        viewModelScope.launch { reloadData() }
    }

    suspend fun reloadData() {
        state.value = endpointsService.fetchAllEndpointConfigs(device).fold(
            onSuccess = { endpoints ->
                State.Idle(
                    initialLatencyMs = endpoints.firstOrNull()?.delayMs?.takeIf {
                        endpoints.all { it.delayMs == endpoints.firstOrNull()?.delayMs }
                    },
                    apiFailureState = when {
                        endpoints.all { it.shouldFail == true } -> ForceFailureBannerState.FullFailure
                        endpoints.none { it.shouldFail == true } -> ForceFailureBannerState.Normal
                        else -> ForceFailureBannerState.PartialFailure
                    },
                    isLoading = false
                )
            },
            onFailure = {
                eventBus.send(EventBus.Event.GenericError)
                State.Loading
            }
        )
    }

    fun resetAll() = viewModelScope.launch {
        setStateLoading()
        clearingService.clearAllCaches(device).onSuccess {
            eventBus.send(EventBus.Event.FullRefresh)
        }
    }

    fun restoreApi() = viewModelScope.launch {
        setStateLoading()
        getAllKeys().map { keys ->
            updateService.setShouldFail(device, keys, false).handleResult(keys)
        }
    }

    fun forceFailure() = viewModelScope.launch {
        setStateLoading()
        getAllKeys().map { keys ->
            updateService.setShouldFail(device, keys, true).handleResult(keys)
        }
    }

    private suspend fun getAllKeys() = endpointsService.fetchAllEndpointConfigs(device)
        .map { endpoints -> endpoints.map { it.key } }

    private fun Result<Unit>.handleResult(keys: List<EndpointConfiguration.Key>) = onSuccess {
        eventBus.send(EventBus.Event.EndpointDataChanged(keys))
    }.onFailure {
        eventBus.send(EventBus.Event.GenericError)
    }

    private fun setStateLoading() {
        val current = state.value as? State.Idle ?: return
        state.value = current.copy(isLoading = true)
    }

    fun updateLatency(latencyMs: Int) {
        suspend fun update(): Result<Unit> {
            setStateLoading()
            return getAllKeys().map { keys ->
                updateService.setDelay(device, keys, latencyMs).handleResult(keys)
            }
        }

        latencyDebounceJob = withDebounce(latencyDebounceJob, ::update)
    }

    fun resetLatency() = viewModelScope.launch {
        setStateLoading()
        getAllKeys().map { keys ->
            updateService.setDelay(device, keys, null).handleResult(keys)
        }
    }

    sealed class State {
        data object Loading : State()
        /**
         * @property initialLatencyMs
         * @property apiFailureState
         * @property isLoading
         */
        data class Idle(
            val initialLatencyMs: Int?,
            val apiFailureState: ForceFailureBannerState,
            val isLoading: Boolean
        ) : State()
    }
}
