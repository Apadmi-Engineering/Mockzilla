@file:NoKDoc

package com.apadmi.mockzilla.ui.engine.device

import com.apadmi.mockzilla.lib.NoKDoc
import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.ui.engine.Config

import io.github.z4kn4fein.semver.toVersion

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal interface MonitorLogsUseCase {
    suspend fun getMonitorLogs(device: Device): Result<MonitorLogsResponse>
    suspend fun clearMonitorLogs(device: Device): Result<Unit>
    suspend fun fetchLogDetail(device: Device, logId: String): Result<LogEvent>
}

@OptIn(ExperimentalTime::class)
internal class MonitorLogsUseCaseImpl(
    private val managementLogsService: MockzillaManagement.LogsService,
    private val metaDataUseCase: MetaDataUseCase,
) : MonitorLogsUseCase {
    private val mutex = Mutex()
    private val cache = mutableMapOf<CacheKey, List<LogEvent>>()
    private val clientSessionStart = Clock.System.now().toEpochMilliseconds()

    private suspend fun doesSupportNonDestructiveLogs(device: Device): Boolean =
        metaDataUseCase.getMetaData(device)
            .map { it.mockzillaVersion.toVersion() >= Config.nonDestructiveLogsMinVersion }
            .getOrDefault(false)

    override suspend fun getMonitorLogs(device: Device): Result<MonitorLogsResponse> = mutex.withLock {
        if (doesSupportNonDestructiveLogs(device)) {
            getMonitorLogsNewPath(device)
        } else {
            getMonitorLogsLegacyPath(device)
        }
    }

    private suspend fun getMonitorLogsNewPath(device: Device): Result<MonitorLogsResponse> {
        val existingKey = cache.keys.firstOrNull { it.device == device }
        val existing = existingKey?.let { cache[it] } ?: emptyList()
        val since = existing.lastOrNull()?.timestamp?.let { it - 1 }

        return managementLogsService.fetchMonitorLogsSince(device, since, clientSessionStart)
            .map { response ->
                val appPackageChanged =
                    existingKey != null && existingKey.appPackage != response.appPackage
                if (appPackageChanged) {
                    cache.remove(existingKey)
                    metaDataUseCase.invalidate(device)
                }
                val key = CacheKey(device, response.appPackage)
                val base = if (appPackageChanged) emptyList() else existing
                val merged = (base + response.logs)
                    .distinctBy { it.id }
                    .sortedBy { it.timestamp }
                    .takeLast(clientMemoryCapacity)
                cache[key] = merged
                MonitorLogsResponse(appPackage = response.appPackage, logs = merged)
            }
    }

    private suspend fun getMonitorLogsLegacyPath(device: Device): Result<MonitorLogsResponse> =
        // Intentionally using deprecated function while people update their mockzilla sdks
        @Suppress("DEPRECATION")
        managementLogsService.fetchMonitorLogsAndClearBuffer(device, hideFromLogs = true).map { response ->
            val cacheKey = CacheKey(device, response.appPackage)
            val existingLogs = cache.getOrElse(cacheKey) { emptyList() }
            val merged = (existingLogs + response.logs).also { cache[cacheKey] = it }
            MonitorLogsResponse(appPackage = response.appPackage, logs = merged)
        }

    override suspend fun clearMonitorLogs(device: Device): Result<Unit> = mutex.withLock {
        val metaData = metaDataUseCase.getMetaData(device).getOrElse {
            return@withLock Result.failure(it)
        }
        val cacheKey = CacheKey(device, metaData.appPackage)
        cache[cacheKey] = emptyList()

        if (metaData.mockzillaVersion.toVersion() >= Config.nonDestructiveLogsMinVersion) {
            managementLogsService.deleteMonitorLogs(device)
        } else {
            Result.success(Unit)
        }
    }

    override suspend fun fetchLogDetail(device: Device, logId: String): Result<LogEvent> =
        if (doesSupportNonDestructiveLogs(device)) {
            managementLogsService.fetchFullBodyLogDetail(device, logId)
        } else {
            Result.failure(UnsupportedOperationException("Server version does not support log detail fetching"))
        }

    companion object {
        private const val clientMemoryCapacity = 1000
    }
}

private data class CacheKey(val device: Device, val appPackage: String)
