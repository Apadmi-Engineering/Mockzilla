package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.PartialMockzillaHttpResponse
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

private typealias UpdateServerBlock = (config: SerializableEndpointConfig, device: Device) -> Unit
private typealias UpdateStateBlock = EndpointDetailsViewModel.State.Endpoint.() -> EndpointDetailsViewModel.State.Endpoint

class EndpointDetailsViewModel(
    private val key: EndpointConfiguration.Key?,
    private val device: Device,
    private val endpointsService: MockzillaManagement.EndpointsService,
    private val updateService: MockzillaManagement.UpdateService,
    private val clearingService: MockzillaManagement.CacheClearingService,
    private val eventBus: EventBus,
    scope: CoroutineScope? = null
) : ViewModel(scope) {
    val state = MutableStateFlow<State>(State.Empty)
    private var delayDebounceJob: Job? = null

    init {
        eventBus.events.filter {
            it is EventBus.Event.FullRefresh || (it as? EventBus.Event.EndpointDataChanged)?.keys?.contains(
                key
            ) == true
        }
            .onEach { reloadData() }
            .launchIn(viewModelScope)

        viewModelScope.launch { reloadData() }
    }

    @Suppress("TOO_LONG_FUNCTION")
    private suspend fun reloadData() {
        val endpoint = endpointsService.fetchAllEndpointConfigs(device).map { endpoint ->
            endpoint.firstOrNull { it.key == key }
        }

        state.value = endpoint.fold(
            onSuccess = { config ->
                config?.let {
                    endpointsService.fetchDashboardOptionsConfig(device, config.key).fold(
                        onSuccess = { presets ->
                            val currentState = state.value
                            val filter = (currentState as? State.Endpoint)?.presets?.filter
                            State.Endpoint(
                                config = config,
                                fail = config.shouldFail,
                                delayMillis = config.delayMs,
                                isLoading = false,
                                presets = State.Endpoint.Presets(
                                    appliedPreset = config.appliedPresetOverride ?: presets.presets.firstOrNull {
                                        // Remove all this once deprecated properties are removed
                                        it.response == PartialMockzillaHttpResponse(
                                            body = config.defaultBody,
                                            statusCode = config.defaultStatus,
                                            headers = config.defaultHeaders
                                        )
                                    },
                                    visiblePresets = presets.presets.filter(filter),
                                    allPresets = presets.presets,
                                    filter = filter ?: ""
                                ),
                            )
                        },
                        onFailure = { State.Empty }
                    )
                } ?: State.Empty
            },
            onFailure = {
                eventBus.send(EventBus.Event.GenericError)
                State.Empty
            }
        )
    }

    private fun <T> handleResult(result: Result<T>) = result.onSuccess {
        key?.let { eventBus.send(EventBus.Event.EndpointDataChanged(listOf(it))) }
    }.onFailure {
        eventBus.send(EventBus.Event.GenericError)
    }

    private fun onPropertyChanged(
        updateState: UpdateStateBlock,
        updateServer: UpdateServerBlock
    ) {
        setStateLoading()
        state.value = when (val state = state.value) {
            is State.Empty -> state
            is State.Endpoint -> {
                updateServer(state.config, device)
                updateState(state)
            }
        }
    }

    fun onFailChange(value: Boolean?) = onPropertyChanged({ copy(fail = value) },
        { config, device ->
            viewModelScope.launch {
                handleResult(
                    updateService.setShouldFail(
                        device,
                        listOf(config.key),
                        value
                    )
                )
            }
        }
    )

    fun updateLatency(value: Int?) =
        onPropertyChanged({ copy(delayMillis = value) },
            { config, device ->
                delayDebounceJob = withDebounce(delayDebounceJob) {
                    handleResult(
                        updateService.setDelay(
                            device,
                            listOf(config.key),
                            value
                        )
                    )
                }
            }
        )

    fun onResetAll() = viewModelScope.launch {
        val state = state.value as? State.Endpoint ?: return@launch

        setStateLoading()
        handleResult(
            clearingService.clearCaches(device, listOf(state.config.key))
        )
    }

    fun onPresetSelected(
        dashboardOverridePreset: DashboardOverridePreset
    ) = onPropertyChanged({
        copy(
            presets = presets.copy(dashboardOverridePreset),
        )
    }, { config, device ->
        viewModelScope.launch {
            handleResult(
                updateService.applyPreset(
                    device,
                    config.key,
                    dashboardOverridePreset
                ).onSuccess {
                    eventBus.send(EventBus.Event.PresetApplied)
                }
            )
        }
    })

    fun onFilterPresetChanged(filter: String): Unit = onPropertyChanged({
        copy(
            presets = presets.copy(
                filter = filter,
                visiblePresets = presets.allPresets.filter(filter)
            )
        )
    }, { _, _ -> })

    private fun setStateLoading() {
        val current = state.value as? State.Endpoint ?: return
        state.value = current.copy(isLoading = true)
    }

    sealed class State {
        data object Empty : State()

        /**
         * @property config
         * @property fail
         * @property delayMillis
         * @property presets
         * @property isLoading
         */
        data class Endpoint(
            val config: SerializableEndpointConfig,
            val fail: Boolean?,
            val delayMillis: Int?,
            val isLoading: Boolean,
            val presets: Presets,
        ) : State() {
            /**
             * @property appliedPreset
             * @property visiblePresets
             * @property allPresets
             * @property filter
             */
            data class Presets(
                val appliedPreset: DashboardOverridePreset?,
                val visiblePresets: List<DashboardOverridePreset>,
                val allPresets: List<DashboardOverridePreset>,
                val filter: String
            )
        }
    }
}

private fun List<DashboardOverridePreset>.filter(filter: String?): List<DashboardOverridePreset> = filter { preset ->
    filter.isNullOrBlank() || sequenceOf(preset.name, preset.description).any {
        it?.lowercase()?.contains(filter.lowercase()) == true
    }
}
