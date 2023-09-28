package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaConfig.LogLevel.Companion.toLogLevel

import co.touchlab.kermit.Severity

import kotlin.test.Test
import kotlin.test.assertEquals

class LogSeverityTests {
    @Test
    fun `Kermit severity and mockzilla log level converts correctly`() {
        assertEquals(MockzillaConfig.LogLevel.values().size, Severity.values().size)
        MockzillaConfig.LogLevel.values().forEach {
            assertEquals(it, it.toKermitSeverity().toLogLevel())
        }
    }
}
