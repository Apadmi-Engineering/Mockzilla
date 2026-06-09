package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.events.EventBus
import com.apadmi.mockzilla.ui.engine.filter.FuzzyFilter
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointsViewModel.State
import com.apadmi.mockzilla.ui.viewmodel.ViewModel

import kotlin.collections.filter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/** Controls how much information each row in the endpoint list shows. */
internal enum class RowDensity {
    Comfy, Compact
}

internal class EndpointsViewModel(
    private val device: Device,
    private val endpointsService: MockzillaManagement.EndpointsService,
    private val eventBus: EventBus,
    scope: CoroutineScope? = null,
) : ViewModel(scope) {
    val state = MutableStateFlow<State>(State.Loading)

    init {
        eventBus.events.filter { it is EventBus.Event.EndpointDataChanged || it is EventBus.Event.FullRefresh }
            .onEach { reloadData() }
            .launchIn(viewModelScope)

        viewModelScope.launch { reloadData() }
    }

    private suspend fun reloadData() {
        state.value = endpointsService.fetchAllEndpointConfigs(device).fold(
            onSuccess = { list ->
                val currentState = state.value as? State.EndpointsList
                State.EndpointsList(
                    allEndpoints = list.map {
                        State.EndpointConfig(
                            key = it.key,
                            name = it.name,
                            fail = it.shouldFail == true,
                            overriddenProperties = it.getOverriddenProperties(),
                            delayMs = it.delayMs,
                        )
                    },
                    filter = currentState?.filter ?: "",
                    rowDensity = currentState?.rowDensity ?: RowDensity.Comfy,
                )
            },
            onFailure = {
                eventBus.send(EventBus.Event.GenericError)
                State.Loading
            }
        )
    }

    fun onFilterChanged(value: String) {
        val currentState = state.value as? State.EndpointsList ?: return
        state.value = currentState.copy(filter = value)
    }

    fun onRowDensityChanged(rowDensity: RowDensity) {
        val currentState = state.value as? State.EndpointsList ?: return
        state.value = currentState.copy(rowDensity = rowDensity)
    }

    sealed class State {
        data object Loading : State()

        /**
         * @property key
         * @property name
         * @property fail
         * @property overriddenProperties
         * @property delayMs Overridden delay in milliseconds, or null if not overridden.
         */
        data class EndpointConfig(
            val key: EndpointConfiguration.Key,
            val name: String,
            val fail: Boolean,
            val overriddenProperties: List<EndpointProperties>,
            val delayMs: Int?,
        )

        /**
         * @property allEndpoints Endpoints list before applying the filter string
         * @property filter Filter string
         * @property rowDensity Display density for the list rows
         */
        data class EndpointsList(
            val allEndpoints: List<EndpointConfig>,
            val filter: String,
            val rowDensity: RowDensity = RowDensity.Comfy,
        ) : State() {
            /**
             * Filtered endpoints
             */
            val endpoints: List<EndpointConfig> = filterEndpoints(filter, allEndpoints)
        }
    }
}

/**
 * @property displayName
 */
internal enum class EndpointProperties(val displayName: String) {
    Body("Body"),
    Delay("Latency"),
    Headers("Headers"),
    Status("Status"),
    ;
}

private fun SerializableEndpointConfig.getOverriddenProperties() = listOfNotNull(
    EndpointProperties.Delay.takeIf { delayMs != null },
    EndpointProperties.Body.takeIf { defaultBody != null || appliedPresetOverride?.response?.body != null },
    EndpointProperties.Status.takeIf { defaultStatus != null || appliedPresetOverride?.response?.statusCode != null },
    EndpointProperties.Headers.takeIf { defaultHeaders != null || appliedPresetOverride?.response?.headers != null }
)

private fun filterEndpoints(
    filter: String,
    endpoints: List<State.EndpointConfig>,
): List<State.EndpointConfig> = FuzzyFilter
    .filter(endpoints, filter) { it.name }
