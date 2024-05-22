package com.apadmi.mockzilla.lib

import android.content.Context
import com.apadmi.mockzilla.lib.internal.discovery.DiscoveryService
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
    config,
    context.also {
        DiscoveryService.context = it
    }.extractMetaData(),
    FileIo(
        context.cacheDir
    ),
)
