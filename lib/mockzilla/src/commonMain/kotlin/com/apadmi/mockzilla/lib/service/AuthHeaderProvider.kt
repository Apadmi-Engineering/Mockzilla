package com.apadmi.mockzilla.lib.service

interface AuthHeaderProvider {
    suspend fun generateHeader(): Header
    /**
     * @property key
     * @property value
     */
    data class Header(val key: String, val value: String)
}
