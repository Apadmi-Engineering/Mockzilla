package com.apadmi.mockzilla.lib.internal.models

import com.apadmi.mockzilla.lib.internal.utils.HttpStatusCodeSerializer
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaHttpRequest
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import io.ktor.http.*
import io.ktor.util.InternalAPI
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO for interaction with the web portal.
 *
 * @property name
 * @property key
 * @property failProbability
 * @property delayMean
 * @property delayVariance
 * @property headers
 * @property defaultBody
 * @property errorBody
 * @property errorStatus
 * @property defaultStatus
 */
@Serializable
data class MockDataEntryDto(
    val key: String,
    val name: String,
    val failProbability: Int,
    val delayMean: Int,
    val delayVariance: Int,
    val headers: Map<String, String>,
    @SerialName("contentJson") val defaultBody: String,
    @SerialName("errorJson") val errorBody: String,
    @Serializable(with = HttpStatusCodeSerializer::class) val errorStatus: HttpStatusCode,
    @Serializable(with = HttpStatusCodeSerializer::class) @SerialName("successStatus") val defaultStatus: HttpStatusCode,
) {
    fun toDefaultMockzillaResponse() = MockzillaHttpResponse(
        defaultStatus,
        headers,
        defaultBody
    )

    fun toErrorMockzillaResponse() = MockzillaHttpResponse(
        errorStatus,
        headers,
        errorBody
    )
}
