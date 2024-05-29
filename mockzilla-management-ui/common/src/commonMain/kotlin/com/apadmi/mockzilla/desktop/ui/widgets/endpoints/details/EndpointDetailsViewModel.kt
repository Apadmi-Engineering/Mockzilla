package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.details

import androidx.compose.runtime.mutableStateOf
import com.apadmi.mockzilla.desktop.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.viewmodel.SelectedDeviceMonitoringViewModel
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig
import com.apadmi.mockzilla.management.MockzillaManagement
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EndpointDetailsViewModel(
    private val endpointsService: MockzillaManagement.EndpointsService,
    private val updateService: MockzillaManagement.UpdateService,
    private val clearingService: MockzillaManagement.CacheClearingService,
    activeDeviceMonitor: ActiveDeviceMonitor,
    scope: CoroutineScope? = null
) : SelectedDeviceMonitoringViewModel(activeDeviceMonitor, scope) {
    // using mutableStateOf here to avoid latency issues with text input
    // see https://medium.com/androiddevelopers/effective-state-management-for-textfield-in-compose-d6e5b070fbe5
    // for reasons
    val state = mutableStateOf<State>(State.Empty)

    private var delayDebounceJob: Job? = null
    private var defaultHeadersDebounceJob: Job? = null
    private var defaultBodyDebounceJob: Job? = null
    private var errorBodyDebounceJob: Job? = null
    private var errorHeadersDebounceJob: Job? = null


    override suspend fun reloadData(selectedDevice: Device?) {
        val device = selectedDevice ?: return run {
            state.value = State.Empty
        }

        // TODO: Actually picking correct endpoint to view details for
        val endpoints = endpointsService.fetchAllEndpointConfigs(device).map { it.firstOrNull() }

        state.value = endpoints.fold(
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

    fun onDefaultBodyChange(value: String?) = onPropertyChanged(
        { copy(defaultBody = value) },
        { config, device ->
            defaultBodyDebounceJob = withDebounce(defaultBodyDebounceJob) {
                emitErrorIfNeeded(updateService.setDefaultBody(device, config.key, value))
            }
        }
    )

    fun onDefaultStatusChange(value: HttpStatusCode?) = onPropertyChanged(
        { copy(defaultStatus = value) },
        { config, device -> emitErrorIfNeeded(updateService.setDefaultStatus(device, config.key, value)) }
    )


    private suspend fun withDebounce(job: Job?, op: suspend () -> Result<Unit>) = coroutineScope {
        job?.cancel()
        launch {
            delay(250)
            op()
        }
    }

    private fun <T> emitErrorIfNeeded(result: Result<T>) = result.onFailure {
        state.value = when (val state = state.value) {
            is State.Empty -> state
            is State.Endpoint -> state.copy(error = "Something went wrong")
        }
    }

    fun onErrorBodyChange(value: String?) = onPropertyChanged(
        { copy(errorBody = value) },
        { config, device ->
            errorBodyDebounceJob = withDebounce(errorBodyDebounceJob) {
                emitErrorIfNeeded(updateService.setErrorBody(device, config.key, value))
            }
        }
    )

    private fun onPropertyChanged(
        updateState: State.Endpoint.() -> State.Endpoint,
        updateServer: suspend (config: SerializableEndpointConfig, device: Device) -> Unit
    ) {
        // TODO: Handle error
        val activeDevice = this.activeDevice ?: return

        state.value = when (val state = state.value) {
            is State.Empty -> state
            is State.Endpoint -> {
                viewModelScope.launch { updateServer(state.config, activeDevice) }
                updateState(state)
            }
        }
    }

    fun onErrorStatusChange(value: HttpStatusCode?) = onPropertyChanged(
        { copy(errorStatus = value) },
        { config, device -> emitErrorIfNeeded(updateService.setErrorStatus(device, config.key, value)) }
    )

    fun onFailChange(value: Boolean?) = onPropertyChanged(
        { copy(fail = value) },
        { config, device -> emitErrorIfNeeded(updateService.setShouldFail(device, listOf(config.key), value)) }
    )

    // Could possibly have numerical picker rather than free text field for this one
    fun onDelayChange(value: String?) = onPropertyChanged(
        { copy(delayMillis = value.takeIf { value == null || value.toIntOrNull() != null }) },
        { config, device ->
            delayDebounceJob = withDebounce(delayDebounceJob) {
                emitErrorIfNeeded(updateService.setDelay(device, listOf(config.key), value?.toIntOrNull()))
            }
        }
    )

    fun onJsonDefaultEditingChange(value: Boolean) = onPropertyChanged(
        { copy(jsonEditingDefault = value) },
        { _,_ -> /* No-op */ }
    )

    fun onJsonErrorEditingChange(value: Boolean) = onPropertyChanged(
        { copy(jsonEditingError = value) },
        { _,_ -> /* No-op */ }
    )

    fun onDefaultHeadersChange(value: List<Pair<String, String>>?) = onPropertyChanged(
        { copy(defaultHeaders = value) },
        { config, device ->
            defaultHeadersDebounceJob = withDebounce(defaultHeadersDebounceJob) {
                emitErrorIfNeeded(updateService.setDefaultHeaders(device, config.key, value?.toMap()))
            }
        }
    )

    fun onErrorHeadersChange(value: List<Pair<String, String>>?) = onPropertyChanged(
        { copy(errorHeaders = value) },
        { config, device ->
            errorHeadersDebounceJob = withDebounce(errorHeadersDebounceJob) {
                emitErrorIfNeeded(updateService.setErrorHeaders(device, config.key, value?.toMap()))
            }
        }
    )

    fun onResetAll()  = viewModelScope.launch {
        val activeDevice = this@EndpointDetailsViewModel.activeDevice ?: return@launch
        val state = state.value as? State.Endpoint ?: return@launch

        // TODO: Loading and error states here
        emitErrorIfNeeded(
            clearingService.clearCaches(activeDevice, listOf(state.config.key))
        ).onSuccess {
            reloadData(activeDevice)
        }
    }

    sealed class State {
        data object Empty : State()

        /**
         * @property config
         * @property defaultBody
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
            val error: String?, // TODO: Make this more robust
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
