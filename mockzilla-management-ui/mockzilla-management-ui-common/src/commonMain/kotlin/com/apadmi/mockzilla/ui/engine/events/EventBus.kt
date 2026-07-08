package com.apadmi.mockzilla.ui.engine.events

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.ui.utils.launchUnit

import co.touchlab.kermit.Logger

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

@InternalMockzillaApi
public interface EventBus {
    public val events: Flow<Event>

    public fun send(event: Event)

    @InternalMockzillaApi
    public sealed interface Event {
        @InternalMockzillaApi
        public data object PresetApplied : Event
        @InternalMockzillaApi
        public data object FullRefresh : Event
        @InternalMockzillaApi
        public data class GenericError(
            val operation: GenericErrorableOperation,
            val error: Throwable
        ) : Event
        @InternalMockzillaApi
        public data class EndpointDataChanged(val keys: Collection<EndpointConfiguration.Key>) : Event
    }
}

@InternalMockzillaApi
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
