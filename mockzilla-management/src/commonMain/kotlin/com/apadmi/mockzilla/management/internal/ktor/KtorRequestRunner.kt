package com.apadmi.mockzilla.management.internal.ktor

import com.apadmi.mockzilla.management.MockzillaConnectionConfig

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class KtorRequestRunner(private val client: HttpClient) {
    suspend inline operator fun <reified SuccessType : Any> invoke(
        block: HttpClient.() -> HttpResponse,
    ): Result<SuccessType> {
        val result = kotlin.runCatching { block(client) }
        val exceptionOrNull = result.exceptionOrNull()
        return when {
            result.isSuccess -> result.getOrThrow().toResult()
            // Kotlin has special handling for these exceptions, so we don't want to get in the way of that
            exceptionOrNull is CancellationException -> throw exceptionOrNull
            else -> Result.failure(exceptionOrNull ?: Exception("Unexpected Ktor error"))
        }
    }

    private suspend inline fun <reified SuccessType : Any> HttpResponse.toResult() =
        withContext(Dispatchers.IO) {
            if (this@toResult.status.isSuccess()) {
                kotlin.runCatching { body<SuccessType>() }
            } else {
                Result.failure(Exception("Failed network call, see logs"))
            }
        }
}

internal suspend inline fun HttpClient.get(
    connection: MockzillaConnectionConfig,
    path: String,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = get {
    url(connection.url(path))
    block()
}

internal suspend inline fun HttpClient.patch(
    connection: MockzillaConnectionConfig,
    path: String,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = patch {
    url(connection.url(path))
    block()
}

internal suspend inline fun HttpClient.delete(
    connection: MockzillaConnectionConfig,
    path: String,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = delete {
    url(connection.url(path))
    block()
}

private fun MockzillaConnectionConfig.url(path: String) =
    "http://$ip:$port/${path.removePrefix("/")}"
