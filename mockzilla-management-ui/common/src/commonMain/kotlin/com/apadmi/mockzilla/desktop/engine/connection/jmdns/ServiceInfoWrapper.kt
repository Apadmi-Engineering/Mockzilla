package com.apadmi.mockzilla.desktop.engine.connection.jmdns

import javax.jmdns.ServiceInfo

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
    constructor(
        info: ServiceInfo,
        hostAddresses: List<String>,
        state: State
    ) : this(
        connectionName = info.name,
        hostAddress = info.hostAddress,
        hostAddresses = hostAddresses,
        attributes = info.propertyNames.toList().associateWith { info.getPropertyString(it) },
        port = info.port,
        state = state
    )
    enum class State {
        Found,
        Removed,
        Resolved,
        ;
    }
}
