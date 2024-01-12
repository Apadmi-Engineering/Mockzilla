package com.apadmi.mockzilla.lib.internal.utils

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

internal class FiloIoTests {
    @Test
    fun `read write and delete - various filenames - as expected`() = runTest {
        listOf(
            "file.txt",
            "this has spaces & special ` chars.txt",
            "1.txt",
            "*.txt",
        ).forEach {
            readWriteAndDelete(it)
        }
    }

    private suspend fun readWriteAndDelete(fileName: String) {
        /* Setup */
        val sut = createFileIoforTesting()

        /* Run Test */
        sut.saveToCache(fileName, "my contents")
        val read1 = sut.readFromCache(fileName)
        sut.deleteCacheFile(fileName)
        val read2 = sut.readFromCache(fileName)

        /* Verify */
        assertEquals("my contents", read1)
        assertEquals(null, read2)
    }

    @Test
    fun `read and overwrite - as expected`() = runTest {
        /* Setup */
        val sut = createFileIoforTesting()

        /* Run Test */
        sut.saveToCache("file.txt", "my contents")
        sut.saveToCache("file.txt", "my contents 2")
        val read = sut.readFromCache("file.txt")

        /* Verify */
        assertEquals("my contents 2", read)
        sut.deleteCacheFile("file.txt")
    }

    @Test
    fun `deleteAllCaches- as expected`() = runTest {
        /* Setup */
        val sut = createFileIoforTesting()
        sut.saveToCache("file1.txt", "my contents 1")
        sut.saveToCache("file2.txt", "my contents 2")

        /* Run Test */
        sut.deleteAllCaches()

        /* Verify */
        assertEquals(null, sut.readFromCache("file1.txt"))
        assertEquals(null, sut.readFromCache("file2.txt"))
    }

    @Test
    fun `deleteAllCaches - read write still works as expected`() = runTest {
        /* Setup */
        val sut = createFileIoforTesting()
        sut.saveToCache("file1.txt", "my contents 1")

        /* Run Test */
        sut.deleteAllCaches()

        /* Verify */
        assertEquals(null, sut.readFromCache("file1.txt"))
        sut.saveToCache("file1.txt", "my contents 2")
        assertEquals("my contents 2", sut.readFromCache("file1.txt"))
    }
}
