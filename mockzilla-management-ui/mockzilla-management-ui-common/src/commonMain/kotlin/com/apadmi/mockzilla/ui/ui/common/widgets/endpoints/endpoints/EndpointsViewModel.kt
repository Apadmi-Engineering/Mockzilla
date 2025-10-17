package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.engine.events.EventBus
import com.apadmi.mockzilla.ui.viewmodel.ViewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class EndpointsViewModel(
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
            onSuccess = {
                val currentState = state.value as? State.EndpointsList
                State.EndpointsList(it.toConfig(), currentState?.filter ?: "")
            },
            onFailure = {
                eventBus.send(EventBus.Event.GenericError)
                State.Loading
            }
        )
    }

    private fun List<SerializableEndpointConfig>.toConfig(): List<State.EndpointConfig> {
        val currentState = state.value as? State.EndpointsList

        return map {
            State.EndpointConfig(
                key = it.key,
                name = it.name,
                fail = it.shouldFail == true,
                overriddenProperties = it.getOverriddenProperties(),
                display = filter(currentState?.filter ?: "", it.name)
            )
        }
    }

    fun onFilterChanged(value: String) {
        val currentState = state.value as? State.EndpointsList ?: return

        state.value = currentState.copy(
            endpoints = currentState.endpoints.map {
                it.copy(display = filter(value, it.name))
            },
            filter = value
        )
    }

    private fun filter(value: String, name: String) = name.lowercase().contains(value.lowercase())

    sealed class State {
        data object Loading : State()

        /**
         * @property key
         * @property name
         * @property fail
         * @property overriddenProperties
         * @property display
         */
        data class EndpointConfig(
            val key: EndpointConfiguration.Key,
            val name: String,
            val fail: Boolean,
            val overriddenProperties: List<EndpointProperties>,
            val display: Boolean,
        )

        /**
         * @property endpoints
         * @property filter
         */
        data class EndpointsList(
            val endpoints: List<EndpointConfig>,
            val filter: String,
        ) : State()
    }
}

/**
 * @property displayName
 */
enum class EndpointProperties(val displayName: String) {
    Body("Body"),
    Delay("Delay"),
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
