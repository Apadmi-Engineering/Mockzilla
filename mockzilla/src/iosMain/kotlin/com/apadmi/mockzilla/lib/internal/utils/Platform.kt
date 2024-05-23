package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.BuildKonfig
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.RunTarget

import platform.Foundation.NSBundle
import platform.Foundation.NSProcessInfo
import platform.UIKit.UIDevice

internal fun extractMetaData() = MetaData(
    appName = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleExecutable").toString(),
    appPackage = NSBundle.mainBundle.bundleIdentifier ?: "-",
    runTarget = if (isRunningInSimulator()) RunTarget.iOSSimulator else RunTarget.iOSDevice,
    operatingSystemVersion = NSProcessInfo.processInfo.operatingSystemVersionString,
    deviceModel = UIDevice.currentDevice.name,
    appVersion = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString")
        .toString(),
    mockzillaVersion = BuildKonfig.VERSION_NAME
)

private fun isRunningInSimulator(): Boolean {
    val deviceName = NSProcessInfo.processInfo.environment["SIMULATOR_DEVICE_NAME"]
    return deviceName != null && deviceName.toString().isNotBlank()
}

