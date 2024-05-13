package com.apadmi.mockzilla.management.internal.service

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfigurationPatchRequestDto
import com.apadmi.mockzilla.management.MockzillaConnectionConfig
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.management.internal.MockzillaManagementRepository
import io.ktor.http.HttpStatusCode
import kotlin.time.Duration

internal class UpdateServiceImpl(private val repo: MockzillaManagementRepository) : MockzillaManagement.UpdateService {
    override fun setShouldFail(
        connection: MockzillaConnectionConfig,
        key: String,
        shouldFail: Boolean
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun setDelay(
        connection: MockzillaConnectionConfig,
        key: String,
        delay: Duration?
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun setHeaders(
        connection: MockzillaConnectionConfig,
        key: String,
        header: Map<String, String>
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun setDefaultBody(
        connection: MockzillaConnectionConfig,
        key: String,
        body: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun setDefaultStatus(
        connection: MockzillaConnectionConfig,
        key: String,
        statusCode: HttpStatusCode
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun setErrorBody(
        connection: MockzillaConnectionConfig,
        key: String,
        body: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun setErrorStatus(
        connection: MockzillaConnectionConfig,
        key: String,
        statusCode: HttpStatusCode
    ): Result<Unit> {
        TODO("Not yet implemented")
    }


}
