package com.apadmi.mockzilla.lib

import com.apadmi.mockzilla.BuildKonfig
import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams
import java.nio.file.Files

/**
 * Starts the Mockzilla server,
 *
 * @param config The config with which to initialise mockzilla.
 * @param context The android context
 * @return runtimeParams Configuration of the mockzilla runtime environment
 */
fun startMockzilla(config: MockzillaConfig): MockzillaRuntimeParams = startMockzilla(
    config,
    MetaData("", "", "", "", "", "", BuildKonfig.VERSION_NAME),
    FileIo(Files.createTempDirectory("").toFile()),
)
