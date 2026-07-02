package com.apadmi.mockzilla.desktop.engine.connection

import com.apadmi.mockzilla.desktop.jmds.create

import co.touchlab.kermit.Logger

import java.io.IOException
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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield

private const val tag = "ZeroConfSdkWrapper"
private const val bufferCapacity = 16

actual class ZeroConfSdkWrapper actual constructor(
    private val serviceType: String,
    private val scope: CoroutineScope
) : ServiceListener {
    private val jmDnsInstances = mutableMapOf<InetAddress, JmDNS>()
    private val output = MutableSharedFlow<DeviceDiscoveryEvent>(
        extraBufferCapacity = bufferCapacity,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private var pollingJob: Job? = null

    actual fun setListener(listener: suspend (DeviceDiscoveryEvent) -> Unit) {
        pollingJob?.cancel()
        pollingJob = scope.launch {
            // Start collecting before the polling loop — no lateinit race possible
            launch { output.distinctUntilChanged().collect { listener(it) } }
            withContext(Dispatchers.IO) {
                while (isActive) {
                    syncInterfaces()
                    delay(10.seconds)
                }
            }
        }
    }

    actual suspend fun stop() {
        pollingJob?.cancel()
        pollingJob = null
        val closingJobs = jmDnsInstances.values.map {
            scope.async {
                runCatching { it.close() }
                yield()
            }
        }
        jmDnsInstances.clear()
        closingJobs.awaitAll()
    }

    private fun syncInterfaces() {
        val liveAddresses = try {
            NetworkInterface.getNetworkInterfaces().findMdnsAddresses().toSet()
        } catch (e: IOException) {
            Logger.e(tag, e) { "Failed to enumerate network interfaces" }
            return
        }

        // Remove stale instances and close them to release sockets/threads
        jmDnsInstances.keys.filter { it !in liveAddresses }.forEach { addr ->
            jmDnsInstances.remove(addr)?.let { jmDns ->
                runCatching {
                    jmDns.removeServiceListener(serviceType, this)
                    jmDns.close()
                }
                Logger.d(tag) { "Removed stale listener for $addr" }
            }
        }

        // Add listeners for newly discovered interfaces
        liveAddresses.filter { it !in jmDnsInstances }.forEach { addr ->
            try {
                val jmDns = JmDNS.create(addr, addr.hostAddress)
                jmDns.addServiceListener(serviceType, this)
                jmDnsInstances[addr] = jmDns
                Logger.i(tag) { "Listening on ${addr.hostAddress}" }
            } catch (e: IOException) {
                Logger.e(tag, e) { "Failed to create JmDNS for $addr" }
            }
        }
    }

    override fun serviceAdded(event: ServiceEvent?) = serviceChanged(event, DeviceDiscoveryEvent.State.Found)

    override fun serviceRemoved(event: ServiceEvent?) = serviceChanged(event, DeviceDiscoveryEvent.State.Removed)

    override fun serviceResolved(event: ServiceEvent?) = serviceChanged(event, DeviceDiscoveryEvent.State.Resolved)

    private fun serviceChanged(event: ServiceEvent?, state: DeviceDiscoveryEvent.State) {
        Logger.d { "Service changed: ${event?.name} ${state.name}" }
        event ?: return

        scope.launch {
            val shouldCallListener = state == DeviceDiscoveryEvent.State.Found ||
                    state == DeviceDiscoveryEvent.State.Removed ||
                    state == DeviceDiscoveryEvent.State.Resolved && event.info.inet4Addresses.isNotEmpty()

            if (shouldCallListener) {
                output.emit(event.info.parse(state))
            }
        }
    }

    private fun ServiceInfo.parse(state: DeviceDiscoveryEvent.State): DeviceDiscoveryEvent {
        val hostAddresses = (inet6Addresses.toList() + inet4Addresses + inetAddresses).mapNotNull {
            it.hostAddress
        } + hostAddresses

        return DeviceDiscoveryEvent.create(
            this,
            hostAddresses.map { it.removePrefix("[").removeSuffix("]") }.distinct(),
            state
        )
    }
}
