@file:Suppress("COMPLEX_EXPRESSION")

package com.apadmi.mockzilla.ui.i18n

import cafe.adriel.lyricist.LyricistStrings
import io.ktor.http.HttpStatusCode
import kotlin.math.roundToInt

@LyricistStrings(languageTag = "En", default = true)
val EnStrings = Strings(
    common = Strings.Common(
        closeDescription = "Close",
        backDescription = "Back",
        debugDescription = "Debug",
        resetDescription = "Reset",
        deleteDescription = "Delete"
    ),
    widgets = Strings.Widgets(
        deviceConnection = Strings.Widgets.DeviceConnection(
            tabTitle = "Connect Device",
            heading = "Enter IP and port to connect to a device",
            autoConnectHeading = "Or…",
            autoConnectSubHeading = "Choose a device to connect automatically",
            autoConnectButton = "Connect",
            ipInputLabel = "e.g 127.0.0.1:8080",
            androidDevConnectButton = "Connect to development Mockzilla",
            errorTitle = "Failed to connect",
            errorMessage = "Please check the following:" +
                    "\n1. You have called startMockzilla() during your application's launch." +
                    "\n2. The Mockzilla library you are using is above the minimum versions (KMP: 2.4.1, Flutter: 1.3.0).",
            connected = "Connected",
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
            },
            closeButtonDescription = "Close"
        ),
        metaData = Strings.Widgets.MetaData(
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
            js = "Web - JS"
        ),
        logs = Strings.Widgets.Logs(
            title = "Logs",
            clearAll = "Clear all",
        ),
        logDetails = Strings.Widgets.LogDetails(
            title = "Log Detail",
            emptyTitle = "\uD83D\uDC47",
            emptyDescription = "Choose a Log to view details",
            responseDelayUnits = "ms delay",
            intendedFailure = "Used error response",
            intendedSuccess = "Used non error response",
            requestHeaders = "Request headers",
            requestBody = "Request body",
            responseHeaders = "Response headers",
            responseBody = "Response body",
            noHeaders = "None",
            noBody = "Empty"
        ),
        endpoints = Strings.Widgets.Endpoints(
            filterPlaceholder = "Filter endpoints...",
            numberOfEndpointsShown = { current, max ->
                "Showing $current of $max endpoints"
            },
            overrides = { number ->
                when (number) {
                    1 -> "1 override:"
                    else -> "$number overrides:"
                }
            }
        ),
        globalControls = Strings.Widgets.GlobalControls(
            title = "Global Controls",
            subtitle = "Apply to all endpoints",
            resetAllLabel = "Reset All",
            failButtonLabel = "Force Failure",
            restoreButtonLabel = "Restore API",
            normalBehaviourBannerConfig = Strings.Widgets.GlobalControls.GlobalConfigBanner(
                title = "Normal API Behavior",
                subtitle = "API will respond as normal. (Error presets will still return their errors)",
            ),
            partialFailureBannerConfig = Strings.Widgets.GlobalControls.GlobalConfigBanner(
                title = "Partial Failure Enabled",
                subtitle = "Some API calls are forced to fail, others are not.",
            ),
            forcedFailureBannerConfig = Strings.Widgets.GlobalControls.GlobalConfigBanner(
                title = "Forced API Failure Enabled",
                subtitle = "All API calls will return error responses regardless of your configured presets.",
            ),
        ),
        latency = Strings.Widgets.Latency(
            title = "Response Latency",
            sliderMin = "0s",
            sliderMax = "60s",
            millisecondLabel = "ms",
        ),
        endpointDetails = Strings.Widgets.EndpointDetails(
            title = "Editor",
            subtitle = "Configure HTTP response",
            none = "No endpoint selected",
            statusCode = "Status code",
            edit = "Edit",
            reset = "Reset",
            resetUseErrorResponse = "Reset",
            headersUnset = "Headers unset",
            emptyTitle = "\uD83D\uDC48",
            emptyDescription = "Choose an Endpoint to start editing",
            forcedApiFailureBannerTitle = "Forced API Failure Enabled",
            forcedApiFailureBannerSubtitle = "This setting is currently being overridden",
            presets = Strings.Widgets.EndpointDetails.Presets(
                title = "Presets",
                noPresetTitle = "No Override Selected",
                noPresetBody = "Select a preset or create a custom response",
                typeDescriptions = Strings.Widgets.EndpointDetails.Presets.TypeDescriptions(
                    error = "Error",
                    informational = "Info",
                    other = "Custom",
                    redirect = "Redirect",
                    success = "Success"
                ),
                noAvailablePresetsTitle = "No presets yet!",
                noAvailablePresetsBody = "Define presets in code to easily switch your responses for pre-defined ones",
                moreInfoButton = "More Information",
                activePresetTitle = "Configure Overrides",
                createCustomButton = "Create Custom",
                filterPlaceholder = "Filter Presets",
                filterPlaceholderEmpty = "Nothing here :(",
                statusCodeFallback = "XXX",
                applyLabel = "Apply",
                appliedLabel = "Applied",
                editLabel = "Edit"
            )
        ),
        miscControls = Strings.Widgets.MiscControls(
            refreshAll = "Re-sync all",
            clearOverrides = "Reset all overrides",
            title = "Tools",
            presentationMode = "Presentation mode",
            fontScaleLabel = { scale -> "${(scale * 100).roundToInt()}%" }
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
        ),
        createEditPreset = Strings.Widgets.CreateEditPreset(
            createTitle = "Create Preset",
            editTitle = "Edit Preset",
            statusCodeTitle = "HTTP Status Code",
            noOverrideStatusCode = "Unset",
            statusCodeLabel = { statusCode: HttpStatusCode ->
                "${statusCode.value} ${statusCode.description}"
            },
            bodyTitle = "Response Body",
            bodyTypeJson = "JSON",
            bodyTypePlain = "Plain Text",
            responseBodyFormat = "Format",
            responseBodyCopy = "Copy",
            responseBodyPlaceholder = "Enter response body...",
            responseCharacters = { chars -> "$chars characters" },
            validLabel = "Valid",
            invalidLabel = "Invalid",
            headersTitle = "Headers",
            addHeaderTitle = "Add New Header",
            addHeaderButton = "Add Header",
            save = "Save",
            addHeaderKeyPlaceholder = "Header name",
            addHeaderValuePlaceholder = "Header value",
            unset = "Unset"
        )
    )
)
