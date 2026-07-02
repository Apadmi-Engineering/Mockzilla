package com.apadmi.mockzilla.desktop.engine.connection

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.ui.engine.connection.AdbConnection

import kotlinx.coroutines.CoroutineScope

/**
 * @property connectionName
 * @property hostAddress
 * @property hostAddresses
 * @property attributes
 * @property port
 * @property state
 * @property adbConnection
 * @property metaData
 */
data class DeviceDiscoveryEvent(
    val connectionName: String,
    val hostAddress: String,
    val hostAddresses: List<String>,
    val attributes: Map<String, String>,
    val port: Int,
    val state: State,
    val adbConnection: AdbConnection? = null,
    val metaData: MetaData? = null
) {
    enum class State {
        Found,
        Removed,
        Resolved,
        ;
    }
    companion object
}

expect class ZeroConfSdkWrapper(serviceType: String, scope: CoroutineScope) {
    fun setListener(listener: suspend (DeviceDiscoveryEvent) -> Unit)
    suspend fun stop()
}
