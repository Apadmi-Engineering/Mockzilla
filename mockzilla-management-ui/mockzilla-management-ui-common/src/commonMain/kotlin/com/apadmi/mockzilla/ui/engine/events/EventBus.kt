package com.apadmi.mockzilla.ui.engine.events

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.ui.utils.launchUnit

import co.touchlab.kermit.Logger

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface EventBus {
    val events: Flow<Event>

    fun send(event: Event)

    sealed interface Event {
        data object PresetApplied : Event
        data object FullRefresh : Event
        /**
         * @property operation
         * @property error
         */
        data class GenericError(
            val operation: GenericErrorableOperation,
            val error: Throwable
        ) : Event
        /**
         * @property keys
         */
        data class EndpointDataChanged(val keys: Collection<EndpointConfiguration.Key>) : Event
    }
}

enum class GenericErrorableOperation {
    ApplyPreset,
    ClearCaches,
    FetchDashboardOptionsConfig,
    FetchEndpointConfigs,
    UpdateGlobalOverrides,
    UpdateMockData,
    ;
}

internal class EventBusImpl(
    private val coroutineScope: CoroutineScope
) : EventBus {
    override val events = MutableSharedFlow<EventBus.Event>()

    override fun send(event: EventBus.Event) = coroutineScope.launchUnit {
        Logger.v(tag = "EventBus") { "Sending Event: $event" }
        events.emit(event)
    }
}
