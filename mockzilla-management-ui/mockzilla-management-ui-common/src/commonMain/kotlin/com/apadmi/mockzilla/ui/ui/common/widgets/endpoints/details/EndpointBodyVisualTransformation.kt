package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details

import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.ui.text.SpanStyle
import com.apadmi.mockzilla.ui.ui.common.components.editor.JsonTokens
import com.apadmi.mockzilla.ui.ui.common.components.editor.Token

class EndpointBodyVisualTransformation(
    private val comment: SpanStyle,
    private val brace: SpanStyle,
    private val comma: SpanStyle,
    private val colon: SpanStyle,
    private val key: SpanStyle,
    private val string: SpanStyle,
    private val keyword: SpanStyle,
    private val number: SpanStyle,
    private val default: SpanStyle,
) : OutputTransformation {
    @Suppress("TOO_LONG_FUNCTION")
    override fun TextFieldBuffer.transformOutput() {
        if (length == 0) {
            return
        }
        val body = toString()
        var cursor = 0
        while (cursor < body.length) {
            val token = JsonTokens.nextToken(body, cursor)
            val spanStyle = when (token.token) {
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
            if (token.startIndex > cursor) {
                addStyle(default, cursor, token.startIndex)
            }
            addStyle(spanStyle, token.startIndex, token.endIndex)
            cursor = token.endIndex
        }
    }
}
