package com.apadmi.mockzilla.management

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.internal.models.MonitorLogsResponse
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.lib.models.MetaData
import com.apadmi.mockzilla.management.internal.MockzillaManagementRepository
import com.apadmi.mockzilla.management.internal.MockzillaManagementRepositoryImpl
import com.apadmi.mockzilla.management.internal.service.UpdateServiceImpl

/**
 * A client for remotely manipulating a running Mockzilla server. Used by external tooling (such as
 * the Mockzilla management desktop app or automated tests) to inspect and control mock endpoint
 * behaviour at runtime without modifying application code.
 *
 * Create an instance via [constructInstance].
 */
interface MockzillaManagement {
    /**
     * Provides direct access to the underlying HTTP repository for cases where the higher-level
     * services do not cover a required operation. Prefer using the typed service properties where
     * possible.
     *
     * Note: requires `@OptIn(InternalMockzillaApi::class)` since [MockzillaManagementRepository]
     * is an internal type.
     */
    val underlyingRepository: MockzillaManagementRepository

    /**
     * Service for modifying endpoint behaviour on a connected device at runtime.
     */
    val updateService: UpdateService

    /**
     * Service for fetching device and application metadata from a connected device.
     */
    val metaDataService: MetaDataService

    /**
     * Service for fetching monitor logs from a connected device.
     */
    val logsService: LogsService

    /**
     * Service for clearing endpoint response caches on a connected device.
     */
    val cacheClearingService: CacheClearingService

    /**
     * Service for querying endpoint configurations from a connected device.
     */
    val endpointsService: EndpointsService
    val appIconService: AppIconService

    /**
     * Clears endpoint response caches on a connected device. Clearing a cache causes the next
     * request to that endpoint to return a fresh response rather than a cached one.
     */
    interface CacheClearingService {
        /**
         * Clears the response cache for every endpoint on the device at [connection].
         *
         * @param connection The device to target.
         * @return [Result.success] on success, [Result.failure] if the request could not be completed.
         */
        suspend fun clearAllCaches(connection: MockzillaConnectionConfig): Result<Unit>

        /**
         * Clears the response cache for the specified endpoints on the device at [connection].
         *
         * @param connection The device to target.
         * @param keys The keys of the endpoints whose caches should be cleared.
         * @return [Result.success] on success, [Result.failure] if the request could not be completed.
         */
        suspend fun clearCaches(
            connection: MockzillaConnectionConfig,
            keys: List<EndpointConfiguration.Key>
        ): Result<Unit>
    }

    /**
     * Queries endpoint configurations from a connected device.
     */
    interface EndpointsService {
        /**
         * Fetches the current configuration for all endpoints registered on the device at [connection].
         *
         * @param connection The device to target.
         * @return [Result.success] wrapping the list of endpoint configs, or [Result.failure] if the
         * request could not be completed.
         */
        suspend fun fetchAllEndpointConfigs(connection: MockzillaConnectionConfig): Result<List<SerializableEndpointConfig>>

        /**
         * Fetches the dashboard options configuration for the endpoint identified by [key] on the
         * device at [connection].
         *
         * @param connection The device to target.
         * @param key The key of the endpoint to query.
         * @return [Result.success] wrapping the dashboard options config, or [Result.failure] if the
         * request could not be completed.
         */
        suspend fun fetchDashboardOptionsConfig(
            connection: MockzillaConnectionConfig,
            key: EndpointConfiguration.Key
        ): Result<DashboardOptionsConfig>
    }

    /**
     * Modifies endpoint behaviour on a connected device at runtime. Changes take effect immediately
     * and are applied on top of the endpoint's static configuration — passing `null` for any value
     * resets it to the endpoint's configured default.
     */
    interface UpdateService {
        /**
         * Overrides whether the specified endpoints return error responses on the device at
         * [connection].
         *
         * @param connection The device to target.
         * @param keys The keys of the endpoints to update.
         * @param shouldFail `true` to force error responses, `false` to force success responses,
         * `null` to reset to each endpoint's configured default.
         * @return [Result.success] on success, [Result.failure] if the request could not be completed.
         */
        suspend fun setShouldFail(
            connection: MockzillaConnectionConfig,
            keys: Collection<EndpointConfiguration.Key>,
            shouldFail: Boolean?
        ): Result<Unit>

        /**
         * Overrides the response delay for the specified endpoints on the device at [connection].
         *
         * @param connection The device to target.
         * @param keys The keys of the endpoints to update.
         * @param delayMs The delay to apply in milliseconds, or `null` to reset to each endpoint's
         * configured default.
         * @return [Result.success] on success, [Result.failure] if the request could not be completed.
         */
        suspend fun setDelay(
            connection: MockzillaConnectionConfig,
            keys: Collection<EndpointConfiguration.Key>,
            delayMs: Int?
        ): Result<Unit>

