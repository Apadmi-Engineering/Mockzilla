package com.apadmi.mockzilla.lib.internal.models

import com.apadmi.mockzilla.lib.internal.utils.HttpStatusCodeSerializer
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MockzillaHttpRequest
import com.apadmi.mockzilla.lib.models.MockzillaHttpResponse
import io.ktor.http.*
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

fun EndpointConfiguration.toMockDataEntryForWeb(): MockDataEntryDto {
    val defaultRequest = FakeMockzillaHttpRequest(
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

/**
 * This class is a temporary stopgap while we build the new management UI.
 *
 * Once that's implemented the mechanisms for defining mock data will be more advanced and this shouldn't be needed.
 * @property uri
 * @property headers
 * @property body
 * @property method
 */
internal class FakeMockzillaHttpRequest(
    override val uri: String,
    override val headers: Map<String, String>,
    @Deprecated("`body`is deprecated", replaceWith = ReplaceWith("bodyAsString()"))
    override val body: String,
    override val method: HttpMethod
) : MockzillaHttpRequest {
    override val underlyingKtorRequest get() = throw NotImplementedError("This is a fake request, it does not have an underlying ktor request")

    override fun bodyAsBytes() = bodyAsString().encodeToByteArray()
    override fun bodyAsString() = body
}

