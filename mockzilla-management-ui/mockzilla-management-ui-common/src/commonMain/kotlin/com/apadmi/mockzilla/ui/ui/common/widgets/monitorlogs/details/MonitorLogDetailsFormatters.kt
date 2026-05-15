@file:Suppress("COMPLEX_EXPRESSION", "FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

import kotlin.time.Instant

internal const val BYTES_PER_KB = 1024
internal const val TENTHS_FACTOR = 10
internal const val ALPHA_MUTED = 0.5f

/**
 * @property keyColor Color for JSON object keys.
 * @property stringColor Color for JSON string values.
 * @property numberColor Color for JSON numeric values.
 * @property boolColor Color for JSON boolean values.
 * @property nullColor Color for JSON null values.
 */
internal data class JsonHighlightColors(
    val keyColor: Color,
    val stringColor: Color,
    val numberColor: Color,
    val boolColor: Color,
    val nullColor: Color,
)

internal fun String.toKbLabel(): String {
    val tenths = encodeToByteArray().size * TENTHS_FACTOR / BYTES_PER_KB
    return "${tenths / TENTHS_FACTOR}.${tenths % TENTHS_FACTOR} KB"
}

@Suppress("TOO_LONG_FUNCTION", "NESTED_BLOCK")
internal fun String.prettyPrintJson(): String {
    val trimmed = trim()
    if (!trimmed.startsWith('{') && !trimmed.startsWith('[')) {
        return this
    }
    val sb = StringBuilder()
    var indent = 0
    val indentStr = "  "
    var idx = 0
    while (idx < trimmed.length) {
        when (trimmed[idx]) {
            '{', '[' -> {
                sb.append(trimmed[idx])
                indent++
                sb.append('\n')
                repeat(indent) { sb.append(indentStr) }
            }
            '}', ']' -> {
                indent--
                sb.append('\n')
                repeat(indent) { sb.append(indentStr) }
                sb.append(trimmed[idx])
            }
            ',' -> {
                sb.append(',')
                sb.append('\n')
                repeat(indent) { sb.append(indentStr) }
            }
            ':' -> sb.append(": ")
            '"' -> {
                sb.append('"')
                idx++
                while (idx < trimmed.length) {
                    val ch = trimmed[idx]
                    if (ch == '\\' && idx + 1 < trimmed.length) {
                        sb.append(ch)
                        idx++
                        sb.append(trimmed[idx])
                    } else if (ch == '"') {
                        sb.append(ch)
                        break
                    } else {
                        sb.append(ch)
                    }
                    idx++
                }
            }
            ' ', '\t', '\r', '\n' -> Unit
            else -> sb.append(trimmed[idx])
        }
        idx++
    }
    return sb.toString()
}

internal fun urlToTitle(url: String): String = url
    .trimEnd('/')
    .substringAfterLast('/')
    .substringBefore('?')
    .replaceFirstChar { it.uppercaseChar() }
    .ifBlank { url }

internal fun formatTimestamp(timestamp: Long): String {
    val raw = Instant.fromEpochMilliseconds(timestamp)
        .toString()
        .substringAfterLast('T')
        .removeSuffix("Z")
    return if ('.' in raw) {
        raw.substringBefore('.') + "." + raw.substringAfter('.').take(3)
    } else {
        raw
    }
}

@Suppress("IDENTIFIER_LENGTH", "TOO_LONG_FUNCTION")
internal fun buildJsonAnnotatedString(text: String, colors: JsonHighlightColors): AnnotatedString {
    val pretty = text.prettyPrintJson()
    return buildAnnotatedString {
        if (pretty.isBlank() ||
                (!pretty.trimStart().startsWith('{') && !pretty.trimStart().startsWith('['))
        ) {
            append(pretty)
            return@buildAnnotatedString
        }
        var ci = 0
        while (ci < pretty.length) {
            when {
                pretty[ci] == '"' -> {
                    val start = ci++
                    while (ci < pretty.length) {
                        if (pretty[ci] == '\\') {
                            ci++
                        }
                        if (ci >= pretty.length) {
                            break
                        }
                        if (pretty[ci] == '"') {
                            ci++
                            break
                        }
                        ci++
                    }
                    val token = pretty.substring(start, ci)
                    var peek = ci
                    while (peek < pretty.length && pretty[peek] in " \t\r\n") {
                        peek++
                    }
                    val isKey = peek < pretty.length && pretty[peek] == ':'
                    withStyle(SpanStyle(color = if (isKey) colors.keyColor else colors.stringColor)) {
                        append(token)
                    }
                }
                pretty[ci].isDigit() || (pretty[ci] == '-' && ci + 1 < pretty.length && pretty[ci + 1].isDigit()) -> {
                    val start = ci
                    if (pretty[ci] == '-') {
                        ci++
                    }
                    while (ci < pretty.length && (pretty[ci].isDigit() || pretty[ci] == '.')) {
                        ci++
                    }
                    withStyle(SpanStyle(color = colors.numberColor)) { append(pretty.substring(start, ci)) }
                }
                ci + 4 <= pretty.length && pretty.substring(ci, ci + 4) == "true" -> {
                    withStyle(SpanStyle(color = colors.boolColor)) { append("true") }
                    ci += 4
                }
                ci + 5 <= pretty.length && pretty.substring(ci, ci + 5) == "false" -> {
                    withStyle(SpanStyle(color = colors.boolColor)) { append("false") }
                    ci += 5
                }
                ci + 4 <= pretty.length && pretty.substring(ci, ci + 4) == "null" -> {
                    withStyle(SpanStyle(color = colors.nullColor)) { append("null") }
                    ci += 4
                }
                else -> {
                    append(pretty[ci])
                    ci++
                }
            }
        }
    }
}
