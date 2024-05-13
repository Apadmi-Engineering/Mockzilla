package com.apadmi.mockzilla.management

import com.apadmi.mockzilla.lib.internal.models.MockDataEntryDto
import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.internal.MockzillaManagementRepository
import com.apadmi.mockzilla.management.internal.MockzillaManagementRepositoryImpl
import com.apadmi.mockzilla.management.internal.ktor.KtorClientProvider
import com.apadmi.mockzilla.management.internal.ktor.KtorRequestRunner
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger
import kotlin.time.Duration

interface MockzillaManagement {

    /**
     * In cases where the wrapper isn't  granular enough this gives access to handle manually make
     * the raw requests to the server.
     */
    val underlyingRepository: MockzillaManagementRepository

    suspend fun fetchMetaData(connection: MockzillaConnectionConfig): Result<MetaData>
    suspend fun fetchMonitorLogsAndClearBuffer(connection: MockzillaConnectionConfig): Result<MonitorLogsResponse>

    fun setShouldFail(connection: MockzillaConnectionConfig, shouldFail: Boolean)
    fun setDelay(connection: MockzillaConnectionConfig, delay: Duration?)
    
    // TODO in next PR: fill out remaining methods

}
