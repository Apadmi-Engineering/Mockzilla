package com.apadmi.mockzilla.desktop.engine.device

import com.apadmi.mockzilla.desktop.utils.DataWithTimestamp
import com.apadmi.mockzilla.desktop.utils.TimeStampAccessor
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.MockzillaManagement

import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface MetaDataUseCase {
    suspend fun getMetaData(device: Device, isPolling: Boolean = false): Result<MetaData>
}

class MetaDataUseCaseImpl(
    private val managementMetaDataService: MockzillaManagement.MetaDataService,
    private val currentTimeStamp: TimeStampAccessor = System::currentTimeMillis
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
