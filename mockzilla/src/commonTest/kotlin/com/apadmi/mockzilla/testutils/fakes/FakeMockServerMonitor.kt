package com.apadmi.mockzilla.testutils.fakes

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.service.MockServerMonitor

class FakeMockServerMonitor(
    private val consumeCurrentLogsReturnValue: List<LogEvent> = emptyList()
) : MockServerMonitor {
    override suspend fun log(event: LogEvent) = Unit
    override suspend fun consumeCurrentLogs(): List<LogEvent> = consumeCurrentLogsReturnValue
}
