package com.apadmi.mockzilla.lib.internal.utils

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import kotlinx.serialization.json.Json

@InternalMockzillaApi
public object JsonProvider {
    public val json: Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }
}
