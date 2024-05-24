package com.apadmi.mockzilla.testutils

import com.apadmi.mockzilla.lib.internal.discovery.ZeroConfDiscoveryService
import com.apadmi.mockzilla.lib.internal.service.LocalCacheService
import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.lib.internal.utils.createFileIoforTesting
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams
import com.apadmi.mockzilla.lib.models.RunTarget
import com.apadmi.mockzilla.lib.prepareMockzilla
import com.apadmi.mockzilla.lib.startMockzilla
import com.apadmi.mockzilla.lib.stopMockzilla

import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest

private val zeroConfStub = object : ZeroConfDiscoveryService {
    override fun makeDiscoverable(metaData: MetaData, port: Int) = Unit
}

internal typealias SetupBlock = suspend (cacheService: LocalCacheService) -> Unit
internal typealias TestBlock = suspend (params: MockzillaRuntimeParams, cacheService: LocalCacheService) -> Unit

private object Constants {
    const val maxRetries = 3
}
private fun MetaData.Companion.dummy() = MetaData(
    appName = "",
    appPackage = "",
    operatingSystemVersion = "",
    deviceModel = "",
    appVersion = "",
    runTarget = RunTarget.IosDevice,
    mockzillaVersion = ""
)

/**
 * Specifically in test sometimes connections to the server timeout. This utility
 * provides retry capacity for trying network calls a few times before failing.
 *
 * @param config The mockzilla config
 * @param metaData The test metadata
 * @param fileIo The file io to use
 * @param setup Block that gets executed before starting the server
 * @param block The test block itself.
 * @return
 * @throws Exception if the last test attempt throws an exception
 */
internal fun runIntegrationTest(
    config: MockzillaConfig,
    metaData: MetaData = MetaData.dummy(),
    fileIo: FileIo = createFileIoforTesting(),
    setup: SetupBlock = { /* Run Setup */ },
    block: TestBlock,
) = runTest {
    var currentRun = 0
    while (currentRun < Constants.maxRetries) {
        try {
            runFullIntegrationTest(config, metaData, fileIo, setup, block)

            // No crash, exit the loop
            currentRun = Int.MAX_VALUE
        } catch (e: Exception) {
            Logger(StaticConfig()).e { "Failed with exception, run: $currentRun" }
            if (++currentRun == Constants.maxRetries) {
                throw e
            }
        }

        /* Cleanup */
        stopMockzilla()
        runBlocking { delay(100) }
    }
}

private suspend fun runFullIntegrationTest(
    config: MockzillaConfig,
    metaData: MetaData,
    fileIo: FileIo,
    setup: SetupBlock,
    block: TestBlock,
) {
    /* Setup */
    val di = prepareMockzilla(
        config,
        metaData,
        fileIo,
        Logger(StaticConfig()),
        zeroConfStub
    )
    fileIo.deleteAllCaches()
    setup(di.localCacheService)
    val params = startMockzilla(config, di)

    /* Run Test & Verify */
    block(params, di.localCacheService)

    /* Cleanup */
    fileIo.deleteAllCaches()
}
