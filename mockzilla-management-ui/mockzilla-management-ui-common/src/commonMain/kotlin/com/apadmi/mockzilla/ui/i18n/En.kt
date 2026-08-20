@file:Suppress("COMPLEX_EXPRESSION", "MAGIC_NUMBER")

package com.apadmi.mockzilla.ui.i18n

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.ui.engine.events.GenericErrorableOperation

import cafe.adriel.lyricist.LyricistStrings
import io.ktor.http.HttpStatusCode
import kotlin.String

import kotlin.math.roundToInt

@InternalMockzillaApi
@LyricistStrings(languageTag = "En", default = true)
public val EnStrings: Strings = Strings(
    common = Strings.Common(
        closeDescription = "Close",
        backDescription = "Back",
        debugDescription = "Debug",
        resetDescription = "Reset",
        deleteDescription = "Delete",
        globalDescription = "Global Controls",
        metaDescription = "Meta Data",
    ),
    menu = Strings.Menu(
        openSourceLicenses = "Open source licences",
        about = "About",
        github = "GitHub",
        documentation = "Documentation",
        apadmi = "About Us",
        tools = "Tools",
        codeGen = "Code Generator",
    ),
    widgets = Strings.Widgets(
        deviceConnection = Strings.Widgets.DeviceConnection(
            title = "Mockzilla",
            subTile = "Dynamic mock server configuration for Android & iOS development",
            bullet1 = "Intercept HTTP at runtime",
            bullet2 = "Switch presets on the fly",
            bullet3 = "Simulate network latency",
            bullet4 = "Inspect request/response logs",
            tabTitle = "Connect Device",
            heading = "Enter IP and port to connect to a device",
            autoConnectHeading = "Or…",
            autoConnectSubHeading = "Choose a device to connect automatically",
            autoConnectButton = "Connect",
            ipInputLabel = "e.g. 127.0.0.1:8080",
            androidDevConnectButton = "Connect to development Mockzilla",
            errorTitle = "Failed to connect",
            errorMessage = "Please check the following:" +
                    "\n1. You have called startMockzilla() during your application's launch." +
                    "\n2. The Mockzilla library you are using is at or above the minimum version (KMP: 2.4.1, Flutter: 1.3.0).",
            connected = "Connected",
            tooltips = Strings.Widgets.DeviceConnection.ToolTips(
                notYourSimulator = "We don't think this is your simulator, but you can try to connect! (Probably won't work)",
                readyToConnect = "",
                removed = "This device seems to have disconnected",
                resolving = "We're still waiting for this device to come online",
            ),
            ipAndPort = "127.0.0.1:8080",
            networkConnection = "MANUAL CONNECTION",
            promptToEnterIp = "Enter the IP address and port of the device running your app",
            connectAutomatically = "or connect automatically",
            discoveredNetwork = "DISCOVERED ON NETWORK",
            scanning = "scanning...",
            noDevicesFound = "No devices found yet",
            noDevicesDescription = "Scanning the local network for devices running the Mockzilla SDK. Or enter an address above to connect manually.",
            connect = "Connect",
            dot = "..."
        ),
        deviceTabs = Strings.Widgets.DeviceTabs(
            tabTitle = { "Device $it" },
            addDevice = "Add Device",
            connected = "Connected",
            disconnected = "Disconnected",
            empty = "0 devices",
            closeButtonDescription = "Close",
            betaBanner = "BETA"
        ),
        metaData = Strings.Widgets.MetaData(
            title = "Meta Data",
            viewAppMetaData = "View app metadata",
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
            js = "Web - JS",
            deviceSection = "Device",
            error = "Failed to load device info",
        ),
        logs = Strings.Widgets.Logs(
            title = "Logs",
            clearAll = "Clear all",
            openInPanel = "Open in panel →",
            streaming = "STREAMING",
            clickToInspect = "·  CLICK ROW TO INSPECT",
            emptyTitle = "No logs yet",
            emptyDescription = "Make some requests to see them here",
        ),
        logDetails = Strings.Widgets.LogDetails(
            title = "Log Detail",
            emptyTitle = "Click a log entry to inspect it",
            emptyDescription = null,
            responseDelayUnits = "ms delay",
            intendedFailure = "Used error response",
            intendedSuccess = "Used non-error response",
            requestHeaders = "Request headers",
            requestBody = "Request body",
            responseHeaders = "Response headers",
            responseBody = "Response body",
            noHeaders = "None",
            noBody = "Empty",
            emptyBody = "(none)",
            bodyLoadError = "Failed to load full body"
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
            },
            noOverrides = "no overrides",
            forced = "FORCED",
            emptyTitle = "No endpoints found",
            emptyDescription = "Try adjusting your filter"
        ),
        globalControls = Strings.Widgets.GlobalControls(
            title = "Global Controls",
            subtitle = "Apply to all endpoints",
            resetAllLabel = "Reset All",
            failButtonLabel = "Force Fail",
            restoreButtonLabel = "Resume",
            normalBehaviourBannerConfig = Strings.Widgets.GlobalControls.GlobalConfigBanner(
                title = "Normal Behaviour",
                subtitle = "API responds normally. Error presets still apply",
            ),
            partialFailureBannerConfig = Strings.Widgets.GlobalControls.GlobalConfigBanner(
                title = "Partial Failure",
                subtitle = "Some API calls are forced to fail, others are not.",
            ),
            forcedFailureBannerConfig = Strings.Widgets.GlobalControls.GlobalConfigBanner(
                title = "Forced Failure",
                subtitle = "All requests call error handler. Presets ignored.",
            ),
            activeOverrides = { " · $it active" },
            perEndpointStatus = "PER-ENDPOINT STATUS",
            forcedStatus = "FORCED",
            latencyStatus = "LATENCY",
            bodyStatus = "BODY",
            headersStatus = "HEADERS",
            statusStatus = "STATUS",
        ),
        latency = Strings.Widgets.Latency(
            title = "Response Latency",
            sliderMin = "0s",
            sliderMax = "60s",
            millisecondLabel = { "${it}ms" },
            secondLabel = { "${it}s" },
            notSet = "Not Set",
            clear = "Clear",
        ),
        endpointDetails = Strings.Widgets.EndpointDetails(
            title = "Editor",
            subtitle = "Configure mock response",
            none = "No endpoint selected",
            statusCode = "Status code",
            edit = "Edit",
            reset = "Reset",
            resetUseErrorResponse = "Reset",
            headersUnset = "Headers unset",
            emptyTitle = "Choose an Endpoint to start editing",
            emptyDescription = null,
            forcedApiFailureBannerTitle = "Forced Failure",
            forcedApiFailureBannerSubtitle = "This setting is currently being overridden",
            behavior = "Behaviour",
            latency = "Latency",
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
                filterPlaceholderEmpty = "No matches",
                statusCodeFallback = "XXX",
                applyLabel = "Apply",
                appliedLabel = "Applied",
                editLabel = "Edit",
                forceFailureBannerTitle = "Forced Failure",
                forceFailureBannerBody = "Presets are ignored and locked",
                forceFailureAppliedPresetMessage = "Ignored - Forced Failure is on",
                failedToLoad = "Failed to load presets"
            )
        ),
        miscControls = Strings.Widgets.MiscControls(
            refreshAll = "Re-sync all",
            clearOverrides = "Reset all overrides",
            title = "Tools",
            actionsSection = "Actions",
            presentationMode = "Presentation mode",
            darkMode = "Dark Mode",
            fontScaleLabel = { scale -> "${(scale * 100).roundToInt()}%" }
        ),
        unsupportedMockzilla = Strings.Widgets.UnsupportedMockzillaVersion(
            heading = "Unsupported SDK",
            subtitle = "This app doesn't support the version of Mockzilla running on your device",
            footer = "Please update to the latest version of Mockzilla",
        ),
        errorBanner = Strings.Widgets.ErrorBanner(
            connectionLost = "Attempting to reconnect...",
            refreshButton = "Re-sync everything",
            operationError = { operation ->
                when (operation) {
                    GenericErrorableOperation.FetchDashboardOptionsConfig -> "Couldn't fetch the dashboard config for that endpoint"
                    GenericErrorableOperation.FetchEndpointConfigs -> "Couldn't fetch the endpoint configs"
                    GenericErrorableOperation.UpdateMockData -> "Couldn't push new config"
                    GenericErrorableOperation.ApplyPreset -> "Couldn't apply the preset"
                    GenericErrorableOperation.ClearCaches -> "Couldn't clear caches"
                    GenericErrorableOperation.UpdateGlobalOverrides -> "Couldn't override those properties"
                    null -> "Something went wrong"
                }
            },
            apiErrorDescription = "This is an unexpected error and is likely irrecoverable. Re-syncing everything is advised. (You will lose unsaved changes.)",
            connectionErrorTitlesAndBodies = listOf(
                " · Is in the foreground." to " Background apps may have networking suspended by the OS.",
                " · Is on the same network." to " Mockzilla discovers over LAN. Check Wi-Fi vs. Data. ",
                " · Port reachable." to " Confirm the Mockzilla port isn't blocked by a firewall or VPN."
            ),
            statusLabel = "Status: ",
            messageLabel = "Message: ",
            connectionErrorTitle = "Please ensure the app:",
        ),
        linuxUnsupportedBanner = Strings.Widgets.LinuxUnsupportedBanner(
            title = "Linux is not officially supported",
            message = "Mockzilla for desktop Linux is provided as-is. Rendering issues may occur and this " +
                    "platform does not receive the same testing as macOS and Windows.",
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

            bodyTypeHtml = "HTML",
            bodyTypeNone = "None",
            responseBodyFormat = "Format",
            responseBodyCopy = "Copy",
            responseBodyPlaceholder = "{\"key\": \"value\"}",
            htmlBodyPlaceholder = "<p>Hello world</p>",
            plainBodyPlaceholder = "Response body...",
            responseCharacters = { chars ->
                when {
                    chars > 9999 -> "${(chars / 1000)}k chars"
                    else -> "$chars chars"
                }
            },
            validLabel = "Valid",
            invalidLabel = "Invalid JSON",
            headersTitle = "Headers",
            addHeaderTitle = "Add New Header",
            addHeaderButton = "Add Header",
            save = "Save & Close",
            addHeaderKeyPlaceholder = "Header name",
            addHeaderValuePlaceholder = "Value",
            unset = "Unset",
            cancel = "Cancel",
            endpointSubtitle = { name -> "for $name" },
            statusCodeRowLabel = "Status code",
            bodyTypeLabel = "Body type",
            responseSectionLabel = "Response",
            bodyLabel = "Body",
            jsonErrorTitle = "Invalid JSON:",
            collapse = "Collapse",
            expand = "Expand",
            apply = "Apply"
        ),
        openSourceLicenses = Strings.Widgets.OpenSourceLicenses(
            error = "Failed to load licences",
            title = "Open source licences",
            devBuildsMessage = "Licenses not generated for debug builds"
        ),
        codeGen = Strings.Widgets.CodeGen(
            title = "Mockzilla Config Code Generator",
            description = "This tool allows you to input a swagger file (either yaml or json) to autogenerate Mockzilla Config.",
            inputInvalid = "Input type must be .yaml/.yml/.json",
            outputInvalid = "Output type must be .dart",
            generatorError = { throwable -> "File failed to generate: ${throwable.message}" },
            success = "File generated successfully!",
            inputTitle = "INPUT FILE NAME",
            inputDesc = "Enter the full path to yaml/json swagger file.",
            inputPlaceholder = "/Users/example_path/example.yaml",
            outputTitle = "OUTPUT FILE NAME",
            outputDesc = "Full path to where the new generated file should be written. Accepted file types: .dart",
            outputPlaceholder = "/Users/generated_path/mockzilla_config.g.dart",
            button = "Generate"
        )
    ),
    components = Strings.Components(
        editor = Strings.Components.Editor(
            largeFileSyntaxHighlightError = "Syntax highlighting disabled for large files",
            jsonErrorTitle = "Invalid JSON:",
        ),
        findReplace = Strings.Components.FindReplace(
            findPlaceholder = "Find",
            replacePlaceholder = "Replace",
            noResults = "No results",
            matchCount = { current, total -> "$current/$total" },
            collapseReplaceDescription = "Collapse replace",
            expandReplaceDescription = "Expand replace",
            previousMatchDescription = "Previous match",
            nextMatchDescription = "Next match",
            closeFindBarDescription = "Close find bar",
            replaceButton = "Replace",
            replaceAllButton = "All",
        ),
        genericError = Strings.Components.GenericError(
            title = "Something went wrong",
            body = "Please check your device is connected and try again",
            retryButton = "Retry"
        )
    ),
    links = Strings.Links(
        docsHome = "https://mockzilla.apadmi.dev/",
        docsPresets = "https://mockzilla.apadmi.dev/presets/",
        github = "https://github.com/Apadmi-Engineering/Mockzilla",
        apadmi = "https://apadmi.dev/"
    )
)
