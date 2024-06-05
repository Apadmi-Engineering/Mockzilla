package com.apadmi.mockzilla.lib.internal.discovery

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.RunTarget

interface ZeroConfDiscoveryService {
    suspend fun makeDiscoverable(metaData: MetaData, port: Int)
}

internal fun MetaData.bonjourServiceName(deviceIdentifier: String) = """${
    when (runTarget) {
        RunTarget.AndroidDevice -> "AD"
        RunTarget.AndroidEmulator -> "AE"
        RunTarget.IosDevice -> "ID"
        RunTarget.IosSimulator -> "IS"
        RunTarget.Jvm -> "JVM"
        null -> "[]"
    }
}-$appPackage-$deviceIdentifier""".take(62)