package com.apadmi.mockzilla.ui.engine.connection

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.lib.models.MetaData
import kotlin.jvm.JvmInline

@InternalMockzillaApi
public typealias AdbConnectionDeviceSerial = String

/**
 * @property connectionId
 * @property prettyName
 * @property metaData
 * @property hostAddress
 * @property hostAddresses
 * @property port
 * @property adbConnection
 * @property state
 */
@InternalMockzillaApi
public data class DetectedDevice(
    val connectionId: String,
    val prettyName: String,
    val metaData: MetaData?,
    val hostAddress: String,
    val hostAddresses: List<IpAddress>,
    val port: Int,
    val adbConnection: AdbConnection?,
    val state: State
) {
    @InternalMockzillaApi
    public enum class State {
        NotYourSimulator,
        ReadyToConnect,
        Removed,
        Resolving,
        ;
    }
}

/**
 * @property deviceSerial
 * @property isActive
 * @property ipAddresses
 */
@InternalMockzillaApi
public data class AdbConnection(
    val deviceSerial: AdbConnectionDeviceSerial,
    val isActive: Boolean,
    val ipAddresses: List<IpAddress>
) {
    public companion object
}

/**
 * @property raw
 */
@InternalMockzillaApi
@JvmInline
public value class IpAddress(public val raw: String)
