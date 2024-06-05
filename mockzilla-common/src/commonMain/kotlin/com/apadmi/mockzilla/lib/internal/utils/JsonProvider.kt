package com.apadmi.mockzilla.lib.internal.utils

import kotlinx.serialization.json.Json

object JsonProvider {
    val json = Json {
        ignoreUnknownKeys = true
    }
}
