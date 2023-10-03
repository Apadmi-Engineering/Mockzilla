package com.apadmi.mockzilla.management.internal

import com.apadmi.mockzilla.management.MockzillaManagement

internal class MockzillaManagementImpl(
    val networkService: NetworkService
) : MockzillaManagement {
    override suspend fun isConnected(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun fetchMetaData() {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAllMockData() {
        TODO("Not yet implemented")
    }

    override suspend fun postMockData() {
        TODO("Not yet implemented")
    }

    override suspend fun fetchMonitorLogsAndClearBuffer() {
        TODO("Not yet implemented")
    }

}