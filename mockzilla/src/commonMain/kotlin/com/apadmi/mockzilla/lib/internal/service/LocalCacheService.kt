package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.internal.models.MockDataEntryDto
import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.lib.internal.utils.JsonProvider

import co.touchlab.kermit.Logger
import com.apadmi.mockzilla.lib.internal.models.MockDataEntryUpdateRequestDto
import com.apadmi.mockzilla.lib.internal.models.SetOrDont

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString

internal interface LocalCacheService {
    suspend fun getLocalCache(endpointKey: String): MockDataEntryDto?
    suspend fun clearAllCaches()
    suspend fun updateLocalCache(entry: MockDataEntryUpdateRequestDto)
}

internal class LocalCacheServiceImpl(
    private val fileIo: FileIo,
    private val logger: Logger,
) : LocalCacheService {
    private val lock = Mutex()

    private val MockDataEntryUpdateRequestDto.fileName get() = key.fileName
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
        getLocalCacheUnlocked(endpointKey)
    }

    private suspend fun getLocalCacheUnlocked(
        endpointKey: String
    ) = fileIo.readFromCache(endpointKey.fileName)?.let {
        try {
            JsonProvider.json.decodeFromString<MockDataEntryDto>(it)
        } catch (e: Exception) {
            throw parseException(e)
        }
    }.also {
        logger.v { "Reading from cache $endpointKey - $it" }
    }


    override suspend fun clearAllCaches() = lock.withLock {
        fileIo.deleteAllCaches()
    }

    override suspend fun updateLocalCache(entry: MockDataEntryUpdateRequestDto) = lock.withLock {
        logger.v { "Writing to cache ${entry.key} - $entry" }

        val currentCache = getLocalCacheUnlocked(entry.key) ?: MockDataEntryDto.allNulls(
            key = entry.key, name = entry.name
        )

        val newCache = currentCache.copy(
            shouldFail = entry.shouldFail.valueOrDefault(currentCache.shouldFail),
            delayMs = entry.delayMs.valueOrDefault(currentCache.delayMs),
            headers = entry.headers.valueOrDefault(currentCache.headers),
            defaultBody = entry.defaultBody.valueOrDefault(currentCache.defaultBody),
            defaultStatus = entry.defaultStatus.valueOrDefault(currentCache.defaultStatus),
            errorBody = entry.errorBody.valueOrDefault(currentCache.errorBody),
            errorStatus = entry.errorStatus.valueOrDefault(currentCache.errorStatus)

        )
        fileIo.saveToCache(entry.fileName, JsonProvider.json.encodeToString<MockDataEntryDto>(newCache))
    }

    private fun <T> SetOrDont<T?>?.valueOrDefault(default: T): T = when (this) {
        is SetOrDont.Set -> value ?: default
        null,
        SetOrDont.DoNotSet -> default
    }

}
