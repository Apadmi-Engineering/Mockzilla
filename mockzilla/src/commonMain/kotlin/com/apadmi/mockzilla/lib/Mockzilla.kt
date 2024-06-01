package com.apadmi.mockzilla.lib

import com.apadmi.mockzilla.lib.internal.di.DependencyInjector
import com.apadmi.mockzilla.lib.internal.discovery.ZeroConfDiscoveryService
import com.apadmi.mockzilla.lib.internal.service.validate
import com.apadmi.mockzilla.lib.internal.startServer
import com.apadmi.mockzilla.lib.internal.stopServer
import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams
import com.apadmi.mockzilla.lib.service.toKermitLogWriter
import com.apadmi.mockzilla.lib.service.toKermitSeverity

import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig
import co.touchlab.kermit.platformLogWriter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Stops the Mockzilla server,
 */
fun stopMockzilla() {
    stopServer()
}

internal fun startMockzilla(
    config: MockzillaConfig,
    metaData: MetaData,
    fileIo: FileIo,
    zeroConfDiscoveryService: ZeroConfDiscoveryService
) = startMockzilla(config, prepareMockzilla(config, metaData, fileIo, Logger(
    StaticConfig(
        config.logLevel.toKermitSeverity(),
        listOf(platformLogWriter()) + config.additionalLogWriters.map { it.toKermitLogWriter() }
    ), "Mockzilla"
), zeroConfDiscoveryService))

internal fun prepareMockzilla(
    config: MockzillaConfig,
    metaData: MetaData,
    fileIo: FileIo,
    logger: Logger,
    zeroConfDiscoveryService: ZeroConfDiscoveryService,
) = DependencyInjector(config, metaData, fileIo, zeroConfDiscoveryService, logger).also {
    config.validate()
}

internal fun startMockzilla(
    config: MockzillaConfig,
    di: DependencyInjector,
    scope: CoroutineScope = GlobalScope
): MockzillaRuntimeParams {
    scope.launch { di.localCacheService.clearStaleCaches(config.endpoints) }
    return startServer(config.port, di)
}
