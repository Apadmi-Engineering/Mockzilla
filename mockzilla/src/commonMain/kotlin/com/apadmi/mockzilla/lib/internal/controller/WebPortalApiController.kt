package com.apadmi.mockzilla.lib.internal.controller

import com.apadmi.mockzilla.lib.internal.models.GlobalOverridesDto
import com.apadmi.mockzilla.lib.internal.models.MockDataEntryDto
import com.apadmi.mockzilla.lib.internal.models.toMockDataEntryForWeb
import com.apadmi.mockzilla.lib.internal.service.LocalCacheService
import com.apadmi.mockzilla.lib.internal.service.MockServerMonitor
import com.apadmi.mockzilla.lib.models.EndpointConfiguration

internal class WebPortalApiController(
    private val endpoints: List<EndpointConfiguration>,
    private val localCacheService: LocalCacheService,
    private val monitor: MockServerMonitor,
) {
    @Throws(IllegalStateException::class)
    suspend fun updateEntry(key: String, entry: MockDataEntryDto) {
        check(key == entry.key) { "Endpoint key mismatch, $key does not match ${entry.key}" }
        localCacheService.updateLocalCache(entry)
    }

    suspend fun getAllMockDataEntries() = endpoints.map {
        localCacheService.getLocalCache(it.key) ?: it.toMockDataEntryForWeb()
    }

    suspend fun clearAllCaches() {
        localCacheService.clearAllCaches()
    }

    suspend fun getGlobalOverrides() = localCacheService.getGlobalOverrides()

    suspend fun updateGlobalOverrides(
        globalOverrides: GlobalOverridesDto,
    ) = localCacheService.updateGlobalOverrides(globalOverrides)

    suspend fun consumeLogEntries() = monitor.consumeCurrentLogs()
}
