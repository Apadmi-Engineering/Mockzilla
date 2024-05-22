package com.apadmi.mockzilla.lib

import android.content.Context
import com.apadmi.mockzilla.lib.internal.discovery.ZeroConfDiscoveryService
import com.apadmi.mockzilla.lib.internal.discovery.ZeroConfDiscoveryServiceImpl
import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.lib.internal.utils.extractMetaData

import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams

/**
 * Starts the Mockzilla server,
 *
 * @param config The config with which to initialise mockzilla.
 * @param context The android context
 * @return runtimeParams Configuration of the mockzilla runtime environment
 */
fun startMockzilla(config: MockzillaConfig, context: Context): MockzillaRuntimeParams = startMockzilla(
    config = config,
    metaData = context.extractMetaData(),
    fileIo = FileIo(
        context.cacheDir
    ),
    zeroConfDiscoveryService = ZeroConfDiscoveryServiceImpl(context)
)
