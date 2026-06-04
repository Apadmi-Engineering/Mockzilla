package com.apadmi.mockzilla.lib

import com.apadmi.mockzilla.lib.internal.discovery.ZeroConfDiscoveryServiceImpl
import com.apadmi.mockzilla.lib.internal.discovery.validateInfoPlist
import com.apadmi.mockzilla.lib.internal.persistance.KeychainSettings
import com.apadmi.mockzilla.lib.internal.stopServer
import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.lib.internal.utils.extractMetaData
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams
import com.apadmi.mockzilla.lib.models.PortConflictException
import kotlinx.coroutines.runBlocking

/**
 * Starts the Mockzilla server.
 *
 * @param config The config with which to initialise mockzilla.
 * @throws PortConflictException if the port specified in [config] is already in use.
 */
@Throws(PortConflictException::class)
fun startMockzilla(config: MockzillaConfig): MockzillaRuntimeParams = runBlocking {
    config.validateInfoPlist()

    startMockzilla(
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

/**
 * Stops the running Mockzilla server.
 *
 */
actual fun stopMockzilla() = runBlocking {
    stopServer()
}
