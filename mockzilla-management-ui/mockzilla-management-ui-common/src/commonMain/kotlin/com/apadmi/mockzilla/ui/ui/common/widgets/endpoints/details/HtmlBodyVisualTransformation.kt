package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details

import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.ui.text.SpanStyle

class HtmlBodyVisualTransformation(
    private val bracket: SpanStyle,
    private val tagName: SpanStyle,
    private val attributeName: SpanStyle,
    private val attributeValue: SpanStyle,
    private val comment: SpanStyle,
    private val default: SpanStyle,
) : OutputTransformation {
    @Suppress("TOO_LONG_FUNCTION")
    override fun TextFieldBuffer.transformOutput() {
        if (length == 0) return
        val body = toString()
        var cursor = 0
        while (cursor < body.length) {
            val token = HtmlTokens.nextToken(body, cursor)
            val spanStyle = when (token.token) {
                HtmlToken.Bracket, HtmlToken.AttributeEquals -> bracket
                HtmlToken.TagName -> tagName
                HtmlToken.AttributeName -> attributeName
                HtmlToken.AttributeValue -> attributeValue
                HtmlToken.Comment, HtmlToken.DocType -> comment
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
