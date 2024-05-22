@file:OptIn(ExperimentalForeignApi::class)

package com.apadmi.mockzilla.lib.internal.discovery

import com.apadmi.mockzilla.lib.nativedarwin.localdiscovery.BonjourService
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIDevice

actual class DiscoveryService actual constructor(x: String) {
    private val bonjour = BonjourService()
    @Suppress("diktat")
    actual fun makeDiscoverable() {
        val deviceName = UIDevice.currentDevice.name
        bonjour.startWithName(deviceName);
    }
}
