package com.apadmi.mockzilla.ui.engine

import com.apadmi.mockzilla.ui.utils.Platform
import io.github.z4kn4fein.semver.Version

internal object Config {
    val minSupportedMockzillaVersion get() = when (Platform.current) {
        Platform.Desktop -> Version.parse("2.99.99")
        Platform.Android,
        Platform.Ios -> Version.parse("3.99.99")
        Platform.Js -> Version.parse("3.99.99")
        else -> throw IllegalStateException("Unsupported platform ${Platform.current}")
    }
    val nonDestructiveLogsMinVersion = Version.parse("3.99.99")
}
