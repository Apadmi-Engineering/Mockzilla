package com.apadmi.mockzilla.lib.internal.models

import kotlinx.serialization.Serializable

/**
 * @property failProbability
 * @property delayMean
 * @property delayVariance
 */
@Serializable
data class GlobalOverridesDto(
    val failProbability: Int? = null,
    val delayMean: Int? = null,
    val delayVariance: Int? = null
)
