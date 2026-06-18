package com.apadmi.mockzilla.testutils.fakes

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.service.MockServerMonitor

class FakeMockServerMonitor(
    private val consumeCurrentLogsReturnValue: List<LogEvent> = emptyList(),
    private val getLogsSinceReturnValue: List<LogEvent> = emptyList(),
    private val getLogDetailReturnValue: LogEvent? = null,
) : MockServerMonitor {
    var clearAllLogsCallCount = 0

    override suspend fun log(event: LogEvent) = Unit
    override suspend fun consumeCurrentLogs(): List<LogEvent> = consumeCurrentLogsReturnValue
    override suspend fun getLogsSince(since: Long?): List<LogEvent> = getLogsSinceReturnValue
    override suspend fun getLogDetail(logId: String): LogEvent? = getLogDetailReturnValue
    override suspend fun clearAllLogs() { clearAllLogsCallCount++ }
}
