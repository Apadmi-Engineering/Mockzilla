package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.internal.utils.FileIo

internal class LogBodyStore(private val fileIo: FileIo) {

    suspend fun storeRequestBody(logId: String, body: String) =
        fileIo.saveToCache("$bodiesDir/${logId}_req", body)

    suspend fun storeResponseBody(logId: String, body: String) =
        fileIo.saveToCache("$bodiesDir/${logId}_res", body)

    suspend fun fetchRequestBody(logId: String): String? =
        fileIo.readFromCache("$bodiesDir/${logId}_req")

    suspend fun fetchResponseBody(logId: String): String? =
        fileIo.readFromCache("$bodiesDir/${logId}_res")

    /** Deletes both body files for [logId]; safe to call even if they don't exist. */
    suspend fun evict(logId: String) {
        fileIo.deleteCacheFile("$bodiesDir/${logId}_req")
        fileIo.deleteCacheFile("$bodiesDir/${logId}_res")
    }

    /** Deletes the entire body directory — no ID tracking required. */
    suspend fun clearAll() = fileIo.deleteDirectory(bodiesDir)

    companion object {
        const val bodySizeLimit = 10
        private const val bodiesDir = "mockzilla_log_bodies"
    }
}
