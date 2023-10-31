package com.apadmi.mockzilla.testutils

expect val currentWorkingDirectory: String

expect suspend fun readBytes(absolutePath: String): ByteArray