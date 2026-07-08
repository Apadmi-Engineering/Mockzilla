package com.apadmi.mockzilla.testutils.fakes

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.service.MockServerMonitor

class FakeMockServerMonitor(
    private val consumeCurrentLogsReturnValue: List<LogEvent> = emptyList(),
    private val allLogEvents: List<LogEvent> = emptyList(),
    private val diskBodyCache: Map<String, LogEvent> = emptyMap(),
) : MockServerMonitor {
    var clearAllLogsCallCount = 0
    var onClientSessionStartArgument: Long? = null

    override suspend fun log(event: LogEvent) = Unit
    override suspend fun consumeCurrentLogs(): List<LogEvent> = consumeCurrentLogsReturnValue
    override suspend fun getLogsSince(since: Long?): List<LogEvent> = since?.let {
        allLogEvents.filter { it.timestamp > since }
    } ?: allLogEvents
    override suspend fun getFullBodyLogDetail(logId: String): LogEvent? =
        diskBodyCache[logId] ?: allLogEvents.firstOrNull { it.id == logId }
    override suspend fun onClientSessionStart(sessionStart: Long) {
        onClientSessionStartArgument = sessionStart
    }
    override suspend fun clearAllLogs() {
        clearAllLogsCallCount++
    }
}
