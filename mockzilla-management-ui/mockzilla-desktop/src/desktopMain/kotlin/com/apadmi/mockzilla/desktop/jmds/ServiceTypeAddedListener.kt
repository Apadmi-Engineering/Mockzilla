@file:NoKDoc

package com.apadmi.mockzilla.desktop.jmds

import com.apadmi.mockzilla.lib.NoKDoc

import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceTypeListener

internal class ServiceTypeAddedListener(
    val onTypeAdded: (event: ServiceEvent?) -> Unit
) : ServiceTypeListener {
    override fun serviceTypeAdded(event: ServiceEvent?) = onTypeAdded(event)
    override fun subTypeForServiceTypeAdded(event: ServiceEvent?) {
        /* No-op */
    }
}
