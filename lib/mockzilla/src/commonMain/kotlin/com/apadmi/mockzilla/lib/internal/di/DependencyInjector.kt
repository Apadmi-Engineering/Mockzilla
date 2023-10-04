package com.apadmi.mockzilla.lib.internal.di

import com.apadmi.mockzilla.lib.internal.controller.LocalMockController
import com.apadmi.mockzilla.lib.internal.controller.WebPortalApiController
import com.apadmi.mockzilla.lib.internal.service.*
import com.apadmi.mockzilla.lib.internal.service.DelayAndFailureDecisionImpl
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
 */
@Suppress("USE_DATA_CLASS")
internal class DependencyInjector(
    val config: MockzillaConfig,
    val metaData: MetaData,
    fileIo: FileIo,
    val logger: Logger,
) {
    /* Service */
    private val delayAndFailureDecision = DelayAndFailureDecisionImpl
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
        delayAndFailureDecision,
        config.endpoints,
        logger
    )
    val webPortalApiController = WebPortalApiController(
        config.endpoints,
        localCacheService,
        monitor
    )
}
