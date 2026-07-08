package com.apadmi.mockzilla.desktop.engine.licenses

import com.apadmi.mockzilla.lib.internal.utils.JsonProvider

internal interface LicensesUseCase {
    suspend fun getLicenses(): Result<List<LibraryForAttribution>>
}

internal class LicensesUseCaseImpl(
    private val jsonProvider: suspend () -> String
) : LicensesUseCase {
    override suspend fun getLicenses() = runCatching {
        JsonProvider
            .json
            .decodeFromString<AboutLibrariesRootDto>(jsonProvider())
            .toDisplayModels()
    }
}

private fun AboutLibrariesRootDto.toDisplayModels() = libraries
    .sortedBy { it.name.lowercase() }
    .map { lib ->
        LibraryForAttribution(
            name = lib.name,
            version = lib.artifactVersion,
            licenses = lib.licenses.mapNotNull { id ->
                licenses[id]?.let { lic ->
                    LicenseDisplayModel(
                        name = lic.name,
                        url = lic.url,
                        spdxId = lic.spdxId,
                        content = lic.content
                    )
                }
            }
        )
    }
