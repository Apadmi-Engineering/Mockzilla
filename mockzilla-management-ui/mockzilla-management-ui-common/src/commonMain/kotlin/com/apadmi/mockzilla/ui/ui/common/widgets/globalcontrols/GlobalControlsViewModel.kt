@file:NoKDoc

package com.apadmi.mockzilla.ui.ui.common.widgets.globalcontrols

import com.apadmi.mockzilla.lib.NoKDoc
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.events.EventBus
import com.apadmi.mockzilla.ui.engine.events.GenericErrorableOperation
import com.apadmi.mockzilla.ui.internal.viewmodel.ViewModel
import com.apadmi.mockzilla.ui.ui.common.components.ForceFailureBannerState
import com.apadmi.mockzilla.ui.ui.common.utils.withDebounce
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointProperties
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointsViewModel

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
                val endpointConfigs = endpoints.map {
                    EndpointsViewModel.State.EndpointConfig(
                        key = it.key,
                        name = it.name,
                        fail = it.shouldFail == true,
                        overriddenProperties = it.getOverriddenProperties(),
                        delayMs = it.delayMs,
                    )
                }
                State.Idle(
                    initialLatencyMs = endpoints.firstOrNull()?.delayMs?.takeIf {
                        endpoints.all { it.delayMs == endpoints.firstOrNull()?.delayMs }
                    },
                    apiFailureState = when {
                        endpoints.all { it.shouldFail == true } -> ForceFailureBannerState.FullFailure
                        endpoints.none { it.shouldFail == true } -> ForceFailureBannerState.Normal
                        else -> ForceFailureBannerState.PartialFailure
                    },
                    endpoints = endpointConfigs,
                    activeOverridesCount = endpointConfigs.count { it.overriddenProperties.isNotEmpty() || it.fail },
                    isLoading = false
                )
            },
            onFailure = {
                eventBus.send(EventBus.Event.GenericError(GenericErrorableOperation.UpdateGlobalOverrides, it))
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

    private fun Result<Unit>.handleResult(
        keys: List<EndpointConfiguration.Key>,
        operation: GenericErrorableOperation = GenericErrorableOperation.UpdateGlobalOverrides
    ) = onSuccess {
        eventBus.send(EventBus.Event.EndpointDataChanged(keys))
    }.onFailure {
        eventBus.send(EventBus.Event.GenericError(operation, it))
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

    private fun SerializableEndpointConfig.getOverriddenProperties() = listOfNotNull(
        EndpointProperties.Delay.takeIf { delayMs != null },
        EndpointProperties.Body.takeIf { defaultBody != null || appliedPresetOverride?.response?.body != null },
        EndpointProperties.Status.takeIf { defaultStatus != null || appliedPresetOverride?.response?.statusCode != null },
        EndpointProperties.Headers.takeIf { defaultHeaders != null || appliedPresetOverride?.response?.headers != null }
    )

    sealed class State {
        data object Loading : State()

        data class Idle(
            val initialLatencyMs: Int?,
            val apiFailureState: ForceFailureBannerState,
            val endpoints: List<EndpointsViewModel.State.EndpointConfig>,
            val activeOverridesCount: Int,
            val isLoading: Boolean
        ) : State()
    }
}
