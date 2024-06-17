package com.apadmi.mockzilla.desktop.engine.connection

import com.apadmi.mockzilla.desktop.jmds.create

import co.touchlab.kermit.Logger

import java.net.NetworkInterface
import javax.jmdns.JmDNS
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceInfo
import javax.jmdns.ServiceListener

import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val tag = "ZeroConfSdkWrapper"
actual class ZeroConfSdkWrapper actual constructor(
    private val serviceType: String,
    private val scope: CoroutineScope
) : ServiceListener {
    private var jmDnsInstances = mutableMapOf<String, JmDNS>()
    private var listenerJob: Job? = null
    private lateinit var listener: suspend (ServiceInfoWrapper) -> Unit

    actual fun setListener(listener: suspend (ServiceInfoWrapper) -> Unit) {
        jmDnsInstances.forEach { it.value.removeServiceListener(serviceType, this) }
        this.listener = listener

        listenerJob?.cancel()
        listenerJob = scope.launch {
            withContext(Dispatchers.IO) {
                while (listenerJob == null || listenerJob?.isCancelled == false) {
                    attachListenersIfNeeded()
                    delay(4.seconds)
                }
            }
        }
    }

    private suspend fun attachListenersIfNeeded() = withContext(Dispatchers.IO) {
        val newIps = NetworkInterface.getNetworkInterfaces().findMdnsAddresses()

        // Remove stale listeners
        jmDnsInstances.filterNot { (hostAddress, _) ->
            newIps.any { it.hostAddress == hostAddress }
        }.forEach { (hostAddress, jmDns) ->
            jmDns.removeServiceListener(serviceType, this@ZeroConfSdkWrapper)
            jmDnsInstances.remove(hostAddress)
            Logger.d(tag = tag) { "Removing stale listener for $hostAddress" }
        }

        // Add new listeners
        newIps.filterNot {
            jmDnsInstances.keys.contains(it.hostAddress)
        }.forEach { inetAddress ->
            val jmDns = JmDNS.create(inetAddress)

            jmDns.registerServiceType(serviceType)
            jmDns.addServiceListener(serviceType, this@ZeroConfSdkWrapper)
            jmDnsInstances[inetAddress.hostAddress] = jmDns
            Logger.i(tag = tag) { "Listening on ${inetAddress.hostAddress}" }
        }
    }

    override fun serviceAdded(
        event: ServiceEvent?
    ) = serviceChanged(event, ServiceInfoWrapper.State.Found)

    private fun serviceChanged(event: ServiceEvent?, state: ServiceInfoWrapper.State) {
        event ?: return
        Logger.d { "Service changed: ${event.name} ${state.name}" }

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
