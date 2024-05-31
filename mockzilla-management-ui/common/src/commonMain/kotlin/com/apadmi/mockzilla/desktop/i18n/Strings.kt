package com.apadmi.mockzilla.desktop.i18n

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
 */
data class Strings(
    val widgets: Widgets
) {
    /**
     * @property deviceConnection
     * @property deviceTabs
     * @property metaData
     * @property logs
     * @property endpointDetails
     */
    data class Widgets(
        val deviceConnection: DeviceConnection,
        val deviceTabs: DeviceTabs,
        val metaData: MetaData,
        val logs: Logs,
        val endpointDetails: EndpointDetails,
        val endpoints: Endpoints,
    ) {
        /**
         * @property title
         */
        data class Logs(val title: String)

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
        )

        /**
         * @property tabTitle
         * @property ipInputLabel
         */
        data class DeviceConnection(
            val tabTitle: String,
            val ipInputLabel: String,
        )

        /**
         * @property tabTitle
         * @property addDevice
         * @property connected
         * @property disconnected
         * @property devices
         */
        data class DeviceTabs(
            val tabTitle: (index: Int) -> String,
            val addDevice: String,
            val connected: String,
            val disconnected: String,
            val devices: (number: Int) -> String,
        )

        data class Endpoints(
            val selectAllTooltip: String,
            val errorSwitchLabel: String,
            val valuesOverriddenIndicatorTooltip: String
        )
        /**
         * @property none
         * @property useErrorResponse
         * @property defaultDataTab
         * @property errorDataTab
         * @property generalTab
         * @property noOverrideStatusCode
         * @property statusCodeLabel
         * @property statusCode
         * @property edit
         * @property reset
         * @property resetUseErrorResponse
         * @property bodyLabel
         * @property bodyUnset
         * @property delayLabel
         * @property jsonEditingLabel
         * @property failOptionsLabel
         * @property failLabel
         * @property responseDelay
         * @property noOverrideResponseDelay
         * @property customResponseDelay
         * @property responseDelayUnits
         * @property responseDelayLabel
         * @property resetAll
         * @property headersLabel
         * @property headerKeyLabel
         * @property headerValueLabel
         * @property headerDeleteContentDescription
         * @property addHeader
         * @property editHeaders
         * @property resetHeaders
         * @property noHeaders
         * @property headersUnset
         * @property title
         */
        data class EndpointDetails(
            val title: String,
            val none: String,
            val useErrorResponse: String,
            val defaultDataTab: String,
            val errorDataTab: String,
            val generalTab: String,
            val noOverrideStatusCode: String,
            val statusCodeLabel: (HttpStatusCode) -> String,
            val statusCode: String,
            val edit: String,
            val reset: String,
            val resetUseErrorResponse: String,
            val bodyLabel: String,
            val bodyUnset: String,
            val delayLabel: String,
            val jsonEditingLabel: (Boolean) -> String,
            val failOptionsLabel: String,
            val failLabel: (Boolean?) -> String,
            val responseDelay: String,
            val noOverrideResponseDelay: String,
            val customResponseDelay: String,
            val responseDelayUnits: String,
            val responseDelayLabel: String,
            val resetAll: String,
            val headersLabel: String,
            val headerKeyLabel: String,
            val headerValueLabel: String,
            val headerDeleteContentDescription: String,
            val addHeader: String,
            val editHeaders: String,
            val resetHeaders: String,
            val noHeaders: String,
            val headersUnset: String
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
