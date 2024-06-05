package com.apadmi.mockzilla.lib

import com.apadmi.mockzilla.lib.internal.discovery.ZeroConfDiscoveryServiceImpl
import com.apadmi.mockzilla.lib.internal.discovery.validateInfoPlistOrThrow
import com.apadmi.mockzilla.lib.internal.persistance.KeychainSettings
import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.lib.internal.utils.extractMetaData
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams

/**
 * Starts the Mockzilla server,
 *
 * @param config The config with which to initialise mockzilla.
 */
fun startMockzilla(config: MockzillaConfig): MockzillaRuntimeParams {
    config.validateInfoPlistOrThrow()

    return startMockzilla(
        config = config,
        metaData = extractMetaData(),
        fileIo = FileIo(),
        zeroConfDiscoveryService = { _ ->
            ZeroConfDiscoveryServiceImpl(
                KeychainSettings("mockzilla_keychain_settings")
            )
        }
    )
}
