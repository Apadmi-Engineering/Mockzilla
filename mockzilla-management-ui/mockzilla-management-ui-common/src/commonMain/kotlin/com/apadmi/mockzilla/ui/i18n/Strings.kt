package com.apadmi.mockzilla.ui.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

import com.apadmi.mockzilla.ui.engine.events.GenericErrorableOperation

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
 * @property components
 */
data class Strings(
    val common: Common,
    val widgets: Widgets,
    val components: Components
) {
    /**
     * @property editor
     * @property findReplace
     * @property genericError
     */
    data class Components(
        val editor: Editor,
        val findReplace: FindReplace,
        val genericError: GenericError,
    ) {
        /**
         * @property title
         * @property body
         * @property retryButton
         */
        data class GenericError(
            val title: String,
            val body: String,
            val retryButton: String
        )

        /**
         * @property largeFileSyntaxHighlightError
         * @property jsonErrorTitle
         */
        data class Editor(
            val largeFileSyntaxHighlightError: String,
            val jsonErrorTitle: String,
        )

        /**
         * @property findPlaceholder
         * @property replacePlaceholder
         * @property noResults
         * @property matchCount
         * @property collapseReplaceDescription
         * @property expandReplaceDescription
         * @property previousMatchDescription
         * @property nextMatchDescription
         * @property closeFindBarDescription
         * @property replaceButton
         * @property replaceAllButton
         */
        data class FindReplace(
            val findPlaceholder: String,
            val replacePlaceholder: String,
            val noResults: String,
            val matchCount: (current: Int, total: Int) -> String,
            val collapseReplaceDescription: String,
            val expandReplaceDescription: String,
            val previousMatchDescription: String,
            val nextMatchDescription: String,
            val closeFindBarDescription: String,
            val replaceButton: String,
            val replaceAllButton: String,
        )
    }
    /**
     * @property closeDescription
     * @property backDescription
     * @property debugDescription
     * @property resetDescription
     * @property deleteDescription
     * @property openSourceLicenses
     */
    data class Common(
        val closeDescription: String,
        val backDescription: String,
        val debugDescription: String,
        val resetDescription: String,
        val deleteDescription: String,
        val openSourceLicenses: String
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
         * @property refreshButton
         * @property operationError
         * @property apiErrorDescription
         * @property connectionErrorTitlesAndBodies
         * @property statusLabel
         * @property messageLabel
         * @property connectionErrorTitle
         */
        data class ErrorBanner(
            val connectionLost: String,
            val refreshButton: String,
            val operationError: (GenericErrorableOperation?) -> String,
            val apiErrorDescription: String,
            val connectionErrorTitlesAndBodies: List<Pair<String, String>>,
            val statusLabel: String,
            val messageLabel: String,
            val connectionErrorTitle: String
        )

        /**
         * @property title
         * @property clearAll
         * @property openInPanel
         * @property streaming
         * @property clickToInspect
         * @property emptyTitle
         * @property emptyDescription
         */
        data class Logs(
            val title: String,
            val clearAll: String,
            val openInPanel: String,
            val streaming: String,
            val clickToInspect: String,
            val emptyTitle: String,
            val emptyDescription: String,
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
         * @property emptyBody
         * @property bodyLoadError
         */
        data class LogDetails(
            val title: String,
            val emptyTitle: String,
            val emptyDescription: String?,
            val responseDelayUnits: String,
            val intendedFailure: String,
            val intendedSuccess: String,
            val requestHeaders: String,
            val requestBody: String,
            val responseHeaders: String,
            val responseBody: String,
            val noHeaders: String,
            val noBody: String,
            val emptyBody: String,
            val bodyLoadError: String,
        )

        /**
         * @property refreshAll
         * @property clearOverrides
         * @property title
         * @property presentationMode
         * @property fontScaleLabel
         * @property actionsSection
         * @property darkMode
         */
        data class MiscControls(
            val refreshAll: String,
            val clearOverrides: String,
            val title: String,
            val actionsSection: String,
            val presentationMode: String,
            val darkMode: String,
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
         * @property deviceSection
         * @property error
         * @property viewAppMetaData
         */
        data class MetaData(
            val title: String,
            val viewAppMetaData: String,
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
            val deviceSection: String,
            val error: String,
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
         * @property title
         * @property subTile
         * @property bullet1
         * @property bullet2
         * @property bullet3
         * @property bullet4
         * @property ipAndPort
         * @property networkConnection
         * @property promptToEnterIp
         * @property connectAutomatically
         * @property discoveredNetwork
         * @property scanning
         * @property connect
         * @property dot
         * @property noDevicesFound
         * @property noDevicesDescription
         */
        data class DeviceConnection(
            val title: String,
            val subTile: String,
            val bullet1: String,
            val bullet2: String,
            val bullet3: String,
            val bullet4: String,
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
            val connected: String,
            val ipAndPort: String,
            val networkConnection: String,
            val promptToEnterIp: String,
            val connectAutomatically: String,
            val discoveredNetwork: String,
            val scanning: String,
            val noDevicesFound: String,
            val noDevicesDescription: String,
            val connect: String,
            val dot: String,
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
         * @property empty
         * @property closeButtonDescription
         * @property betaBanner
         */
        data class DeviceTabs(
            val tabTitle: (index: Int) -> String,
            val addDevice: String,
            val connected: String,
            val disconnected: String,
            val empty: String,
            val closeButtonDescription: String,
            val betaBanner: String
        )

        /**
         * @property filterPlaceholder
         * @property numberOfEndpointsShown
         * @property overrides
         * @property noOverrides
         * @property forced
         * @property emptyTitle
         * @property emptyDescription
         */
        data class Endpoints(
            val filterPlaceholder: String,
            val numberOfEndpointsShown: (shown: Int, max: Int) -> String,
            val overrides: (number: Int) -> String,
            val noOverrides: String,
            val forced: String,
            val emptyTitle: String,
            val emptyDescription: String,
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
         * @property activeOverrides
         * @property perEndpointStatus
         * @property forcedStatus
         * @property latencyStatus
         * @property bodyStatus
         * @property headersStatus
         * @property statusStatus
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
            val activeOverrides: (Int) -> String,
            val perEndpointStatus: String,
            val forcedStatus: String,
            val latencyStatus: String,
            val bodyStatus: String,
            val headersStatus: String,
            val statusStatus: String,
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
         * @property secondLabel
         * @property sliderMin
         * @property sliderMax
         * @property notSet
         * @property clear
         */
        data class Latency(
            val title: String,
            val millisecondLabel: (Int) -> String,
            val secondLabel: (Int) -> String,
            val sliderMin: String,
            val sliderMax: String,
            val notSet: String,
            val clear: String,
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
         * @property behavior
         * @property latency
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
            val emptyDescription: String?,
            val forcedApiFailureBannerTitle: String,
            val forcedApiFailureBannerSubtitle: String,
            val behavior: String,
            val latency: String,
            val presets: Presets
        ) {
            /**
             * @property forceFailureAppliedPresetMessage
             * @property failedToLoad
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
             * @property forceFailureBannerTitle
             * @property forceFailureBannerBody
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
                val editLabel: String,
                val forceFailureBannerTitle: String,
                val forceFailureBannerBody: String,
                val forceFailureAppliedPresetMessage: String,
                val failedToLoad: String,
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
         * @property bodyTypeHtml
         * @property bodyTypeNone
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
         * @property cancel
         * @property endpointSubtitle
         * @property statusCodeRowLabel
         * @property bodyTypeLabel
         * @property responseSectionLabel
         * @property bodyLabel
         * @property htmlBodyPlaceholder
         * @property plainBodyPlaceholder
         * @property jsonErrorTitle
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
            val bodyTypeHtml: String,
            val bodyTypeNone: String,
            val responseBodyFormat: String,
            val responseBodyCopy: String,
            val responseCharacters: (numChars: Int) -> String,
            val validLabel: String,
            val invalidLabel: String,
            val headersTitle: String,
            val addHeaderTitle: String,
            val addHeaderButton: String,
            val responseBodyPlaceholder: String,
            val htmlBodyPlaceholder: String,
            val plainBodyPlaceholder: String,
            val addHeaderKeyPlaceholder: String,
            val addHeaderValuePlaceholder: String,
            val unset: String,
            val save: String,
            val cancel: String,
            val endpointSubtitle: (endpointName: String) -> String,
            val statusCodeRowLabel: String,
            val bodyTypeLabel: String,
            val responseSectionLabel: String,
            val bodyLabel: String,
            val jsonErrorTitle: String,
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
