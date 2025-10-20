package com.apadmi.mockzilla.management.internal.service

import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointPatchItemDto
import com.apadmi.mockzilla.lib.internal.models.SetOrDont
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.management.MockzillaConnectionConfig
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.management.internal.MockzillaManagementRepository

internal class UpdateServiceImpl(
    private val repo: MockzillaManagementRepository
) : MockzillaManagement.UpdateService {
    override suspend fun setShouldFail(
        connection: MockzillaConnectionConfig,
        keys: Collection<EndpointConfiguration.Key>,
        shouldFail: Boolean?
    ) = repo.updateMockDataEntries(
        keys.map { key ->
            SerializableEndpointPatchItemDto(
                key = key,
                shouldFail = SetOrDont.Set(shouldFail)
            )
        }, connection
    )

    override suspend fun setDelay(
        connection: MockzillaConnectionConfig,
        keys: Collection<EndpointConfiguration.Key>,
        delayMs: Int?
    ) = repo.updateMockDataEntries(
        keys.map { key ->
            SerializableEndpointPatchItemDto(
                key = key,
                delayMs = SetOrDont.Set(delayMs)
            )
        }, connection
    )

    override suspend fun applyPreset(
        connection: MockzillaConnectionConfig,
        key: EndpointConfiguration.Key,
        dashboardOverridePreset: DashboardOverridePreset
    ) = repo.updateMockDataEntry(
        SerializableEndpointPatchItemDto(
            key = key,
            defaultBody = SetOrDont.Set(dashboardOverridePreset.response.body),
            defaultStatus = SetOrDont.Set(dashboardOverridePreset.response.statusCode),
            defaultHeaders = SetOrDont.Set(dashboardOverridePreset.response.headers),
            appliedPresetOverride = SetOrDont.Set(dashboardOverridePreset),
        ), connection
    )
}
