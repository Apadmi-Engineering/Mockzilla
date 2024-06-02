package com.apadmi.mockzilla.desktop.i18n

import cafe.adriel.lyricist.LyricistStrings
import io.ktor.http.HttpStatusCode

@LyricistStrings(languageTag = "En", default = true)
val EnStrings = Strings(
    widgets = Strings.Widgets(
        Strings.Widgets.DeviceConnection(
            tabTitle = "Connect Device",
            heading = "Enter IP and port to connect to a device",
            autoConnectHeading = "Or…",
            autoConnectSubHeading = "Choose a device to connect automatically",
            autoConnectButton = "Connect",
            ipInputLabel = "e.g 127.0.0.1:8080",
            androidDevConnectButton = "Connect to development Mockzilla",
            tooltips = Strings.Widgets.DeviceConnection.ToolTips(
                notYourSimulator = "We don't think this is your simulator, but you can try to connect! (Probably won't work)",
                readyToConnect = "",
                removed = "This device seems to have disconnected",
                resolving = "We're still for this device to come online"

            )
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
        ), metaData = Strings.Widgets.MetaData(
            title = "Meta Data",
            noDeviceConnected = "No device connected",
            appName = "App Name",
            appPackage = "App Package",
            operatingSystem = "OS",
            operatingSystemVersion = "OS Version",
            deviceModel = "Device",
            appVersion = "App Version",
            mockzillaVersion = "Mockzilla Version",
            android = "Android",
            ios = "iOS",
            jvm = "JVM",
        ),
        logs = Strings.Widgets.Logs(
            "Logs"
        ),
        endpoints = Strings.Widgets.Endpoints(
            selectAllTooltip = "Select all",
            errorSwitchLabel = "Use Error Response",
            valuesOverriddenIndicatorTooltip = "Some or all of the properties of this response are being overridden"
        ),
        endpointDetails = Strings.Widgets.EndpointDetails(
            title = "Editor",
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
            failOptionsLabel = "User Error Response:",
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
        ),
        miscControls = Strings.Widgets.MiscControls(
            refreshAll = "Re-sync all",
            clearOverrides = "Reset all overrides",
            title = "Tools"
        ),
        unsupportedMockzilla = Strings.Widgets.UnsupportedMockzillaVersion(
            heading = "Unsupported SDK",
            subtitle = "This app doesn't support the version of Mockzilla running on your device",
            footer = "Please update to the latest version of Mockzilla",
        ),
        errorBanner = Strings.Widgets.ErrorBanner(
            connectionLost = "Connection Lost: Please check your app is in the foreground",
            unknownError = "Something went wrong, try refreshing everything \uD83D\uDC49",
            refreshButton = "Refresh"
        )
    )
)
