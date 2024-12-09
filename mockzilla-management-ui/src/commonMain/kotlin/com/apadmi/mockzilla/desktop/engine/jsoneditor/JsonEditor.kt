package com.apadmi.mockzilla.desktop.engine.jsoneditor

import kotlinx.serialization.json.Json

// Ideally we should have the Mockzilla app tell the management UI if trailing commas
// or comments are allowed for JSON responses so we can match the application's validation
private val jsonConfiguration = Json

class JsonEditor(
    private val body: String
) {
    fun isValidJson(): Boolean = try {
        jsonConfiguration.parseToJsonElement(body)
        true
    } catch (error: Throwable) {
        false
    }

    fun parseError(): String? {
        if (body.isBlank()) {
            return null
        }
        return try {
            jsonConfiguration.parseToJsonElement(body)
            null
        } catch (error: Throwable) {
            error.message
        }
    }
}
