package com.apadmi.mockzilla.lib.internal.di

import com.apadmi.mockzilla.lib.internal.controller.LocalMockController
import com.apadmi.mockzilla.lib.internal.controller.ManagementApiController
import com.apadmi.mockzilla.lib.internal.discovery.ZeroConfDiscoveryService
import com.apadmi.mockzilla.lib.internal.service.*
import com.apadmi.mockzilla.lib.internal.service.LocalCacheServiceImpl
import com.apadmi.mockzilla.lib.internal.service.MockServerMonitorImpl
import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.service.AuthHeaderProvider

import co.touchlab.kermit.Logger

/**
 * @property logger
 * @property config
 * @property metaData
 * @property zeroConfDiscoveryService
 */
@Suppress("USE_DATA_CLASS")
internal class DependencyInjector(
    val config: MockzillaConfig,
    val metaData: MetaData,
    fileIo: FileIo,
    val zeroConfDiscoveryService: ZeroConfDiscoveryService,
    val logger: Logger,
) {
    /* Service */
    private val monitor = MockServerMonitorImpl()
    internal val tokensService = TokensServiceImpl(config.releaseModeConfig.tokenLifeSpan)
    val authHeaderProvider: AuthHeaderProvider = if (config.isRelease) {
        ReleaseAuthHeaderProvider(tokensService)
    } else {
        DebugAuthHeaderProvider
    }
    internal val localCacheService = LocalCacheServiceImpl(fileIo, logger)

    /* Controller */
    val localMockController = LocalMockController(
        localCacheService,
        monitor,
        config.endpoints,
        logger
    )
    val managementApiController = ManagementApiController(
        config.endpoints,
        localCacheService,
        monitor
    )
}
