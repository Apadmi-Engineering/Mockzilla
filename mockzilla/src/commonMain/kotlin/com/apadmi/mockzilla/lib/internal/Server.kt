package com.apadmi.mockzilla.lib.internal

import com.apadmi.mockzilla.lib.internal.di.DependencyInjector
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams

internal expect suspend fun startServer(port: Int, di: DependencyInjector): MockzillaRuntimeParams
internal expect suspend fun stopServer()
