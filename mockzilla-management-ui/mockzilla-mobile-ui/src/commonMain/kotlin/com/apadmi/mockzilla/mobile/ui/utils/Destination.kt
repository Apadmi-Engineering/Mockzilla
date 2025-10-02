package com.apadmi.mockzilla.mobile.ui.utils

import kotlinx.serialization.Serializable

@Serializable
sealed class Destination {
    @Serializable
    data object EndpointList : Destination()

    /**
     * @property key
     */
    @Serializable
    data class EndpointDetails(val key: String) : Destination()
}
