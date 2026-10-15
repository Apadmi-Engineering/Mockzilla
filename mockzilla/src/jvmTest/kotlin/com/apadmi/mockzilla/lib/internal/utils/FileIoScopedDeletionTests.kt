package com.apadmi.mockzilla.lib.internal.utils

import java.io.File
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class FileIoScopedDeletionTests {
    @Test
    fun `deleteAllCaches - files outside mockzilla directory - are left untouched`() = runTest {
        /* Setup */
        val hostDir = Files.createTempDirectory("").toFile()
        val dataStoreFile = File(hostDir, "datastore/settings.preferences_pb").apply {
            parentFile?.mkdirs()
            writeText("host app data store")
        }
        val otherFile = File(hostDir, "other.txt").apply { writeText("host app file") }
        val sut = FileIo(hostDir)
        sut.saveToCache("file.txt", "my contents")

        /* Run Test */
        sut.deleteAllCaches()

        /* Verify */
        assertEquals(null, sut.readFromCache("file.txt"))
        assertTrue(dataStoreFile.exists(), "Host app's datastore file should not be deleted")
        assertTrue(otherFile.exists(), "Host app's file should not be deleted")

        /* Cleanup */
        hostDir.deleteRecursively()
    }

    @Test
    fun `deleteAllCaches - read write still works afterwards`() = runTest {
        /* Setup */
        val hostDir = Files.createTempDirectory("").toFile()
        val sut = FileIo(hostDir)
        sut.saveToCache("file.txt", "my contents 1")

        /* Run Test */
        sut.deleteAllCaches()
        sut.saveToCache("file.txt", "my contents 2")

        /* Verify */
        assertEquals("my contents 2", sut.readFromCache("file.txt"))

        /* Cleanup */
        hostDir.deleteRecursively()
    }
}
