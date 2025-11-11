package com.apadmi.mockzilla.lib.internal.service

import com.apadmi.mockzilla.lib.internal.utils.generateUuid

import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal interface TokensService {
    suspend fun getValidToken(): String
    suspend fun isTokenValid(tokenString: String): Boolean
}

internal class TokensServiceImpl @OptIn(ExperimentalTime::class) constructor(
    private val tokenLifeSpan: Duration,
    private val clock: Clock = Clock.System
) : TokensService {
    private val mutex = Mutex()
    private val tokens: MutableSet<Token> = mutableSetOf()

    /* Each request should use a fresh token */
    override suspend fun getValidToken(): String = newToken().also {
        mutex.withLock { tokens.add(it) }
    }.value

    @OptIn(ExperimentalTime::class)
    override suspend fun isTokenValid(tokenString: String): Boolean {
        val token = tokens.firstOrNull { it.value == tokenString } ?: return false
        mutex.withLock { tokens.remove(token) }

        return token.expiry > clock.now()
    }

    @OptIn(ExperimentalTime::class)
    private fun newToken() = Token(
        clock.now().plus(tokenLifeSpan),
        generateUuid()
    )
    /**
     * @property expiry
     * @property value
     */
    @OptIn(ExperimentalTime::class)
    private data class Token(val expiry: Instant, val value: String)
}
