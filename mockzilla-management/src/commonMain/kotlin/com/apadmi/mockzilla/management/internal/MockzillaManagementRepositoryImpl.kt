package com.apadmi.mockzilla.management.internal

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.lib.internal.models.ApplyPresetRequestDto
import com.apadmi.mockzilla.lib.internal.models.ClearCachesRequestDto
import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.models.MockDataResponseDto
import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfigPatchRequestDto
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointPatchItemDto
import com.apadmi.mockzilla.lib.internal.utils.multiPlatformIo
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.MockzillaConnectionConfig
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.management.internal.ktor.CustomHeaders
import com.apadmi.mockzilla.management.internal.ktor.KtorClientProvider
import com.apadmi.mockzilla.management.internal.ktor.KtorRequestRunner
import com.apadmi.mockzilla.management.internal.ktor.delete
import com.apadmi.mockzilla.management.internal.ktor.get
import com.apadmi.mockzilla.management.internal.ktor.patch
import com.apadmi.mockzilla.management.internal.ktor.put

import co.touchlab.kermit.Logger
import io.ktor.client.call.body
import io.ktor.client.plugins.logging.Logger as KtorLogger
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.isSuccess

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException

@InternalMockzillaApi
public interface MockzillaManagementRepository {
    public suspend fun fetchMetaData(connection: MockzillaConnectionConfig, hideFromLogs: Boolean): Result<MetaData>
    public suspend fun fetchAllEndpointConfigs(connection: MockzillaConnectionConfig): Result<List<SerializableEndpointConfig>>
    public suspend fun updateMockDataEntry(
        entry: SerializableEndpointPatchItemDto,
        connection: MockzillaConnectionConfig
    ): Result<Unit>

    public suspend fun updateMockDataEntries(
        entries: List<SerializableEndpointPatchItemDto>,
        connection: MockzillaConnectionConfig
    ): Result<Unit>

    public suspend fun applyPresetByName(
        connection: MockzillaConnectionConfig,
        key: EndpointConfiguration.Key,
        presetName: String
    ): Result<SerializableEndpointConfig>

    public suspend fun fetchMonitorLogsAndClearBuffer(connection: MockzillaConnectionConfig, hideFromLogs: Boolean): Result<MonitorLogsResponse>
    public suspend fun fetchMonitorLogsSince(
        connection: MockzillaConnectionConfig,
        since: Long?,
        clientSessionStart: Long
    ): Result<MonitorLogsResponse>
    public suspend fun fetchFullBodyLogDetail(connection: MockzillaConnectionConfig, logId: String): Result<LogEvent>
    public suspend fun deleteMonitorLogs(connection: MockzillaConnectionConfig): Result<Unit>
    public suspend fun clearAllCaches(connection: MockzillaConnectionConfig): Result<Unit>
    public suspend fun clearCaches(connection: MockzillaConnectionConfig, keys: List<EndpointConfiguration.Key>): Result<Unit>
}

/**
 * @property runner The KTOR wrapper that actually runs requests
 */
