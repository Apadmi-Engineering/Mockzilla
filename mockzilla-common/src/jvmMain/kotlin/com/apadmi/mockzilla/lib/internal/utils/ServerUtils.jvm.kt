@file:JvmName("ServerUtilsKt")

package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@InternalMockzillaApi
actual val Dispatchers.multiPlatformIo: CoroutineDispatcher get() = Dispatchers.IO
