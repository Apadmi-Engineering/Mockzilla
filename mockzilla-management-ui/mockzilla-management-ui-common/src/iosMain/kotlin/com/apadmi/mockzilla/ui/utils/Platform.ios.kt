package com.apadmi.mockzilla.ui.utils

actual enum class Platform {
    Android,
    Desktop,
    Ios,
    Js,
    ;

    actual companion object {
        actual val current: Platform get() = Platform.Ios
    }
}
