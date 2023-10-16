package com.apadmi.mockzilla.desktop.i18n

data class Strings(
    val widgets: Widgets
) {
    data class Widgets(
        val deviceConnection: DeviceConnection,
        val deviceTabs: DeviceTabs,
        val metaData: MetaData,
    ) {
        data class MetaData(val title: String)

        data class DeviceConnection(
            val tabTitle: String
        )

        data class DeviceTabs(
            val tabTitle: (index: Int) -> String,
            val addDevice: String
        )
    }
}