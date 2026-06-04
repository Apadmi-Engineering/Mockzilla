package com.apadmi.mockzilla.desktop.engine.connection

import com.apadmi.mockzilla.ui.engine.connection.AdbConnection
import com.apadmi.mockzilla.ui.engine.connection.IpAddress

import com.malinskiy.adam.AndroidDebugBridgeClient
import com.malinskiy.adam.AndroidDebugBridgeClientFactory
import com.malinskiy.adam.interactor.StartAdbInteractor
import com.malinskiy.adam.request.device.Device
import com.malinskiy.adam.request.device.DeviceState
import com.malinskiy.adam.request.device.ListDevicesRequest
import com.malinskiy.adam.request.forwarding.ListPortForwardsRequest
import com.malinskiy.adam.request.forwarding.LocalTcpPortSpec
import com.malinskiy.adam.request.forwarding.PortForwardRequest
import com.malinskiy.adam.request.forwarding.RemoteTcpPortSpec
import com.malinskiy.adam.request.shell.v2.ShellCommandRequest

import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

/**
 * @property connection
 * @property localPort
 */
data class AdbPortForwardingResult(val connection: AdbConnection, val localPort: Int)
interface AdbConnectorService {
    suspend fun listConnectedDevices(): Result<List<AdbConnection>>
    suspend fun setupPortForwardingIfNeeded(
        emulator: AdbConnection,
        localPort: Int,
        emulatorPort: Int
    ): Result<AdbPortForwardingResult>
    suspend fun getListeningTcpPorts(serial: String): Result<List<Int>>
}

object AdbConnectorServiceImpl : AdbConnectorService {
    private val ipParsingRegex = "addr:\\s*([^\\/\\s]*)".toRegex()
    private suspend fun prepareAdb(): AndroidDebugBridgeClient {
        StartAdbInteractor().execute()
        return AndroidDebugBridgeClientFactory().build()
    }

    private suspend fun <T> runAdbCommandsSafely(
        timeout: Duration = 1.seconds,
        block: suspend (adb: AndroidDebugBridgeClient) -> T
    ) = withContext(Dispatchers.IO) {
        try {
            withTimeout(timeout) {
                runCatching {
                    block(prepareAdb())
                }
            }
        } catch (e: CancellationException) {
            // Let coroutine cancellation propagate unchanged.
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun listConnectedDevices() = runAdbCommandsSafely { adb ->
        adb.execute(request = ListDevicesRequest()).map { it.toAdbConnection(adb) }
    }

    private suspend fun Device.toAdbConnection(
        adb: AndroidDebugBridgeClient
    ): AdbConnection {
        val isActive = state == DeviceState.DEVICE

        val ipAddresses = if (isActive) {
            adb.getIpAddresses(serial).map { IpAddress(it) }
        } else {
            emptyList()
        }
        return AdbConnection(deviceSerial = serial, isActive = isActive, ipAddresses = ipAddresses)
    }

    private suspend fun AndroidDebugBridgeClient.getIpAddresses(serial: String): List<String> {
        val output = execute(
            request = ShellCommandRequest("ifconfig wlan0"),
            serial = serial
        ).output

        return ipParsingRegex.findAll(output)
            .map { it.groupValues.drop(1) }
            .flatten()
            .toList()
    }

    override suspend fun getListeningTcpPorts(serial: String) = runAdbCommandsSafely { adb ->
        val tcp4 = adb.execute(ShellCommandRequest("cat /proc/net/tcp"), serial).output
        val tcp6 = adb.execute(ShellCommandRequest("cat /proc/net/tcp6"), serial).output
        (parseTcpListeningPorts(tcp4) + parseTcpListeningPorts(tcp6))
    }

    // /proc/net/tcp rows look like:
    // sl  local_address rem_address st tx_queue:rx_queue tr:tm->when retrnsmt uid ...
    // 0: 00000000:1F90 00000000:0000 0A 00000000:00000000 ...
    // State 0A = TCP_LISTEN.
    private fun parseTcpListeningPorts(procNetTcp: String): List<Int> {
        return procNetTcp.lines()
            .drop(1)
            .mapNotNull { line ->
                val cols = line.trim().split(Regex("\\s+"))
                if (cols.size < 4) {
                    return@mapNotNull null
                }
                if (cols[3] != "0A") {
                    return@mapNotNull null
                }
                val portHex = cols[1].substringAfter(":")
                portHex.toLongOrNull(16)?.toInt()
            }
    }

    override suspend fun setupPortForwardingIfNeeded(
        emulator: AdbConnection,
        localPort: Int,
        emulatorPort: Int
    ) = runAdbCommandsSafely { adb ->
        val rules = adb.execute(ListPortForwardsRequest(emulator.deviceSerial))
        val existingRule = rules.filter {
            // The `ListPortForwardsRequest` doesn't seem to actually filter by device
            // serial number so doing that explicitly here
            it.serial == emulator.deviceSerial
        }.firstOrNull {
            (it.remoteSpec as? RemoteTcpPortSpec)?.port == emulatorPort
        }?.localSpec as? LocalTcpPortSpec

        existingRule?.let {
            AdbPortForwardingResult(emulator, existingRule.port)
        } ?: adb.addPortForwardingRule(localPort, emulatorPort, emulator)
    }

    private suspend fun AndroidDebugBridgeClient.addPortForwardingRule(
        localPort: Int,
        emulatorPort: Int,
        emulator: AdbConnection
    ) = AdbPortForwardingResult(
        emulator, execute(
            request = PortForwardRequest(
                local = LocalTcpPortSpec(localPort),
                remote = RemoteTcpPortSpec(emulatorPort),
                serial = emulator.deviceSerial,
            ),
        ) ?: throw Exception("Port forwarding failed")
    )
}
