package com.apadmi.mockzilla.lib.internal.utils

import platform.posix.gettimeofday
import platform.posix.timeval

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr

@OptIn(ExperimentalForeignApi::class)
@Suppress("GENERIC_VARIABLE_WRONG_DECLARATION")
internal actual fun epochMillis(): Long = memScoped {
    val timeVal = alloc<timeval>()
    gettimeofday(timeVal.ptr, null)
    (timeVal.tv_sec * 1000) + (timeVal.tv_usec / 1000)
}
