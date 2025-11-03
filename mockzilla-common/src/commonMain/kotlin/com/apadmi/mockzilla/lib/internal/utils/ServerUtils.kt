package com.apadmi.mockzilla.lib.internal.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

expect val Dispatchers.multiPlatformIo: CoroutineDispatcher
