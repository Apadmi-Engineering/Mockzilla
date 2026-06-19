package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal interface MockServerMonitor {
    suspend fun log(event: LogEvent)
    suspend fun consumeCurrentLogs(): List<LogEvent>
    suspend fun getLogsSince(since: Long?): List<LogEvent>
    suspend fun getLogDetail(logId: String): LogEvent?
    suspend fun clearAllLogs()
}

internal class MockServerMonitorImpl(
    private val localBodyCacheService: LocalBodyCacheService,
) : MockServerMonitor {
    private val lockingMutex = Mutex()
    private val events: MutableList<LogEvent> = mutableListOf()

    override suspend fun log(event: LogEvent) {
        var storedEvent = event
        if (event.requestBody.length > LocalBodyCacheService.bodySizeLimit) {
            localBodyCacheService.storeRequestBody(event.id, event.requestBody)
            storedEvent = storedEvent.copy(
                requestBody = event.requestBody.take(LocalBodyCacheService.bodySizeLimit),
                isRequestBodyTruncated = true,
            )
        }
        if (event.responseBody.length > LocalBodyCacheService.bodySizeLimit) {
            localBodyCacheService.storeResponseBody(event.id, event.responseBody)
            storedEvent = storedEvent.copy(
                responseBody = event.responseBody.take(LocalBodyCacheService.bodySizeLimit),
                isResponseBodyTruncated = true,
            )
        }

        val evicted: LogEvent? = lockingMutex.withLock {
            events.add(storedEvent)
            if (events.size > memoryCapacity) events.removeFirst() else null
        }

        evicted?.let { localBodyCacheService.evict(it.id) }
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
        val event = lockingMutex.withLock { events.firstOrNull { it.id == logId } }
            ?: return null
        if (!event.isRequestBodyTruncated && !event.isResponseBodyTruncated) return event
        return event.copy(
            requestBody = if (event.isRequestBodyTruncated)
                localBodyCacheService.fetchRequestBody(logId) ?: event.requestBody
            else event.requestBody,
            responseBody = if (event.isResponseBodyTruncated)
                localBodyCacheService.fetchResponseBody(logId) ?: event.responseBody
            else event.responseBody,
            isRequestBodyTruncated = false,
            isResponseBodyTruncated = false,
        )
    }

    override suspend fun clearAllLogs() {
        lockingMutex.withLock { events.clear() }
        localBodyCacheService.clearAll()
    }

    companion object {
        private const val memoryCapacity = 500
    }
}
