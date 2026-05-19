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
private const val MILLISECONDS_FRACTION_DIGITS = 3
private const val JSON_TRUE_LENGTH = 4
private const val JSON_FALSE_LENGTH = 5
private const val JSON_NULL_LENGTH = 4

private val timestampFormat = DateTimeComponents.Format {
    hour()
    char(':')
    minute()
    char(':')
    second()
    char('.')
    secondFraction(MILLISECONDS_FRACTION_DIGITS)
}

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

internal fun String.prettyPrintJson(): String {
    val trimmed = trim()
    if (!trimmed.startsWith('{') && !trimmed.startsWith('[')) {
        return this
    }
    val sb = StringBuilder()
    var depth = 0
    var idx = 0
    while (idx < trimmed.length) {
        val (newIdx, newDepth) = sb.processPrettyJsonChar(trimmed, idx, depth)
        idx = newIdx
        depth = newDepth
    }
    return sb.toString()
}

// ── JSON pretty-printer ───────────────────────────────────────────────────────

private fun StringBuilder.appendNewlineIndent(depth: Int) {
    append('\n')
    repeat(depth) { append(JSON_INDENT) }
}

/**
 * Processes a single character from [source] at [startIdx] during JSON pretty-printing,
 * appending the formatted output to the receiver [StringBuilder].
 *
 * @return A pair of (next index to process, updated nesting depth).
 */
private fun StringBuilder.processPrettyJsonChar(
    source: String,
    startIdx: Int,
    depth: Int,
): Pair<Int, Int> = when (source[startIdx]) {
    '{', '[' -> {
        append(source[startIdx])
        appendNewlineIndent(depth + 1)
        startIdx + 1 to depth + 1
    }
    '}', ']' -> {
        appendNewlineIndent(depth - 1)
        append(source[startIdx])
        startIdx + 1 to depth - 1
    }
    ',' -> {
        append(',')
        appendNewlineIndent(depth)
        startIdx + 1 to depth
    }
    ':' -> {
        append(": ")
        startIdx + 1 to depth
    }
    '"' -> appendJsonString(source, startIdx, this) + 1 to depth
    ' ', '\t', '\r', '\n' -> startIdx + 1 to depth
    else -> {
        append(source[startIdx])
        startIdx + 1 to depth
    }
}

/**
 * Processes a single character (or token) at [charIdx] in [text] during JSON syntax
 * highlighting, appending styled spans to the receiver [AnnotatedString.Builder].
 *
 * @return The index of the next character to process.
 */
private fun AnnotatedString.Builder.processHighlightToken(
    text: String,
    charIdx: Int,
    colors: JsonHighlightColors,
): Int = when {
    text[charIdx] == '"' -> {
        val (token, next) = readStringToken(text, charIdx)
        val color = if (isFollowedByColon(text, next)) colors.keyColor else colors.stringColor
        withStyle(SpanStyle(color = color)) { append(token) }
        next
    }
    isNumberStart(text, charIdx) -> {
        val (token, next) = readNumberToken(text, charIdx)
        withStyle(SpanStyle(color = colors.numberColor)) { append(token) }
        next
    }
    text.startsWith("true", charIdx) -> {
        withStyle(SpanStyle(color = colors.boolColor)) { append("true") }
        charIdx + JSON_TRUE_LENGTH
    }
    text.startsWith("false", charIdx) -> {
        withStyle(SpanStyle(color = colors.boolColor)) { append("false") }
        charIdx + JSON_FALSE_LENGTH
    }
    text.startsWith("null", charIdx) -> {
        withStyle(SpanStyle(color = colors.nullColor)) { append("null") }
        charIdx + JSON_NULL_LENGTH
    }
    else -> {
        append(text[charIdx])
        charIdx + 1
    }
}

internal fun urlToTitle(url: String): String = url
    .trimEnd('/')
    .substringAfterLast('/')
    .substringBefore('?')
    .replaceFirstChar { it.uppercaseChar() }
    .ifBlank { url }

internal fun formatTimestamp(timestamp: Long): String =
    Instant.fromEpochMilliseconds(timestamp).format(timestampFormat)

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
            charIdx = processHighlightToken(pretty, charIdx, colors)
        }
    }
}

/**
 * Reads a JSON string literal (including surrounding quotes and escape sequences)
 * from [source] starting at [openQuoteIdx], appending each character to [sink].
 *
 * @return The index of the closing `"` in [source], so the caller can advance past it.
 */
private fun appendJsonString(
    source: String,
    openQuoteIdx: Int,
    sink: StringBuilder
): Int {
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
            else -> {
                // this is a generated else block
            }
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
    while (idx < text.length && text[idx] in " \t\r\n") {
        idx++
    }
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
    if (idx < text.length && text[idx] == '-') {
        idx++
    }
    while (idx < text.length && (text[idx].isDigit() || text[idx] == '.')) {
        idx++
    }
    return text.substring(startIdx, idx) to idx
}
