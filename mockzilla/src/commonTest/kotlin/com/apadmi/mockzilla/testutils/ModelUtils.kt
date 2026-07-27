package com.apadmi.mockzilla.testutils

import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.PartialMockzillaHttpResponse
import io.ktor.http.HttpStatusCode

fun DashboardOverridePreset.Companion.dummy(
    statusCode: HttpStatusCode? = null,
    headers: Map<String, String>? = null,
    body: String? = null
) = DashboardOverridePreset(
    "",
    response = PartialMockzillaHttpResponse(
        statusCode = statusCode,
        headers = headers,
        body = body
    ),
    description = "",
    type = null,
)
