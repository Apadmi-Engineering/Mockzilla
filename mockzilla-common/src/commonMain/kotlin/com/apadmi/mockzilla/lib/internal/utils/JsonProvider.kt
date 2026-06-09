package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import kotlinx.serialization.json.Json

@InternalMockzillaApi
object JsonProvider {
    val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }
}
