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
     */
    data class Widgets(
        val deviceConnection: DeviceConnection,
        val deviceTabs: DeviceTabs,
        val metaData: MetaData,
    ) {
        /**
         * @property title
         */
        data class MetaData(val title: String)

        /**
         * @property tabTitle
         */
        data class DeviceConnection(
            val tabTitle: String
        )

        /**
         * @property tabTitle
         * @property addDevice
         */
        data class DeviceTabs(
            val tabTitle: (index: Int) -> String,
            val addDevice: String
        )
    }
}

@Composable
fun ProvideLocalisableStrings(content: @Composable () -> Unit) {
    val lyricist = rememberStrings(strings)

    ProvideStrings(lyricist, LocalStrings, content)
}
