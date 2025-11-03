package com.apadmi.mockzilla.lib.internal.utils

import kotlinx.browser.localStorage

actual class FileIo {
    private val filePrefix = "mockzilla_cache_"

    actual suspend fun readFromCache(filename: String): String? = localStorage.getItem(filePrefix + filename)

    actual suspend fun saveToCache(filename: String, contents: String) {
        localStorage.setItem(filePrefix + filename, contents)
    }

    actual suspend fun deleteCacheFile(filename: String) {
        localStorage.removeItem(filePrefix + filename)
    }

    actual suspend fun deleteAllCaches() {
        (0..localStorage.length)
            .map { localStorage.key(it) }
            .filter { it?.startsWith(filePrefix) == true }
            .filterNotNull()
            .forEach { localStorage.removeItem(it) }
    }
}

actual fun createFileIoforTesting() = FileIo()
