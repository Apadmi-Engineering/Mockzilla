package com.apadmi.mockzilla.desktop.utils

actual enum class Platform {
    Android,
    Desktop,
    ;

    actual companion object {
        actual val current: Platform get() = Platform.Android
    }
}
