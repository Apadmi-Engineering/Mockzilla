package com.apadmi.mockzilla.ui.engine.events

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.ui.utils.launchUnit

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface EventBus {
    val events: Flow<Event>

    fun send(event: Event)

    sealed interface Event {
        data object PresetApplied : Event
        data class GenericError(
            val operation: GenericErrorableOperation,
            val error: Throwable
        ) : Event
        data object FullRefresh : Event
        /**
         * @property keys
         */
        data class EndpointDataChanged(val keys: Collection<EndpointConfiguration.Key>) : Event
    }
}

enum class GenericErrorableOperation {
    FetchDashboardOptionsConfig,
    FetchEndpointConfigs,
    UpdateMockData,
    ApplyPreset,
    ClearCaches,
    UpdateGlobalOverrides
}

internal class EventBusImpl(
    private val coroutineScope: CoroutineScope
) : EventBus {
    override val events = MutableSharedFlow<EventBus.Event>()

    override fun send(event: EventBus.Event) = coroutineScope.launchUnit {
        events.emit(event)
    }
}
