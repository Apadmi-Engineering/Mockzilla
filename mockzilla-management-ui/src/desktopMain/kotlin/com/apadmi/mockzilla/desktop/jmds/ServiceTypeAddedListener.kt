package com.apadmi.mockzilla.desktop.jmds

import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceTypeListener

/**
 * @property onTypeAdded
 */
internal class ServiceTypeAddedListener(
    val onTypeAdded: (event: ServiceEvent?) -> Unit
) : ServiceTypeListener {
    override fun serviceTypeAdded(event: ServiceEvent?) = onTypeAdded(event)
    override fun subTypeForServiceTypeAdded(event: ServiceEvent?) {
        /* No-op */
    }
}
