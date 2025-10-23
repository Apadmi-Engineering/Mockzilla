package com.apadmi.mockzilla.mobile.ui.utils

import kotlinx.serialization.Serializable

@Serializable
internal sealed class Destination {
    @Serializable
    internal data object EndpointList : Destination()

    @Serializable
    internal data object GlobalControls : Destination()

    @Serializable
    internal data object Debug : Destination()

    /**
     * @property key
     * @property creatingNewPreset
     */
    @Serializable
    internal data class CreateEditPreset(val key: String, val creatingNewPreset: Boolean) : Destination()

    /**
     * @property key
     */
    @Serializable
    internal data class EndpointDetails(val key: String) : Destination()
}
