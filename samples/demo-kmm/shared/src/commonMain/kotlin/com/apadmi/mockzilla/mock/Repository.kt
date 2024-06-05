package com.apadmi.mockzilla.mock

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

sealed class DataResult<out T, out E> {
    fun dataOrNull() = when (this) {
        is Success -> ok
        is Failure -> null
    }

    fun errorOrNull() = when (this) {
        is Success -> null
        is Failure -> error
    }

    fun isSuccess() = this is Success

    /**
     * @property error
     */
    data class Failure<E>(val error: E) : DataResult<Nothing, E>()
    /**
     * @property ok
     */
    data class Success<T>(val ok: T) : DataResult<T, Nothing>()

    companion object
}

class Repository(private val baseUrl: String) {
    suspend fun getCow(someRequestValue: String): DataResult<CowDto, String> {
        val response = HttpClient(CIO).post(
            "$baseUrl/cow") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(GetCowRequestDto(someRequestValue)))
        }

        val parsed = runCatching { Json.decodeFromString<CowDto>(response.bodyAsText()) }
        return if (response.status.isSuccess() && parsed.isSuccess) {
            DataResult.Success(parsed.getOrThrow())
        } else {
            DataResult.Failure("${response.status} - ${parsed.exceptionOrNull() ?: response.bodyAsText()}")
        }
    }
}
