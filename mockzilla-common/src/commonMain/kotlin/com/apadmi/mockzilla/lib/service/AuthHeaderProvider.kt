package com.apadmi.mockzilla.lib.service

/**
 * Generates the authentication header required to make requests to a running Mockzilla server.
 * An instance pre-configured for the running server is available via
 * [com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams.authHeaderProvider].
 */
interface AuthHeaderProvider {
    /**
     * Generates a fresh authentication header. Each invocation may produce a new token value.
     *
     * @return The header key and value to include in requests to the Mockzilla server.
     */
    suspend fun generateHeader(): Header

    /**
     * An HTTP header represented as a key-value pair.
     *
     * @property key The header field name.
     * @property value The header field value.
     */
    data class Header(val key: String, val value: String)
}
