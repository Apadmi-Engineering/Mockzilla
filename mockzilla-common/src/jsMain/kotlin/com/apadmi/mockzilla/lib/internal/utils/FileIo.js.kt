package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import kotlin.random.Random
import kotlinx.browser.localStorage

private var incrementForUniqueness = 0

@InternalMockzillaApi
public actual class FileIo(private val filePrefix: String = "mockzilla_cache_") {
    public actual suspend fun readFromCache(filename: String): String? = localStorage.getItem(filePrefix + filename)

    public actual suspend fun saveToCache(filename: String, contents: String) {
        localStorage.setItem(filePrefix + filename, contents)
    }

    public actual suspend fun deleteCacheFile(filename: String) {
        localStorage.removeItem(filePrefix + filename)
    }

    public actual suspend fun deleteAllCaches() {
        (0..localStorage.length)
            .map { localStorage.key(it) }
            .filter { it?.startsWith(filePrefix) == true }
            .filterNotNull()
            .forEach { localStorage.removeItem(it) }
    }

    public actual suspend fun deleteDirectory(dirName: String) {
        val prefix = "$filePrefix$dirName/"
        (0 until localStorage.length)
            .mapNotNull { localStorage.key(it) }
            .filter { it.startsWith(prefix) }
            .forEach { localStorage.removeItem(it) }
    }

    public actual suspend fun listFiles(dirName: String): List<String> {
        val prefix = "$filePrefix$dirName/"
        return (0 until localStorage.length)
            .mapNotNull { localStorage.key(it) }
            .filter { it.startsWith(prefix) }
            .map { it.removePrefix(prefix) }
    }
}
@InternalMockzillaApi
public actual fun createFileIoforTesting(): FileIo = FileIo(
    // Ensure each test has a de-facto isolated storage bucket to prevent overlap
    // in parallel tests
    filePrefix = "mockzilla_test_${Random.nextDouble()}_${incrementForUniqueness++}"
)
