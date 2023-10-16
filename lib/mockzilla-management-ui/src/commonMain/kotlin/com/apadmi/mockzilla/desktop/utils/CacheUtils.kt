package com.apadmi.mockzilla.desktop.utils

import java.time.Clock
import kotlin.time.Duration
import java.time.ZonedDateTime

data class DataWithTimestamp<T>(val data: T, val timeStamp: Long = System.currentTimeMillis()) {
    fun isExpired(cacheLife: Duration) = timeStamp + cacheLife.inWholeMilliseconds > System.currentTimeMillis()
}
