@file:OptIn(ExperimentalUuidApi::class)

package com.apadmi.mockzilla.lib.internal.models

import com.apadmi.mockzilla.lib.internal.utils.HttpStatusCodeSerializer

import io.ktor.http.*

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

/**
 * @property timestamp milliseconds from 1st Jan 1970 epoch
 * @property url
 * @property requestBody
 * @property requestHeaders
 * @property responseHeaders
 * @property responseBody
 * @property status
 * @property delay
 * @property method
 * @property isIntendedFailure
 * @property id
 */
@Serializable
data class LogEvent(
    val id: String = Uuid.random().toString(),
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
    val isRequestBodyTruncated: Boolean = false,
    val isResponseBodyTruncated: Boolean = false,
)

/**
 * @property appPackage
 * @property logs
 */
@Serializable
data class MonitorLogsResponse(val appPackage: String, val logs: List<LogEvent>)
