package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.details

import kotlin.test.Test
import kotlin.test.assertEquals

class JsonTokensTests {
    @Suppress("TOO_LONG_FUNCTION")
    @Test
    fun `nextToken tokenises the entire json input`() {
        val json = """
            {
               "foo": "bar",
               "true": true, // a comment
               "false": false,
                "null": null,
                "list": [ false, true, null, /* comment */ "hi", "not a /* */ comment", "//" ],
                "numbers": [ 2, 5, -2, +5, 3e2, -2.5, .9, 9.8E2 ]
            }
        """.trimIndent()

        var cursor = 0
        val tokens = mutableListOf<TokenIndex>()
        while (cursor < json.length) {
            val token = JsonTokens.nextToken(json, cursor)
            cursor = token.endIndex
            tokens.add(token)
        }

        assertEquals(
            listOf(
                TokenIndex(startIndex = 0, endIndex = 1, token = Token.OpenObject),
                TokenIndex(startIndex = 5, endIndex = 10, token = Token.String),
                TokenIndex(startIndex = 10, endIndex = 11, token = Token.KeySeparator),
                TokenIndex(startIndex = 12, endIndex = 17, token = Token.String),
                TokenIndex(startIndex = 17, endIndex = 18, token = Token.ValueSeparator),
                TokenIndex(startIndex = 22, endIndex = 28, token = Token.String),
                TokenIndex(startIndex = 28, endIndex = 29, token = Token.KeySeparator),
                TokenIndex(startIndex = 30, endIndex = 34, token = Token.Boolean),
                TokenIndex(startIndex = 34, endIndex = 35, token = Token.ValueSeparator),
                TokenIndex(startIndex = 36, endIndex = 49, token = Token.LineComment),
                TokenIndex(startIndex = 52, endIndex = 59, token = Token.String),
                TokenIndex(startIndex = 59, endIndex = 60, token = Token.KeySeparator),
                TokenIndex(startIndex = 61, endIndex = 66, token = Token.Boolean),
                TokenIndex(startIndex = 66, endIndex = 67, token = Token.ValueSeparator),
                TokenIndex(startIndex = 72, endIndex = 78, token = Token.String),
                TokenIndex(startIndex = 78, endIndex = 79, token = Token.KeySeparator),
                TokenIndex(startIndex = 80, endIndex = 84, token = Token.Null),
                TokenIndex(startIndex = 84, endIndex = 85, token = Token.ValueSeparator),
                TokenIndex(startIndex = 90, endIndex = 96, token = Token.String),
                TokenIndex(startIndex = 96, endIndex = 97, token = Token.KeySeparator),
                TokenIndex(startIndex = 98, endIndex = 99, token = Token.OpenArray),
                TokenIndex(startIndex = 100, endIndex = 105, token = Token.Boolean),
                TokenIndex(startIndex = 105, endIndex = 106, token = Token.ValueSeparator),
                TokenIndex(startIndex = 107, endIndex = 111, token = Token.Boolean),
                TokenIndex(startIndex = 111, endIndex = 112, token = Token.ValueSeparator),
                TokenIndex(startIndex = 113, endIndex = 117, token = Token.Null),
                TokenIndex(startIndex = 117, endIndex = 118, token = Token.ValueSeparator),
                TokenIndex(startIndex = 119, endIndex = 132, token = Token.BlockComment),
                TokenIndex(startIndex = 133, endIndex = 137, token = Token.String),
                TokenIndex(startIndex = 137, endIndex = 138, token = Token.ValueSeparator),
                TokenIndex(startIndex = 139, endIndex = 160, token = Token.String),
                TokenIndex(startIndex = 160, endIndex = 161, token = Token.ValueSeparator),
                TokenIndex(startIndex = 162, endIndex = 166, token = Token.String),
                TokenIndex(startIndex = 167, endIndex = 168, token = Token.CloseArray),
                TokenIndex(startIndex = 168, endIndex = 169, token = Token.ValueSeparator),
                TokenIndex(startIndex = 174, endIndex = 183, token = Token.String),
                TokenIndex(startIndex = 183, endIndex = 184, token = Token.KeySeparator),
                TokenIndex(startIndex = 185, endIndex = 186, token = Token.OpenArray),
                TokenIndex(startIndex = 187, endIndex = 188, token = Token.Number),
                TokenIndex(startIndex = 188, endIndex = 189, token = Token.ValueSeparator),
                TokenIndex(startIndex = 190, endIndex = 191, token = Token.Number),
                TokenIndex(startIndex = 191, endIndex = 192, token = Token.ValueSeparator),
                TokenIndex(startIndex = 193, endIndex = 195, token = Token.Number),
                TokenIndex(startIndex = 195, endIndex = 196, token = Token.ValueSeparator),
                TokenIndex(startIndex = 197, endIndex = 199, token = Token.Number),
                TokenIndex(startIndex = 199, endIndex = 200, token = Token.ValueSeparator),
                TokenIndex(startIndex = 201, endIndex = 204, token = Token.Number),
                TokenIndex(startIndex = 204, endIndex = 205, token = Token.ValueSeparator),
                TokenIndex(startIndex = 206, endIndex = 210, token = Token.Number),
                TokenIndex(startIndex = 210, endIndex = 211, token = Token.ValueSeparator),
                TokenIndex(startIndex = 213, endIndex = 214, token = Token.Number),
                TokenIndex(startIndex = 214, endIndex = 215, token = Token.ValueSeparator),
                TokenIndex(startIndex = 216, endIndex = 221, token = Token.Number),
                TokenIndex(startIndex = 222, endIndex = 223, token = Token.CloseArray),
                TokenIndex(startIndex = 224, endIndex = 225, token = Token.CloseObject)
            ),
            tokens
        )
    }
}