package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.internal.models.MockDataEntryDto
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaHttpRequest
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode

internal fun EndpointConfiguration.toMockDataEntry(): MockDataEntryDto {
    val defaultRequest = MockzillaHttpRequest(
        "https://this-is-being-called-from-the-web-api.com",
        emptyMap(),
        "This is being called from the web portal",
        HttpMethod.Get
    )

    val defaultResponse = webApiDefaultResponse ?: runCatching {
        defaultHandler.invoke(defaultRequest)
    }.getOrDefault(MockzillaHttpResponse(body = "{}"))

    val errorResponse = webApiErrorResponse ?: runCatching {
        errorHandler.invoke(defaultRequest)
    }.getOrDefault(MockzillaHttpResponse(statusCode = HttpStatusCode.BadRequest, body = "{}"))

    return MockDataEntryDto(
        name = name,
        key = key,
        failProbability = failureProbability ?: 0,
        delayMean = delayMean ?: 0,
        delayVariance = delayVariance ?: 0,
        headers = defaultResponse.headers,
        defaultBody = defaultResponse.body,
        errorBody = errorResponse.body,
        errorStatus = errorResponse.statusCode,
        defaultStatus = defaultResponse.statusCode
    )
}
