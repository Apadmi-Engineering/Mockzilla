package com.apadmi.mockzilla.desktop.utils

import kotlin.time.Duration

/**
 * @property data
 * @property timeStamp
 */
data class DataWithTimestamp<T>(val data: T, val timeStamp: Long = System.currentTimeMillis()) {
    fun isExpired(cacheLife: Duration) = timeStamp + cacheLife.inWholeMilliseconds > System.currentTimeMillis()
}
