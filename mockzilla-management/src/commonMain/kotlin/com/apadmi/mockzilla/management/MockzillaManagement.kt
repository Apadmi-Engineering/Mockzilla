package com.apadmi.mockzilla.management

import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.internal.MockzillaManagementRepository
import com.apadmi.mockzilla.management.internal.MockzillaManagementRepositoryImpl
import com.apadmi.mockzilla.management.internal.service.UpdateServiceImpl
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
        fun setShouldFail(connection: MockzillaConnectionConfig, shouldFail: Boolean)
        fun setDelay(connection: MockzillaConnectionConfig, delay: Duration?)

        // TODO: Fill this out
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
