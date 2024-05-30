package com.apadmi.mockzilla.desktop.engine.connection

import kotlinx.coroutines.CoroutineScope

/**
 * @property connectionName
 * @property hostAddress
 * @property hostAddresses
 * @property attributes
 * @property port
 * @property state
 */
data class ServiceInfoWrapper(
    val connectionName: String,
    val hostAddress: String,
    val hostAddresses: List<String>,
    val attributes: Map<String, String>,
    val port: Int,
    val state: State
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
    fun setListener(listener: suspend (ServiceInfoWrapper) -> Unit)
}
