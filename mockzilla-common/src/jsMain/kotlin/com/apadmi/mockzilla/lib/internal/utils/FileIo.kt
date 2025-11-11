package com.apadmi.mockzilla.lib.internal.utils

import kotlinx.browser.localStorage
import kotlin.random.Random

actual class FileIo(private val filePrefix: String = "mockzilla_cache_") {

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

var incramentForUniqueness = 0
actual fun createFileIoforTesting() = FileIo(
    // Ensure each test has a de-facto isolated storage bucket to prevent overlap
    // in parallel tests
    filePrefix = "mockzilla_test_${Random.nextDouble()}_${incramentForUniqueness++}"
)
