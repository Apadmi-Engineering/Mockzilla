package com.apadmi.mockzilla.lib

import android.content.Context
import com.apadmi.mockzilla.lib.internal.PlatformConfig
import com.apadmi.mockzilla.lib.internal.discovery.ZeroConfDiscoveryServiceImpl
import com.apadmi.mockzilla.lib.internal.stopServer
import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.lib.internal.utils.extractMetaData
import com.apadmi.mockzilla.lib.internal.utils.runHandlingPortConflict
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams

import java.net.ServerSocket

import kotlin.use
import kotlinx.coroutines.runBlocking

/**
 * Starts the Mockzilla server,
 *
 * @param config The config with which to initialise mockzilla.
 * @param context The android context
 * @return runtimeParams Configuration of the mockzilla runtime environment
 */
fun startMockzilla(config: MockzillaConfig, context: Context): MockzillaRuntimeParams = runBlocking {
    // On Android we must check if the port is available before launching Mockzilla since the
    // Ktor exception cannot be correctly caught and crashes the app regardless off error handling
    // https://github.com/Apadmi-Engineering/Mockzilla/issues/557
    runHandlingPortConflict(config.port) {
        ServerSocket(config.port).use { socket ->
            // If we did manage to make the Socket make sure
            // we don't block Mockzilla from using it immediately
            // after we're done
            socket.reuseAddress = true
        }
    }

    startMockzilla(
        config = config,
        metaData = context.extractMetaData(),
        fileIo = FileIo(context.cacheDir),
        zeroConfDiscoveryService = { logger -> ZeroConfDiscoveryServiceImpl(logger, context) },
        platformConfig = PlatformConfig(context.applicationContext),
    )
}

/**
 * Stops the Mockzilla server,
 *
 * @return
 */
fun stopMockzilla() = runBlocking {
    stopServer()
}
