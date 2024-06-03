package com.apadmi.mockzilla.management

import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.internal.MockzillaManagementRepository
import com.apadmi.mockzilla.management.internal.MockzillaManagementRepositoryImpl
import com.apadmi.mockzilla.management.internal.service.UpdateServiceImpl
import io.ktor.http.HttpStatusCode

interface MockzillaManagement {
    /**
     * In cases where the wrapper isn't  granular enough this gives access to handle manually make
     * the raw requests to the server.
     */
    val underlyingRepository: MockzillaManagementRepository
    val updateService: UpdateService
    val metaDataService: MetaDataService
    val logsService: LogsService
    val cacheClearingService: CacheClearingService
    val endpointsService: EndpointsService

    interface CacheClearingService {
        suspend fun clearAllCaches(connection: MockzillaConnectionConfig): Result<Unit>
        suspend fun clearCaches(
            connection: MockzillaConnectionConfig,
            keys: List<EndpointConfiguration.Key>
        ): Result<Unit>
    }

    interface EndpointsService {
        suspend fun fetchAllEndpointConfigs(connection: MockzillaConnectionConfig): Result<List<SerializableEndpointConfig>>
        suspend fun fetchDashboardOptionsConfig(
            connection: MockzillaConnectionConfig,
            key: EndpointConfiguration.Key
        ): Result<DashboardOptionsConfig>
    }

    interface UpdateService {
        suspend fun setShouldFail(
            connection: MockzillaConnectionConfig,
            keys: Collection<EndpointConfiguration.Key>,
            shouldFail: Boolean?
        ): Result<Unit>

        suspend fun setDelay(
            connection: MockzillaConnectionConfig,
            keys: Collection<EndpointConfiguration.Key>,
            delayMs: Int?
        ): Result<Unit>

        suspend fun setDefaultHeaders(
            connection: MockzillaConnectionConfig,
            key: EndpointConfiguration.Key,
            headers: Map<String, String>?
        ): Result<Unit>

        suspend fun setDefaultBody(
            connection: MockzillaConnectionConfig,
            key: EndpointConfiguration.Key,
            body: String?
        ): Result<Unit>

        suspend fun setDefaultStatus(
            connection: MockzillaConnectionConfig,
            key: EndpointConfiguration.Key,
            statusCode: HttpStatusCode?
        ): Result<Unit>

        suspend fun setErrorBody(
            connection: MockzillaConnectionConfig,
            key: EndpointConfiguration.Key,
            body: String?
        ): Result<Unit>

        suspend fun setErrorHeaders(
            connection: MockzillaConnectionConfig,
            key: EndpointConfiguration.Key,
            headers: Map<String, String>?
        ): Result<Unit>

        suspend fun setErrorStatus(
            connection: MockzillaConnectionConfig,
            key: EndpointConfiguration.Key,
            statusCode: HttpStatusCode?
        ): Result<Unit>
    }

    interface MetaDataService {
        suspend fun fetchMetaData(
            connection: MockzillaConnectionConfig,
            hideFromLogs: Boolean
        ): Result<MetaData>
    }

    interface LogsService {
        suspend fun fetchMonitorLogsAndClearBuffer(
            connection: MockzillaConnectionConfig,
            hideFromLogs: Boolean
        ): Result<MonitorLogsResponse>
    }

    private data class Instance(
        override val underlyingRepository: MockzillaManagementRepository,
        override val updateService: UpdateService,
        override val metaDataService: MetaDataService,
        override val logsService: LogsService,
        override val cacheClearingService: CacheClearingService,
        override val endpointsService: EndpointsService
    ) : MockzillaManagement

    companion object {
        val instance: MockzillaManagement by lazy {
            val repo = MockzillaManagementRepositoryImpl.create()
            Instance(repo, UpdateServiceImpl(repo), repo, repo, repo, repo)
        }
    }
}
