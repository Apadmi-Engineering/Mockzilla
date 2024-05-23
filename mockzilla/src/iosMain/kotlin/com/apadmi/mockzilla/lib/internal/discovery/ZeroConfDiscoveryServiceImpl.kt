@file:OptIn(ExperimentalForeignApi::class)

package com.apadmi.mockzilla.lib.internal.discovery

import com.apadmi.mockzilla.lib.config.ZeroConfConfig
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.nativedarwin.localdiscovery.BonjourService
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.convert
import platform.Foundation.NSUUID
import platform.UIKit.UIDevice

class ZeroConfDiscoveryServiceImpl : ZeroConfDiscoveryService {
    private val bonjour = BonjourService()

    @OptIn(ExperimentalForeignApi::class)
    override fun makeDiscoverable(metaData: MetaData, port: Int) = bonjour.startWithType(
        ZeroConfConfig.serviceType,
        metaData.toMap().map { it.key to it.value }.toMap(),
        port.convert(),
        NSUUID.UUID().UUIDString
    )
}