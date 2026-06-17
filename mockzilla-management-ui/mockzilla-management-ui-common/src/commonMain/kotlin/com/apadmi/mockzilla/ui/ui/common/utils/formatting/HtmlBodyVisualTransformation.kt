package com.apadmi.mockzilla.ui.ui.common.utils.formatting

import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

internal class HtmlBodyVisualTransformation(
    private val bracket: SpanStyle,
    private val tagName: SpanStyle,
    private val attributeName: SpanStyle,
    private val attributeValue: SpanStyle,
    private val comment: SpanStyle,
    private val default: SpanStyle,
) : HighlightingTransformation {

    private fun HtmlToken?.toSpanStyle() = when (this) {
        HtmlToken.Bracket, HtmlToken.AttributeEquals -> bracket
        HtmlToken.TagName -> tagName
        HtmlToken.AttributeName -> attributeName
        HtmlToken.AttributeValue -> attributeValue
        HtmlToken.Comment, HtmlToken.DocType -> comment
        null -> default
    }

    override fun TextFieldBuffer.transformOutput() {
        if (length == 0) return
        val body = toString()
        var cursor = 0
        while (cursor < body.length) {
            val token = HtmlTokens.nextToken(body, cursor)
            if (token.startIndex > cursor) addStyle(default, cursor, token.startIndex)
            addStyle(token.token.toSpanStyle(), token.startIndex, token.endIndex)
            cursor = token.endIndex
        }
    }

    override fun highlight(body: String): AnnotatedString {
        if (body.isEmpty()) return AnnotatedString(body)
        return buildAnnotatedString {
            append(body)
            var cursor = 0
            while (cursor < body.length) {
                val token = HtmlTokens.nextToken(body, cursor)
                if (token.startIndex > cursor) addStyle(default, cursor, token.startIndex)
                addStyle(token.token.toSpanStyle(), token.startIndex, token.endIndex)
                cursor = token.endIndex
            }
        }
    }
}