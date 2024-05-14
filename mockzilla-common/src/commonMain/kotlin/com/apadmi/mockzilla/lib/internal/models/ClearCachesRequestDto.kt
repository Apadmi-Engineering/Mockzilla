package com.apadmi.mockzilla.lib.internal.models

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import kotlinx.serialization.Serializable

/**
 * @property keys
 */
@Serializable
data class ClearCachesRequestDto(
    val keys: List<EndpointConfiguration.Key>
)
