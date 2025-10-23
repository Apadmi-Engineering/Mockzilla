package com.apadmi.mockzilla.demo.engine

import com.apadmi.mockzilla.demo.engine.network.DataResult
import com.apadmi.mockzilla.demo.engine.network.MockzillaTokenProvider

import io.ktor.client.*
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import okhttp3.OkHttpClient
import okhttp3.Protocol

import java.net.Proxy

import kotlinx.serialization.json.Json

class Repository(
    private val baseUrl: String,
    private val tokenProvider: MockzillaTokenProvider
) {
    suspend fun getAnimal(
        urlSuffix: String,
        someRequestValue: String
    ): DataResult<AnimalDto, String> = try {
        val response = HttpClient(OkHttp) {
            engine {
                preconfigured = OkHttpClient.Builder()
                    .proxy(Proxy.NO_PROXY)
                    .protocols(listOf(Protocol.HTTP_1_1)).build()
            }
        }.post("$baseUrl/$urlSuffix") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(GetAnimalRequestDto(someRequestValue)))

            // In release mode this matters, otherwise it can be omitted
            val header = tokenProvider.getTokenHeader()
            headers.append(header.key, header.value)
        }

        if (response.status.isSuccess()) {
            DataResult.Success(Json.decodeFromString(response.bodyAsText()))
        } else {
            DataResult.Failure("${response.status} - ${response.bodyAsText()}")
        }
    } catch (error: Throwable) {
        DataResult.Failure(error.toString())
    }
}
