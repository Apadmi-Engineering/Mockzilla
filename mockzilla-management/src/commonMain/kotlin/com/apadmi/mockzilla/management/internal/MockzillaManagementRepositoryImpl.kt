package com.apadmi.mockzilla.management.internal

import com.apadmi.mockzilla.lib.internal.models.MockDataResponseDto
import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointPatchItemDto
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.MockzillaConnectionConfig
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.management.internal.ktor.KtorClientProvider
import com.apadmi.mockzilla.management.internal.ktor.KtorRequestRunner
import com.apadmi.mockzilla.management.internal.ktor.get

import co.touchlab.kermit.Logger
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfigPatchRequestDto
import com.apadmi.mockzilla.management.internal.ktor.patch
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger as KtorLogger
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface MockzillaManagementRepository {
    suspend fun fetchMetaData(connection: MockzillaConnectionConfig): Result<MetaData>
    suspend fun fetchAllEndpointConfigs(connection: MockzillaConnectionConfig): Result<List<SerializableEndpointConfig>>
    suspend fun updateMockDataEntry(entry: SerializableEndpointPatchItemDto, connection: MockzillaConnectionConfig): Result<Unit>
    suspend fun updateMockDataEntries(entries: List<SerializableEndpointPatchItemDto>, connection: MockzillaConnectionConfig): Result<Unit>
    suspend fun fetchMonitorLogsAndClearBuffer(connection: MockzillaConnectionConfig): Result<MonitorLogsResponse>
}

/**
 * @property runner The KTOR wrapper that actually runs requests
 */
internal class MockzillaManagementRepositoryImpl(
    val runner: KtorRequestRunner
) : MockzillaManagementRepository,
MockzillaManagement.LogsService,
MockzillaManagement.MetaDataService {
    override suspend fun fetchMetaData(
        connection: MockzillaConnectionConfig
    ) = runner<MetaData> {
        get(connection, "/api/meta")
    }.onFailure {
        Logger.v(tag = "Management", it) { "Request Failed: /api/meta" }
    }

    override suspend fun fetchAllEndpointConfigs(
        connection: MockzillaConnectionConfig
    ) = runner<MockDataResponseDto> {
        get(connection, "/api/mock-data")
    }.onFailure {
        Logger.v(tag = "Management", it) { "Request Failed: /api/mock-data" }
    }.map { it.entries }

    override suspend fun updateMockDataEntry(
        entry: SerializableEndpointPatchItemDto,
        connection: MockzillaConnectionConfig,
    ) = updateMockDataEntries(listOf(entry), connection)

    override suspend fun updateMockDataEntries(
        entries: List<SerializableEndpointPatchItemDto>,
        connection: MockzillaConnectionConfig
    ) = runner<Unit> {
        patch(connection, "/api/mock-data") {
            contentType(ContentType.Application.Json)
            setBody(SerializableEndpointConfigPatchRequestDto(entries))
        }
    }.onFailure {
        Logger.v(tag = "Management", it) { "Request Failed: /api/monitor-logs" }
    }

    override suspend fun fetchMonitorLogsAndClearBuffer(
        connection: MockzillaConnectionConfig
    ) = runner<MonitorLogsResponse> {
        get(connection, "/api/monitor-logs")
    }.onFailure {
        Logger.v(tag = "Management", it) { "Request Failed: /api/monitor-logs" }
    }

    companion object {
        internal fun create(logger: KtorLogger) = MockzillaManagementRepositoryImpl(
            KtorRequestRunner(KtorClientProvider.createKtorClient(logger = logger))
        )

        fun create() = create(KtorLogger.DEFAULT)
    }
}
