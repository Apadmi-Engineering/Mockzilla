package com.apadmi.mockzilla.codegen.data

import io.swagger.models.HttpMethod

data class EndpointSpec(
    val path: String,
    val method: HttpMethod,
    val successResponse: ExampleResponse,
    val errorResponse: ExampleResponse,
    val otherResponses: Array<ExampleResponse>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EndpointSpec

        if (path != other.path) return false
        if (method != other.method) return false
        if (successResponse != other.successResponse) return false
        if (errorResponse != other.errorResponse) return false
        if (!otherResponses.contentEquals(other.otherResponses)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = path.hashCode()
        result = 31 * result + method.hashCode()
        result = 31 * result + successResponse.hashCode()
        result = 31 * result + errorResponse.hashCode()
        result = 31 * result + otherResponses.contentHashCode()
        return result
    }
}

data class ExampleResponse(
    val statusCode: Int,
    val name: String,
    val bodyValue: String = "",
)

