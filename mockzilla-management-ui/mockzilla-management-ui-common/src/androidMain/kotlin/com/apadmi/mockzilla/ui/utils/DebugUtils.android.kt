package com.apadmi.mockzilla.ui.utils

import com.apadmi.mockzilla.mobile.ui.BuildConfig

actual object DebugUtils {
    actual val isDebug: Boolean = BuildConfig.DEBUG
}
