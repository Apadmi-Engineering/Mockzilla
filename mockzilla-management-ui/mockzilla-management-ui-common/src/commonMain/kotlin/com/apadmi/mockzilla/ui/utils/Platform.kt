package com.apadmi.mockzilla.ui.utils

expect enum class Platform {
    Android,
    Desktop,
    Ios,
    ;

    companion object {
        val current: Platform
    }
}