        /**
         * Applies a dashboard preset to the endpoint identified by [key] on the device at
         * [connection], overriding the endpoint's response until the override is cleared.
         *
         * @param connection The device to target.
         * @param key The key of the endpoint to update.
         * @param dashboardOverridePreset The preset to apply.
         * @return [Result.success] on success, [Result.failure] if the request could not be completed.
         */
        suspend fun applyPreset(
            connection: MockzillaConnectionConfig,
            key: EndpointConfiguration.Key,
            dashboardOverridePreset: DashboardOverridePreset
        ): Result<Unit>
    }

    /**
     * Fetches device and application metadata from a connected device.
     */
    interface MetaDataService {
        /**
         * Fetches the device and application metadata from the device at [connection].
         *
         * @param connection The device to target.
         * @param hideFromLogs When `true`, suppresses console logging for this call. Useful for
         * frequently-polled calls to avoid cluttering the console output.
         * @return [Result.success] wrapping the device metadata, or [Result.failure] if the
         * request could not be completed.
         */
        suspend fun fetchMetaData(
            connection: MockzillaConnectionConfig,
            hideFromLogs: Boolean
        ): Result<MetaData>
    }

    /**
     * Fetches monitor logs from a connected device.
     */
    interface LogsService {
        /**
         * Fetches all buffered monitor logs from the device at [connection] and clears the buffer.
         * Subsequent calls will not return the same log entries.
         *
         * @param connection The device to target.
         * @param hideFromLogs When `true`, suppresses console logging for this call. Useful for
         * frequently-polled calls to avoid cluttering the console output.
         * @return [Result.success] wrapping the log response, or [Result.failure] if the
         * request could not be completed.
         */
        suspend fun fetchMonitorLogsAndClearBuffer(
            connection: MockzillaConnectionConfig,
            hideFromLogs: Boolean
        ): Result<MonitorLogsResponse>

        /**
         * Non-destructively polls log entries since the given timestamp. Safe to call repeatedly
         * without losing entries.
         *
         * @param connection The device to target.
         * @param since Only return entries with timestamp strictly after this value (epoch ms).
         *   Pass `null` to retrieve all buffered entries.
         * @return [Result.success] wrapping the log response, or [Result.failure] if the
         * request could not be completed.
         */
        suspend fun fetchMonitorLogsSince(
            connection: MockzillaConnectionConfig,
            since: Long?,
        ): Result<MonitorLogsResponse>

        /**
         * Fetches the full detail for a single log entry, including any body content that was
         * truncated in the list response.
         *
         * @param connection The device to target.
         * @param logId The [LogEvent.id] of the entry to retrieve.
         * @return [Result.success] wrapping the full [LogEvent], or [Result.failure] if the entry
         * no longer exists or the request could not be completed.
         */
        suspend fun fetchLogDetail(
            connection: MockzillaConnectionConfig,
            logId: String,
        ): Result<LogEvent>

        /**
         * Deletes all buffered log entries on the device at [connection] and clears disk-cached
         * body files.
         *
         * @param connection The device to target.
         * @return [Result.success] on success, [Result.failure] if the request could not be completed.
         */
        suspend fun deleteMonitorLogs(connection: MockzillaConnectionConfig): Result<Unit>
    }

    /**
     * Configuration for a [MockzillaManagement] instance.
     *
     * @property disableProxy When `true`, management API calls bypass any system-level HTTP proxy
     * configured on the machine.
     */
    data class Config(
        val disableProxy: Boolean = false
    )

    /**
     * Fetches the app icon from a connected device.
     */
    interface AppIconService {
        /**
         * Fetches the app icon from the app at [connection] as a byte array
         *
         * @param connection The device to target.
         * @return [Result.success] wrapping the raw byts of the icon, or [Result.failure] if the
         * request could not be completed.
         */
        suspend fun fetchAppIcon(connection: MockzillaConnectionConfig): Result<ByteArray?>
    }

    private data class Instance(
        override val underlyingRepository: MockzillaManagementRepository,
        override val updateService: UpdateService,
        override val metaDataService: MetaDataService,
        override val logsService: LogsService,
        override val cacheClearingService: CacheClearingService,
        override val endpointsService: EndpointsService,
        override val appIconService: AppIconService
    ) : MockzillaManagement

    companion object {
        @Deprecated("This property is deprecated")
        val instance: MockzillaManagement by lazy { constructInstance() }

        /**
         * Creates a new [MockzillaManagement] instance.
         *
         * @param config Configuration for this instance.
         * @return A fully initialised [MockzillaManagement] ready to connect to devices.
         */
        fun constructInstance(config: Config = Config()): MockzillaManagement {
            val repo = MockzillaManagementRepositoryImpl.create(config)
            return Instance(repo, UpdateServiceImpl(repo), repo, repo, repo, repo, repo)
        }
    }
}
