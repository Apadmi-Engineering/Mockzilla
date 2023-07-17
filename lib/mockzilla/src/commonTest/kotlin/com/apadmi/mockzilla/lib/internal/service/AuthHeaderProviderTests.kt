package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.service.AuthHeaderProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class AuthHeaderProviderTests {
    @Test
    fun `DebugProvider - returns header`() = runTest {
        /* Setup */
        val sut = DebugAuthHeaderProvider

        /* Run Test */
        val result = sut.generateHeader()

        /* Verify */
        assertEquals(
            AuthHeaderProvider.Header(
                AuthenticationConstants.headerKey,
                "this-value-is-not-used-since-mockzilla-is-in-debug-mode"
            ), result
        )
    }

    @Test
    fun `ReleaseProvider - calls through`() = runTest {
        /* Setup */
        val dummyService = object : TokensService {
            override suspend fun getValidToken() = "dummy token"
            override suspend fun isTokenValid(tokenString: String) = false
        }
        val sut = ReleaseAuthHeaderProvider(tokensService = dummyService)

        /* Run Test */
        val result = sut.generateHeader()

        /* Verify */
        assertEquals(
            AuthHeaderProvider.Header(
                AuthenticationConstants.headerKey,
                "dummy token"
            ), result
        )
    }
}
