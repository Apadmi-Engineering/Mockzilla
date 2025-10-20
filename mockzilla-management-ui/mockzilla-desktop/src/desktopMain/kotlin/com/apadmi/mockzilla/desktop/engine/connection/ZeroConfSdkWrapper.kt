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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val tag = "ZeroConfSdkWrapper"
actual class ZeroConfSdkWrapper actual constructor(
    private val serviceType: String,
    private val scope: CoroutineScope
) : ServiceListener {
    private var jmDnsInstances = mutableMapOf<InetAddress, JmDNS>()
    private var listenerJob: Job? = null
    private val output = MutableSharedFlow<ServiceInfoWrapper>()
    private lateinit var listener: suspend (ServiceInfoWrapper) -> Unit

    actual fun setListener(listener: suspend (ServiceInfoWrapper) -> Unit) {
        jmDnsInstances.forEach { it.value.removeServiceListener(serviceType, this) }
        this.listener = listener

        listenerJob?.cancel()
        listenerJob = scope.launch {
            output.distinctUntilChanged()
                .onEach(listener)
                .launchIn(this)

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
            newIps.any { it == hostAddress }
        }.forEach { (hostAddress, jmDns) ->
            jmDns.removeServiceListener(serviceType, this@ZeroConfSdkWrapper)
            jmDnsInstances.remove(hostAddress)
            Logger.d(tag) { "Removing stale listener for $hostAddress" }
        }

        // Add new listeners
        newIps.filterNot {
            jmDnsInstances.keys.contains(it)
        }.forEach { inetAddress ->
            val jmDns = JmDNS.create(inetAddress)

            jmDns.registerServiceType(serviceType)
            jmDnsInstances[inetAddress] = jmDns
        }

        jmDnsInstances.forEach { (inetAddress, jmDns) ->
            // Removing and re-adding listeners seems to give everything a kick and improves
            // reliability of device detection
            jmDns.removeServiceListener(serviceType, this@ZeroConfSdkWrapper)
            jmDns.addServiceListener(serviceType, this@ZeroConfSdkWrapper)
            Logger.i(tag) { "Listening on ${inetAddress.hostAddress}" }
        }
    }

    override fun serviceAdded(
        event: ServiceEvent?
    ) = serviceChanged(event, ServiceInfoWrapper.State.Found)

    private fun serviceChanged(event: ServiceEvent?, state: ServiceInfoWrapper.State) {
        Logger.d { "Service changed: ${event?.name} ${state?.name}" }
        event ?: return

        scope.launch {
            // If there's no ipv4 addresses there's no way we can connect to it so ignore the Resolved event in this case
            val shouldCallListener = state == ServiceInfoWrapper.State.Found ||
                    state == ServiceInfoWrapper.State.Removed ||
                    state == ServiceInfoWrapper.State.Resolved && event.info.inet4Addresses.isNotEmpty()

            if (shouldCallListener) {
                output.emit(event.info.parse(state))
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
