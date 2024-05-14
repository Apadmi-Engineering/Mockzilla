package com.apadmi.mockzilla.management.internal.service

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointPatchItemDto
import com.apadmi.mockzilla.lib.internal.models.SetOrDont
import com.apadmi.mockzilla.management.MockzillaConnectionConfig
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.management.internal.MockzillaManagementRepository
import io.ktor.http.HttpStatusCode

internal class UpdateServiceImpl(private val repo: MockzillaManagementRepository) :
    MockzillaManagement.UpdateService {
    override suspend fun setShouldFail(
        connection: MockzillaConnectionConfig,
        keys: List<String>,
        shouldFail: Boolean
    ) = repo.updateMockDataEntries(
        keys.map { key ->
            SerializableEndpointPatchItemDto(
                key = key,
                shouldFail = SetOrDont.Set(shouldFail)
            )
        }, connection
    )

    override suspend fun setDelay(
        connection: MockzillaConnectionConfig,
        keys: List<String>,
        delayMs: Int?
    ) = repo.updateMockDataEntries(
        keys.map { key ->
            SerializableEndpointPatchItemDto(
                key = key,
                delayMs = SetOrDont.Set(delayMs)
            )
        }, connection
    )

    override suspend fun setDefaultHeaders(
        connection: MockzillaConnectionConfig,
        key: String,
        headers: Map<String, String>
    ) = repo.updateMockDataEntry(
        SerializableEndpointPatchItemDto(
            key = key,
            headers = SetOrDont.Set(headers)
        ), connection
    )

    override suspend fun setDefaultBody(
        connection: MockzillaConnectionConfig,
        key: String,
        body: String
    ) = repo.updateMockDataEntry(
        SerializableEndpointPatchItemDto(
            key = key,
            defaultBody = SetOrDont.Set(body)
        ), connection
    )

    override suspend fun setDefaultStatus(
        connection: MockzillaConnectionConfig,
        key: String,
        statusCode: HttpStatusCode
    ) = repo.updateMockDataEntry(
        SerializableEndpointPatchItemDto(
            key = key,
            defaultStatus = SetOrDont.Set(statusCode)
        ), connection
    )

    override suspend fun setErrorBody(
        connection: MockzillaConnectionConfig,
        key: String,
        body: String
    ) = repo.updateMockDataEntry(
        SerializableEndpointPatchItemDto(
            key = key,
            errorBody = SetOrDont.Set(body)
        ), connection
    )

    override suspend fun setErrorHeaders(
        connection: MockzillaConnectionConfig,
        key: String,
        headers: Map<String, String>
    ) = repo.updateMockDataEntry(
        SerializableEndpointPatchItemDto(
            key = key,
            errorHeaders = SetOrDont.Set(headers)
        ), connection
    )

    override suspend fun setErrorStatus(
        connection: MockzillaConnectionConfig,
        key: String,
        statusCode: HttpStatusCode
    ) = repo.updateMockDataEntry(
        SerializableEndpointPatchItemDto(
            key = key,
            errorStatus = SetOrDont.Set(statusCode)
        ), connection
    )
}
