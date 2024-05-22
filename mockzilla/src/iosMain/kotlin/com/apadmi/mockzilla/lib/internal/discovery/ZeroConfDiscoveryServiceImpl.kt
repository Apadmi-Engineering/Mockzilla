@file:OptIn(ExperimentalForeignApi::class)

package com.apadmi.mockzilla.lib.internal.discovery

import com.apadmi.mockzilla.lib.nativedarwin.localdiscovery.BonjourService
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIDevice

class ZeroConfDiscoveryServiceImpl(): ZeroConfDiscoveryService {
    private val bonjour = BonjourService()

    @OptIn(ExperimentalForeignApi::class)
    override fun makeDiscoverable() {
        bonjour.startWithName(UIDevice.currentDevice.name)
    }
}
