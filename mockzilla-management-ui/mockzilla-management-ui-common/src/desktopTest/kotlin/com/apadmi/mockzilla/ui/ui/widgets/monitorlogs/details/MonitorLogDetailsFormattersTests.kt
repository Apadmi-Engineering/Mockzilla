package com.apadmi.mockzilla.ui.ui.widgets.monitorlogs.details

import androidx.compose.ui.graphics.Color

import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.JsonHighlightColors
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.buildHighlightedAnnotatedString
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.formatTimestamp
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.prettyPrintJson
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.toKbLabel
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.urlToTitle

import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MonitorLogDetailsFormattersTests {
    // ── buildJsonAnnotatedString ──────────────────────────────────────────────

    private val colors = JsonHighlightColors(
        keyColor = Color.Blue,
        stringColor = Color.Green,
        numberColor = Color.Yellow,
        boolColor = Color.Magenta,
        nullColor = Color.Gray,
    )

    // ── toKbLabel ─────────────────────────────────────────────────────────────

    @Test
    fun `toKbLabel returns zero for empty string`() {
        assertEquals("0.0 KB", "".toKbLabel())
    }

    @Test
    fun `toKbLabel returns half a kilobyte for 512 bytes`() {
        assertEquals("0.5 KB", "a".repeat(512).toKbLabel())
    }

    @Test
    fun `toKbLabel returns 1 point 0 for exactly 1024 bytes`() {
        assertEquals("1.0 KB", "a".repeat(1024).toKbLabel())
    }

    // ── urlToTitle ────────────────────────────────────────────────────────────

    @Test
    fun `urlToTitle capitalises last path segment`() {
        assertEquals("Repairs", urlToTitle("https://api.example.com/repairs"))
    }

    @Test
    fun `urlToTitle strips trailing slash before extracting segment`() {
        assertEquals("Repairs", urlToTitle("https://api.example.com/repairs/"))
    }

    @Test
    fun `urlToTitle strips query parameters from the segment`() {
        assertEquals("Repairs", urlToTitle("https://api.example.com/repairs?status=upcoming"))
    }

    @Test
    fun `urlToTitle uses hostname when url has no path segment`() {
        // substringAfterLast('/') on "https://api.example.com" returns "api.example.com"
        // (the last '/' is the one in "https://"), so the hostname is used and capitalised.
        assertEquals("Api.example.com", urlToTitle("https://api.example.com"))
    }

    @Test
    fun `urlToTitle falls back to full url when result would be blank`() {
        // "/" → trimEnd('/') → "" → substringAfterLast('/') → "" → replaceFirstChar → ""
        // ifBlank returns the original url.
        assertEquals("/", urlToTitle("/"))
    }

    // ── formatTimestamp ───────────────────────────────────────────────────────

    @Test
    fun `formatTimestamp always emits 3 millisecond digits for whole seconds`() {
        // secondFraction(3) pads with trailing zeros, so 00:00:01 becomes 00:00:01.000
        assertEquals("00:00:01.000", formatTimestamp(1_000L))
    }

    @Test
    fun `formatTimestamp formats sub-second precision correctly`() {
        assertEquals("00:00:01.500", formatTimestamp(1_500L))
    }

    @Test
    fun `formatTimestamp formats single millisecond correctly`() {
        assertEquals("00:00:00.001", formatTimestamp(1L))
    }

    // ── prettyPrintJson ───────────────────────────────────────────────────────

    @Test
    fun `prettyPrintJson returns non-json input unchanged`() {
        assertEquals("hello world", "hello world".prettyPrintJson())
    }

    @Test
    fun `prettyPrintJson formats a flat object`() {
        val expected = """
            {
              "name": "John",
              "age": 30
            }
        """.trimIndent()
        val input = """{"name":"John","age":30}"""
        assertEquals(expected, input.prettyPrintJson())
    }

    @Test
    fun `prettyPrintJson formats a nested object`() {
        val expected = """
            {
              "a": {
                "b": "c"
              }
            }
        """.trimIndent()
        val input = """{"a":{"b":"c"}}"""
        assertEquals(expected, input.prettyPrintJson())
    }

    @Test
    fun `prettyPrintJson formats an array`() {
        val expected = """
            [
              1,
              2,
              3
            ]
        """.trimIndent()
        val input = """[1,2,3]"""
        assertEquals(expected, input.prettyPrintJson())
    }

    @Test
    fun `prettyPrintJson preserves escaped quotes inside strings`() {
        assertTrue(
            """{"msg":"hello \"world\""}""".prettyPrintJson().contains(""""hello \"world\"""""),
            "Expected escaped quotes to be preserved in output"
        )
    }

    @Test
    fun `prettyPrintJson strips insignificant whitespace`() {
        val withSpaces = """{ "key" : "value" }"""
        val compact = """{"key":"value"}"""
        assertEquals(compact.prettyPrintJson(), withSpaces.prettyPrintJson())
    }

    @Test
    fun `buildHighlightedAnnotatedString plain text equals pretty printed json`() {
        val json = """{"key":"value"}"""
        assertEquals(json.prettyPrintJson(), buildHighlightedAnnotatedString(json, colors).text)
    }

    @Test
    fun `buildHighlightedAnnotatedString returns unstyled text for non-json input`() {
        val plain = "not json at all"
        val result = buildHighlightedAnnotatedString(plain, colors)
        assertEquals(plain, result.text)
        assertTrue(result.spanStyles.isEmpty(), "Expected no spans for plain text")
    }

    @Test
    fun `buildHighlightedAnnotatedString colours object keys with keyColor`() {
        val result = buildHighlightedAnnotatedString("""{"name":"John"}""", colors)
        val span = result.spanStyles.first { it.item.color == Color.Blue }
        assertEquals(""""name"""", result.text.substring(span.start, span.end))
    }

    @Test
    fun `buildHighlightedAnnotatedString colours string values with stringColor`() {
        val result = buildHighlightedAnnotatedString("""{"name":"John"}""", colors)
        val span = result.spanStyles.first { it.item.color == Color.Green }
        assertEquals(""""John"""", result.text.substring(span.start, span.end))
    }

    @Test
    fun `buildHighlightedAnnotatedString colours integer numbers with numberColor`() {
        val result = buildHighlightedAnnotatedString("""{"age":30}""", colors)
        val span = result.spanStyles.first { it.item.color == Color.Yellow }
        assertEquals("30", result.text.substring(span.start, span.end))
    }

    @Test
    fun `buildHighlightedAnnotatedString colours negative numbers with numberColor`() {
        val result = buildHighlightedAnnotatedString("""{"temp":-5}""", colors)
        val span = result.spanStyles.first { it.item.color == Color.Yellow }
        assertEquals("-5", result.text.substring(span.start, span.end))
    }

    @Test
    fun `buildHighlightedAnnotatedString colours true with boolColor`() {
        val result = buildHighlightedAnnotatedString("""{"active":true}""", colors)
        val span = result.spanStyles.first { it.item.color == Color.Magenta }
        assertEquals("true", result.text.substring(span.start, span.end))
    }

    @Test
    fun `buildHighlightedAnnotatedString colours false with boolColor`() {
        val result = buildHighlightedAnnotatedString("""{"active":false}""", colors)
        val span = result.spanStyles.first { it.item.color == Color.Magenta }
        assertEquals("false", result.text.substring(span.start, span.end))
    }

    @Test
    fun `buildHighlightedAnnotatedString colours null with nullColor`() {
        val result = buildHighlightedAnnotatedString("""{"value":null}""", colors)
        val span = result.spanStyles.first { it.item.color == Color.Gray }
        assertEquals("null", result.text.substring(span.start, span.end))
    }

    @Test
    fun `buildHighlightedAnnotatedString highlights HTML tags`() {
        val html = """<html><body>Hi</body></html>"""
        val result = buildHighlightedAnnotatedString(html, colors)
        assertEquals(html, result.text)
        assertEquals(4, result.spanStyles.size)  // <html>, <body>, </body>, </html>
        result.spanStyles.forEach {
            assertEquals(Color.Blue, it.item.color)
        }
    }
}
