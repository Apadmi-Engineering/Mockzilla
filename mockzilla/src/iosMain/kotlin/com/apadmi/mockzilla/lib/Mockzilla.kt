package com.apadmi.mockzilla.lib

import com.apadmi.mockzilla.lib.internal.discovery.ZeroConfDiscoveryServiceImpl
import com.apadmi.mockzilla.lib.internal.discovery.validateInfoPlistOrThrow
import com.apadmi.mockzilla.lib.internal.persistance.KeychainSettings
import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.lib.internal.utils.extractMetaData
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams
import com.apadmi.mockzilla.lib.models.PortConflictException

/**
 * Internal method to start the Mockzilla server. Consumer apps should prefer using the top-level
 * `startMockzilla()` function to avoid breaking changes.
 *
 * @param config The config with which to initialise mockzilla.
 */
@Throws(PortConflictException::class)
fun startMockzilla(config: MockzillaConfig): MockzillaRuntimeParams {
    config.validateInfoPlistOrThrow()

    return startMockzilla(
        config = config,
        metaData = extractMetaData(),
        fileIo = FileIo(),
        zeroConfDiscoveryService = { logger ->
            ZeroConfDiscoveryServiceImpl(
                logger,
                KeychainSettings("mockzilla_keychain_settings")
            )
        }
    )
}
