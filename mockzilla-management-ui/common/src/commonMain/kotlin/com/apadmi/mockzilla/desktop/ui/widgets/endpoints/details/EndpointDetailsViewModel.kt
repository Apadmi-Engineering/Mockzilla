package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.details

import androidx.compose.runtime.mutableStateOf

import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.engine.events.EventBus
import com.apadmi.mockzilla.desktop.engine.jsoneditor.JsonEditor
import com.apadmi.mockzilla.desktop.ui.widgets.endpoints.details.EndpointDetailsViewModel.*
import com.apadmi.mockzilla.desktop.viewmodel.ViewModel
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.management.MockzillaManagement

import io.ktor.http.HttpStatusCode

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

private typealias UpdateServerBlock = suspend (config: SerializableEndpointConfig, device: Device) -> Unit
private typealias UpdateStateBlock = State.Endpoint.() -> State.Endpoint

class EndpointDetailsViewModel(
    private val key: EndpointConfiguration.Key?,
    private val device: Device,
    private val endpointsService: MockzillaManagement.EndpointsService,
    private val updateService: MockzillaManagement.UpdateService,
    private val clearingService: MockzillaManagement.CacheClearingService,
    private val eventBus: EventBus,
    scope: CoroutineScope? = null
) : ViewModel(scope) {
    // using mutableStateOf here to avoid latency issues with text input
    // see https://medium.com/androiddevelopers/effective-state-management-for-textfield-in-compose-d6e5b070fbe5
    // for reasons
    val state = mutableStateOf<State>(State.Empty)
    private var delayDebounceJob: Job? = null
    private var defaultHeadersDebounceJob: Job? = null
    private var defaultBodyDebounceJob: Job? = null
    private var errorBodyDebounceJob: Job? = null
    private var errorHeadersDebounceJob: Job? = null

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

        val currentState = state.value
        state.value = endpoint.fold(
            onSuccess = { config ->
                config?.let {
                    endpointsService.fetchDashboardOptionsConfig(device, config.key).fold(
                        onSuccess = { presets ->
                            State.Endpoint(
                                config = config,
                                defaultBody = config.defaultBody,
                                defaultStatus = config.defaultStatus,
                                defaultHeaders = config.defaultHeaders?.toList(),
                                errorBody = config.errorBody,
                                errorStatus = config.errorStatus,
                                errorHeaders = config.errorHeaders?.toList(),
                                fail = config.shouldFail,
                                delayMillis = config.delayMs?.toString(),
                                // Retain existing value for jsonEditing booleans so we don't
                                // swap states as the user is typing into the text field,
                                // but infer what the starting value should be on the first
                                // reload so we start with a reasonable default per endpoint.
                                jsonEditingDefault = (currentState as? State.Endpoint)
                                    ?.jsonEditingDefault
                                    ?: JsonEditor(config.defaultBody ?: "").isValidJson(),
                                jsonEditingError = (currentState as? State.Endpoint)
                                    ?.jsonEditingError
                                    ?: JsonEditor(config.errorBody ?: "").isValidJson(),
                                presets = presets
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

    fun onDefaultBodyChange(value: String?) {
        onPropertyChanged({ copy(defaultBody = value) },
            { config, device ->
                defaultBodyDebounceJob = withDebounce(defaultBodyDebounceJob) {
                    emitErrorAndEventIfNeeded(
                        updateService.setDefaultBody(
                            device,
                            config.key,
                            value
                        )
                    )
                }
            }
        )
    }

    fun onDefaultStatusChange(value: HttpStatusCode?) =
        onPropertyChanged({ copy(defaultStatus = value) },
            { config, device ->
                emitErrorAndEventIfNeeded(
                    updateService.setDefaultStatus(
                        device,
                        config.key,
                        value
                    )
                )
            }
        )

    private suspend fun withDebounce(job: Job?, op: suspend () -> Result<Unit>): Job {
        job?.cancel()
        return viewModelScope.launch(Dispatchers.IO) {
            delay(600)
            yield()
            op()
        }
    }

    private fun <T> emitErrorAndEventIfNeeded(result: Result<T>) = result.onSuccess {
        key?.let { eventBus.send(EventBus.Event.EndpointDataChanged(listOf(it))) }
    }.onFailure {
        eventBus.send(EventBus.Event.GenericError)
    }

    fun onErrorBodyChange(value: String?) = onPropertyChanged({ copy(errorBody = value) },
        { config, device ->
            errorBodyDebounceJob = withDebounce(errorBodyDebounceJob) {
                emitErrorAndEventIfNeeded(updateService.setErrorBody(device, config.key, value))
            }
        }
    )

    private fun onPropertyChanged(
        updateState: UpdateStateBlock,
        updateServer: UpdateServerBlock
    ) {
        state.value = when (val state = state.value) {
            is State.Empty -> state
            is State.Endpoint -> {
                viewModelScope.launch { updateServer(state.config, device) }
                updateState(state)
            }
        }
    }

    fun onErrorStatusChange(value: HttpStatusCode?) =
        onPropertyChanged({ copy(errorStatus = value) },
            { config, device ->
                emitErrorAndEventIfNeeded(
                    updateService.setErrorStatus(
                        device,
                        config.key,
                        value
                    )
                )
            }
        )

    fun onFailChange(value: Boolean?) = onPropertyChanged({ copy(fail = value) },
        { config, device ->
            emitErrorAndEventIfNeeded(
                updateService.setShouldFail(
                    device,
                    listOf(config.key),
                    value
                )
            )
        }
    )

    // Could possibly have numerical picker rather than free text field for this one
    fun onDelayChange(value: String?) =
        onPropertyChanged({ copy(delayMillis = value.takeIf { value == null || value.toIntOrNull() != null }) },
            { config, device ->
                delayDebounceJob = withDebounce(delayDebounceJob) {
                    emitErrorAndEventIfNeeded(
                        updateService.setDelay(
                            device,
                            listOf(config.key),
                            value?.toIntOrNull()
                        )
                    )
                }
            }
        )

    fun onJsonDefaultEditingChange(value: Boolean) =
        onPropertyChanged({ copy(jsonEditingDefault = value) },
            { _, _ -> /* No-op */ }
        )

    fun onJsonErrorEditingChange(value: Boolean) =
        onPropertyChanged({ copy(jsonEditingError = value) },
            { _, _ -> /* No-op */ }
        )

    fun onDefaultHeadersChange(value: List<Pair<String, String>>?) =
        onPropertyChanged({ copy(defaultHeaders = value) },
            { config, device ->
                defaultHeadersDebounceJob = withDebounce(defaultHeadersDebounceJob) {
                    emitErrorAndEventIfNeeded(
                        updateService.setDefaultHeaders(
                            device,
                            config.key,
                            value?.toMap()
                        )
                    )
                }
            }
        )

    fun onErrorHeadersChange(value: List<Pair<String, String>>?) =
        onPropertyChanged({ copy(errorHeaders = value) },
            { config, device ->
                errorHeadersDebounceJob = withDebounce(errorHeadersDebounceJob) {
                    emitErrorAndEventIfNeeded(
                        updateService.setErrorHeaders(
                            device,
                            config.key,
                            value?.toMap()
                        )
                    )
                }
            }
        )

    fun onResetAll() = viewModelScope.launch {
        val state = state.value as? State.Endpoint ?: return@launch

        // TODO: Loading and error states here
        emitErrorAndEventIfNeeded(
            clearingService.clearCaches(device, listOf(state.config.key))
        ).onSuccess { reloadData() }
    }

    fun onDefaultPresetSelected(
        dashboardOverridePreset: DashboardOverridePreset
    ) = onPropertyChanged({
        copy(
            defaultHeaders = dashboardOverridePreset.response.headers.toList(),
            defaultStatus = dashboardOverridePreset.response.statusCode,
            defaultBody = dashboardOverridePreset.response.body,
            jsonEditingDefault = JsonEditor(dashboardOverridePreset.response.body).isValidJson()
        )
    }, { config, device ->
        emitErrorAndEventIfNeeded(
            updateService.setDefaultPreset(
                device,
                config.key,
                dashboardOverridePreset
            )
        )
    })

    fun onErrorPresetSelected(
        dashboardOverridePreset: DashboardOverridePreset
    ) = onPropertyChanged({
        copy(
            errorHeaders = dashboardOverridePreset.response.headers.toList(),
            errorStatus = dashboardOverridePreset.response.statusCode,
            errorBody = dashboardOverridePreset.response.body,
            jsonEditingError = JsonEditor(dashboardOverridePreset.response.body).isValidJson()
        )
    }, { config, device ->
        emitErrorAndEventIfNeeded(
            updateService.setErrorPreset(
                device,
                config.key,
                dashboardOverridePreset
            )
        )
    })

    sealed class State {
        data object Empty : State()

        /**
         * @property config
         * @property defaultBody// TODO: Make this more robust
         * @property defaultStatus
         * @property errorBody
         * @property errorStatus
         * @property fail
         * @property delayMillis
         * @property jsonEditingDefault
         * @property jsonEditingError
         * @property presets
         * @property defaultHeaders
         * @property errorHeaders
         */
        data class Endpoint(
            val config: SerializableEndpointConfig,
            val defaultBody: String?,
            val defaultStatus: HttpStatusCode?,
            val defaultHeaders: List<Pair<String, String>>?,
            val errorBody: String?,
            val errorStatus: HttpStatusCode?,
            val errorHeaders: List<Pair<String, String>>?,
            val fail: Boolean?,
            val delayMillis: String?,
            val jsonEditingDefault: Boolean,
            val jsonEditingError: Boolean,
            val presets: DashboardOptionsConfig
        ) : State() {
            val defaultBodyJsonError: String? = defaultBody?.let { JsonEditor(it).parseError() }
            val errorBodyJsonError: String? = errorBody?.let { JsonEditor(it).parseError() }
        }
    }
}
