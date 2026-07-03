@file:NoKDoc

package com.apadmi.mockzilla.ui.utils

import com.apadmi.mockzilla.lib.NoKDoc
import kotlin.time.Clock
import kotlin.time.Duration

internal data class DataWithTimestamp<T>(val data: T, val timeStamp: Long = Clock.System.now().toEpochMilliseconds()) {
    fun isExpired(
        cacheLife: Duration,
        currentTimeStamp: Long = Clock.System.now().toEpochMilliseconds()
    ) = timeStamp + cacheLife.inWholeMilliseconds < currentTimeStamp
}
