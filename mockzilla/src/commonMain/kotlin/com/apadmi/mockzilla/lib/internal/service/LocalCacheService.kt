package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointPatchItemDto
import com.apadmi.mockzilla.lib.internal.models.SetOrDont
import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.lib.internal.utils.JsonProvider
import com.apadmi.mockzilla.lib.models.EndpointConfiguration

import co.touchlab.kermit.Logger

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString

internal interface LocalCacheService {
    suspend fun getLocalCache(endpointKey: EndpointConfiguration.Key): SerializableEndpointConfig?
    suspend fun clearAllCaches()
    suspend fun clearCache(keys: List<EndpointConfiguration.Key>)
    suspend fun patchLocalCaches(
        patches: Map<EndpointConfiguration, SerializableEndpointPatchItemDto>,
    )
}

internal class LocalCacheServiceImpl(
    private val fileIo: FileIo,
    private val logger: Logger,
) : LocalCacheService {
    private val SerializableEndpointPatchItemDto.fileName get() = key.fileName
    private val EndpointConfiguration.Key.fileName get() = "$raw.json"

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
        endpointKey: EndpointConfiguration.Key,
    ): SerializableEndpointConfig? = lock.withLock {
        getLocalCacheUnlocked(endpointKey)
    }

    private suspend fun getLocalCacheUnlocked(
        endpointKey: EndpointConfiguration.Key
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

    override suspend fun clearCache(keys: List<EndpointConfiguration.Key>) = lock.withLock {
        keys.forEach { clearCacheUnlocked(it) }
    }

    private suspend fun clearCacheUnlocked(key: EndpointConfiguration.Key) {
        logger.v { "Clearing cache for ${key.raw}" }
        fileIo.deleteCacheFile(key.fileName)
    }

    override suspend fun patchLocalCaches(
        patches: Map<EndpointConfiguration, SerializableEndpointPatchItemDto>,
    ) = lock.withLock {
        patches.forEach { (patch, endpoint) -> patchLocalCache(patch, endpoint) }
    }

    private suspend fun patchLocalCache(endpoint: EndpointConfiguration, patch: SerializableEndpointPatchItemDto) {
        logger.v { "Writing to cache ${patch.key} - $patch" }
        val currentCache = getLocalCacheUnlocked(patch.key) ?: SerializableEndpointConfig.allNulls(
            key = patch.key, name = endpoint.name, versionCode = endpoint.versionCode
        )

        val newCache = currentCache.copy(
            name = endpoint.name,
            versionCode = endpoint.versionCode,
            shouldFail = patch.shouldFail.valueOrDefault(currentCache.shouldFail),
            delayMs = patch.delayMs.valueOrDefault(currentCache.delayMs),
            defaultHeaders = patch.defaultHeaders.valueOrDefault(currentCache.defaultHeaders),
            defaultBody = patch.defaultBody.valueOrDefault(currentCache.defaultBody),
            defaultStatus = patch.defaultStatus.valueOrDefault(currentCache.defaultStatus),
            errorBody = patch.errorBody.valueOrDefault(currentCache.errorBody),
            errorHeaders = patch.errorHeaders.valueOrDefault(currentCache.errorHeaders),
            errorStatus = patch.errorStatus.valueOrDefault(currentCache.errorStatus)

        )
        fileIo.saveToCache(patch.fileName, JsonProvider.json.encodeToString<SerializableEndpointConfig>(newCache))
    }

    suspend fun clearStaleCaches(endpoints: List<EndpointConfiguration>) = lock.withLock {
        endpoints.filter { endpoint ->
            val cache = getLocalCacheUnlocked(endpoint.key)
            cache != null && cache.versionCode != endpoint.versionCode
        }.apply {
            if (isEmpty()) {
                logger.v { "No caches to clear" }
            } else {
                logger.v { "Clearing stale caches" }
            }
        }.forEach { clearCacheUnlocked(it.key) }
    }

    private fun <T> SetOrDont<T?>?.valueOrDefault(default: T): T? = when (this) {
        is SetOrDont.Set -> value
        null,
        SetOrDont.DoNotSet -> default
    }

    companion object {
        private val lock = Mutex()
    }
}
