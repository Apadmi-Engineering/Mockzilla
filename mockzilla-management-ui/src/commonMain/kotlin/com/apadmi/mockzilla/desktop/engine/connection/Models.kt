package com.apadmi.mockzilla.desktop.engine.connection

import com.apadmi.mockzilla.lib.models.MetaData

/**
 * @property connectionName
 * @property metaData
 * @property hostAddress
 * @property hostAddresses
 * @property port
 * @property adbConnection
 * @property state
 */
data class DetectedDevice(
    val connectionName: String,
    val metaData: MetaData?,
    val hostAddress: String,
    val hostAddresses: List<IpAddress>,
    val port: Int,
    val adbConnection: AdbConnection?,
    val state: State
) {
    enum class State {
        NotYourSimulator,
        ReadyToConnect,
        Removed,
        Resolving,
        ;
    }
}

/**
 * @property raw
 */
@JvmInline
value class IpAddress(val raw: String)
