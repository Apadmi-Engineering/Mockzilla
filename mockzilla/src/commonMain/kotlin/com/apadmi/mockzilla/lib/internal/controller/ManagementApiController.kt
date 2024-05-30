package com.apadmi.mockzilla.lib.internal.controller

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointPatchItemDto
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
    suspend fun patchEntries(
        patches: List<SerializableEndpointPatchItemDto>
    ) {
        val endpointsToPatches = patches.associateBy { patch ->
            endpoints.firstOrNull {
                it.key == patch.key
            } ?: throw IllegalStateException("No endpoint with key ${patch.key}")
        }

        localCacheService.patchLocalCaches(endpointsToPatches)
    }

    suspend fun getAllMockDataEntries() = endpoints.map { config ->
        localCacheService.getLocalCache(config.key)?.copy(name = config.name)
            ?: SerializableEndpointConfig.allNulls(config.key, config.name, config.versionCode)
    }

    fun getDashboardConfig(key: EndpointConfiguration.Key): DashboardOptionsConfig {
        val endpoint =
            endpoints.firstOrNull { it.key == key } ?: throw Exception("No such endpoint: ${key.raw}")
        return endpoint.dashboardOptionsConfig
    }

    suspend fun clearAllCaches() = localCacheService.clearAllCaches()
    suspend fun clearCache(key: List<EndpointConfiguration.Key>) = localCacheService.clearCache(key)
    suspend fun consumeLogEntries() = monitor.consumeCurrentLogs()
}
