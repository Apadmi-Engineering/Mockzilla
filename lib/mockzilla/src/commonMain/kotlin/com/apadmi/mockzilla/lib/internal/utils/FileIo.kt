package com.apadmi.mockzilla.lib.internal.utils

internal expect class FileIo {
    suspend fun readFromCache(filename: String): String?
    suspend fun saveToCache(filename: String, contents: String)
    suspend fun deleteCacheFile(filename: String)
    suspend fun deleteAllCaches()
}

internal expect fun createFileIoforTesting(): FileIo
