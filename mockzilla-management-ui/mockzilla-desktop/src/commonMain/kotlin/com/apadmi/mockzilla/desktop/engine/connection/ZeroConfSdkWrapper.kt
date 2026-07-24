@file:NoKDoc

package com.apadmi.mockzilla.desktop.engine.connection

import com.apadmi.mockzilla.lib.NoKDoc
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.ui.engine.connection.AdbConnection

import kotlinx.coroutines.CoroutineScope

internal data class DeviceDiscoveryEvent(
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

internal expect class ZeroConfSdkWrapper(serviceType: String, scope: CoroutineScope) {
    fun setListener(listener: suspend (DeviceDiscoveryEvent) -> Unit)
    suspend fun stop()
}
