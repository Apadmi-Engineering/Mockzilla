package com.apadmi.mockzilla.management.internal.service

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfigurationPatchRequestDto
import com.apadmi.mockzilla.lib.internal.models.SetOrDont
import com.apadmi.mockzilla.management.MockzillaConnectionConfig
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.management.internal.MockzillaManagementRepository
import io.ktor.http.HttpStatusCode

internal class UpdateServiceImpl(private val repo: MockzillaManagementRepository) :
    MockzillaManagement.UpdateService {
    override suspend fun setShouldFail(
        connection: MockzillaConnectionConfig,
        key: String,
        shouldFail: Boolean
    ) = repo.updateMockDataEntry(
        SerializableEndpointConfigurationPatchRequestDto(
            key = key,
            shouldFail = SetOrDont.Set(shouldFail)
        ), connection
    )

    override suspend fun setDelay(
        connection: MockzillaConnectionConfig,
        key: String,
        delayMs: Int?
    ) = repo.updateMockDataEntry(
        SerializableEndpointConfigurationPatchRequestDto(
            key = key,
            delayMs = SetOrDont.Set(delayMs)
        ), connection
    )

    override suspend fun setHeaders(
        connection: MockzillaConnectionConfig,
        key: String,
        header: Map<String, String>
    ) = repo.updateMockDataEntry(
        SerializableEndpointConfigurationPatchRequestDto(
            key = key,
            headers = SetOrDont.Set(header)
        ), connection
    )

    override suspend fun setDefaultBody(
        connection: MockzillaConnectionConfig,
        key: String,
        body: String
    ) = repo.updateMockDataEntry(
        SerializableEndpointConfigurationPatchRequestDto(
            key = key,
            defaultBody = SetOrDont.Set(body)
        ), connection
    )

    override suspend fun setDefaultStatus(
        connection: MockzillaConnectionConfig,
        key: String,
        statusCode: HttpStatusCode
    ) = repo.updateMockDataEntry(
        SerializableEndpointConfigurationPatchRequestDto(
            key = key,
            defaultStatus = SetOrDont.Set(statusCode)
        ), connection
    )

    override suspend fun setErrorBody(
        connection: MockzillaConnectionConfig,
        key: String,
        body: String
    ) = repo.updateMockDataEntry(
        SerializableEndpointConfigurationPatchRequestDto(
            key = key,
            errorBody = SetOrDont.Set(body)
        ), connection
    )

    override suspend fun setErrorStatus(
        connection: MockzillaConnectionConfig,
        key: String,
        statusCode: HttpStatusCode
    ) = repo.updateMockDataEntry(
        SerializableEndpointConfigurationPatchRequestDto(
            key = key,
            errorStatus = SetOrDont.Set(statusCode)
        ), connection
    )
}
