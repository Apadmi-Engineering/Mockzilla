package com.apadmi.mockzilla.management.internal

import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.management.internal.ktor.KtorRequestRunner
import com.apadmi.mockzilla.management.internal.ktor.get

import co.touchlab.kermit.Logger

/**
 * @property runner
 */
internal class MockzillaManagementImpl(
    val runner: KtorRequestRunner
) : MockzillaManagement {
    override suspend fun fetchMetaData(connection: MockzillaManagement.ConnectionConfig): Result<MetaData> =
        runner<MetaData> {
            get(connection, "/api/meta")
        }.onFailure {
            Logger.v(tag = "Management", it) { "Request Failed: /api/meta" }
        }

    override suspend fun fetchAllMockData(connection: MockzillaManagement.ConnectionConfig) {
        TODO("Not yet implemented")
    }

    override suspend fun postMockData(connection: MockzillaManagement.ConnectionConfig) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchMonitorLogsAndClearBuffer(
        connection: MockzillaManagement.ConnectionConfig
    ): Result<MonitorLogsResponse> =
        runner<MonitorLogsResponse> {
            get(connection, "/api/monitor-logs")
        }.onFailure {
            Logger.v(tag = "Management", it) { "Request Failed: /api/monitor-logs" }
        }
}
