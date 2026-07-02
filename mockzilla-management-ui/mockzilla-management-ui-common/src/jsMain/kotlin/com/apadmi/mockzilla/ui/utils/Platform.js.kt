package com.apadmi.mockzilla.ui.utils

internal actual enum class Platform {
    Android,
    Desktop,
    Ios,
    Js,
    ;

    actual companion object {
        actual val current: Platform = Js
    }
}
