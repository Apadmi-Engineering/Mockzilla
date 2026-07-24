@file:JvmName("FileIoKt")

package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import java.io.File
import java.io.IOException
import java.nio.file.Files

@InternalMockzillaApi
public actual class FileIo(private val cacheDir: File) {
    private val cacheDirectory
        get() = File(
            cacheDir,
            "com.apadmi.mockzilla.lib"
        ).also { it.mkdirs() }

    private fun String.fileInCache() = File(cacheDirectory, this)

    public actual suspend fun readFromCache(
        filename: String,
    ): String? = filename.fileInCache()
        .takeIf { it.exists() }
        ?.readLines()
        ?.joinToString("\n")

    public actual suspend fun saveToCache(filename: String, contents: String): Unit =
        filename.fileInCache().also {
            it.parentFile?.mkdirs()
            it.createNewFile()
        }.writeText(contents)

    public actual suspend fun deleteCacheFile(filename: String): Unit = filename.fileInCache()
        .takeIf {
            it.exists()
        }
        ?.delete()
        .let {
            if (it == false) {
                throw IOException("Failed to delete cache")
            }
        }

    public actual suspend fun deleteAllCaches(): Unit = cacheDir.deleteRecursively().let {
        if (!it) {
            throw IOException("Failed to delete caches")
        }
    }

    public actual suspend fun deleteDirectory(dirName: String) {
        File(cacheDirectory, dirName).deleteRecursively()
    }

    public actual suspend fun listFiles(dirName: String): List<String> =
        File(cacheDirectory, dirName).listFiles()?.map { it.name } ?: emptyList()
}

@InternalMockzillaApi
public actual fun createFileIoforTesting(): FileIo = FileIo(Files.createTempDirectory("").toFile())
