package com.apadmi.mockzilla.management

import com.apadmi.mockzilla.lib.internal.models.MockDataEntryDto
import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.internal.MockzillaManagementImpl
import com.apadmi.mockzilla.management.internal.ktor.KtorClientProvider
import com.apadmi.mockzilla.management.internal.ktor.KtorRequestRunner
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger

interface MockzillaManagement {
    suspend fun fetchMetaData(connection: ConnectionConfig): Result<MetaData>
    suspend fun fetchAllMockData(connection: ConnectionConfig): Result<List<MockDataEntryDto>>
    suspend fun updateMockDataEntry(entry: MockDataEntryDto, connection: ConnectionConfig): Result<Unit>
    suspend fun fetchMonitorLogsAndClearBuffer(connection: ConnectionConfig): Result<MonitorLogsResponse>

    /**
     * Defines the info needed to create a connection to a device. (i.e. make a request)
     */
    interface ConnectionConfig {
        val ip: String
        val port: String
    }

    companion object {
        internal fun create(logger: Logger): MockzillaManagement = MockzillaManagementImpl(
            KtorRequestRunner(KtorClientProvider.createKtorClient(logger = logger))
        )

        fun create(): MockzillaManagement = create(Logger.DEFAULT)
    }
}
