package com.apadmi.mockzilla.lib.internal.utils

import platform.Foundation.*

import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
internal actual class FileIo {
    private val directoryPath by lazy {
        val dir = NSSearchPathForDirectoriesInDomains(
            NSCachesDirectory,
            NSUserDomainMask,
            true
        ).firstOrNull() as? String ?: throw IllegalStateException("Failed to get cache directory :(")
        "${dir.removeSuffix("/")}/com.apadmi.mockzilla.lib"
    }
    actual suspend fun readFromCache(filename: String): String? {
        val filePath = filePath(filename)
        return NSString.stringWithContentsOfFile(filePath, NSUTF8StringEncoding, null)
    }

    actual suspend fun saveToCache(filename: String, contents: String) {
        val filePath = filePath(filename)
        NSFileManager.defaultManager.createDirectoryAtPath(directoryPath, true, null, null)

        (contents as NSString).writeToFile(filePath, true, NSUTF8StringEncoding, null)
    }

    actual suspend fun deleteCacheFile(filename: String) {
        NSFileManager.defaultManager.removeItemAtPath(filePath(filename), null)
    }

    actual suspend fun deleteAllCaches() {
        NSFileManager.defaultManager.removeItemAtPath(directoryPath, null)
    }

    private fun filePath(filename: String) = "$directoryPath/$filename"
}

internal actual fun createFileIoforTesting() = FileIo()
