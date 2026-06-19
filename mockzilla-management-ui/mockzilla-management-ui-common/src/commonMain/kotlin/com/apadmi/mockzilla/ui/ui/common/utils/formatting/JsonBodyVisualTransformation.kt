package com.apadmi.mockzilla.ui.ui.common.utils.formatting

import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

class JsonBodyVisualTransformation(
    private val comment: SpanStyle,
    private val brace: SpanStyle,
    private val comma: SpanStyle,
    private val colon: SpanStyle,
    private val key: SpanStyle,
    private val string: SpanStyle,
    private val keyword: SpanStyle,
    private val number: SpanStyle,
    private val default: SpanStyle,
) : HighlightingTransformation {
    private fun Token?.toSpanStyle() = when (this) {
        Token.OpenObject, Token.CloseObject -> brace
        Token.OpenArray, Token.CloseArray -> brace
        Token.BlockComment, Token.LineComment -> comment
        Token.Key -> key
        Token.String -> string
        Token.Boolean, Token.Null -> keyword
        Token.ValueSeparator -> comma
        Token.KeySeparator -> colon
        Token.Number -> number
        null -> default
    }

    override fun TextFieldBuffer.transformOutput() {
        if (length == 0) {
            return
        }
        val body = toString()
        var cursor = 0
        while (cursor < body.length) {
            val token = JsonTokens.nextToken(body, cursor)
            if (token.startIndex > cursor) {
                addStyle(default, cursor, token.startIndex)
            }
            addStyle(token.token.toSpanStyle(), token.startIndex, token.endIndex)
            cursor = token.endIndex
        }
    }

    override fun highlight(body: String): AnnotatedString {
        if (body.isEmpty()) {
            return AnnotatedString(body)
        }
        return buildAnnotatedString {
            append(body)
            var cursor = 0
            while (cursor < body.length) {
                val token = JsonTokens.nextToken(body, cursor)
                if (token.startIndex > cursor) {
                    addStyle(default, cursor, token.startIndex)
                }
                addStyle(token.token.toSpanStyle(), token.startIndex, token.endIndex)
                cursor = token.endIndex
            }
        }
    }
}
