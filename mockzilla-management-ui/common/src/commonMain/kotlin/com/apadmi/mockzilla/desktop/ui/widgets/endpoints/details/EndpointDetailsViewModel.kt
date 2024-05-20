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

class EndpointDetailsViewModel(
    private val endpointsService: MockzillaManagement.EndpointsService,
    activeDeviceMonitor: ActiveDeviceMonitor,
    scope: CoroutineScope? = null
) : SelectedDeviceMonitoringViewModel(activeDeviceMonitor, scope) {
    // using mutableStateOf here to avoid latency issues with text input
    // see https://medium.com/androiddevelopers/effective-state-management-for-textfield-in-compose-d6e5b070fbe5
    // for reasons
    val state = mutableStateOf<State>(State.Empty)
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
                                errorBody = config.errorBody,
                                errorStatus = config.errorStatus,
                                fail = config.shouldFail,
                                delayMillis = config.delayMs.toString(),
                                // TODO: Should auto infer based on if body is valid JSON
                                jsonEditingDefault = true,
                                jsonEditingError = true,
                                presets = presets
                            )
                        },
                        onFailure = { State.Empty }
                    )
                } ?: State.Empty
            },
            onFailure = { State.Empty }
        )
    }

    fun onDefaultBodyChange(value: String?) {
        state.value = when (val state = state.value) {
            is State.Empty -> state
            is State.Endpoint -> state.copy(defaultBody = value)
        }
    }

    fun onDefaultStatusChange(value: HttpStatusCode?) {
        state.value = when (val state = state.value) {
            is State.Empty -> state
            is State.Endpoint -> state.copy(defaultStatus = value)
        }
    }

    fun onErrorBodyChange(value: String?) {
        state.value = when (val state = state.value) {
            is State.Empty -> state
            is State.Endpoint -> state.copy(errorBody = value)
        }
    }

    fun onErrorStatusChange(value: HttpStatusCode?) {
        state.value = when (val state = state.value) {
            is State.Empty -> state
            is State.Endpoint -> state.copy(errorStatus = value)
        }
    }

    fun onFailChange(value: Boolean?) {
        state.value = when (val state = state.value) {
            is State.Empty -> state
            is State.Endpoint -> state.copy(fail = value)
        }
    }

    // Could possibly have numerical picker rather than free text field for this one
    fun onDelayChange(value: String?) {
        state.value = when (val state = state.value) {
            is State.Empty -> state
            is State.Endpoint -> state.copy(delayMillis = value)
        }
    }

    fun onJsonDefaultEditingChange(value: Boolean) {
        state.value = when (val state = state.value) {
            is State.Empty -> state
            is State.Endpoint -> state.copy(jsonEditingDefault = value)
        }
    }

    fun onJsonErrorEditingChange(value: Boolean) {
        state.value = when (val state = state.value) {
            is State.Empty -> state
            is State.Endpoint -> state.copy(jsonEditingError = value)
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
         */
        data class Endpoint(
            val config: SerializableEndpointConfig,
            val defaultBody: String?,
            val defaultStatus: HttpStatusCode?,
            val errorBody: String?,
            val errorStatus: HttpStatusCode?,
            val fail: Boolean?,
            val delayMillis: String?,
            val jsonEditingDefault: Boolean,
            val jsonEditingError: Boolean,
            val presets: DashboardOptionsConfig
        ) : State()
    }
}
