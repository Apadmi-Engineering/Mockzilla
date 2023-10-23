package com.apadmi.mockzilla.testutils.dummymodels

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
import io.ktor.http.HttpStatusCode


fun MonitorLogsResponse.Companion.dummy()  = MonitorLogsResponse(
    appPackage = "package",
    logs = listOf(
        LogEvent(
            timestamp = 1234,
            url = "url",
            requestBody = "body",
            requestHeaders = mapOf("1" to "request1", "2" to "request2"),
            responseHeaders = mapOf("1" to "response1", "2" to "request2"),
            responseBody = "body",
            status = HttpStatusCode.Accepted,
            delay = 1000,
            method = "method",
            isIntendedFailure = false,

        )


    )
)