package com.apadmi.mockzilla.desktop.engine.connection

import com.apadmi.mockzilla.desktop.engine.connection.jmdns.JmdnsWrapper
import com.apadmi.mockzilla.lib.config.ZeroConfConfig
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.MetaData.Companion.parseMetaData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class DetectedDevice(
    val connectionName: String,
    val metaData: MetaData?,
    val ipAddress: String,
    val port: Int
)

interface DeviceDetectionUseCase {
    val devices: StateFlow<List<DetectedDevice>>
}

class DeviceDetectionUseCaseImpl(
    jmdnsWrapper: JmdnsWrapper
) : DeviceDetectionUseCase {
    override val devices = MutableStateFlow<List<DetectedDevice>>(emptyList())

    init {
        jmdnsWrapper.registerListener(ZeroConfConfig.serviceType + ".local.") {
            val newList = devices.value.toMutableList() + DetectedDevice(
                it.connectionName,
                it.attributes.parseMetaData(),
                it.hostAddress,
                it.port
            )
            devices.value = newList.distinctBy { it.connectionName }
        }
    }

}