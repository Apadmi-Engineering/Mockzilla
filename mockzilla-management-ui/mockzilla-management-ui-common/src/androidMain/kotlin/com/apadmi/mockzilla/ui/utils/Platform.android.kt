package com.apadmi.mockzilla.ui.utils

actual enum class Platform {
    Android,
    Desktop,
    Ios,
    ;

    actual companion object {
        actual val current: Platform get() = Platform.Android
    }
}
