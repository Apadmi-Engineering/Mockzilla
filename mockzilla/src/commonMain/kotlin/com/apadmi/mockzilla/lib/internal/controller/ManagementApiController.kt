package com.apadmi.mockzilla.lib.internal.controller

import com.apadmi.mockzilla.lib.internal.models.MockDataEntryDto
import com.apadmi.mockzilla.lib.internal.models.MockDataEntryUpdateRequestDto
import com.apadmi.mockzilla.lib.internal.service.LocalCacheService
import com.apadmi.mockzilla.lib.internal.service.MockServerMonitor
import com.apadmi.mockzilla.lib.models.EndpointConfiguration

internal class ManagementApiController(
    private val endpoints: List<EndpointConfiguration>,
    private val localCacheService: LocalCacheService,
    private val monitor: MockServerMonitor,
) {
    @Throws(IllegalStateException::class)
    suspend fun updateEntry(key: String, entry: MockDataEntryUpdateRequestDto) {
        check(key == entry.key) { "Endpoint key mismatch, $key does not match ${entry.key}" }
        localCacheService.updateLocalCache(entry)
    }

    suspend fun getAllMockDataEntries() = endpoints.map { config ->
        localCacheService.getLocalCache(config.key) ?: MockDataEntryDto(
            config.key, config.name,
            shouldFail = null,
            delayMs = null,
            headers = null,
            defaultBody = null,
            defaultStatus = null,
            errorBody = null,
            errorStatus = null,
        )
    }

    suspend fun clearAllCaches() {
        localCacheService.clearAllCaches()
    }

    suspend fun consumeLogEntries() = monitor.consumeCurrentLogs()
}
