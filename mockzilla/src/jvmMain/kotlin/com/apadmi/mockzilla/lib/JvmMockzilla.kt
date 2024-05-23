package com.apadmi.mockzilla.lib

import co.touchlab.kermit.Logger
import com.apadmi.mockzilla.BuildKonfig
import com.apadmi.mockzilla.lib.internal.discovery.ZeroConfDiscoveryService
import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams
import com.apadmi.mockzilla.lib.models.RunTarget
import java.nio.file.Files

/**
 * Starts the Mockzilla server,
 *
 * @param appName The name of the client app
 * @param appVersion The version of the client app
 * @param config The config with which to initialise mockzilla.
 * @return runtimeParams Configuration of the mockzilla runtime environment
 */
fun startMockzilla(
    appName: String,
    appVersion: String,
    config: MockzillaConfig,
): MockzillaRuntimeParams = startMockzilla(
    config,
    MetaData(
        appName = appName,
        appPackage = "-",  // Not really a thing on non-mobile platforms
        operatingSystemVersion = System.getProperty("os.version"),
        deviceModel = "-",  // Covered by `operatingSystem`
        appVersion = appVersion,
        runTarget = RunTarget.Jvm,
        mockzillaVersion = BuildKonfig.VERSION_NAME
    ),
    FileIo(Files.createTempDirectory("").toFile()),
    object : ZeroConfDiscoveryService {
        override fun makeDiscoverable(metaData: MetaData, port: Int) {
            Logger.i(tag = "Mockzilla") { "ZeroConf not supported for JVM Mockzilla" }
        }
    }
)
