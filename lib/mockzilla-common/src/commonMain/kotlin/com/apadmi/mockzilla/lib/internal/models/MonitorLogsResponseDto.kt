package com.apadmi.mockzilla.lib.internal.models

import com.apadmi.mockzilla.lib.internal.utils.HttpStatusCodeSerializer
import io.ktor.http.*
import kotlinx.serialization.Serializable

/**
 * @property timestamp
 * @property url
 * @property requestBody
 * @property requestHeaders
 * @property responseHeaders
 * @property responseBody
 * @property status
 * @property delay
 * @property method
 * @property isIntendedFailure
 */
@Serializable
data class LogEvent(
    val timestamp: Long,
    val url: String,
    val requestBody: String,
    val requestHeaders: Map<String, String>,
    val responseHeaders: Map<String, String>,
    val responseBody: String,
    @Serializable(with = HttpStatusCodeSerializer::class) val status: HttpStatusCode,
    val delay: Long,
    val method: String,
    val isIntendedFailure: Boolean,
)

/**
 * @property appPackage
 * @property logs
 */
@Serializable
data class MonitorLogsResponse(val appPackage: String, val logs: List<LogEvent>)
