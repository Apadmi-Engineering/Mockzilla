package com.apadmi.mockzilla.lib.internal.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val Dispatchers.multiPlatformIo: CoroutineDispatcher get() = Dispatchers.Main
