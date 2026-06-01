package com.apadmi.mockzilla.desktop.engine.connection

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.RunTarget
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.ui.engine.connection.AdbConnection
import com.apadmi.mockzilla.ui.engine.device.Device

import co.touchlab.kermit.Logger

import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val tag = "AdbEmulatorDiscovery"

/**
 * Discovers Android emulators running Mockzilla via ADB rather than mDNS.
 *
 * mDNS (JmDNS) is unreliable for emulator discovery especially when connected to a VPN
 * We detect the mockzilla port directly from ADB and derive the connection from that.
 */
interface AdbEmulatorDiscoveryService {
    fun start(
        scope: CoroutineScope,
        onEvent: suspend (DeviceDiscoveryEvent) -> Unit
    )
    fun stop()
}

class AdbEmulatorDiscoveryServiceImpl(
    private val adbConnectorService: AdbConnectorService,
    private val metaDataService: MockzillaManagement.MetaDataService
) : AdbEmulatorDiscoveryService {
    private var pollingJob: Job? = null

    // Maps device serial → set of known Mockzilla emulator ports.
    // Once a port is confirmed via /api/meta we only need to verify it stays in the
    // /proc/net/tcp LISTEN set — no need to re-hit the HTTP endpoint each cycle.
    // A set is used because the same emulator may run multiple Mockzilla apps on different ports.
    private val knownEmulators = mutableMapOf<String, MutableSet<Int>>()

    override fun start(
        scope: CoroutineScope,
        onEvent: suspend (DeviceDiscoveryEvent) -> Unit
    ) {
        pollingJob = scope.launch {
            while (isActive) {
                pollEmulators(onEvent)
                delay(2.seconds)
            }
        }
    }

    override fun stop() {
        pollingJob?.cancel()
        pollingJob = null
    }

    private suspend fun pollEmulators(onEvent: suspend (DeviceDiscoveryEvent) -> Unit) {
        // ADB is used here instead of mDNS because mDNS multicast is unreliable on emulator
        // virtual NICs and is often blocked entirely by VPN software. ADB port forwarding
        // tunnels over the USB/ADB connection and is unaffected by either issue.
        val devices = adbConnectorService.listConnectedDevices().getOrNull() ?: return
        val activeEmulators = devices.filter {
            it.deviceSerial.startsWith("emulator-") && it.isActive
        }
        val activeSerials = activeEmulators.map { it.deviceSerial }.toSet()

        // Emit a lost event for each known port on emulators that have disconnected from ADB
        (knownEmulators.keys.toSet() - activeSerials).forEach { serial ->
            knownEmulators[serial]?.forEach { port ->
                onEvent(DeviceDiscoveryEvent(
                    connectionName = "adb:$serial:$port",
                    hostAddress = "127.0.0.1",
                    hostAddresses = emptyList(),
                    attributes = emptyMap(),
                    port = port,
                    state = DeviceDiscoveryEvent.State.Removed,
                    adbConnection = AdbConnection(serial, false, emptyList())
                ))
            }
        }
        knownEmulators.keys.retainAll(activeSerials)

        activeEmulators.forEach { connection ->
            probeEmulator(connection, onEvent)
        }
    }

    private suspend fun probeEmulator(
        connection: AdbConnection,
        onEvent: suspend (DeviceDiscoveryEvent) -> Unit
    ) {
        val candidatePorts = getCandidatePorts(connection)
        val knownPorts = knownEmulators[connection.deviceSerial].orEmpty().toSet()

        // For ports we already know about, just verify they are still LISTEN — no HTTP probe needed
        (knownPorts - candidatePorts).forEach { lostPort ->
            knownEmulators[connection.deviceSerial]?.remove(lostPort)
            Logger.i(tag = tag) { "Mockzilla port $lostPort gone from ${connection.deviceSerial}" }
            onEvent(DeviceDiscoveryEvent(
                connectionName = "adb:${connection.deviceSerial}:$lostPort",
                hostAddress = "127.0.0.1",
                hostAddresses = emptyList(),
                attributes = emptyMap(),
                port = lostPort,
                state = DeviceDiscoveryEvent.State.Removed,
                adbConnection = connection
            ))
        }

        // Only probe ports we haven't confirmed yet — avoids hammering /api/meta each cycle
        val newCandidates = candidatePorts - knownPorts
        for (port in newCandidates) {
            probeCandidate(port, connection)?.also { onEvent(it) }
        }
    }

    private suspend fun probeCandidate(
        port: Int, connection: AdbConnection
    ): DeviceDiscoveryEvent? {
        val forward = adbConnectorService.setupPortForwardingIfNeeded(
            emulator = connection,
            localPort = 0,
            emulatorPort = port
        ).getOrNull() ?: return null
        val metaData = withContext(Dispatchers.IO) {
            fetchMeta(forward.localPort)
        }?.takeIf { metaData ->
            metaData.runTarget == RunTarget.AndroidEmulator
        } ?: return null

        Logger.i(tag = tag) { "Found Mockzilla on ${connection.deviceSerial} at emulator port $port" }
        knownEmulators.getOrPut(connection.deviceSerial) { mutableSetOf() } += port
        return DeviceDiscoveryEvent(
            connectionName = "adb:${connection.deviceSerial}:$port",
            hostAddress = "127.0.0.1",
            hostAddresses = listOf("127.0.0.1"),
            attributes = emptyMap(),
            port = port,
            state = DeviceDiscoveryEvent.State.Resolved,
            adbConnection = connection,
            metaData = metaData
        )
    }

    // Gets ports that *might* be Mockzilla
    private suspend fun getCandidatePorts(connection: AdbConnection): Set<Int> = withContext(Dispatchers.IO) {
        val tcp4 = adbConnectorService
            .executeShellCommand(connection.deviceSerial, "cat /proc/net/tcp")
            .getOrNull().orEmpty()
        val tcp6 = adbConnectorService
            .executeShellCommand(connection.deviceSerial, "cat /proc/net/tcp6")
            .getOrNull().orEmpty()
        // Merge IPv4 and IPv6 listener lists — emulators may bind on either family
        (parseListeningPorts(tcp4) + parseListeningPorts(tcp6)).distinct().toSet()
    }

    // /proc/net/tcp rows look like:
    // sl  local_address rem_address st tx_queue:rx_queue tr:tm->when retrnsmt uid ...
    // 0: 00000000:1F90 00000000:0000 0A 00000000:00000000 ...
    // Fields are space-separated; local_address is host:port in hex (big-endian).
    // State 0A = TCP_LISTEN. We read this via `adb shell` — no port forwarding needed.
    private fun parseListeningPorts(procNetTcp: String): List<Int> {
        return procNetTcp.lines()
            .drop(1)  // skip header row
            .mapNotNull { line ->
                val cols = line.trim().split(Regex("\\s+"))
                if (cols.size < 4) {
                    return@mapNotNull null
                }
                if (cols[3] != "0A") {
                    return@mapNotNull null
                }  // 0A = LISTEN
                val portHex = cols[1].substringAfter(":")  // "00000000:1F90" → "1F90"
                portHex.toLongOrNull(16)?.toInt()
            }
            .filter { it > 1024 }  // exclude well-known system ports
    }

    private suspend fun fetchMeta(localPort: Int): MetaData? = runCatching {
        metaDataService.fetchMetaData(Device("127.0.0.1", localPort.toString()), true).getOrNull()
    }.getOrNull()
}
