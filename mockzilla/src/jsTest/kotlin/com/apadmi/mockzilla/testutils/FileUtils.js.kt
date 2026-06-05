package com.apadmi.mockzilla.testutils

import com.apadmi.mockzilla.lib.internal.utils.multiPlatformIo
import io.ktor.client.fetch.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual val currentWorkingDirectory: String get() = js("process.cwd()") as String

@OptIn(ExperimentalWasmJsInterop::class)
actual suspend fun readBytes(absolutePath: String) = withContext(Dispatchers.multiPlatformIo) {
    // Since Mockzilla's JS module is for the browser only we can't really read from a file so just using
    // a hardcoded byte array
    ByteArray(0)
}
