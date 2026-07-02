package com.apadmi.mockzilla.ui.engine.device

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.MockzillaManagement

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

public interface MetaDataUseCase {
    public suspend fun getMetaData(device: Device): Result<MetaData>
    public suspend fun invalidate(device: Device)
}

internal class MetaDataUseCaseImpl(
    private val managementMetaDataService: MockzillaManagement.MetaDataService,
) : MetaDataUseCase {
    private val mutex = Mutex()
    private val cache = mutableMapOf<Device, MetaData>()

    override suspend fun getMetaData(device: Device): Result<MetaData> = mutex.withLock {
        cache[device]?.let { Result.success(it) }
            ?: managementMetaDataService.fetchMetaData(device, hideFromLogs = false).onSuccess {
                cache[device] = it
            }
    }

    override suspend fun invalidate(device: Device) = mutex.withLock {
        cache.remove(device)
        Unit
    }
}
