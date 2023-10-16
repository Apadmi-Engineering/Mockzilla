package com.apadmi.mockzilla.desktop.i18n

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
