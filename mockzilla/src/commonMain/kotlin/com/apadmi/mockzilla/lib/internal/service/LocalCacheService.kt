package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfigurationPatchRequestDto
import com.apadmi.mockzilla.lib.internal.models.SetOrDont
import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.lib.internal.utils.JsonProvider
import com.apadmi.mockzilla.lib.models.EndpointConfiguration

import co.touchlab.kermit.Logger

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString

internal interface LocalCacheService {
    suspend fun getLocalCache(endpointKey: String): SerializableEndpointConfig?
    suspend fun clearAllCaches()
    suspend fun updateLocalCache(
        patch: SerializableEndpointConfigurationPatchRequestDto,
        endpoint: EndpointConfiguration
    ): SerializableEndpointConfig
}

internal class LocalCacheServiceImpl(
    private val fileIo: FileIo,
    private val logger: Logger,
) : LocalCacheService {
    private val lock = Mutex()

    private val SerializableEndpointConfigurationPatchRequestDto.fileName get() = key.fileName
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
    ): SerializableEndpointConfig? = lock.withLock {
        getLocalCacheUnlocked(endpointKey)
    }

    private suspend fun getLocalCacheUnlocked(
        endpointKey: String
    ) = fileIo.readFromCache(endpointKey.fileName)?.let {
        try {
            JsonProvider.json.decodeFromString<SerializableEndpointConfig>(it)
        } catch (e: Exception) {
            throw parseException(e)
        }
    }.also {
        logger.v { "Reading from cache $endpointKey - $it" }
    }

    override suspend fun clearAllCaches() = lock.withLock {
        fileIo.deleteAllCaches()
    }

    override suspend fun updateLocalCache(
        patch: SerializableEndpointConfigurationPatchRequestDto,
        endpoint: EndpointConfiguration
    ) = lock.withLock {
        logger.v { "Writing to cache ${patch.key} - $patch" }

        val currentCache = getLocalCacheUnlocked(patch.key) ?: SerializableEndpointConfig.allNulls(
            key = patch.key, name = endpoint.name
        )

        val newCache = currentCache.copy(
            shouldFail = patch.shouldFail.valueOrDefault(currentCache.shouldFail),
            delayMs = patch.delayMs.valueOrDefault(currentCache.delayMs),
            headers = patch.headers.valueOrDefault(currentCache.headers),
            defaultBody = patch.defaultBody.valueOrDefault(currentCache.defaultBody),
            defaultStatus = patch.defaultStatus.valueOrDefault(currentCache.defaultStatus),
            errorBody = patch.errorBody.valueOrDefault(currentCache.errorBody),
            errorStatus = patch.errorStatus.valueOrDefault(currentCache.errorStatus)

        )
        fileIo.saveToCache(patch.fileName, JsonProvider.json.encodeToString<SerializableEndpointConfig>(newCache))
        newCache
    }

    private fun <T> SetOrDont<T?>?.valueOrDefault(default: T): T = when (this) {
        is SetOrDont.Set -> value ?: default
        null,
        SetOrDont.DoNotSet -> default
    }
}
