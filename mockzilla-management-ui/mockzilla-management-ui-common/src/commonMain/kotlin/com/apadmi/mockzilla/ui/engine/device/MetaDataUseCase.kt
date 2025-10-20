package com.apadmi.mockzilla.ui.engine.device

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.ui.utils.DataWithTimestamp
import com.apadmi.mockzilla.ui.utils.TimeStampAccessor

import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface MetaDataUseCase {
    suspend fun getMetaData(device: Device, isPolling: Boolean = false): Result<MetaData>
}

class MetaDataUseCaseImpl(
    private val managementMetaDataService: MockzillaManagement.MetaDataService,
    private val currentTimeStamp: TimeStampAccessor = { Clock.System.now().toEpochMilliseconds() }
) : MetaDataUseCase {
    private val mutex = Mutex()
    private val cache = mutableMapOf<Device, DataWithTimestamp<MetaData>>()

    override suspend fun getMetaData(device: Device, isPolling: Boolean): Result<MetaData> = mutex.withLock {
        cache[device]?.takeUnless { it.isExpired(cacheLife = 0.5.seconds, currentTimeStamp()) }
            ?.let { Result.success(it.data) }
            ?: managementMetaDataService.fetchMetaData(device, hideFromLogs = isPolling).onSuccess {
                cache[device] = DataWithTimestamp(it, currentTimeStamp())
            }
    }
}
