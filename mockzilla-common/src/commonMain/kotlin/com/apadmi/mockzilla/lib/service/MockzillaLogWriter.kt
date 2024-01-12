package com.apadmi.mockzilla.lib.service

import com.apadmi.mockzilla.lib.models.MockzillaConfig


interface MockzillaLogWriter {
    fun log(
        logLevel: MockzillaConfig.LogLevel,
        message: String,
        tag: String,
        throwable: Throwable? = null
    )
}
