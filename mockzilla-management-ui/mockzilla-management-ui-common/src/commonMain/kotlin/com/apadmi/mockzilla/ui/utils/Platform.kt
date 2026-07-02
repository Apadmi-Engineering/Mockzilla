package com.apadmi.mockzilla.ui.utils

internal expect enum class Platform {
    Android,
    Desktop,
    Ios,
    Js,
    ;

    companion object {
        val current: Platform
    }
}
