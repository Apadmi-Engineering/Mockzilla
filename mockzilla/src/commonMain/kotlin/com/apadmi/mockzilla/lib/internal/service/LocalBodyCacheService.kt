package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.internal.utils.FileIo

internal class LocalBodyCacheService(private val fileIo: FileIo) {

    suspend fun storeRequestBody(logId: String, body: String) =
        fileIo.saveToCache("${logId}_req.txt", body)

    suspend fun storeResponseBody(logId: String, body: String) =
        fileIo.saveToCache("${logId}_res.txt", body)

    suspend fun fetchRequestBody(logId: String): String? =
        fileIo.readFromCache("${logId}_req.txt")

    suspend fun fetchResponseBody(logId: String): String? =
        fileIo.readFromCache("${logId}_res.txt")

    /** Deletes both body files for [logId]; safe to call even if they don't exist. */
    suspend fun evict(logId: String) {
        fileIo.deleteCacheFile("$bodiesDir/${logId}_req.txt")
        fileIo.deleteCacheFile("$bodiesDir/${logId}_res.txt")
    }

    /** Deletes the entire body directory — no ID tracking required. */
    suspend fun clearAll() = fileIo.deleteDirectory(bodiesDir)


    companion object Companion {
        const val bodySizeLimit = 10
        private const val bodiesDir = "mockzilla_log_bodies"
    }
}
