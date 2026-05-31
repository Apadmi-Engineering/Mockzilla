package com.apadmi.mockzilla.desktop.engine.connection

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.RunTarget
import com.apadmi.mockzilla.ui.engine.connection.AdbConnection

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import kotlin.time.Duration.Companion.seconds

/**
 * Discovers Android emulators running Mockzilla via ADB rather than mDNS.
 *
 * mDNS (JmDNS) is unreliable for emulator discovery for two reasons:
 *  1. Android emulators run on virtual NICs (eth0/ethX) that don't participate in
 *     multicast the same way as a real Wi-Fi interface, so JmDNS often never sees
 *     the emulator's NSD broadcast.
 *  2. VPN software intercepts and re-routes multicast traffic, meaning developers
 *     on a corporate or personal VPN will almost never receive the mDNS packets
 *     regardless of interface configuration.
 *
 * ADB port forwarding is unaffected by both issues — it tunnels directly over the
 * USB/ADB connection and bypasses the host's IP stack entirely.
 */
interface AdbEmulatorDiscoveryService {
    fun start(
        scope: CoroutineScope,
        onDiscovered: suspend (connection: AdbConnection, metaData: MetaData, emulatorPort: Int) -> Unit,
        onLost: suspend (deviceSerial: String) -> Unit
    )
    fun stop()
}

class AdbEmulatorDiscoveryServiceImpl(
    private val adbConnectorService: AdbConnectorService
) : AdbEmulatorDiscoveryService {
    private var pollingJob: Job? = null
    private val knownSerials = mutableSetOf<String>()
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    override fun start(
        scope: CoroutineScope,
        onDiscovered: suspend (connection: AdbConnection, metaData: MetaData, emulatorPort: Int) -> Unit,
        onLost: suspend (deviceSerial: String) -> Unit
    ) {
        pollingJob = scope.launch {
            while (isActive) {
                pollEmulators(onDiscovered, onLost)
                delay(10.seconds)
            }
        }
    }

    override fun stop() {
        pollingJob?.cancel()
        pollingJob = null
    }

    private suspend fun pollEmulators(
        onDiscovered: suspend (AdbConnection, MetaData, Int) -> Unit,
        onLost: suspend (String) -> Unit
    ) {
        // ADB is used here instead of mDNS because mDNS multicast is unreliable on emulator
        // virtual NICs and is often blocked entirely by VPN software. ADB port forwarding
        // tunnels over the USB/ADB connection and is unaffected by either issue.
        val devices = adbConnectorService.listConnectedDevices().getOrNull() ?: return
        val activeEmulators = devices.filter {
            it.deviceSerial.startsWith("emulator-") && it.isActive
        }
        val activeSerials = activeEmulators.map { it.deviceSerial }.toSet()

        // Emit lost events for emulators that have disconnected since the last poll
        (knownSerials - activeSerials).forEach { serial -> onLost(serial) }
        knownSerials.retainAll(activeSerials)

        activeEmulators.forEach { connection ->
            probeEmulator(connection, onDiscovered)
        }
    }

    private suspend fun probeEmulator(
        connection: AdbConnection,
        onDiscovered: suspend (AdbConnection, MetaData, Int) -> Unit
    ) {
        val candidatePorts = withContext(Dispatchers.IO) {
            val tcp4 = adbConnectorService
                .executeShellCommand(connection.deviceSerial, "cat /proc/net/tcp")
                .getOrNull().orEmpty()
            val tcp6 = adbConnectorService
                .executeShellCommand(connection.deviceSerial, "cat /proc/net/tcp6")
                .getOrNull().orEmpty()
            // Merge IPv4 and IPv6 listener lists — emulators may bind on either family
            (parseListeningPorts(tcp4) + parseListeningPorts(tcp6)).distinct()
        }
        if (candidatePorts.isEmpty()) {
            Logger.d("AdbEmulatorDiscovery") { "No candidate ports for ${connection.deviceSerial}" }
            return
        }

        for (port in candidatePorts) {
            val forward = adbConnectorService
                .setupPortForwardingIfNeeded(connection, 0, port)
                .getOrNull() ?: continue
            val metaData = withContext(Dispatchers.IO) { fetchMeta(forward.localPort) } ?: continue
            if (metaData.runTarget == RunTarget.AndroidEmulator) {
                Logger.i("AdbEmulatorDiscovery") {
                    "Found Mockzilla on ${connection.deviceSerial} at emulator port $port"
                }
                knownSerials += connection.deviceSerial
                onDiscovered(connection, metaData, port)
                return
            }
        }
    }

    // /proc/net/tcp rows look like:
    //   sl  local_address rem_address st tx_queue:rx_queue tr:tm->when retrnsmt uid ...
    //   0: 00000000:1F90 00000000:0000 0A 00000000:00000000 ...
    // Fields are space-separated; local_address is host:port in hex (big-endian).
    // State 0A = TCP_LISTEN. We read this via `adb shell` — no port forwarding needed.
    private fun parseListeningPorts(procNetTcp: String): List<Int> {
        return procNetTcp.lines()
            .drop(1)                         // skip header row
            .mapNotNull { line ->
                val cols = line.trim().split(Regex("\\s+"))
                if (cols.size < 4) return@mapNotNull null
                if (cols[3] != "0A") return@mapNotNull null  // 0A = LISTEN
                val portHex = cols[1].substringAfter(":")    // "00000000:1F90" → "1F90"
                portHex.toLongOrNull(16)?.toInt()
            }
            .filter { it > 1024 }            // exclude well-known system ports
    }

    private fun fetchMeta(localPort: Int): MetaData? = runCatching {
        val conn = URL("http://127.0.0.1:$localPort/api/meta")
            .openConnection() as HttpURLConnection
        conn.connectTimeout = 1000
        conn.readTimeout = 1000
        val body = conn.inputStream.bufferedReader().readText()
        conn.disconnect()
        json.decodeFromString<MetaData>(body)
    }.getOrNull()
}
