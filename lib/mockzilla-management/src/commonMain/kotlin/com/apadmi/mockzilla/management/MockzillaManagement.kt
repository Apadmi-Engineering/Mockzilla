package com.apadmi.mockzilla.management

import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.internal.MockzillaManagementImpl
import com.apadmi.mockzilla.management.internal.ktor.KtorClientProvider
import com.apadmi.mockzilla.management.internal.ktor.KtorRequestRunner

interface MockzillaManagement {
    /**
     * Defines the info needed to create a connection to a device. (i.e. make a request)
     * @property ip
     * @property port
     */
    data class ConnectionConfig(val ip: String, val port: String)

    suspend fun fetchMetaData(connection: ConnectionConfig): Result<MetaData>
    suspend fun fetchAllMockData(connection: ConnectionConfig)
    suspend fun postMockData(connection: ConnectionConfig)
    suspend fun fetchMonitorLogsAndClearBuffer(connection: ConnectionConfig)

    companion object {
        fun create(): MockzillaManagement = MockzillaManagementImpl(
            KtorRequestRunner(KtorClientProvider.createKtorClient())
        )
    }
}
