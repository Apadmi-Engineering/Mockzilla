package com.apadmi.mockzilla.management.internal

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.management.internal.ktor.KtorRequestRunner
import com.apadmi.mockzilla.management.internal.ktor.get

/**
 * @property runner
 */
internal class MockzillaManagementImpl(
    val runner: KtorRequestRunner
) : MockzillaManagement {
    override suspend fun fetchMetaData(connection: MockzillaManagement.ConnectionConfig): Result<MetaData> = runner {
        get(connection, "/api/meta")
    }

    override suspend fun fetchAllMockData(connection: MockzillaManagement.ConnectionConfig) {
        TODO("Not yet implemented")
    }

    override suspend fun postMockData(connection: MockzillaManagement.ConnectionConfig) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchMonitorLogsAndClearBuffer(connection: MockzillaManagement.ConnectionConfig) {
        TODO("Not yet implemented")
    }
}
