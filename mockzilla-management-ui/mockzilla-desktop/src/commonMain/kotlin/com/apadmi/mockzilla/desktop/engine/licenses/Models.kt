@file:NoKDoc

package com.apadmi.mockzilla.desktop.engine.licenses

import com.apadmi.mockzilla.lib.NoKDoc

import java.util.Collections.emptyList

import kotlinx.serialization.Serializable

@Serializable
internal data class AboutLibrariesRootDto(
    val libraries: List<Library>,
    val licenses: Map<String, License>
) {
    @Serializable
    data class Library(
        val uniqueId: String,
        val name: String,
        val artifactVersion: String? = null,
        val licenses: List<String> = emptyList()
    )

    @Serializable
    data class License(
        val name: String,
        val content: String? = null,
        val url: String? = null,
        val spdxId: String? = null
    )
}

internal data class LibraryForAttribution(
    val name: String,
    val version: String?,
    val licenses: List<LicenseDisplayModel>
)

internal data class LicenseDisplayModel(
    val name: String,
    val url: String?,
    val spdxId: String?,
    val content: String?
)
