@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.service.AuthHeaderProvider
import com.apadmi.mockzilla.lib.service.AuthHeaderProvider.*

internal object DebugAuthHeaderProvider : AuthHeaderProvider {
    override suspend fun generateHeader(): Header = Header(
        AuthenticationConstants.headerKey,
        "this-value-is-not-used-since-mockzilla-is-in-debug-mode"
    )
}

internal class ReleaseAuthHeaderProvider internal constructor(
    private val tokensService: TokensService,
) : AuthHeaderProvider {
    override suspend fun generateHeader(): Header = Header(
        AuthenticationConstants.headerKey,
        tokensService.getValidToken()
    )
}

internal object AuthenticationConstants {
    const val headerKey = "Mockzilla-Authorization"
}
