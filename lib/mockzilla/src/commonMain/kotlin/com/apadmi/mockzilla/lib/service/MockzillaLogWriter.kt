package com.apadmi.mockzilla.lib.service

import com.apadmi.mockzilla.lib.models.MockzillaConfig
import com.apadmi.mockzilla.lib.models.MockzillaConfig.LogLevel.Companion.toLogLevel

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Severity

interface MockzillaLogWriter {
    fun log(
        logLevel: MockzillaConfig.LogLevel,
        message: String,
        tag: String,
        throwable: Throwable? = null
    )
}

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
