package com.apadmi.mockzilla.management

import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.lib.service.AuthHeaderProvider
import com.apadmi.mockzilla.management.internal.MockzillaManagementRepository
import com.apadmi.mockzilla.management.internal.MockzillaManagementRepositoryImpl
import com.apadmi.mockzilla.management.internal.service.UpdateServiceImpl
import io.ktor.http.HttpStatusCode
import kotlin.time.Duration

interface MockzillaManagement {
    /**
     * In cases where the wrapper isn't  granular enough this gives access to handle manually make
     * the raw requests to the server.
     */
    val underlyingRepository: MockzillaManagementRepository
    val updateService: UpdateService
    val metaDataService: MetaDataService
    val logsService: LogsService

    interface UpdateService {
        fun setShouldFail(connection: MockzillaConnectionConfig, key: String, shouldFail: Boolean): Result<Unit>
        fun setDelay(connection: MockzillaConnectionConfig, key: String, delay: Duration?): Result<Unit>

        fun setHeaders(connection: MockzillaConnectionConfig, key: String, header: Map<String, String>): Result<Unit>
        fun setDefaultBody(connection: MockzillaConnectionConfig, key: String, body: String): Result<Unit>
        fun setDefaultStatus(connection: MockzillaConnectionConfig, key: String, statusCode: HttpStatusCode): Result<Unit>
        fun setErrorBody(connection: MockzillaConnectionConfig, key: String, body: String): Result<Unit>
        fun setErrorStatus(connection: MockzillaConnectionConfig, key: String, statusCode: HttpStatusCode): Result<Unit>
    }

    interface MetaDataService {
        suspend fun fetchMetaData(connection: MockzillaConnectionConfig): Result<MetaData>
    }

    interface LogsService {
        suspend fun fetchMonitorLogsAndClearBuffer(connection: MockzillaConnectionConfig): Result<MonitorLogsResponse>
    }

    private data class Instance(
        override val underlyingRepository: MockzillaManagementRepository,
        override val updateService: UpdateService,
        override val metaDataService: MetaDataService,
        override val logsService: LogsService
    ) : MockzillaManagement

    companion object {
        val instance: MockzillaManagement by lazy {
            val repo = MockzillaManagementRepositoryImpl.create()
            Instance(repo, UpdateServiceImpl(repo), repo, repo)
        }
    }
}
