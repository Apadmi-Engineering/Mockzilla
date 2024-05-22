package com.apadmi.mockzilla.desktop.engine.adb

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
import com.malinskiy.adam.request.prop.GetSinglePropRequest

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

/**
 * @property name
 * @property deviceSerial
 * @property isActive
 */
data class AdbConnection(
    val name: String,
    val deviceSerial: String,
    val isActive: Boolean
)

/**
 * @property connection
 * @property localPort
 */
data class AdbPortForwardingResult(val connection: AdbConnection, val localPort: Int)
interface AdbConnectorUseCase {
    suspend fun listConnectedDevices(): Result<List<AdbConnection>>
    suspend fun setupPortForwardingIfNeeded(
        emulator: AdbConnection,
        localPort: Int,
        emulatorPort: Int
    ): Result<AdbPortForwardingResult>
}

object AdbConnectorUseCaseImpl : AdbConnectorUseCase {
    private suspend fun prepareAdb(): AndroidDebugBridgeClient {
        StartAdbInteractor().execute()
        return AndroidDebugBridgeClientFactory().build()
    }

    private suspend fun <T> runAdbCommandsSafely(
        timeout: Duration = 1.seconds,
        block: suspend (adb: AndroidDebugBridgeClient) -> T
    ) = withContext(Dispatchers.IO) {
        withTimeout(timeout) {
            runCatching {
                block(prepareAdb())
            }
        }
    }

    override suspend fun listConnectedDevices() = runAdbCommandsSafely { adb ->
        adb.execute(request = ListDevicesRequest()).map { it.toAdbConnection(adb) }
    }

    private suspend fun Device.toAdbConnection(
        adb: AndroidDebugBridgeClient
    ): AdbConnection {
        val isActive = state == DeviceState.DEVICE
        val name = if (isActive) {
            runCatching { adb.getHumanReadableName(serial) }.getOrNull() ?: serial
        } else {
            serial
        }

        return AdbConnection(name, serial, isActive)
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

        if (existingRule == null) {
            adb.addPortForwardingRule(localPort, emulatorPort, emulator)
        } else {
            AdbPortForwardingResult(emulator, existingRule.port)
        }
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


    private suspend fun AndroidDebugBridgeClient.getHumanReadableName(
        deviceSerial: String
    ): String = getPropUnlessBlank(
        deviceSerial = deviceSerial,
        "ro.boot.qemu.avd_name"
    ) ?: buildString {
        getPropUnlessBlank(deviceSerial, "ro.product.name").also {
            append(it)
            append(" ")
        }
        getPropUnlessBlank(deviceSerial, "ro.product.model").also {
            append(it)
            append(" ")
        }
        getPropUnlessBlank(deviceSerial, "ro.product.device").also { append(it) }
    }

    private suspend fun AndroidDebugBridgeClient.getPropUnlessBlank(
        deviceSerial: String,
        prop: String
    ) = execute(
        request = GetSinglePropRequest(name = prop),
        serial = deviceSerial
    ).takeUnless { it.isBlank() }
}
