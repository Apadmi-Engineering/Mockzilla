package com.apadmi.mockzilla.desktop.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.lyricist.ProvideStrings
import cafe.adriel.lyricist.rememberStrings

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
     */
    data class Widgets(
        val deviceConnection: DeviceConnection,
        val deviceTabs: DeviceTabs,
        val metaData: MetaData,
        val logs: Logs
    ) {
        /**
         * @property title
         */
        data class Logs(val title: String)

        /**
         * @property noDeviceConnected
         * @property appName
         * @property appPackage
         * @property operatingSystemVersion
         * @property deviceModel
         * @property appVersion
         * @property operatingSystem
         * @property mockzillaVersion
         */
        data class MetaData(
            val noDeviceConnected: String,
            val appName: String,
            val appPackage: String,
            val operatingSystemVersion: String,
            val deviceModel: String,
            val appVersion: String,
            val operatingSystem: String,
            val mockzillaVersion: String
        )

        /**
         * @property tabTitle
         */
        data class DeviceConnection(
            val tabTitle: String
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
