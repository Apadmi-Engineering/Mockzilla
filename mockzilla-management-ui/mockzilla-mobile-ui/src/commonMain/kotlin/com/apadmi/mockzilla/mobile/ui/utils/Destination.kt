@file:NoKDoc

package com.apadmi.mockzilla.mobile.ui.utils

import com.apadmi.mockzilla.lib.NoKDoc

import kotlinx.serialization.Serializable

@Serializable
internal sealed class Destination {
    @Serializable
    internal data object EndpointList : Destination()

    @Serializable
    internal data object GlobalControls : Destination()

    @Serializable
    internal data object Debug : Destination()

    @Serializable
    internal data object MetaData : Destination()

    @Serializable
    internal data class CreateEditPreset(val key: String, val creatingNewPreset: Boolean) : Destination()

    @Serializable
    internal data class EndpointDetails(val key: String?) : Destination()
}
