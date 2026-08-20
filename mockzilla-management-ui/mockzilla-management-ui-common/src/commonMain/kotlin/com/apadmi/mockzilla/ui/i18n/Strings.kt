package com.apadmi.mockzilla.ui.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.ui.engine.events.GenericErrorableOperation

import cafe.adriel.lyricist.ProvideStrings
import cafe.adriel.lyricist.rememberStrings
import io.ktor.http.HttpStatusCode

@InternalMockzillaApi
@Suppress("VARIABLE_NAME_INCORRECT_FORMAT")
public val LocalStrings: ProvidableCompositionLocal<Strings> =
    staticCompositionLocalOf { EnStrings }

private val strings = mapOf(
    "en" to EnStrings
)

@InternalMockzillaApi
public data class Strings(
    val common: Common,
    val widgets: Widgets,
    val components: Components,
    val menu: Menu,
    val links: Links
) {
    @InternalMockzillaApi
    public data class Links(
        val docsHome: String,
        val docsPresets: String,
        val github: String,
        val apadmi: String
    )

    @InternalMockzillaApi
    public data class Menu(
        val openSourceLicenses: String,
        val about: String,
        val github: String,
        val documentation: String,
        val apadmi: String,

        val tools: String,

        val codeGen: String

    )

    @InternalMockzillaApi
    public data class Components(
        val editor: Editor,
        val findReplace: FindReplace,
        val genericError: GenericError,
    ) {
        @InternalMockzillaApi
        public data class GenericError(
            val title: String,
            val body: String,
            val retryButton: String
        )

        @InternalMockzillaApi
        public data class Editor(
            val largeFileSyntaxHighlightError: String,
            val jsonErrorTitle: String,
        )

        @InternalMockzillaApi
        public data class FindReplace(
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

    @InternalMockzillaApi
    public data class Common(
        val closeDescription: String,
        val backDescription: String,
        val debugDescription: String,
        val resetDescription: String,
        val deleteDescription: String,
        val globalDescription: String,
        val metaDescription: String,
    )

    @InternalMockzillaApi
    public data class Widgets(
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
        val linuxUnsupportedBanner: LinuxUnsupportedBanner,
        val latency: Latency,
        val openSourceLicenses: OpenSourceLicenses,
        val codeGen: CodeGen
    ) {
        @InternalMockzillaApi
        public data class CodeGen(
            val title: String,
            val description: String,
            val inputInvalid: String,
            val outputInvalid: String,
            val generatorError: (Throwable) -> String,
            val success: String,
            val inputTitle: String,
            val inputDesc: String,
            val inputPlaceholder: String,
            val outputTitle: String,
            val outputDesc: String,
            val outputPlaceholder: String,
            val button: String
        )

        @InternalMockzillaApi
        public data class OpenSourceLicenses(
            val error: String,
            val title: String,
            val devBuildsMessage: String
        )

        @InternalMockzillaApi
        public data class ErrorBanner(
            val connectionLost: String,
            val refreshButton: String,
            val operationError: (GenericErrorableOperation?) -> String,
            val apiErrorDescription: String,
            val connectionErrorTitlesAndBodies: List<Pair<String, String>>,
            val statusLabel: String,
            val messageLabel: String,
            val connectionErrorTitle: String
        )

        @InternalMockzillaApi
        public data class LinuxUnsupportedBanner(
            val title: String,
            val message: String
        )

        @InternalMockzillaApi
        public data class Logs(
            val title: String,
            val clearAll: String,
            val openInPanel: String,
            val streaming: String,
            val clickToInspect: String,
            val emptyTitle: String,
            val emptyDescription: String,
        )

        @InternalMockzillaApi
        public data class LogDetails(
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

        @InternalMockzillaApi
        public data class MiscControls(
            val refreshAll: String,
            val clearOverrides: String,
            val title: String,
            val actionsSection: String,
            val presentationMode: String,
            val darkMode: String,
            val fontScaleLabel: (Float) -> String,
        )

        @InternalMockzillaApi
        public data class MetaData(
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

        @InternalMockzillaApi
        public data class DeviceConnection(
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
            @InternalMockzillaApi
            public data class ToolTips(
                val notYourSimulator: String,
                val readyToConnect: String,
                val removed: String,
                val resolving: String
            )
        }

        @InternalMockzillaApi
        public data class DeviceTabs(
            val tabTitle: (index: Int) -> String,
            val addDevice: String,
            val connected: String,
            val disconnected: String,
            val empty: String,
            val closeButtonDescription: String,
            val betaBanner: String
        )

        @InternalMockzillaApi
        public data class Endpoints(
            val filterPlaceholder: String,
            val numberOfEndpointsShown: (shown: Int, max: Int) -> String,
            val overrides: (number: Int) -> String,
            val noOverrides: String,
            val forced: String,
            val emptyTitle: String,
            val emptyDescription: String,
        )

        @InternalMockzillaApi
        public data class GlobalControls(
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
            @InternalMockzillaApi
            public data class GlobalConfigBanner(
                val title: String,
                val subtitle: String,
            )
        }

        @InternalMockzillaApi
        public data class Latency(
            val title: String,
            val millisecondLabel: (Int) -> String,
            val secondLabel: (Int) -> String,
            val sliderMin: String,
            val sliderMax: String,
            val notSet: String,
            val clear: String,
        )

        @InternalMockzillaApi
        public data class EndpointDetails(
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
            @InternalMockzillaApi
            public data class Presets(
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
                @InternalMockzillaApi
                public data class TypeDescriptions(
                    val error: String,
                    val informational: String,
                    val other: String,
                    val redirect: String,
                    val success: String
                )
            }
        }

        @InternalMockzillaApi
        public data class CreateEditPreset(
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
            val apply: String,
            val endpointSubtitle: (endpointName: String) -> String,
            val statusCodeRowLabel: String,
            val bodyTypeLabel: String,
            val responseSectionLabel: String,
            val bodyLabel: String,
            val jsonErrorTitle: String,
            val collapse: String,
            val expand: String,
        )

        @InternalMockzillaApi
        public data class UnsupportedMockzillaVersion(
            val heading: String,
            val subtitle: String,
            val footer: String
        )
    }
}

@InternalMockzillaApi
@Composable
public fun ProvideLocalisableStrings(content: @Composable () -> Unit) {
    // Hardcoding the locale to english for now since we're only supporting english.
    // If we want to support multiple languages we'll have to check if the following bug is fixed,
    // if not we'll have to work around it:
    // https://github.com/adrielcafe/lyricist/issues/10
    val lyricist = rememberStrings(strings, strings.keys.first())

    ProvideStrings(lyricist, LocalStrings, content)
}
