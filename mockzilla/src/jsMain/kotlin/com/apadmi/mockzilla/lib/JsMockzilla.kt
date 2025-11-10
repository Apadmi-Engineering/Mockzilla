@file:OptIn(ExperimentalJsExport::class, DelicateCoroutinesApi::class)

package com.apadmi.mockzilla.lib

import com.apadmi.mockzilla.BuildKonfig
import com.apadmi.mockzilla.lib.internal.discovery.ZeroConfDiscoveryService
import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.lib.internal.utils.getBrowserInfo
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams
import com.apadmi.mockzilla.lib.models.RunTarget

import co.touchlab.kermit.Logger
import com.apadmi.mockzilla.lib.internal.jsinterface.JsMockzillaConfig
import com.apadmi.mockzilla.lib.internal.jsinterface.JsMockzillaRuntimeParams
import com.apadmi.mockzilla.lib.internal.jsinterface.toJs
import com.apadmi.mockzilla.lib.internal.stopServer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.js.Promise

/**
 * Starts the Mockzilla server,
 *
 * @param appName The name of the client app
 * @param appVersion The version of the client app
 * @param config The config with which to initialise mockzilla.
 * @return runtimeParams Configuration of the mockzilla runtime environment
 */
suspend fun startMockzilla(
    appName: String,
    appVersion: String,
    config: MockzillaConfig,
): MockzillaRuntimeParams {
    val browserInfo = getBrowserInfo()
    return startMockzilla(
        config,
        MetaData(
            appName = appName,
            appPackage = "-",  // Not really a thing on non-mobile platforms
            operatingSystemVersion = browserInfo.version,
            deviceModel = browserInfo.name,
            appVersion = appVersion,
            runTarget = RunTarget.Js,
            mockzillaVersion = BuildKonfig.VERSION_NAME
        ),
        FileIo()
    ) {
        object : ZeroConfDiscoveryService {
            override suspend fun makeDiscoverable(metaData: MetaData, port: Int) {
                Logger.i("Mockzilla") { "ZeroConf not supported for JS Mockzilla" }
            }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun stopMockzilla() = GlobalScope.promise {
    stopServer()
}