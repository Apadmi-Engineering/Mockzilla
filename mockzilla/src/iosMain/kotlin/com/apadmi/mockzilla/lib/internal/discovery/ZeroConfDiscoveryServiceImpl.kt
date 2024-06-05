@file:OptIn(ExperimentalForeignApi::class)

package com.apadmi.mockzilla.lib.internal.discovery

import com.apadmi.mockzilla.lib.config.ZeroConfConfig
import com.apadmi.mockzilla.lib.internal.persistance.KeychainSettings
import com.apadmi.mockzilla.lib.models.MetaData

import co.touchlab.kermit.Logger
import platform.CoreFoundation.CFSwapInt16HostToBig
import platform.Foundation.NSUUID
import platform.darwin.DNSServiceRefDeallocate
import platform.darwin.DNSServiceRefVar
import platform.darwin.DNSServiceRegister
import platform.darwin.TXTRecordCreate
import platform.darwin.TXTRecordGetBytesPtr
import platform.darwin.TXTRecordGetLength
import platform.darwin.TXTRecordRef
import platform.darwin.TXTRecordSetValue
import platform.darwin.kDNSServiceErr_NoError
import platform.posix.uint16_t

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.refTo
import kotlinx.cinterop.value

class ZeroConfDiscoveryServiceImpl(
    private val logger: Logger,
    private val keychainSettings: KeychainSettings
) : ZeroConfDiscoveryService {
    private var serviceRef: CPointer<DNSServiceRefVar>? = null

    override suspend fun makeDiscoverable(metaData: MetaData, port: Int) {
        startBonjourService(
            serviceType = ZeroConfConfig.serviceType,
            txtRecords = metaData.toMap().map { it.key to it.value }.toMap(),
            port = port,
            serviceName = metaData.bonjourServiceName(keychainSettings.getDeviceIdentifier())
        )
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
        val serviceRefPtr = alloc<DNSServiceRefVar>()
        serviceRef = serviceRefPtr.ptr

        val (txtRecordLength, txtRecordBytes) = createTxtRecords(txtRecords)

        val errorCode = DNSServiceRegister(
            serviceRefPtr.ptr,
            0u,
            0u,
            serviceName,
            serviceType,
            "local.",
            null,
            CFSwapInt16HostToBig(port.toUShort()),
            txtRecordLength,
            txtRecordBytes,
            null,
            null
        )

        if (errorCode != kDNSServiceErr_NoError) {
            logger.e("Failed to register service: $errorCode")
        } else {
            logger.i("Service successfully registered.")
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun createTxtRecords(txtRecords: Map<String, String>): Pair<uint16_t, COpaquePointer?> {
        val txtRecord = nativeHeap.alloc<TXTRecordRef>()
        TXTRecordCreate(txtRecord.ptr, 0u, null)
        for ((key, value) in txtRecords) {
            TXTRecordSetValue(
                txtRecord.ptr,
                key,
                value.encodeToByteArray().size.convert(),
                value.encodeToByteArray().refTo(0)
            )
        }
        val txtRecordLength = TXTRecordGetLength(txtRecord.ptr)
        val txtRecordBytes = TXTRecordGetBytesPtr(txtRecord.ptr)
        return Pair(txtRecordLength, txtRecordBytes)
    }

    fun stopBonjourService() = serviceRef?.let {
        DNSServiceRefDeallocate(it.pointed.value)
        logger.i { "Service stopped." }
    }

    companion object {
        private const val deviceIdentifierKey = "device_identifier"
    }
}
