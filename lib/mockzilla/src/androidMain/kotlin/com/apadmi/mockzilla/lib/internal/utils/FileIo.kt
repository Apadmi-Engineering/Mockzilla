package com.apadmi.mockzilla.lib.internal.utils

import android.annotation.TargetApi
import android.os.Build
import java.io.File
import java.io.IOException
import java.nio.file.Files

internal actual class FileIo(private val cacheDir: File) {
    private val cacheDirectory
        get() = File(
            cacheDir,
            "com.apadmi.mockzilla.lib"
        ).also { it.mkdirs() }

    private fun String.fileInCache() = File(cacheDirectory, this)
    actual suspend fun readFromCache(
        filename: String,
    ): String? = filename.fileInCache()
        .takeIf { it.exists() }
        ?.readLines()
        ?.joinToString("\n")

    actual suspend fun saveToCache(filename: String, contents: String) =
            filename.fileInCache().also {
                it.createNewFile()
            }.writeText(contents)

    actual suspend fun deleteCacheFile(filename: String) = filename.fileInCache()
        .takeIf {
            it.exists()
        }
        ?.delete()
        .let {
            if (it == false) {
                throw IOException("Failed to delete cache")
            }
        }

    actual suspend fun deleteAllCaches() = cacheDir.deleteRecursively().let {
        if (!it) {
            throw IOException("Failed to delete caches")
        }
    }
}

@TargetApi(Build.VERSION_CODES.O)
internal actual fun createFileIoforTesting() = FileIo(Files.createTempDirectory("").toFile())
