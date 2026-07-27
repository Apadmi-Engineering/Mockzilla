package com.apadmi.mockzilla.lib.internal.models

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import kotlinx.serialization.Serializable

/**
 * Payload for applying a preset by name rather than supplying individual properties
 *
 * @property presetName
 */
@InternalMockzillaApi
@Serializable
public data class ApplyPresetRequestDto(
    val presetName: String
)
