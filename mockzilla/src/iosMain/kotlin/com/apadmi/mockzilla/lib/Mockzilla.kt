package com.apadmi.mockzilla.lib

import co.touchlab.kermit.Logger
import com.apadmi.mockzilla.lib.config.ZeroConfConfig
import com.apadmi.mockzilla.lib.internal.discovery.ZeroConfDiscoveryServiceImpl
import com.apadmi.mockzilla.lib.internal.discovery.validateInfoPlistOrThrow
import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.lib.internal.utils.extractMetaData
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams
import platform.Foundation.NSArray
import platform.Foundation.NSBundle
import platform.Foundation.NSException
import platform.Foundation.NSString
import platform.Foundation.containsObject
import platform.Foundation.raise

/**
 * Starts the Mockzilla server,
 *
 * @param config The config with which to initialise mockzilla.
 */
fun startMockzilla(config: MockzillaConfig): MockzillaRuntimeParams {
    validateInfoPlistOrThrow()

    return startMockzilla(
        config = config,
        metaData = extractMetaData(),
        fileIo = FileIo(),
        zeroConfDiscoveryService = ZeroConfDiscoveryServiceImpl()
    )
}
