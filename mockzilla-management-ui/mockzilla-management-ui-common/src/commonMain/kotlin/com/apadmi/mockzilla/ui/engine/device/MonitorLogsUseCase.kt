package com.apadmi.mockzilla.ui.engine.device

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.ui.engine.Config
import io.github.z4kn4fein.semver.toVersion
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal interface MonitorLogsUseCase {
    suspend fun getMonitorLogs(device: Device): Result<Sequence<LogEvent>>
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

    private suspend fun supportsNonDestructiveLogs(device: Device): Boolean =
        metaDataUseCase.getMetaData(device)
            .map { it.mockzillaVersion.toVersion() >= Config.nonDestructiveLogsMinVersion  }
            .getOrDefault(false)

    override suspend fun getMonitorLogs(device: Device): Result<Sequence<LogEvent>> = mutex.withLock {
        if (supportsNonDestructiveLogs(device)) {
            getMonitorLogsNewPath(device)
        } else {
            getMonitorLogsLegacyPath(device)
        }
    }

    private suspend fun getMonitorLogsNewPath(device: Device): Result<Sequence<LogEvent>> {
        val cacheKey = cache.keys.firstOrNull { it.device == device }
        val existing = cacheKey?.let { cache[it] } ?: emptyList()
        val since = existing.lastOrNull()?.timestamp?.let { it - 1 }

        return managementLogsService.fetchMonitorLogsSince(device, since, clientSessionStart).map { response ->
            val key = cacheKey ?: CacheKey(device, response.appPackage)
            val merged = (existing + response.logs)
                .distinctBy { it.id }
                .sortedBy { it.timestamp }
                .takeLast(clientMemoryCapacity)
            cache[key] = merged
            merged.asSequence()
        }
    }

    private suspend fun getMonitorLogsLegacyPath(device: Device): Result<Sequence<LogEvent>> =
        managementLogsService.fetchMonitorLogsAndClearBuffer(device, hideFromLogs = true).map { response ->
            val cacheKey = CacheKey(device, response.appPackage)
            val existingLogs = cache.getOrElse(cacheKey) { emptyList() }

            (existingLogs + response.logs).also {
                cache[cacheKey] = it
            }.asSequence()
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
        if (supportsNonDestructiveLogs(device)) {
            managementLogsService.fetchFullBodyLogDetail(device, logId)
        } else {
            Result.failure(UnsupportedOperationException("Server version does not support log detail fetching"))
        }

    companion object {
        private const val clientMemoryCapacity = 1000
    }
}

/**
 * @property device
 * @property appPackage
 */
private data class CacheKey(val device: Device, val appPackage: String)
