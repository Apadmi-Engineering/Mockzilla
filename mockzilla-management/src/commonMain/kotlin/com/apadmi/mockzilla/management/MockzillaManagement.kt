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
public interface MockzillaManagement {
    /**
     * Provides direct access to the underlying HTTP repository for cases where the higher-level
     * services do not cover a required operation. Prefer using the typed service properties where
     * possible.
     *
     * Note: requires `@OptIn(InternalMockzillaApi::class)` since [MockzillaManagementRepository]
     * is an internal type.
     */
    public val underlyingRepository: MockzillaManagementRepository

    /**
     * Service for modifying endpoint behaviour on a connected device at runtime.
     */
    public val updateService: UpdateService

    /**
     * Service for fetching device and application metadata from a connected device.
     */
    public val metaDataService: MetaDataService

    /**
     * Service for fetching monitor logs from a connected device.
     */
    public val logsService: LogsService

    /**
     * Service for clearing endpoint response caches on a connected device.
     */
    public val cacheClearingService: CacheClearingService

    /**
     * Service for querying endpoint configurations from a connected device.
     */
    public val endpointsService: EndpointsService
    public val appIconService: AppIconService

    /**
     * Clears endpoint response caches on a connected device. Clearing a cache causes the next
     * request to that endpoint to return a fresh response rather than a cached one.
     */
    public interface CacheClearingService {
        /**
         * Clears the response cache for every endpoint on the device at [connection].
         *
         * @param connection The device to target.
         * @return [Result.success] on success, [Result.failure] if the request could not be completed.
         */
        public suspend fun clearAllCaches(connection: MockzillaConnectionConfig): Result<Unit>

        /**
         * Clears the response cache for the specified endpoints on the device at [connection].
         *
         * @param connection The device to target.
         * @param keys The keys of the endpoints whose caches should be cleared.
         * @return [Result.success] on success, [Result.failure] if the request could not be completed.
         */
        public suspend fun clearCaches(
            connection: MockzillaConnectionConfig,
            keys: List<EndpointConfiguration.Key>
        ): Result<Unit>
    }

    /**
     * Queries endpoint configurations from a connected device.
     */
    public interface EndpointsService {
        /**
         * Fetches the current configuration for all endpoints registered on the device at [connection].
         *
         * @param connection The device to target.
         * @return [Result.success] wrapping the list of endpoint configs, or [Result.failure] if the
         * request could not be completed.
         */
        public suspend fun fetchAllEndpointConfigs(connection: MockzillaConnectionConfig): Result<List<SerializableEndpointConfig>>

        /**
         * Fetches the dashboard options configuration for the endpoint identified by [key] on the
         * device at [connection].
         *
         * @param connection The device to target.
         * @param key The key of the endpoint to query.
         * @return [Result.success] wrapping the dashboard options config, or [Result.failure] if the
         * request could not be completed.
         */
        public suspend fun fetchDashboardOptionsConfig(
            connection: MockzillaConnectionConfig,
            key: EndpointConfiguration.Key
        ): Result<DashboardOptionsConfig>
    }

