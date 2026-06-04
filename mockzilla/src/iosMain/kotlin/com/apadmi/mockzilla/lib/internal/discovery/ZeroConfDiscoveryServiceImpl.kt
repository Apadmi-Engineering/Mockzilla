@file:OptIn(ExperimentalForeignApi::class)

package com.apadmi.mockzilla.lib.internal.discovery

import com.apadmi.mockzilla.lib.config.ZeroConfConfig
import com.apadmi.mockzilla.lib.internal.persistance.KeychainSettings
import com.apadmi.mockzilla.lib.models.MetaData

import co.touchlab.kermit.Logger
import platform.CoreFoundation.CFSwapInt16HostToBig
import platform.Foundation.NSUUID
import platform.darwin.DNSServiceRef
import platform.darwin.DNSServiceRefDeallocate
import platform.darwin.DNSServiceRefVar
import platform.darwin.DNSServiceRegister
import platform.darwin.TXTRecordCreate
import platform.darwin.TXTRecordDeallocate
import platform.darwin.TXTRecordGetBytesPtr
import platform.darwin.TXTRecordGetLength
import platform.darwin.TXTRecordRef
import platform.darwin.TXTRecordSetValue
import platform.darwin.kDNSServiceErr_NoError

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.refTo
import kotlinx.cinterop.value

class ZeroConfDiscoveryServiceImpl(
    private val logger: Logger,
    private val keychainSettings: KeychainSettings
) : ZeroConfDiscoveryService {
    // Store the DNSServiceRef value directly — not a pointer into a memScoped arena
    private var serviceRef: DNSServiceRef? = null

    override suspend fun makeDiscoverable(metaData: MetaData, port: Int) {
        startBonjourService(
            serviceType = ZeroConfConfig.serviceType,
            txtRecords = metaData.toMap(),
            port = port,
            serviceName = metaData.bonjourServiceName(keychainSettings.getDeviceIdentifier())
        )
    }

    override suspend fun stop() {
        serviceRef?.let {
            DNSServiceRefDeallocate(it)
            serviceRef = null
            logger.i { "Bonjour service stopped." }
        }
    }

    private fun KeychainSettings.getDeviceIdentifier() = getStringOrNull(
        deviceIdentifierKey
    )?.takeUnless { it.isBlank() } ?: run {
        val newId = NSUUID().UUIDString
        putString(deviceIdentifierKey, newId)
        newId
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun startBonjourService(
        serviceType: String,
        txtRecords: Map<String, String>,
        port: Int,
        serviceName: String
    ) = memScoped {
        // Build TXT records within the same memScoped block so both the TXTRecord struct
        // and the DNSServiceRefVar container are freed together after the register call.
        val txtRecord = alloc<TXTRecordRef>()
        TXTRecordCreate(txtRecord.ptr, 0u, null)
        for ((key, value) in txtRecords) {
            val bytes = value.encodeToByteArray()  // encode once per key
            TXTRecordSetValue(txtRecord.ptr, key, bytes.size.convert(), bytes.refTo(0))
        }

        val serviceRefPtr = alloc<DNSServiceRefVar>()
        val errorCode = DNSServiceRegister(
            serviceRefPtr.ptr,
            0u,
            0u,
            serviceName,
            serviceType,
            "local.",
            null,
            CFSwapInt16HostToBig(port.toUShort()),
            TXTRecordGetLength(txtRecord.ptr),
            TXTRecordGetBytesPtr(txtRecord.ptr),
            null,
            null
        )

        TXTRecordDeallocate(txtRecord.ptr)

        if (errorCode == kDNSServiceErr_NoError) {
            // Extract the value before memScoped frees the container
            serviceRef = serviceRefPtr.value
            logger.i { "Service successfully registered." }
        } else {
            logger.e("Failed to register service: $errorCode")
        }
    }

    companion object {
        private const val deviceIdentifierKey = "device_identifier"
    }
}
