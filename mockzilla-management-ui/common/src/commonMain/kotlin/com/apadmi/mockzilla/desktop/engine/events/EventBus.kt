package com.apadmi.mockzilla.desktop.engine.events

import com.apadmi.mockzilla.desktop.utils.launchUnit
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface EventBus {
    val events: Flow<Event>

    fun send(event: Event)

    sealed interface Event {
        data object FullRefresh : Event
        /**
         * @property keys
         */
        data class EndpointDataChanged(val keys: Collection<EndpointConfiguration.Key>) : Event
    }
}

class EventBusImpl(
    private val coroutineScope: CoroutineScope
) : EventBus {
    override val events = MutableSharedFlow<EventBus.Event>()

    override fun send(event: EventBus.Event) = coroutineScope.launchUnit {
        events.emit(event)
    }
}
