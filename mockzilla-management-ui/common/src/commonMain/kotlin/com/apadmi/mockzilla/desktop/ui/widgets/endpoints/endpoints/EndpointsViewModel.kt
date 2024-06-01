package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.endpoints

import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.engine.events.EventBus
import com.apadmi.mockzilla.desktop.viewmodel.ViewModel
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.management.MockzillaManagement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class EndpointsViewModel(
    private val device: Device,
    private val endpointsService: MockzillaManagement.EndpointsService,
    private val updateService: MockzillaManagement.UpdateService,
    private val eventBus: EventBus,
    scope: CoroutineScope? = null
) : ViewModel(scope) {
    val state = MutableStateFlow<State>(State.Loading)
    private val checkboxStates = mutableMapOf<Device, MutableList<EndpointConfiguration.Key>>()

    init {
        eventBus.events.filter { it is EventBus.Event.EndpointDataChanged || it is EventBus.Event.FullRefresh }
            .onEach { reloadData() }
            .launchIn(viewModelScope)

        viewModelScope.launch { reloadData() }
    }

    private suspend fun reloadData() {
        state.value = endpointsService.fetchAllEndpointConfigs(device).fold(
            onSuccess = { State.EndpointsList(it.toConfig(checkboxStates[device])) },
            onFailure = { State.Loading }
        )
    }

    private fun List<SerializableEndpointConfig>.toConfig(
        tickedCheckboxes: List<EndpointConfiguration.Key>?
    ) = map {
        State.EndpointConfig(
            key = it.key,
            name = it.name,
            fail = it.shouldFail == true,
            isCheckboxTicked = tickedCheckboxes?.contains(it.key) == true,
            hasValuesOverridden = it.hasValuesOverridden()
        )
    }

    fun onCheckboxChanged(key: EndpointConfiguration.Key, value: Boolean) {
        val currentState = state.value as? State.EndpointsList ?: return

        checkboxStates.getOrPut(device) { mutableListOf() }.apply {
            if (value) {
                add(key)
            } else {
                remove(key)
            }
        }
        val newEndpoints = currentState.endpoints.map {
            it.copy(isCheckboxTicked = if (it.key == key) value else it.isCheckboxTicked)
        }
        state.value = currentState.copy(
            endpoints = newEndpoints
        )
    }

    fun onFailChanged(key: EndpointConfiguration.Key, value: Boolean) = viewModelScope.launch {
        val currentState = state.value as? State.EndpointsList ?: return@launch

        val keysToChange =
            checkboxStates[device].takeIf { it?.contains(key) == true } ?: listOf(key)

        state.value = currentState.copy(endpoints = currentState.endpoints.map {
            it.copy(
                fail = if (keysToChange.contains(it.key)) value else it.fail,
            )
        })

        // TODO: Handle error
        updateService.setShouldFail(device, keysToChange, value).onSuccess {
            eventBus.send(EventBus.Event.EndpointDataChanged(keysToChange))
        }
    }

    fun onAllCheckboxChanged(value: Boolean) {
        val currentState = state.value as? State.EndpointsList ?: return

        checkboxStates.getOrPut(device) { mutableListOf() }.apply {
            if (value) {
                addAll(currentState.endpoints.map { it.key })
            } else {
                clear()
            }
        }

        state.value = currentState.copy(
            endpoints = currentState.endpoints.map { it.copy(isCheckboxTicked = value) }
        )
    }

    sealed class State {
        data object Loading : State()
        /**
         * @property key
         * @property name
         * @property fail
         * @property isCheckboxTicked
         * @property hasValuesOverridden
         */
        data class EndpointConfig(
            val key: EndpointConfiguration.Key,
            val name: String,
            val fail: Boolean,
            val isCheckboxTicked: Boolean,
            val hasValuesOverridden: Boolean
        )

        /**
         * @property endpoints
         */
        data class EndpointsList(
            val endpoints: List<EndpointConfig>,
        ) : State() {
            val selectAllTicked: Boolean get() = endpoints.all { it.isCheckboxTicked }
        }
    }
}

private fun SerializableEndpointConfig.hasValuesOverridden() = delayMs != null ||
        defaultBody != null ||
        defaultStatus != null ||
        defaultHeaders != null ||
        errorStatus != null ||
        errorBody != null ||
        errorHeaders != null
