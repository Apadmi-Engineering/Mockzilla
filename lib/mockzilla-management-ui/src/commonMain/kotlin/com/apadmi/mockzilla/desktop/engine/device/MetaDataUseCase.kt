package com.apadmi.mockzilla.desktop.engine.device

import com.apadmi.mockzilla.desktop.utils.DataWithTimestamp
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.management.MockzillaManagement.*

import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface MetaDataUseCase {
    suspend fun getMetaData(device: Device): Result<MetaData>
}

class MetaDataUseCaseImpl(
    private val mockzillaManagement: MockzillaManagement
) : MetaDataUseCase {
    private val mutex = Mutex()
    private val cache = mutableMapOf<ConnectionConfig, DataWithTimestamp<MetaData>>()

    override suspend fun getMetaData(device: Device): Result<MetaData> = mutex.withLock {
        cache[device.connection]?.takeUnless { it.isExpired(0.5.seconds) }
            ?.let { Result.success(it.data) }
            ?: mockzillaManagement.fetchMetaData(device.connection).onSuccess {
                cache[device.connection] = DataWithTimestamp(it)
            }
    }
}
