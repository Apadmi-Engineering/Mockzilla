package com.apadmi.mockzilla.ui.utils

import kotlin.time.Clock
import kotlin.time.Duration

typealias TimeStampAccessor = () -> Long

/**
 * @property data
 * @property timeStamp
 */
data class DataWithTimestamp<T>(val data: T, val timeStamp: Long = Clock.System.now().toEpochMilliseconds()) {
    fun isExpired(
        cacheLife: Duration,
        currentTimeStamp: Long = Clock.System.now().toEpochMilliseconds()
    ) = timeStamp + cacheLife.inWholeMilliseconds < currentTimeStamp
}
