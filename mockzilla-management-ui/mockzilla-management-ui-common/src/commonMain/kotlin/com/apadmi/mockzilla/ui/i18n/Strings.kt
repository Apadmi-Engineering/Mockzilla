package com.apadmi.mockzilla.ui.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.lyricist.ProvideStrings
import cafe.adriel.lyricist.rememberStrings
import io.ktor.http.HttpStatusCode

@Suppress("VARIABLE_NAME_INCORRECT_FORMAT")
val LocalStrings = staticCompositionLocalOf { EnStrings }

private val strings = mapOf(
    "en" to EnStrings
)

/**
 * @property widgets
 * @property common
 */
data class Strings(
    val common: Common,
    val widgets: Widgets
) {
    /**
     * @property closeDescription
     * @property backDescription
     * @property debugDescription
     * @property resetDescription
     * @property deleteDescription
     */
    data class Common(
        val closeDescription: String,
        val backDescription: String,
        val debugDescription: String,
        val resetDescription: String,
        val deleteDescription: String
    )
    /**
     * @property deviceConnection
     * @property deviceTabs
     * @property metaData
     * @property logs
     * @property logDetails
     * @property endpointDetails
     * @property endpoints
     * @property miscControls
     * @property unsupportedMockzilla
     * @property errorBanner
     * @property globalControls
     * @property latency
     * @property createEditPreset
     */
    data class Widgets(
        val deviceConnection: DeviceConnection,
        val deviceTabs: DeviceTabs,
        val metaData: MetaData,
        val logs: Logs,
        val logDetails: LogDetails,
        val endpointDetails: EndpointDetails,
        val createEditPreset: CreateEditPreset,
        val endpoints: Endpoints,
        val globalControls: GlobalControls,
        val miscControls: MiscControls,
        val unsupportedMockzilla: UnsupportedMockzillaVersion,
        val errorBanner: ErrorBanner,
        val latency: Latency
    ) {
        /**
         * @property connectionLost
         * @property unknownError
         * @property refreshButton
         */
        data class ErrorBanner(
            val connectionLost: String,
            val unknownError: String,
            val refreshButton: String
        )

        /**
         * @property title
         * @property clearAll
         */
        data class Logs(
            val title: String,
            val clearAll: String,
        )

        /**
         * @property title
         * @property emptyTitle
         * @property emptyDescription
         * @property responseDelayUnits
         * @property intendedFailure
         * @property intendedSuccess
         * @property requestHeaders
         * @property requestBody
         * @property responseHeaders
         * @property responseBody
         * @property noHeaders
         * @property noBody
         */
        data class LogDetails(
            val title: String,
            val emptyTitle: String,
            val emptyDescription: String,
            val responseDelayUnits: String,
            val intendedFailure: String,
            val intendedSuccess: String,
            val requestHeaders: String,
            val requestBody: String,
            val responseHeaders: String,
            val responseBody: String,
            val noHeaders: String,
            val noBody: String
        )

        /**
         * @property refreshAll
         * @property clearOverrides
         * @property title
         * @property presentationMode
         * @property fontScaleLabel
         */
        data class MiscControls(
            val refreshAll: String,
            val clearOverrides: String,
            val title: String,
            val presentationMode: String,
            val fontScaleLabel: (Float) -> String,
        )

        /**
         * @property title
         * @property noDeviceConnected
         * @property appName
         * @property appPackage
         * @property operatingSystemVersion
         * @property deviceModel
         * @property appVersion
         * @property operatingSystem
         * @property mockzillaVersion
         * @property android
         * @property ios
         * @property jvm
         * @property js
         */
        data class MetaData(
            val title: String,
            val noDeviceConnected: String,
            val appName: String,
            val appPackage: String,
            val operatingSystemVersion: String,
            val deviceModel: String,
            val appVersion: String,
            val operatingSystem: String,
            val mockzillaVersion: String,
            val android: String,
            val ios: String,
            val jvm: String,
            val js: String,
        )

        /**
         * @property tabTitle
         * @property ipInputLabel
         * @property tooltips
         * @property heading
         * @property autoConnectHeading
         * @property autoConnectSubHeading
         * @property autoConnectButton
         * @property androidDevConnectButton
         * @property errorTitle
         * @property errorMessage
         * @property connected
         */
        data class DeviceConnection(
            val tabTitle: String,
            val ipInputLabel: String,
            val tooltips: ToolTips,
            val heading: String,
            val autoConnectHeading: String,
            val autoConnectSubHeading: String,
            val autoConnectButton: String,
            val androidDevConnectButton: String,
            val errorTitle: String,
            val errorMessage: String,
            val connected: String
        ) {
            /**
             * @property notYourSimulator
             * @property readyToConnect
             * @property removed
             * @property resolving
             */
            data class ToolTips(
                val notYourSimulator: String,
                val readyToConnect: String,
                val removed: String,
                val resolving: String
            )
        }

        /**
         * @property tabTitle
         * @property addDevice
         * @property connected
         * @property disconnected
         * @property devices
         * @property closeButtonDescription
         */
        data class DeviceTabs(
            val tabTitle: (index: Int) -> String,
            val addDevice: String,
            val connected: String,
            val disconnected: String,
            val devices: (number: Int) -> String,
            val closeButtonDescription: String,
        )

        /**
         * @property filterPlaceholder
         * @property numberOfEndpointsShown
         * @property overrides
         */
        data class Endpoints(
            val filterPlaceholder: String,
            val numberOfEndpointsShown: (shown: Int, max: Int) -> String,
            val overrides: (number: Int) -> String
        )

        /**
         * @property title
         * @property subtitle
         * @property resetAllLabel
         * @property normalBehaviourBannerConfig
         * @property forcedFailureBannerConfig
         * @property partialFailureBannerConfig
         * @property failButtonLabel
         * @property restoreButtonLabel
         */
        data class GlobalControls(
            val title: String,
            val subtitle: String,
            val resetAllLabel: String,
            val normalBehaviourBannerConfig: GlobalConfigBanner,
            val forcedFailureBannerConfig: GlobalConfigBanner,
            val partialFailureBannerConfig: GlobalConfigBanner,
            val failButtonLabel: String,
            val restoreButtonLabel: String,
        ) {
            /**
             * @property title
             * @property subtitle
             */
            data class GlobalConfigBanner(
                val title: String,
                val subtitle: String,
            )
        }

        /**
         * @property title
         * @property millisecondLabel
         * @property sliderMin
         * @property sliderMax
         */
        data class Latency(
            val title: String,
            val millisecondLabel: String,
            val sliderMin: String,
            val sliderMax: String
        )

        /**
         * @property none
         * @property statusCode
         * @property edit
         * @property reset
         * @property resetUseErrorResponse
         * @property headersUnset
         * @property subtitle
         * @property emptyTitle
         * @property emptyDescription
         * @property title
         * @property forcedApiFailureBannerTitle
         * @property forcedApiFailureBannerSubtitle
         * @property presets
         */
        data class EndpointDetails(
            val title: String,
            val subtitle: String,
            val none: String,
            val statusCode: String,
            val edit: String,
            val reset: String,
            val resetUseErrorResponse: String,
            val headersUnset: String,
            val emptyTitle: String,
            val emptyDescription: String,
            val forcedApiFailureBannerTitle: String,
            val forcedApiFailureBannerSubtitle: String,
            val presets: Presets
        ) {
            /**
             * @property noPresetTitle
             * @property noPresetBody
             * @property typeDescriptions
             * @property title
             * @property noAvailablePresetsTitle
             * @property noAvailablePresetsBody
             * @property moreInfoButton
             * @property activePresetTitle
             * @property createCustomButton
             * @property filterPlaceholder
             * @property filterPlaceholderEmpty
             * @property statusCodeFallback
             * @property applyLabel
             * @property appliedLabel
             * @property editLabel
             */
            data class Presets(
                val noPresetTitle: String,
                val noPresetBody: String,
                val typeDescriptions: TypeDescriptions,
                val title: String,
                val noAvailablePresetsTitle: String,
                val noAvailablePresetsBody: String,
                val moreInfoButton: String,
                val activePresetTitle: String,
                val createCustomButton: String,
                val filterPlaceholder: String,
                val filterPlaceholderEmpty: String,
                val statusCodeFallback: String,
                val applyLabel: String,
                val appliedLabel: String,
                val editLabel: String
            ) {
                /**
                 * @property error
                 * @property informational
                 * @property other
                 * @property redirect
                 * @property success
                 */
                data class TypeDescriptions(
                    val error: String,
                    val informational: String,
                    val other: String,
                    val redirect: String,
                    val success: String
                )
            }
        }

        /**
         * @property createTitle
         * @property editTitle
         * @property statusCodeTitle
         * @property noOverrideStatusCode
         * @property statusCodeLabel
         * @property bodyTitle
         * @property bodyTypeJson
         * @property bodyTypePlain
         * @property responseBodyFormat
         * @property responseBodyCopy
         * @property responseCharacters
         * @property validLabel
         * @property invalidLabel
         * @property headersTitle
         * @property addHeaderTitle
         * @property addHeaderButton
         * @property responseBodyPlaceholder
         * @property addHeaderKeyPlaceholder
         * @property addHeaderValuePlaceholder
         * @property save
         * @property unset
         */
        data class CreateEditPreset(
            val createTitle: String,
            val editTitle: String,
            val statusCodeTitle: String,
            val noOverrideStatusCode: String,
            val statusCodeLabel: (HttpStatusCode) -> String,
            val bodyTitle: String,
            val bodyTypeJson: String,
            val bodyTypePlain: String,
            val responseBodyFormat: String,
            val responseBodyCopy: String,
            val responseCharacters: (numChars: Int) -> String,
            val validLabel: String,
            val invalidLabel: String,
            val headersTitle: String,
            val addHeaderTitle: String,
            val addHeaderButton: String,
            val responseBodyPlaceholder: String,
            val addHeaderKeyPlaceholder: String,
            val addHeaderValuePlaceholder: String,
            val unset: String,
            val save: String,
        )

        /**
         * @property heading
         * @property subtitle
         * @property footer
         */
        data class UnsupportedMockzillaVersion(
            val heading: String,
            val subtitle: String,
            val footer: String
        )
    }
}

@Composable
fun ProvideLocalisableStrings(content: @Composable () -> Unit) {
    // Hardcoding the locale to english for now since we're only supporting english.
    // If we want to support multiple languages we'll have to check if the following bug is fixed,
    // if not we'll have to work around it:
    // https://github.com/adrielcafe/lyricist/issues/10
    val lyricist = rememberStrings(strings, strings.keys.first())

    ProvideStrings(lyricist, LocalStrings, content)
}