internal class MockzillaManagementRepositoryImpl(
    val runner: KtorRequestRunner
) : MockzillaManagementRepository,
MockzillaManagement.LogsService,
MockzillaManagement.MetaDataService,
MockzillaManagement.EndpointsService,
MockzillaManagement.CacheClearingService,
MockzillaManagement.AppIconService {
    override suspend fun fetchMetaData(
        connection: MockzillaConnectionConfig,
        hideFromLogs: Boolean
    ) = runner<MetaData> {
        get(connection, "/api/meta") {
            header(CustomHeaders.HideFromLogs, hideFromLogs)
        }
    }.apply {
        if (!hideFromLogs) {
            alsoLogFailure("/api/meta")
        }
    }

    override suspend fun fetchAllEndpointConfigs(
        connection: MockzillaConnectionConfig
    ) = runner<MockDataResponseDto> {
        get(connection, "/api/mock-data")
    }.alsoLogFailure("/api/mock-data").map { it.entries }

    override suspend fun fetchDashboardOptionsConfig(
        connection: MockzillaConnectionConfig,
        key: EndpointConfiguration.Key
    ) = runner<DashboardOptionsConfig> {
        get(connection, "/api/mock-data") {
            url {
                appendPathSegments(key.raw, "dashboard-config")
            }
        }
    }.alsoLogFailure("/api/mock-data/{key}/dashboard-config")

    override suspend fun updateMockDataEntry(
        entry: SerializableEndpointPatchItemDto,
        connection: MockzillaConnectionConfig,
    ) = updateMockDataEntries(listOf(entry), connection)

    override suspend fun applyPresetByName(
        connection: MockzillaConnectionConfig,
        key: EndpointConfiguration.Key,
        presetName: String
    ) = runner<SerializableEndpointConfig> {
        put(connection, "/api/mock-data") {
            url {
                appendPathSegments(key.raw)
            }
            contentType(ContentType.Application.Json)
            setBody(ApplyPresetRequestDto(presetName))
        }
    }.alsoLogFailure("/api/mock-data/{key}")

    override suspend fun updateMockDataEntries(
        entries: List<SerializableEndpointPatchItemDto>,
        connection: MockzillaConnectionConfig
    ) = runner<Unit> {
        patch(connection, "/api/mock-data") {
            contentType(ContentType.Application.Json)
            setBody(SerializableEndpointConfigPatchRequestDto(entries))
        }
    }.alsoLogFailure("/api/mock-data")

    override suspend fun fetchMonitorLogsAndClearBuffer(
        connection: MockzillaConnectionConfig,
        hideFromLogs: Boolean
    ) = runner<MonitorLogsResponse> {
        get(connection, "/api/monitor-logs") {
            header(CustomHeaders.HideFromLogs, hideFromLogs)
        }
    }.alsoLogFailure("/api/monitor-logs")

    override suspend fun fetchMonitorLogsSince(
        connection: MockzillaConnectionConfig,
        since: Long?,
        clientSessionStart: Long,
    ) = runner<MonitorLogsResponse> {
        get(connection, "/api/monitor-logs/poll") {
            since?.let { parameter("since", it) }
            parameter("clientSessionStart", clientSessionStart)
            header(CustomHeaders.HideFromLogs, true)
        }
    }.alsoLogFailure("/api/monitor-logs/poll")

    override suspend fun fetchFullBodyLogDetail(
        connection: MockzillaConnectionConfig,
        logId: String,
    ) = runner<LogEvent> {
        get(connection, "/api/monitor-logs/$logId/full-body")
    }.alsoLogFailure("/api/monitor-logs/$logId/full-body")

    override suspend fun deleteMonitorLogs(
        connection: MockzillaConnectionConfig,
    ) = runner<Unit> {
        delete(connection, "/api/monitor-logs")
    }.alsoLogFailure("DELETE /api/monitor-logs")

    override suspend fun clearAllCaches(
        connection: MockzillaConnectionConfig
    ) = runner<Unit> {
        delete(connection, "/api/mock-data/all")
    }.alsoLogFailure("/api/mock-data/all")

    override suspend fun clearCaches(
        connection: MockzillaConnectionConfig,
        keys: List<EndpointConfiguration.Key>
    ) = runner<Unit> {
        delete(connection, "/api/mock-data") {
            contentType(ContentType.Application.Json)
            setBody(ClearCachesRequestDto(keys))
        }
    }.alsoLogFailure("/api/mock-data")

    override suspend fun fetchAppIcon(
        connection: MockzillaConnectionConfig
    ): Result<ByteArray?> = withContext(Dispatchers.multiPlatformIo) {
        runCatching {
            val response = runner.client.get(connection, "/api/app-icon")
            when {
                response.status.isSuccess() -> response.body<ByteArray>()
                response.status == HttpStatusCode.NotFound -> null
                else -> throw Exception("Failed fetching app icon (${response.status})")
            }
        }.alsoLogFailure("/api/app-icon")
    }

    fun <T> Result<T>.alsoLogFailure(path: String): Result<T> = onFailure {
        if (it is IOException) {
            Logger.v(tag = "Management") { "Disconnected: $path" }
        } else {
            Logger.v(tag = "Management", throwable = it) { "Request Failed: $path" }
        }
    }

    companion object {
        internal fun create(config: MockzillaManagement.Config, logger: KtorLogger) = MockzillaManagementRepositoryImpl(
            KtorRequestRunner(KtorClientProvider.createKtorClient(
                disableProxy = config.disableProxy,
                logger = logger
            ))
        )

        fun create(config: MockzillaManagement.Config) = create(config, KtorLogger.SIMPLE)
    }
}
