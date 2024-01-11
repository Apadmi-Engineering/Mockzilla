package com.apadmi.mockzilla.desktop.i18n

import cafe.adriel.lyricist.LyricistStrings

@LyricistStrings(languageTag = "En", default = true)
val EnStrings = Strings(
    widgets = Strings.Widgets(Strings.Widgets.DeviceConnection(tabTitle = "Connect Device"),
        deviceTabs = Strings.Widgets.DeviceTabs(
            tabTitle = { "Device $it" },
            addDevice = "Add Device",
            connected = "Connected",
            disconnected = "Disconnected",
            devices = { number ->
                when (number) {
                    1 -> "1 device"
                    else -> "$number devices"
                }
            }
        ), metaData = Strings.Widgets.MetaData(title = "Meta Data"),
        logs = Strings.Widgets.Logs(
            "Logs"
        )
    )
)
