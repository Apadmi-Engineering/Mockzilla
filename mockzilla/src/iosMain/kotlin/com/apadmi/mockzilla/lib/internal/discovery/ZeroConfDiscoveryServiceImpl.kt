@file:OptIn(ExperimentalForeignApi::class)

package com.apadmi.mockzilla.lib.internal.discovery

import com.apadmi.mockzilla.lib.config.ZeroConfConfig
import com.apadmi.mockzilla.lib.internal.persistance.KeychainSettings
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.nativedarwin.localdiscovery.BonjourService

import platform.Foundation.NSUUID

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.convert

class ZeroConfDiscoveryServiceImpl(
    private val keychainSettings: KeychainSettings
) : ZeroConfDiscoveryService {
    private val bonjour = BonjourService()

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun makeDiscoverable(metaData: MetaData, port: Int) = bonjour.startWithType(
        ZeroConfConfig.serviceType,
        metaData.toMap().map { it.key to it.value }.toMap(),
        port.convert(),
        keychainSettings.getDeviceIdentifier()
    )

    private fun KeychainSettings.getDeviceIdentifier() = getStringOrNull(
        deviceIdentifierKey
    )?.takeUnless { it.isBlank() } ?: run {
        val newId = NSUUID().UUIDString
        putString(deviceIdentifierKey, newId)
        newId
    }

    companion object {
        private const val deviceIdentifierKey = "device_identifier"
    }
}
