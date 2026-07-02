package com.apadmi.mockzilla.ui.engine.events

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.ui.utils.launchUnit

import co.touchlab.kermit.Logger

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

public interface EventBus {
    public val events: Flow<Event>

    public fun send(event: Event)

    public sealed interface Event {
        public data object PresetApplied : Event
        public data object FullRefresh : Event
        /**
         * @property operation
         * @property error
         */
        public data class GenericError(
            val operation: GenericErrorableOperation,
            val error: Throwable
        ) : Event
        /**
         * @property keys
         */
        public data class EndpointDataChanged(val keys: Collection<EndpointConfiguration.Key>) : Event
    }
}

public enum class GenericErrorableOperation {
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
