package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.internal.utils.generateUuid

import kotlin.time.Duration
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

internal interface TokensService {
    suspend fun getValidToken(): String
    suspend fun isTokenValid(tokenString: String): Boolean
}

internal class TokensServiceImpl(private val tokenLifeSpan: Duration) : TokensService {
    private val mutex = Mutex()
    private val tokens: MutableSet<Token> = mutableSetOf()

    /* Each request should use a fresh token */
    override suspend fun getValidToken(): String = newToken().also {
        mutex.withLock { tokens.add(it) }
    }.value

    override suspend fun isTokenValid(tokenString: String): Boolean {
        val token = tokens.firstOrNull { it.value == tokenString } ?: return false
        mutex.withLock { tokens.remove(token) }

        return token.expiry > Clock.System.now()
    }

    private fun newToken() = Token(
        Clock.System.now().plus(tokenLifeSpan),
        generateUuid()
    )
    /**
     * @property expiry
     * @property value
     */
    private data class Token(val expiry: Instant, val value: String)
}
