package com.apadmi.mockzilla.desktop.engine.connection.jmdns

import com.apadmi.mockzilla.lib.config.ZeroConfConfig

import java.net.InetAddress
import javax.jmdns.JmDNS
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceInfo
import javax.jmdns.ServiceListener

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class JmdnsWrapper(
    private val serviceType: String,
    private val scope: CoroutineScope = GlobalScope
) : ServiceListener {
    private val jmdns: JmDNS = JmDNS.create(InetAddress.getLocalHost())
    private lateinit var listener: suspend (ServiceInfoWrapper) -> Unit

    fun setListener(listener: suspend (ServiceInfoWrapper) -> Unit) {
        this.listener = listener
        jmdns.addServiceTypeListener(ServiceTypeAddedListener { event ->
            if (event?.type?.startsWith(ZeroConfConfig.serviceType) == true) {
                jmdns.addServiceListener(serviceType, this)
            }
        })
    }

    override fun serviceAdded(
        event: ServiceEvent?
    ) = serviceChanged(event, ServiceInfoWrapper.State.Found)

    private fun serviceChanged(event: ServiceEvent?, state: ServiceInfoWrapper.State) {
        event ?: return
        scope.launch {
            withContext(Dispatchers.IO) {
                listener(event.info.parse(state))
            }
        }
    }
    override fun serviceRemoved(
        event: ServiceEvent?
    ) = serviceChanged(event, ServiceInfoWrapper.State.Removed)

    override fun serviceResolved(
        event: ServiceEvent?
    ) = serviceChanged(event, ServiceInfoWrapper.State.Resolved)

    private fun ServiceInfo.parse(state: ServiceInfoWrapper.State): ServiceInfoWrapper {
        val hostAddresses = (inet6Addresses.toList() + inet4Addresses + inetAddresses).mapNotNull {
            it.hostAddress
        } + hostAddresses

        return ServiceInfoWrapper(
            this,
            hostAddresses.map { it.removePrefix("[").removeSuffix("]") }.distinct(),
            state
        )
    }
}
