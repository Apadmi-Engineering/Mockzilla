@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

import kotlinx.datetime.Instant
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char

internal const val BYTES_PER_KB = 1024
internal const val TENTHS_FACTOR = 10
internal const val ALPHA_MUTED = 0.5f
private const val JSON_INDENT = "  "

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

internal fun urlToTitle(url: String): String = url
    .trimEnd('/')
    .substringAfterLast('/')
    .substringBefore('?')
    .replaceFirstChar { it.uppercaseChar() }
    .ifBlank { url }

private val TIMESTAMP_FORMAT = DateTimeComponents.Format {
    hour()
    char(':')
    minute()
    char(':')
    second()
    char('.')
    secondFraction(3)
}

internal fun formatTimestamp(timestamp: Long): String =
    Instant.fromEpochMilliseconds(timestamp).format(TIMESTAMP_FORMAT)

// ── JSON pretty-printer ───────────────────────────────────────────────────────

private fun StringBuilder.appendNewlineIndent(depth: Int) {
    append('\n')
    repeat(depth) { append(JSON_INDENT) }
}

/**
 * Reads a JSON string literal (including surrounding quotes and escape sequences)
 * from [source] starting at [openQuoteIdx], appending each character to [sink].
 *
 * @return The index of the closing `"` in [source], so the caller can advance past it.
 */
private fun appendJsonString(source: String, openQuoteIdx: Int, sink: StringBuilder): Int {
    sink.append('"')
    var idx = openQuoteIdx + 1
    while (idx < source.length) {
        val ch = source[idx]
        when {
            ch == '\\' && idx + 1 < source.length -> {
                sink.append(ch)
                idx++
                sink.append(source[idx])
            }
            ch == '"' -> {
                sink.append(ch)
                return idx
            }
            else -> sink.append(ch)
        }
        idx++
    }
    return idx - 1
}

internal fun String.prettyPrintJson(): String {
    val trimmed = trim()
    if (!trimmed.startsWith('{') && !trimmed.startsWith('[')) return this
    val sb = StringBuilder()
    var depth = 0
    var idx = 0
    while (idx < trimmed.length) {
        when (trimmed[idx]) {
            '{', '[' -> {
                sb.append(trimmed[idx])
                depth++
                sb.appendNewlineIndent(depth)
            }
            '}', ']' -> {
                depth--
                sb.appendNewlineIndent(depth)
                sb.append(trimmed[idx])
            }
            ',' -> {
                sb.append(',')
                sb.appendNewlineIndent(depth)
            }
            ':' -> sb.append(": ")
            '"' -> idx = appendJsonString(trimmed, idx, sb)
            ' ', '\t', '\r', '\n' -> Unit
            else -> sb.append(trimmed[idx])
        }
        idx++
    }
    return sb.toString()
}

// ── JSON syntax highlighter ───────────────────────────────────────────────────

/**
 * Reads a quoted JSON string token from [text] starting at [startIdx] (the opening `"`).
 *
 * @return The full token (including surrounding quotes) and the index immediately
 * after the closing `"`.
 */
private fun readStringToken(text: String, startIdx: Int): Pair<String, Int> {
    var idx = startIdx + 1
    while (idx < text.length) {
        when {
            text[idx] == '\\' -> idx++  // skip the escaped character
            text[idx] == '"' -> return text.substring(startIdx, idx + 1) to idx + 1
        }
        idx++
    }
    return text.substring(startIdx, idx) to idx
}

/**
 * Returns `true` if the first non-whitespace character at or after [fromIdx] is `:`,
 * meaning the token that preceded it is a JSON object key.
 */
private fun isFollowedByColon(text: String, fromIdx: Int): Boolean {
    var idx = fromIdx
    while (idx < text.length && text[idx] in " \t\r\n") idx++
    return idx < text.length && text[idx] == ':'
}

/**
 * Returns `true` if the character at [idx] in [text] is the start of a JSON number
 * (a digit, or a `-` followed by a digit).
 */
private fun isNumberStart(text: String, idx: Int): Boolean =
    text[idx].isDigit() || (text[idx] == '-' && idx + 1 < text.length && text[idx + 1].isDigit())

/**
 * Reads a JSON number token (optional `-`, digits, optional decimal part) from [text]
 * starting at [startIdx].
 *
 * @return The token string and the index immediately after the number.
 */
private fun readNumberToken(text: String, startIdx: Int): Pair<String, Int> {
    var idx = startIdx
    if (idx < text.length && text[idx] == '-') idx++
    while (idx < text.length && (text[idx].isDigit() || text[idx] == '.')) idx++
    return text.substring(startIdx, idx) to idx
}

internal fun buildJsonAnnotatedString(text: String, colors: JsonHighlightColors): AnnotatedString {
    val pretty = text.prettyPrintJson()
    return buildAnnotatedString {
        val trimmedStart = pretty.trimStart()
        if (pretty.isBlank() || (!trimmedStart.startsWith('{') && !trimmedStart.startsWith('['))) {
            append(pretty)
            return@buildAnnotatedString
        }
        var charIdx = 0
        while (charIdx < pretty.length) {
            when {
                pretty[charIdx] == '"' -> {
                    val (token, next) = readStringToken(pretty, charIdx)
                    val color = if (isFollowedByColon(pretty, next)) colors.keyColor else colors.stringColor
                    withStyle(SpanStyle(color = color)) { append(token) }
                    charIdx = next
                }
                isNumberStart(pretty, charIdx) -> {
                    val (token, next) = readNumberToken(pretty, charIdx)
                    withStyle(SpanStyle(color = colors.numberColor)) { append(token) }
                    charIdx = next
                }
                pretty.startsWith("true", charIdx) -> {
                    withStyle(SpanStyle(color = colors.boolColor)) { append("true") }
                    charIdx += 4
                }
                pretty.startsWith("false", charIdx) -> {
                    withStyle(SpanStyle(color = colors.boolColor)) { append("false") }
                    charIdx += 5
                }
                pretty.startsWith("null", charIdx) -> {
                    withStyle(SpanStyle(color = colors.nullColor)) { append("null") }
                    charIdx += 4
                }
                else -> {
                    append(pretty[charIdx])
                    charIdx++
                }
            }
        }
    }
}
