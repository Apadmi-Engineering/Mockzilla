package com.apadmi.mockzilla.ui.ui.common.widgets.globalcontrols

import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.events.EventBus
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

    private suspend fun reloadData() {
        state.value = endpointsService.fetchAllEndpointConfigs(device).fold(
            onSuccess = { endpoints ->
                State.Idle(
                    initialLatencyMs = endpoints.firstOrNull()?.delayMs?.takeIf {
                        endpoints.all { it.delayMs == endpoints.firstOrNull()?.delayMs }
                    },
                    apiFailureState = when {
                        endpoints.all { it.shouldFail == true } -> State.ApiFailureState.FullFailure
                        endpoints.none { it.shouldFail == true } -> State.ApiFailureState.Normal
                        else -> State.ApiFailureState.PartialFailure
                    }
                )
            },
            onFailure = {
                eventBus.send(EventBus.Event.GenericError)
                State.Loading
            }
        )
    }

    fun resetAll() = viewModelScope.launch {
        clearingService.clearAllCaches(device).onSuccess {
            eventBus.send(EventBus.Event.FullRefresh)
        }
    }

    fun restoreApi() {

    }

    fun forceFailure() {

    }

    fun updateLatency(latencyMs: Int) {
        suspend fun update(): Result<Unit> =
            endpointsService.fetchAllEndpointConfigs(device).map { endpoints ->
                val keys = endpoints.map { it.key }
                return updateService.setDelay(device, keys, latencyMs).onSuccess {
                    eventBus.send(EventBus.Event.EndpointDataChanged(keys))
                }.onFailure {
                    eventBus.send(EventBus.Event.GenericError)
                }
            }

        latencyDebounceJob = withDebounce(latencyDebounceJob, ::update)
    }

    sealed class State {
        data object Loading: State()
        data class Idle(
            val initialLatencyMs: Int?,
            val apiFailureState: ApiFailureState
        ): State()

        enum class ApiFailureState {
            FullFailure,
            PartialFailure,
            Normal
        }
    }
}