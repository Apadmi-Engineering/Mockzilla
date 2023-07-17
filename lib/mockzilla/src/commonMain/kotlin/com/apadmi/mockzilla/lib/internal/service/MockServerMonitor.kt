package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal interface MockServerMonitor {
    suspend fun log(event: LogEvent)
    suspend fun consumeCurrentLogs(): List<LogEvent>
}

internal class MockServerMonitorImpl : MockServerMonitor {
    private val lockingMutex = Mutex()
    private val events: MutableList<LogEvent> = mutableListOf()

    override suspend fun log(event: LogEvent) = lockingMutex.withLock {
        events.add(event)
        Unit
    }

    override suspend fun consumeCurrentLogs(): List<LogEvent> = lockingMutex.withLock {
        val copy = mutableListOf<LogEvent>().apply { addAll(events) }
        events.clear()
        copy
    }
}
