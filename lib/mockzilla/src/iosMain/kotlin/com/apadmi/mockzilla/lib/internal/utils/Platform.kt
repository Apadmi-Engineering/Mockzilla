package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.BuildKonfig
import com.apadmi.mockzilla.lib.models.MetaData

import platform.Foundation.NSBundle
import platform.Foundation.NSProcessInfo
import platform.UIKit.UIDevice

internal actual object Platform {
    actual fun platformName() = "iOS"
}

internal fun extractMetaData() = MetaData(
    appName = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleExecutable").toString(),
    appPackage = NSBundle.mainBundle.bundleIdentifier ?: "-",
    operatingSystem = Platform.platformName(),
    operatingSystemVersion = NSProcessInfo.processInfo.operatingSystemVersionString,
    deviceModel = UIDevice.currentDevice.name,
    appVersion = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString").toString(),
    mockzillaVersion = BuildKonfig.VERSION_NAME
)
