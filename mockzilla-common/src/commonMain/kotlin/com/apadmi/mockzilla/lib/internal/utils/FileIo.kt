package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.InternalMockzillaApi

@InternalMockzillaApi
expect class FileIo {
    suspend fun readFromCache(filename: String): String?
    suspend fun saveToCache(filename: String, contents: String)
    suspend fun deleteCacheFile(filename: String)
    suspend fun deleteAllCaches()
    suspend fun deleteDirectory(dirName: String)
}

@InternalMockzillaApi
expect fun createFileIoforTesting(): FileIo
