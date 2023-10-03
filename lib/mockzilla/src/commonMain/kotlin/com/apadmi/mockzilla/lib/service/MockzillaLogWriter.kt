package com.apadmi.mockzilla.lib.service

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Severity
import com.apadmi.mockzilla.lib.models.MockzillaConfig

@Suppress("EXTENSION_FUNCTION_WITH_CLASS")
internal fun MockzillaLogWriter.toKermitLogWriter() = object : LogWriter() {
    override fun log(
        severity: Severity,
        message: String,
        tag: String,
        throwable: Throwable?
    ) {
        this@toKermitLogWriter.log(logLevel = severity.toLogLevel(), message, tag, throwable)
    }
}

internal fun MockzillaConfig.LogLevel.toKermitSeverity() = Severity.valueOf(name)
internal fun Severity.toLogLevel() = com.apadmi.mockzilla.lib.models.MockzillaConfig.LogLevel.valueOf(name)
