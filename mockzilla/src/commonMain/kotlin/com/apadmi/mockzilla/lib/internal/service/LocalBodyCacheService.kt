package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.lib.internal.utils.JsonProvider

internal class LocalBodyCacheService(private val fileIo: FileIo) {
    suspend fun storeFullEntry(event: LogEvent) {
        val filename = "$fullEntriesDir/${event.timestamp}_${event.id}.json"
        fileIo.saveToCache(filename, JsonProvider.json.encodeToString(LogEvent.serializer(), event))
    }

    suspend fun fetchFullEntry(logId: String): LogEvent? {
        val match = fileIo.listFiles(fullEntriesDir)
            .firstOrNull { it.endsWith("_$logId.json") }
            ?: return null
        val json = fileIo.readFromCache("$fullEntriesDir/$match") ?: return null
        return runCatching { JsonProvider.json.decodeFromString(LogEvent.serializer(), json) }.getOrNull()
    }

    suspend fun deleteOldFullEntries(olderThan: Long) {
        fileIo.listFiles(fullEntriesDir).forEach { filename ->
            val timestamp = filename.substringBefore("_").toLongOrNull() ?: return@forEach
            if (timestamp < olderThan) {
                fileIo.deleteCacheFile("$fullEntriesDir/$filename")
            }
        }
    }

    suspend fun clearAll() = fileIo.deleteDirectory(fullEntriesDir)

    companion object {
        const val bodySizeLimit = 10
        const val fullEntriesDir = "mockzilla_log_full_entries"
    }
}
