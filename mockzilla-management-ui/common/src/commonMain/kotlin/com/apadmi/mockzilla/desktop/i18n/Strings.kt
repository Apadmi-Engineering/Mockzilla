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
    ) {
        /**
         * @property title
         */
        data class Logs(val title: String)

        /**
         * @property title
         */
        data class MetaData(val title: String)

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

        /**
         * @property none
         * @property successData
         * @property errorData
         */
        data class EndpointDetails(
            val none: String,
            val useErrorResponse: String,
            val defaultData: String,
            val errorData: String,
            val noOverrideBody: String,
            val noOverrideStatusCode: String,
            val edit: String,
            val reset: String,
            val resetUseErrorResponse: String,
            val bodyLabel: String,
            val statusCodeLabel: (HttpStatusCode) -> String,
            val delayLabel: String,
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
