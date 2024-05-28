@file:OptIn(ExperimentalForeignApi::class)

package com.apadmi.mockzilla.lib.internal.discovery

import com.apadmi.mockzilla.lib.config.ZeroConfConfig
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.nativedarwin.localdiscovery.BonjourService

import platform.Foundation.NSUUID

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.convert

class ZeroConfDiscoveryServiceImpl : ZeroConfDiscoveryService {
    private val bonjour = BonjourService()

    @OptIn(ExperimentalForeignApi::class)
    override fun makeDiscoverable(metaData: MetaData, port: Int) = bonjour.startWithType(
        ZeroConfConfig.serviceType,
        metaData.toMap().map { it.key to it.value }.toMap(),
        port.convert(),
        // TODO: Persist this to disk so that we're not generating IDs each time
        NSUUID.UUID().UUIDString
    )
}
