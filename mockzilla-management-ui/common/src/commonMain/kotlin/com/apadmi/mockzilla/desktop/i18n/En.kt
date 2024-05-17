package com.apadmi.mockzilla.desktop.i18n

import cafe.adriel.lyricist.LyricistStrings
import io.ktor.http.HttpStatusCode

@LyricistStrings(languageTag = "En", default = true)
val EnStrings = Strings(
    widgets = Strings.Widgets(
        Strings.Widgets.DeviceConnection(
            tabTitle = "Connect Device",
            ipInputLabel = "IP address to connect to"
        ),
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
        ),
        endpointDetails = Strings.Widgets.EndpointDetails(
            none = "No endpoint selected",
            useErrorResponse = "Return failure response",
            defaultData = "Default Response",
            errorData = "Error Response",
            noOverrideBody = "Response body not overridden",
            noOverrideStatusCode = "Status code not overridden",
            edit = "Add override",
            reset = "Reset",
            resetUseErrorResponse = "Reset",
            bodyLabel = "Response body",
            statusCodeLabel = { statusCode: HttpStatusCode -> "Status code: $statusCode" },
            delayLabel = "Delay (milliseconds)"
        )
    )
)
