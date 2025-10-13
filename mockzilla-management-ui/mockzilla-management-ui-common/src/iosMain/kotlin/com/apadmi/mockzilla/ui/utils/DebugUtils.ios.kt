package com.apadmi.mockzilla.ui.utils

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.Platform

actual object DebugUtils {
    @OptIn(ExperimentalNativeApi::class)
    actual val isDebug: Boolean = Platform.isDebugBinary
}
