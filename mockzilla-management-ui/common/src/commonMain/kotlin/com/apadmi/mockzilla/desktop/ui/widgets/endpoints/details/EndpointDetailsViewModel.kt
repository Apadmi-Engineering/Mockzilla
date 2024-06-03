package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.details

import androidx.compose.runtime.mutableStateOf

import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.engine.events.EventBus
import com.apadmi.mockzilla.desktop.ui.widgets.endpoints.details.EndpointDetailsViewModel.*
import com.apadmi.mockzilla.desktop.viewmodel.ViewModel
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.management.MockzillaManagement

import io.ktor.http.HttpStatusCode

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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
            it is EventBus.Event.FullRefresh || (it as? EventBus.Event.EndpointDataChanged)?.keys?.contains(key) == true
        }
            .onEach { reloadData() }
            .launchIn(viewModelScope)

        viewModelScope.launch { reloadData() }
    }

    private suspend fun reloadData() {
        val endpoint = endpointsService.fetchAllEndpointConfigs(device).map { endpoint ->
            endpoint.firstOrNull { it.key == key }
        }

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
                                // TODO: Should auto infer based on if body is valid JSON
                                jsonEditingDefault = true,
                                jsonEditingError = true,
                                presets = presets,
                                error = null
                            )
                        },
                        onFailure = { State.Empty }
                    )
                } ?: State.Empty
            },
            onFailure = { State.Empty }
        )
    }

    fun onDefaultBodyChange(value: String?) = onPropertyChanged({ copy(defaultBody = value) },
        { config, device ->
            defaultBodyDebounceJob = withDebounce(defaultBodyDebounceJob) {
                emitErrorAndEventIfNeeded(updateService.setDefaultBody(device, config.key, value))
            }
        }
    )

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

    private suspend fun withDebounce(job: Job?, op: suspend () -> Result<Unit>) = coroutineScope {
        job?.cancel()
        launch {
            delay(250)
            op()
        }
    }

    private fun <T> emitErrorAndEventIfNeeded(result: Result<T>) = result.onSuccess {
        key?.let { eventBus.send(EventBus.Event.EndpointDataChanged(listOf(it))) }
    }.onFailure {
        state.value = when (val state = state.value) {
            is State.Empty -> state
            is State.Endpoint -> state.copy(error = "Something went wrong")
        }
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
        // TODO: Handle error
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
         * @property error
         */
        data class Endpoint(
            val config: SerializableEndpointConfig,
            val error: String?,
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
        ) : State()
    }
}
