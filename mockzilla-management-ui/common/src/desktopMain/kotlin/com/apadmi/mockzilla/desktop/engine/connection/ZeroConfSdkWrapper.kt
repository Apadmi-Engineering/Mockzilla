package com.apadmi.mockzilla.desktop.engine.connection

import com.apadmi.mockzilla.desktop.jmds.create

import co.touchlab.kermit.Logger

import java.net.InetAddress
import java.net.NetworkInterface
import javax.jmdns.JmDNS
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceInfo
import javax.jmdns.ServiceListener

import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

actual class ZeroConfSdkWrapper actual constructor(
    private val serviceType: String,
    private val scope: CoroutineScope
) : ServiceListener {
    private var jmDns: JmDNS? = null
    private lateinit var listener: suspend (ServiceInfoWrapper) -> Unit
    actual fun setListener(listener: suspend (ServiceInfoWrapper) -> Unit) {
        this.listener = listener

        scope.launch {
            withContext(Dispatchers.IO) {
                val jmDns = JmDNS.create(awaitLocalIpForMdns()).also {
                    this@ZeroConfSdkWrapper.jmDns = it
                }
                jmDns.registerServiceType(serviceType)
                jmDns.addServiceListener(serviceType, this@ZeroConfSdkWrapper)
            }
        }
    }

    override fun serviceAdded(
        event: ServiceEvent?
    ) = serviceChanged(event, ServiceInfoWrapper.State.Found)

    private fun serviceChanged(event: ServiceEvent?, state: ServiceInfoWrapper.State) {
        event ?: return
        Logger.i { "Service changed: ${event.name} ${state.name}" }

        scope.launch {
            // If there's no ipv4 addresses there's no way we can connect to it so ignore the Resolved event in this case
            val shouldCallListener = state == ServiceInfoWrapper.State.Found ||
                    state == ServiceInfoWrapper.State.Removed ||
                    state == ServiceInfoWrapper.State.Resolved && event.info.inet4Addresses.isNotEmpty()

            if (shouldCallListener) {
                listener(
                    event.info.parse(state)
                )
            }
        }
    }

    override fun serviceRemoved(
        event: ServiceEvent?
    ) = serviceChanged(event, ServiceInfoWrapper.State.Removed)

    override fun serviceResolved(
        event: ServiceEvent?
    ) = serviceChanged(event, ServiceInfoWrapper.State.Resolved)

    private suspend fun awaitLocalIpForMdns(): InetAddress {
        while (true) {
            withContext(Dispatchers.IO) {
                NetworkInterface.getNetworkInterfaces().findMdnsAddress()
            }?.let {
                return it
            }

            delay(1.5.seconds)
        }
    }

    private fun ServiceInfo.parse(state: ServiceInfoWrapper.State): ServiceInfoWrapper {
        val hostAddresses = (inet6Addresses.toList() + inet4Addresses + inetAddresses).mapNotNull {
            it.hostAddress
        } + hostAddresses

        return ServiceInfoWrapper.create(
            this,
            hostAddresses.map { it.removePrefix("[").removeSuffix("]") }.distinct(),
            state
        )
    }
}
