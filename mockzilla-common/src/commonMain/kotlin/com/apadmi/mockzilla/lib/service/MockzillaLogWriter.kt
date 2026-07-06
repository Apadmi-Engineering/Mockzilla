package com.apadmi.mockzilla.lib.service

import com.apadmi.mockzilla.lib.models.MockzillaConfig

/**
 * Extension point for routing Mockzilla's internal log output to a custom sink, such as a crash
 * reporting service or custom logger. Register via [MockzillaConfig.Builder.addLogWriter].
 */
public interface MockzillaLogWriter {
    /**
     * Called by Mockzilla for each log entry.
     *
     * @param logLevel The severity of this log entry.
     * @param message The log message.
     * @param tag The source tag identifying the component that produced this log entry.
     * @param throwable An associated exception, if any.
     */
    public fun log(
        logLevel: MockzillaConfig.LogLevel,
        message: String,
        tag: String,
        throwable: Throwable? = null
    )
}
