package com.apadmi.mockzilla.management.internal.ktor

import co.touchlab.kermit.Logger
import com.apadmi.mockzilla.management.MockzillaManagement
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.options
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

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
            else -> {
                Result.failure(exceptionOrNull ?: Exception("Unexpected Ktor error"))
            }
        }
    }

    private suspend inline fun <reified SuccessType : Any> HttpResponse.toResult() =
        withContext(Dispatchers.IO) {
            kotlin.runCatching { body<SuccessType>() }
        }
}

private fun MockzillaManagement.ConnectionConfig.url(path: String) =
    "http://${ip}:${port}/${path.removePrefix("/")}"

internal suspend inline fun HttpClient.get(
    connectionConfig: MockzillaManagement.ConnectionConfig,
    path: String,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = get { url(connectionConfig.url(path)); block() }

internal suspend inline fun HttpClient.post(
    connectionConfig: MockzillaManagement.ConnectionConfig,
    path: String,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = post { url(connectionConfig.url(path)); block() }


internal suspend inline fun HttpClient.delete(
    connectionConfig: MockzillaManagement.ConnectionConfig,
    path: String,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = delete { url(connectionConfig.url(path)); block() }
