package com.apadmi.mockzilla.testutils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual val currentWorkingDirectory: String get() = System.getProperty("user.dir") ?: throw Exception("Could not get current working directory")

actual suspend fun readBytes(absolutePath: String) = withContext(Dispatchers.IO) {
    File(absolutePath).readBytes()
}