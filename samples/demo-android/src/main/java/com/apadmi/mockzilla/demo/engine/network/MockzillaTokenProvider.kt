package com.apadmi.mockzilla.demo.engine.network

import com.apadmi.mockzilla.lib.service.AuthHeaderProvider

interface MockzillaTokenProvider {
    suspend fun getTokenHeader(): AuthHeaderProvider.Header
}
