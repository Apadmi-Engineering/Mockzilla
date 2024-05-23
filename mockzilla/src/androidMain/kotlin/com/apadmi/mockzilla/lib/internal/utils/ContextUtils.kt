package com.apadmi.mockzilla.lib.internal.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Build.VERSION

import com.apadmi.mockzilla.BuildKonfig
import com.apadmi.mockzilla.lib.models.MetaData

internal val Context.applicationName: String? get() {
    val applicationInfo: ApplicationInfo = applicationInfo
    val stringId = applicationInfo.labelRes
    return if (stringId == 0) {
        applicationInfo.nonLocalizedLabel?.toString() ?: packageName
    } else {
        getString(
            stringId
        )
    }
}

internal fun Context.extractMetaData(): MetaData {
    val packageInfo = runCatching { packageManager.getPackageInfo(packageName, 0) }.getOrNull()
    return MetaData(
        appName = applicationName ?: "-",
        appPackage = packageName,
        operatingSystem = Platform.platformName(),
        operatingSystemVersion = VERSION.SDK_INT.toString(),
        deviceModel = Build.MODEL,
        appVersion = packageInfo?.let { "${it.versionName}-${it.versionCode}" } ?: "-",
        mockzillaVersion = BuildKonfig.VERSION_NAME
    )
}
