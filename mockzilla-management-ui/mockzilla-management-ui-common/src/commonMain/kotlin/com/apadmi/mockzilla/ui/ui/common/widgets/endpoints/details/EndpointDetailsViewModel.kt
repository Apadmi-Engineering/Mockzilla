package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.events.EventBus
import com.apadmi.mockzilla.ui.engine.events.EventBus.Event
import com.apadmi.mockzilla.ui.engine.events.GenericErrorableOperation
import com.apadmi.mockzilla.ui.ui.common.utils.withDebounce
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset.deriveLegacyPreset
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.RowDensity
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

internal class EndpointDetailsViewModel(
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

    internal fun retry() = viewModelScope.launch { reloadData() }

    private suspend fun reloadData() {
        val endpointResult = endpointsService.fetchAllEndpointConfigs(device).map { endpoints ->
            endpoints.firstOrNull { it.key == key }
        }

        key ?: run {
            state.value = State.Empty
            return
        }

        val config = endpointResult.getOrElse {
            eventBus.send(
                Event.GenericError(
                    GenericErrorableOperation.FetchEndpointConfigs,
                    it
                )
            )
            state.value = State.FailedToLoad
            return
        } ?: run {
            state.value = State.FailedToLoad
            return
        }

        val currentState = state.value as? State.Endpoint
        state.value = State.Endpoint(
            config = config,
            fail = config.shouldFail,
            delayMillis = config.delayMs,
            isLoading = false,
            layoutMode = currentState?.layoutMode ?: RowDensity.Compact,
            presets = currentState?.presets ?: State.Endpoint.Presets.Loading
        )

        loadPresets(config)
    }

    private suspend fun loadPresets(config: SerializableEndpointConfig) {
        endpointsService.fetchDashboardOptionsConfig(device, config.key).fold(
            onSuccess = { presets ->
                val currentState = state.value as? State.Endpoint ?: return
                val filter = (currentState.presets as? State.Endpoint.Presets.Populated)?.filter
                state.value = currentState.copy(
                    presets = State.Endpoint.Presets.Populated(
                        appliedPreset = config.appliedPresetOverride ?: presets.presets.firstOrNull {
                            it.response == config.deriveLegacyPreset()?.response
                        } ?: config.deriveLegacyPreset(),
                        visiblePresets = presets.presets.filter(filter),
                        allPresets = listOfNotNull(
                            config.appliedPresetOverride?.takeIf { it.isManagementUiDefinedCustomPreset }
                        ) + presets.presets,
                        filter = filter ?: ""
                    )
                )
            },
            onFailure = {
                val currentState = state.value as? State.Endpoint ?: return
                eventBus.send(
                    Event.GenericError(
                        GenericErrorableOperation.FetchDashboardOptionsConfig,
                        it
                    )
                )
                state.value = currentState.copy(presets = State.Endpoint.Presets.Error)
            }
        )
    }

    private fun <T> handleResult(
        result: Result<T>,
        operation: GenericErrorableOperation = GenericErrorableOperation.UpdateMockData
    ) = result.onSuccess {
        key?.let { eventBus.send(EventBus.Event.EndpointDataChanged(listOf(it))) }
    }.onFailure {
        eventBus.send(EventBus.Event.GenericError(operation, it))
    }

    private fun onPropertyChanged(
        updateState: UpdateStateBlock,
        updateServer: UpdateServerBlock
    ) {
        setStateLoading()
        state.value = when (val state = state.value) {
            is State.FailedToLoad,
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
        val newPresets = (presets as? State.Endpoint.Presets.Populated)?.let {
            it.copy(appliedPreset = dashboardOverridePreset)
        } ?: presets
        copy(presets = newPresets)
    }, { config, device ->
        viewModelScope.launch {
            handleResult(
                updateService.applyPreset(
                    device,
                    config.key,
                    dashboardOverridePreset
                ).onSuccess {
                    eventBus.send(EventBus.Event.PresetApplied)
                },
                operation = GenericErrorableOperation.ApplyPreset
            )
        }
    })

    fun onFilterPresetChanged(filter: String): Unit = onPropertyChanged({
        val newPresets = (presets as? State.Endpoint.Presets.Populated)?.let {
            it.copy(
                filter = filter,
                visiblePresets = it.allPresets.filter(filter)
            )
        } ?: presets
        copy(presets = newPresets)
    }, { _, _ -> })

    fun onRowDensityChanged(layoutMode: RowDensity) = onPropertyChanged({
        copy(layoutMode = layoutMode)
    }, { _, _ -> })

    private fun setStateLoading() {
        val current = state.value as? State.Endpoint ?: return
        state.value = current.copy(isLoading = true)
    }

    sealed class State {
        data object Empty : State()
        data object FailedToLoad : State()

        /**
         * @property config
         * @property fail
         * @property delayMillis
         * @property presets
         * @property isLoading
         * @property layoutMode
         */
        data class Endpoint(
            val config: SerializableEndpointConfig,
            val fail: Boolean?,
            val delayMillis: Int?,
            val isLoading: Boolean,
            val layoutMode: RowDensity,
            val presets: Presets,
        ) : State() {
            sealed class Presets {
                data object Loading : Presets()
                data object Error : Presets()
                /**
                 * @property appliedPreset
                 * @property visiblePresets
                 * @property allPresets
                 * @property filter
                 */
                data class Populated(
                    val appliedPreset: DashboardOverridePreset?,
                    val visiblePresets: List<DashboardOverridePreset>,
                    val allPresets: List<DashboardOverridePreset>,
                    val filter: String
                ) : Presets()
            }
        }
    }
}

private fun List<DashboardOverridePreset>.filter(filter: String?): List<DashboardOverridePreset> = filter { preset ->
    filter.isNullOrBlank() || sequenceOf(preset.name, preset.description).any {
        it?.lowercase()?.contains(filter.lowercase()) == true
    }
}
