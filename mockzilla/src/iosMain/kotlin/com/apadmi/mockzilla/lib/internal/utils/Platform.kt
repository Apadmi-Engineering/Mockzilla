package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.BuildKonfig
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.RunTarget

import platform.Foundation.NSBundle
import platform.Foundation.NSProcessInfo
import platform.UIKit.UIDevice

private fun NSBundle.appName() = objectForInfoDictionaryKey(
    "CFBundleExecutable"
).toString().take(MetaData.maxFieldLength)

private fun NSBundle.appVersion() = objectForInfoDictionaryKey(
    "CFBundleShortVersionString"
).toString().take(MetaData.maxFieldLength)

private fun NSBundle.appPackage() = bundleIdentifier?.take(MetaData.maxFieldLength) ?: "-"

internal fun extractMetaData() = MetaData(
    appName = NSBundle.mainBundle.appName(),
    appPackage = NSBundle.mainBundle.appPackage(),
    runTarget = if (isRunningInSimulator()) RunTarget.Iossimulator else RunTarget.Iosdevice,
    operatingSystemVersion = NSProcessInfo.processInfo.operatingSystemVersionString,
    deviceModel = UIDevice.currentDevice.name,
    appVersion = NSBundle.mainBundle.appVersion(),
    mockzillaVersion = BuildKonfig.VERSION_NAME
)

private fun isRunningInSimulator(): Boolean {
    val deviceName = NSProcessInfo.processInfo.environment["SIMULATOR_DEVICE_NAME"]
    return deviceName != null && deviceName.toString().isNotBlank()
}
