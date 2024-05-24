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
            defaultDataTab = "Default Response",
            errorDataTab = "Error Response",
            generalTab = "General",
            noOverrideStatusCode = "Unset",
            statusCodeLabel = { statusCode: HttpStatusCode ->
                "${statusCode.value} ${statusCode.description}"
            },
            statusCode = "Status code",
            edit = "Edit",
            reset = "Reset",
            resetUseErrorResponse = "Reset",
            bodyLabel = "Response body",
            bodyUnset = "Response body unset",
            delayLabel = "Delay (milliseconds)",
            jsonEditingLabel = { enabled: Boolean ->
                if (enabled) {
                    "JSON"
                } else {
                    "Text"
                }
            },
            failOptionsLabel = "Response to use:",
            failLabel = { shouldFail: Boolean? ->
                when (shouldFail) {
                    null -> "Unset"
                    true -> "Fail"
                    false -> "Default"
                }
            },
            responseDelay = "Response delay:",
            noOverrideResponseDelay = "Unset",
            customResponseDelay = "Custom",
            responseDelayUnits = "ms",
            responseDelayLabel = "Delay",
            resetAll = "Reset all overrides",
            headersLabel = "Headers:",
            headerKeyLabel = "Key",
            headerValueLabel = "Value",
            headerDeleteContentDescription = "Delete header",
            addHeader = "Add header",
            editHeaders = "Edit headers",
            resetHeaders = "Reset all headers",
            noHeaders = "Headers set to an empty list",
            headersUnset = "Headers unset",
        )
    )
)
