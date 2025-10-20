package com.apadmi.mockzilla.desktop.jmds

import com.apadmi.mockzilla.desktop.engine.connection.ServiceInfoWrapper
import javax.jmdns.ServiceInfo

internal fun ServiceInfoWrapper.Companion.create(
    info: ServiceInfo,
    hostAddresses: List<String>,
    state: ServiceInfoWrapper.State
) = ServiceInfoWrapper(
    connectionName = info.name,
    hostAddress = info.inet4Addresses.firstOrNull()?.hostAddress ?: info.hostAddress,
    hostAddresses = hostAddresses,
    attributes = info.propertyNames.toList().associateWith { info.getPropertyString(it) },
    port = info.port,
    state = state
)
