package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.internal.models.GlobalOverridesDto
import com.apadmi.mockzilla.lib.internal.models.MockDataEntryDto
import com.apadmi.mockzilla.lib.internal.utils.FileIo

import co.touchlab.kermit.Logger

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal interface LocalCacheService {
    suspend fun getLocalCache(endpointKey: String): MockDataEntryDto?
    suspend fun getGlobalOverrides(): GlobalOverridesDto?
    suspend fun clearAllCaches()
    suspend fun updateLocalCache(entry: MockDataEntryDto)
    suspend fun updateGlobalOverrides(globalOverrides: GlobalOverridesDto)
}

internal class LocalCacheServiceImpl(
    private val fileIo: FileIo,
    private val logger: Logger,
) : LocalCacheService {
    private val lock = Mutex()

    private val MockDataEntryDto.fileName get() = key.fileName
    private val String.fileName get() = "${this}.json"

    private fun parseException(cause: Throwable) = IllegalStateException(
        """
            Failed to parse data from cache. 
            This is likely due to updating to a later version of Mockzilla.
            Clearing all caches through the web interface (or a clean install of your app)
            should fix this. If not, please report this here: https://github.com/Apadmi-Engineering/Mockzilla
        """.trimIndent(),
        cause
    )

    override suspend fun getLocalCache(
        endpointKey: String,
    ): MockDataEntryDto? = lock.withLock {
        fileIo.readFromCache(endpointKey.fileName)?.let {
            try {
                Json.decodeFromString<MockDataEntryDto>(it)
            } catch (e: Exception) {
                throw parseException(e)
            }
        }.also {
            logger.v { "Reading from cache $endpointKey - $it" }
        }
    }

    override suspend fun getGlobalOverrides() = lock.withLock {
        fileIo.readFromCache(globalOverridesFileName)?.let {
            try {
                Json.decodeFromString<GlobalOverridesDto>(it)
            } catch (e: Exception) {
                throw parseException(e)
            }
        }.also {
            logger.v { "Reading from cache global overrides - $it" }
        }
    }

    override suspend fun clearAllCaches() = lock.withLock {
        fileIo.deleteAllCaches()
    }

    override suspend fun updateLocalCache(entry: MockDataEntryDto) = lock.withLock {
        logger.v { "Writing to cache ${entry.key} - $entry " }

        fileIo.saveToCache(entry.fileName, Json.encodeToString(entry))
    }

    override suspend fun updateGlobalOverrides(
        globalOverrides: GlobalOverridesDto,
    ) = lock.withLock {
        logger.v { "Writing to cache $globalOverrides" }
        fileIo.saveToCache(globalOverridesFileName, Json.encodeToString(globalOverrides))
    }

    companion object {
        internal val globalOverridesFileName = "global-overrides.json"
    }
}
