package com.apadmi.mockzilla.lib.internal.controller

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfigurationPatchRequestDto
import com.apadmi.mockzilla.lib.internal.service.LocalCacheService
import com.apadmi.mockzilla.lib.internal.service.MockServerMonitor
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import io.ktor.http.HttpStatusCode

internal class ManagementApiController(
    private val endpoints: List<EndpointConfiguration>,
    private val localCacheService: LocalCacheService,
    private val monitor: MockServerMonitor,
) {
    @Throws(IllegalStateException::class)
    suspend fun updateEntry(key: String, entry: SerializableEndpointConfigurationPatchRequestDto) {
        check(key == entry.key) { "Endpoint key mismatch, $key does not match ${entry.key}" }
        localCacheService.updateLocalCache(entry)
    }

    suspend fun getAllMockDataEntries() = endpoints.map { config ->
        localCacheService.getLocalCache(config.key) ?: run {
            // TODO: This will be updated once the more sophisticated mechanisms of configuring the
            // mock data are implemented
            SerializableEndpointConfig(
                config.key, config.name,
                shouldFail = config.shouldFail,
                delayMs = config.delay,
                headers = config.webApiDefaultResponse?.headers ?: emptyMap(),
                defaultBody = config.webApiDefaultResponse?.body ?: "{}",
                defaultStatus = config.webApiDefaultResponse?.statusCode ?: HttpStatusCode.OK,
                errorBody = config.webApiErrorResponse?.body ?: "{}",
                errorStatus = config.webApiErrorResponse?.statusCode ?: HttpStatusCode.InternalServerError,
            )
        }
    }

    suspend fun clearAllCaches() {
        localCacheService.clearAllCaches()
    }

    suspend fun consumeLogEntries() = monitor.consumeCurrentLogs()
}
