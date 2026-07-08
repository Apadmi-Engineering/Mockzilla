package com.apadmi.mockzilla.lib.internal.models

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import kotlinx.serialization.Serializable

/**
 * @property keys
 */
@InternalMockzillaApi
@Serializable
public data class ClearCachesRequestDto(
    val keys: List<EndpointConfiguration.Key>
)
