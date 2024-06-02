package com.apadmi.mockzilla.desktop.utils

expect enum class Platform {
    Android,
    Desktop,
    ;

    companion object {
        val current: Platform
    }
}
