package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import platform.Foundation.*

import kotlinx.cinterop.ExperimentalForeignApi

@InternalMockzillaApi
@OptIn(ExperimentalForeignApi::class)
public actual class FileIo {
    private val directoryPath by lazy {
        val dir = NSSearchPathForDirectoriesInDomains(
            NSCachesDirectory,
            NSUserDomainMask,
            true
        ).firstOrNull() as? String ?: throw IllegalStateException("Failed to get cache directory :(")
        "${dir.removeSuffix("/")}/com.apadmi.mockzilla.lib"
    }
    public actual suspend fun readFromCache(filename: String): String? {
        val filePath = filePath(filename)
        return NSString.stringWithContentsOfFile(filePath, NSUTF8StringEncoding, null)
    }

    public actual suspend fun saveToCache(filename: String, contents: String) {
        val filePath = filePath(filename)
        val parentDir = filePath.substringBeforeLast("/")
        NSFileManager.defaultManager.createDirectoryAtPath(parentDir, true, null, null)

        (contents as NSString).writeToFile(filePath, true, NSUTF8StringEncoding, null)
    }

    public actual suspend fun deleteCacheFile(filename: String) {
        NSFileManager.defaultManager.removeItemAtPath(filePath(filename), null)
    }

    public actual suspend fun deleteAllCaches() {
        NSFileManager.defaultManager.removeItemAtPath(directoryPath, null)
    }

    public actual suspend fun deleteDirectory(dirName: String) {
        NSFileManager.defaultManager.removeItemAtPath("$directoryPath/$dirName", null)
    }

    public actual suspend fun listFiles(dirName: String): List<String> =
        NSFileManager.defaultManager
            .contentsOfDirectoryAtPath("$directoryPath/$dirName", null)
            ?.filterIsInstance<String>() ?: emptyList()

    private fun filePath(filename: String) = "$directoryPath/$filename"
}

@InternalMockzillaApi
public actual fun createFileIoforTesting(): FileIo = FileIo()
