package com.apadmi.mockzilla.desktop.engine.connection

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

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

/**
 * @property deviceSerial
 * @property isActive
 * @property ipAddresses
 */
data class AdbConnection(
    val deviceSerial: String,
    val isActive: Boolean,
    val ipAddresses: List<IpAddress>
) {
    companion object
}

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
