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
import io.ktor.util.Platform
import io.ktor.util.PlatformUtils
import io.ktor.util.platform

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield

private val zeroConfStub = object : ZeroConfDiscoveryService {
    override suspend fun makeDiscoverable(metaData: MetaData, port: Int) = Unit
}

internal typealias SetupBlock = suspend (cacheService: LocalCacheService) -> Unit
internal typealias TestBlock = suspend (params: MockzillaRuntimeParams, cacheService: LocalCacheService) -> Unit

private object Constants {
    private const val maxRetriesNative = 3
    val maxRetries = if (PlatformUtils.platform == Platform.Native) maxRetriesNative else 1
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
        } catch (e: Throwable) {
            stopMockzilla()
            Logger(StaticConfig()).e(throwable = e, "", { "Failed with exception, run: $currentRun\n" })
            if (e !is Exception) {
                throw e
            }
            if (++currentRun >= Constants.maxRetries) {
                throw e
            }
        }
    }

    /* Cleanup */
    stopMockzilla()
    delay(100)
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
    yield()

    /* Cleanup */
    fileIo.deleteAllCaches()
}
