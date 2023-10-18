package com.apadmi.mockzilla.desktop.i18n

import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.lyricist.LyricistStrings

@LyricistStrings(languageTag = "En", default = true)
val EnStrings = Strings(
    widgets = Strings.Widgets(Strings.Widgets.DeviceConnection(tabTitle = "Connect Device"),
        deviceTabs = Strings.Widgets.DeviceTabs(
            tabTitle = { "Device $it" },
            addDevice = "Add Device"
        ), metaData = Strings.Widgets.MetaData(title = "Meta Data")
    )
)

val LocalStrings = staticCompositionLocalOf { EnStrings }
