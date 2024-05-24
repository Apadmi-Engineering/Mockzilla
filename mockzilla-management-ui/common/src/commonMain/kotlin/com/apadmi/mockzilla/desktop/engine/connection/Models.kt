package com.apadmi.mockzilla.desktop.engine.connection

import com.apadmi.mockzilla.lib.models.MetaData

data class DetectedDevice(
    val connectionName: String,
    val metaData: MetaData?,
    val hostAddress: String,
    val hostAddresses: List<String>,
    val port: Int,
    val adbConnection: AdbConnection?,
    val state: State
) {
    enum class State {
        Resolving,
        ReadyToConnect,
        NotYourSimulator,
        Removed
    }
}
