package com.apadmi.mockzilla.mobile.ui.utils

import kotlinx.serialization.Serializable

@Serializable
internal sealed class Destination {
    @Serializable
    internal data object EndpointList : Destination()

    /**
     * @property key
     */
    @Serializable
    internal data class EndpointDetails(val key: String) : Destination()
}
