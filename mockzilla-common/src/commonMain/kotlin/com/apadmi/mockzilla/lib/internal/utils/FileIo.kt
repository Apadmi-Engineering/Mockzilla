package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.InternalMockzillaApi

@InternalMockzillaApi
public expect class FileIo {
    public suspend fun readFromCache(filename: String): String?
    public suspend fun saveToCache(filename: String, contents: String)
    public suspend fun deleteCacheFile(filename: String)
    public suspend fun deleteAllCaches()
    public suspend fun deleteDirectory(dirName: String)
    public suspend fun listFiles(dirName: String): List<String>
}

@InternalMockzillaApi
public expect fun createFileIoforTesting(): FileIo
