package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

internal interface MockServerMonitor {
    suspend fun log(event: LogEvent)
    suspend fun consumeCurrentLogs(): List<LogEvent>
    suspend fun getLogsSince(since: Long?): List<LogEvent>
    suspend fun getLogDetail(logId: String): LogEvent?
    suspend fun getFullBodyLogDetail(logId: String): LogEvent?
    suspend fun onClientSessionStart(sessionStart: Long)
    suspend fun clearAllLogs()
}

@OptIn(ExperimentalTime::class)
internal class MockServerMonitorImpl(
    private val localBodyCacheService: LocalBodyCacheService,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
) : MockServerMonitor {
    private val lockingMutex = Mutex()
    private val events: MutableList<LogEvent> = mutableListOf()
    private var lastKnownClientSessionStart: Long? = null

    init {
        scope.launch {
            delay(60_000L)
            if (lastKnownClientSessionStart == null) {
                val threeDaysAgo = Clock.System.now().toEpochMilliseconds() - 3.days.inWholeMilliseconds
                localBodyCacheService.deleteOldFullEntries(threeDaysAgo)
            }
        }
    }

    override suspend fun log(event: LogEvent) {
        var storedEvent = event
        if (event.hasOversizedBody()) {
            localBodyCacheService.storeFullEntry(event)
        }
        if (event.requestBodyOversized()) {
            storedEvent = storedEvent.copy(
                requestBody = event.requestBody.take(LocalBodyCacheService.bodySizeLimit),
                isRequestBodyTruncated = true,
            )
        }
        if (event.responseBodyOversized()) {
            storedEvent = storedEvent.copy(
                responseBody = event.responseBody.take(LocalBodyCacheService.bodySizeLimit),
                isResponseBodyTruncated = true,
            )
        }
        lockingMutex.withLock {
            events.add(storedEvent)
            if (events.size > memoryCapacity) events.removeFirst()
        }
        // Disk files are NOT evicted when the ring buffer wraps — cleanup is handled
        // solely by the session-start policy and the fallback timer.
    }

    /** Legacy destructive drain — kept intact for backward compat. */
    override suspend fun consumeCurrentLogs(): List<LogEvent> = lockingMutex.withLock {
        val copy = mutableListOf<LogEvent>().apply { addAll(events) }
        events.clear()
        copy
    }

    override suspend fun getLogsSince(since: Long?): List<LogEvent> = lockingMutex.withLock {
        if (since == null) events.toList()
        else events.filter { it.timestamp > since }
    }

    override suspend fun getLogDetail(logId: String): LogEvent? {
        return localBodyCacheService.fetchFullEntry(logId) ?: events.firstOrNull { it.id == logId }
    }

    override suspend fun getFullBodyLogDetail(logId: String): LogEvent? =
        localBodyCacheService.fetchFullEntry(logId)

    override suspend fun onClientSessionStart(sessionStart: Long) {
        if (sessionStart == lastKnownClientSessionStart) return
        lastKnownClientSessionStart = sessionStart
        val oldestInMemory = lockingMutex.withLock { events.firstOrNull()?.timestamp }
            ?: Long.MAX_VALUE
        localBodyCacheService.deleteOldFullEntries(minOf(oldestInMemory, sessionStart))
    }

    override suspend fun clearAllLogs() {
        lockingMutex.withLock { events.clear() }
        localBodyCacheService.clearAll()
    }

    companion object {
        private const val memoryCapacity = 500
    }
}

private fun LogEvent.requestBodyOversized() = requestBody.length > LocalBodyCacheService.bodySizeLimit
private fun LogEvent.responseBodyOversized() = responseBody.length > LocalBodyCacheService.bodySizeLimit
private fun LogEvent.hasOversizedBody() = requestBodyOversized() || responseBodyOversized()
