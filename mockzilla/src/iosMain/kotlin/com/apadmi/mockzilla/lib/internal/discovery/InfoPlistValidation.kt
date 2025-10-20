package com.apadmi.mockzilla.lib.internal.discovery

import com.apadmi.mockzilla.lib.config.ZeroConfConfig
import com.apadmi.mockzilla.lib.models.MockzillaConfig

import co.touchlab.kermit.Logger
import platform.Foundation.NSArray
import platform.Foundation.NSBundle
import platform.Foundation.NSException
import platform.Foundation.containsObject
import platform.Foundation.raise

internal fun MockzillaConfig.validateInfoPlist() {
    if (!isNetworkDiscoveryEnabled) {
        return
    }

    val infoDictionary = NSBundle.mainBundle.infoDictionary!!
    val bonjourEntry = infoDictionary["NSBonjourServices"] as? NSArray
    if (bonjourEntry == null || !bonjourEntry.containsObject(ZeroConfConfig.serviceType)) {
        Logger.w {
            """
                👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇
                --------------------------------------------------------------------------------
                In order for the Mockzilla desktop app to discover devices correctly you must add 
                the following entry to your Info.plist
                
                <key>NSBonjourServices</key>
                <array>
                  <string>${ZeroConfConfig.serviceType}</string>
                </array>
                
                Alternatively disable network discovery on your MockzillaConfig but that will hide
                your device from being automatically picked up by the management UI.
                ```
                config.setIsNetworkDiscoveryEnabled(false)
                ```
                --------------------------------------------------------------------------------
                ☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝️☝
            """.trimIndent()
        }
    }
}
