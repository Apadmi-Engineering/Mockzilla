package com.apadmi.mockzilla.desktop.engine.connection.jmdns

import javax.jmdns.ServiceInfo

data class ServiceInfoWrapper(
    val connectionName: String,
    val hostAddress: String,
    val attributes: Map<String, String>,
    val port: Int,
) {
    constructor(info: ServiceInfo) : this(
        connectionName = info.name,
        hostAddress = info.hostAddress,
        attributes = info.propertyNames.toList().associateWith { info.getPropertyString(it) },
        port = info.port,
    )
}