package com.apadmi.mockzilla.ui.ui.widgets.monitorlogs.details

import androidx.compose.ui.graphics.Color
import com.apadmi.mockzilla.ui.ui.common.theme.JsonHighlightColors

import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.prettyPrintJson
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.toKbLabel

import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MonitorLogDetailsFormattersTests {
    private val colors = JsonHighlightColors(
        keyColor = Color.Blue,
        stringColor = Color.Green,
        numberColor = Color.Yellow,
        boolColor = Color.Magenta,
        nullColor = Color.Gray,
    )

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
}
