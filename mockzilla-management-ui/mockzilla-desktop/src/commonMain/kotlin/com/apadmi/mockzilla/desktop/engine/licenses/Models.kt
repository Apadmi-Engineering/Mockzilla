package com.apadmi.mockzilla.desktop.engine.licenses

import java.util.Collections.emptyList

import kotlinx.serialization.Serializable

/**
 * @property libraries
 * @property licenses
 */
@Serializable
internal data class AboutLibrariesRootDto(
    val libraries: List<Library>,
    val licenses: Map<String, License>
) {
    /**
     * @property uniqueId
     * @property name
     * @property artifactVersion
     * @property licenses
     */
    @Serializable
    data class Library(
        val uniqueId: String,
        val name: String,
        val artifactVersion: String? = null,
        val licenses: List<String> = emptyList()
    )

    /**
     * @property name
     * @property content
     * @property url
     * @property spdxId
     */
    @Serializable
    data class License(
        val name: String,
        val content: String? = null,
        val url: String? = null,
        val spdxId: String? = null
    )
}

/**
 * @property name
 * @property version
 * @property licenses
 */
internal data class LibraryForAttribution(
    val name: String,
    val version: String?,
    val licenses: List<LicenseDisplayModel>
)

/**
 * @property name
 * @property url
 * @property spdxId
 * @property content
 */
internal data class LicenseDisplayModel(
    val name: String,
    val url: String?,
    val spdxId: String?,
    val content: String?
)
