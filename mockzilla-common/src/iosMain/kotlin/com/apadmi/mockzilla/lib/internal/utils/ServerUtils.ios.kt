package com.apadmi.mockzilla.lib.internal.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

actual val Dispatchers.multiPlatformIo: CoroutineDispatcher get() = Dispatchers.IO
