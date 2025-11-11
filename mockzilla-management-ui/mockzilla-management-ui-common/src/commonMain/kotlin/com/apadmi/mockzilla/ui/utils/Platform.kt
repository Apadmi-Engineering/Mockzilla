package com.apadmi.mockzilla.ui.utils

expect enum class Platform {
    Android,
    Desktop,
    Ios,
    Js,
    ;

    companion object {
        val current: Platform
    }
}
