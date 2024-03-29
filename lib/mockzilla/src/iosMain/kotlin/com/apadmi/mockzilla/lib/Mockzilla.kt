package com.apadmi.mockzilla.lib

import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.lib.internal.utils.extractMetaData
import com.apadmi.mockzilla.lib.models.MockzillaConfig

/**
 * Starts the Mockzilla server,
 *
 * @param config The config with which to initialise mockzilla.
 */
fun startMockzilla(config: MockzillaConfig) = startMockzilla(config, extractMetaData(), FileIo())
