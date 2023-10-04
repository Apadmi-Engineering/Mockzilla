package com.apadmi.mockzilla.management

import com.apadmi.mockzilla.management.internal.ConnectionConfig
import com.apadmi.mockzilla.management.internal.MockzillaManagementImpl
import com.apadmi.mockzilla.management.internal.ktor.KtorClientProvider

interface MockzillaManagement {
    suspend fun isConnected(): Boolean
    suspend fun fetchMetaData()
    suspend fun fetchAllMockData()
    suspend fun postMockData()
    suspend fun fetchMonitorLogsAndClearBuffer()

    companion object {
        fun createConnection(
            ip: String,
            port: String
        ): MockzillaManagement = MockzillaManagementImpl(KtorClientProvider.createKtorClient(ConnectionConfig(ip, port)))
    }
}
