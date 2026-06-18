@file:Suppress("FILE_NAME_MATCH_CLASS")
@file:OptIn(ExperimentalSerializationApi::class)

package com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.lib.models.PartialMockzillaHttpResponse
import com.apadmi.mockzilla.ui.ui.common.components.editor.EditorMode
import io.ktor.http.HttpHeaders
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char
import kotlinx.datetime.offsetAt
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

internal const val BYTES_PER_KB = 1024
internal const val TENTHS_FACTOR = 10
internal const val ALPHA_MUTED = 0.5f
private const val MILLISECONDS_FRACTION_DIGITS = 3

private val timestampFormat = DateTimeComponents.Format {
    hour()
    char(':')
    minute()
    char(':')
    second()
    char('.')
    secondFraction(MILLISECONDS_FRACTION_DIGITS)
}

private val jsonPrettyPrinter = Json {
    prettyPrint = true
    prettyPrintIndent = "  "
}

internal fun String.toKbLabel(): String {
    val tenths = encodeToByteArray().size * TENTHS_FACTOR / BYTES_PER_KB
    return "${tenths / TENTHS_FACTOR}.${tenths % TENTHS_FACTOR} KB"
}

internal fun String.prettyPrintJson(): String = runCatching {
    jsonPrettyPrinter.encodeToString(JsonElement.serializer(), Json.parseToJsonElement(this))
}.getOrNull() ?: this

internal fun String.minifyJson(): String = runCatching {
    Json.encodeToString(JsonElement.serializer(), Json.parseToJsonElement(this))
}.getOrNull() ?: this

internal fun formatTimestamp(timestamp: Long): String {
    val instant = Instant.fromEpochMilliseconds(timestamp)
    return instant.format(timestampFormat, TimeZone.currentSystemDefault().offsetAt(instant))
}

internal val LogEvent.responseTypeFormat: EditorMode
    get() = responseHeaders[HttpHeaders.ContentType]?.lowercase().contentTypeToEditorMode()

internal val LogEvent.requestTypeFormat: EditorMode
    get() = requestHeaders[HttpHeaders.ContentType]?.lowercase().contentTypeToEditorMode()

internal val PartialMockzillaHttpResponse.typeFormat: EditorMode
    get() = headers?.get(HttpHeaders.ContentType)?.lowercase().contentTypeToEditorMode()

private fun String?.contentTypeToEditorMode() = when {
    this?.contains("json") == true -> EditorMode.Json
    this?.contains("html") == true -> EditorMode.Html
    else -> EditorMode.PlainText
}