    /**
     * Modifies endpoint behaviour on a connected device at runtime. Changes take effect immediately
     * and are applied on top of the endpoint's static configuration — passing `null` for any value
     * resets it to the endpoint's configured default.
     */
    public interface UpdateService {
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
        public suspend fun setShouldFail(
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
        public suspend fun setDelay(
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
        public suspend fun applyPreset(
            connection: MockzillaConnectionConfig,
            key: EndpointConfiguration.Key,
            dashboardOverridePreset: DashboardOverridePreset
        ): Result<Unit>

        /**
         * Applies a code-defined dashboard preset to the endpoint identified by [key] on the
         * device at [connection] by looking it up by [presetName], overriding the endpoint's
         * response until the override is cleared.
         *
         * @param connection The device to target.
         * @param key The key of the endpoint to update.
         * @param presetName The name of the preset to apply, as configured via
         * [EndpointConfiguration.Builder.configureDashboardOverrides].
         * @return [Result.success] on success, [Result.failure] if the request could not be
         * completed or no endpoint/preset matches [key]/[presetName].
         */
        public suspend fun applyPresetByName(
            connection: MockzillaConnectionConfig,
            key: EndpointConfiguration.Key,
            presetName: String
        ): Result<Unit>
    }

    /**
     * Fetches device and application metadata from a connected device.
     */
    public interface MetaDataService {
        /**
         * Fetches the device and application metadata from the device at [connection].
         *
         * @param connection The device to target.
         * @param hideFromLogs When `true`, suppresses console logging for this call. Useful for
         * frequently-polled calls to avoid cluttering the console output.
         * @return [Result.success] wrapping the device metadata, or [Result.failure] if the
         * request could not be completed.
         */
        public suspend fun fetchMetaData(
            connection: MockzillaConnectionConfig,
            hideFromLogs: Boolean
        ): Result<MetaData>
    }

    /**
     * Fetches monitor logs from a connected device.
     */
    public interface LogsService {
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
        @Deprecated("Please use `fetchMonitorLogsSince`")
        public suspend fun fetchMonitorLogsAndClearBuffer(
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
         * @param clientSessionStart A stable epoch-millisecond timestamp representing when the
         *   current management-UI session started. Pass the same value on every poll for the
         *   lifetime of the process. The server uses this to drive disk-cache cleanup: when it
         *   detects a new session (changed value), it deletes full-body files older than
         *   `min(oldest_in_memory_entry, clientSessionStart)`.
         * @return [Result.success] wrapping the log response, or [Result.failure] if the
         * request could not be completed.
         */
        public suspend fun fetchMonitorLogsSince(
            connection: MockzillaConnectionConfig,
            since: Long?,
            clientSessionStart: Long,
        ): Result<MonitorLogsResponse>

        /**
         * Fetches the complete log entry for [logId] directly from the device's disk cache,
         * including the full (un-truncated) request and response bodies.
         *
         * **When to call this:** Only call this for entries where [LogEvent.isRequestBodyTruncated]
         * or [LogEvent.isResponseBodyTruncated] is `true`. The server writes a disk record only
         * when at least one body exceeds the truncation threshold; for all other entries no disk
         * file exists and this method returns [Result.failure].
         *
         * **Behaviour contract:**
         * - Returns [Result.success] with the full [LogEvent] if a disk record exists.
         * - Returns [Result.failure] (HTTP 404) if the entry was never truncated, has been
         *   cleaned up by the session-start eviction policy, or has been explicitly deleted.
         * - Does **not** consult the in-memory ring buffer — the result is independent of
         *   whether the entry is still in memory, making it safe to call after an app restart.
         *
         * **Disk lifetime / cleanup:** The server retains disk records until one of two conditions
         * is met:
         * 1. The management UI reconnects (sends a new `clientSessionStart` timestamp via
         *    [fetchMonitorLogsSince]). The server then deletes records older than
         *    `min(oldest_in_memory_entry, clientSessionStart)`.
         * 2. No management UI connects within 60 seconds of server start — the server falls back
         *    to deleting records older than 2 days.
         *
         * This means entries from a previous app session remain accessible as long as the
         * management UI reconnects within the 2-day window and the session-start eviction
         * threshold hasn't passed them.
         *
         * @param connection The device to target.
         * @param logId The [LogEvent.id] of the entry to retrieve.
         * @return [Result.success] wrapping the full [LogEvent], or [Result.failure] if no disk
         *   record exists for this entry or the request could not be completed.
         */
        public suspend fun fetchFullBodyLogDetail(
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
        public suspend fun deleteMonitorLogs(connection: MockzillaConnectionConfig): Result<Unit>
    }

    /**
     * Configuration for a [MockzillaManagement] instance.
     *
     * @property disableProxy When `true`, management API calls bypass any system-level HTTP proxy
     * configured on the machine.
     */
    public data class Config(
        val disableProxy: Boolean = false
    )

    /**
     * Fetches the app icon from a connected device.
     */
    public interface AppIconService {
        /**
         * Fetches the app icon from the app at [connection] as a byte array
         *
         * @param connection The device to target.
         * @return [Result.success] wrapping the raw byts of the icon, or [Result.failure] if the
         * request could not be completed.
         */
        public suspend fun fetchAppIcon(connection: MockzillaConnectionConfig): Result<ByteArray?>
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

    public companion object {
        @Deprecated("This property is deprecated")
        public val instance: MockzillaManagement by lazy { constructInstance() }

        /**
         * Creates a new [MockzillaManagement] instance.
         *
         * @param config Configuration for this instance.
         * @return A fully initialised [MockzillaManagement] ready to connect to devices.
         */
        public fun constructInstance(config: Config = Config()): MockzillaManagement {
            val repo = MockzillaManagementRepositoryImpl.create(config)
            return Instance(repo, UpdateServiceImpl(repo), repo, repo, repo, repo, repo)
        }
    }
}
