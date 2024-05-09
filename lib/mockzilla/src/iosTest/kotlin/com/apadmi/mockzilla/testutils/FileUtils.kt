@file:OptIn(ExperimentalForeignApi::class)

package com.apadmi.mockzilla.testutils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.dataWithContentsOfFile
import platform.posix.memcpy

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

actual val currentWorkingDirectory: String get() = NSBundle.mainBundle.bundlePath.substringBefore("/build")

private fun NSData.toByteArray(): ByteArray = ByteArray(length.toInt()).apply {
    usePinned {
        memcpy(it.addressOf(0), bytes, length)
    }
}

actual suspend fun readBytes(absolutePath: String) = withContext(Dispatchers.IO) {
    NSData.dataWithContentsOfFile(absolutePath)?.toByteArray()
} ?: throw Exception("Could not read file $absolutePath")
