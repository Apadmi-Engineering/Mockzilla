package com.apadmi.mockzilla.management.internal

import com.apadmi.mockzilla.lib.internal.models.MockDataEntryDto
import com.apadmi.mockzilla.lib.internal.models.MockDataResponseDto
import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.management.internal.ktor.KtorRequestRunner
import com.apadmi.mockzilla.management.internal.ktor.get
import com.apadmi.mockzilla.management.internal.ktor.post

import co.touchlab.kermit.Logger
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.appendEncodedPathSegments
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType

/**
 * @property runner
 */
internal class MockzillaManagementImpl(
    val runner: KtorRequestRunner
) : MockzillaManagement {
    override suspend fun fetchMetaData(
        connection: MockzillaManagement.ConnectionConfig
    ) = runner<MetaData> {
        get(connection, "/api/meta")
    }.onFailure {
        Logger.v(tag = "Management", it) { "Request Failed: /api/meta" }
    }

    override suspend fun fetchAllMockData(
        connection: MockzillaManagement.ConnectionConfig
    ) = runner<MockDataResponseDto> {
        get(connection, "/api/mock-data")
    }.onFailure {
        Logger.v(tag = "Management", it) { "Request Failed: /api/mock-data" }
    }.map { it.entries }

    override suspend fun updateMockDataEntry(
        entry: MockDataEntryDto,
        connection: MockzillaManagement.ConnectionConfig,
    ) = runner<Unit> {
        post(connection, "/api/mock-data") {
            url {
                appendPathSegments(entry.key)
            }
            contentType(ContentType.Application.Json)
            setBody(entry)
        }
    }.onFailure {
        Logger.v(tag = "Management", it) { "Request Failed: /api/monitor-logs" }
    }

    override suspend fun fetchMonitorLogsAndClearBuffer(
        connection: MockzillaManagement.ConnectionConfig
    ) = runner<MonitorLogsResponse> {
        get(connection, "/api/monitor-logs")
    }.onFailure {
        Logger.v(tag = "Management", it) { "Request Failed: /api/monitor-logs" }
    }
}
