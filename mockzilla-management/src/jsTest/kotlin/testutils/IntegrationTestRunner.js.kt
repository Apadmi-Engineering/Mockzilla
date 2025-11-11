package com.apadmi.mockzilla.testutils

import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams
import com.apadmi.mockzilla.lib.startMockzilla

actual suspend fun startTestingMockzilla(
    appName: String,
    appVersion: String,
    config: MockzillaConfig
): MockzillaRuntimeParams = startMockzilla(appName, appVersion, config)
