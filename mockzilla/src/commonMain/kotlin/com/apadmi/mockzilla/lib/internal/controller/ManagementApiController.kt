package com.apadmi.mockzilla.lib.internal.controller

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfigurationPatchRequestDto
import com.apadmi.mockzilla.lib.internal.service.LocalCacheService
import com.apadmi.mockzilla.lib.internal.service.MockServerMonitor
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig
import com.apadmi.mockzilla.lib.models.EndpointConfiguration

internal class ManagementApiController(
    private val endpoints: List<EndpointConfiguration>,
    private val localCacheService: LocalCacheService,
    private val monitor: MockServerMonitor,
) {
    @Throws(IllegalStateException::class)
    suspend fun updateEntry(
        key: String,
        patch: SerializableEndpointConfigurationPatchRequestDto
    ): SerializableEndpointConfig {
        check(key == patch.key) { "Endpoint key mismatch, $key does not match ${patch.key}" }
        val endpoint =
            endpoints.firstOrNull { it.key == key } ?: throw Exception("No such endpoint: $key")
        return localCacheService.updateLocalCache(patch, endpoint)
    }

    suspend fun getAllMockDataEntries() = endpoints.map { config ->
        localCacheService.getLocalCache(config.key)
            ?: SerializableEndpointConfig.allNulls(config.key, config.name)
    }

    fun getPresets(key: String): DashboardOptionsConfig {
        val endpoint =
            endpoints.firstOrNull { it.key == key } ?: throw Exception("No such endpoint: $key")
        return endpoint.dashboardOptionsConfig
    }

    suspend fun clearAllCaches() {
        localCacheService.clearAllCaches()
    }

    suspend fun consumeLogEntries() = monitor.consumeCurrentLogs()
}
