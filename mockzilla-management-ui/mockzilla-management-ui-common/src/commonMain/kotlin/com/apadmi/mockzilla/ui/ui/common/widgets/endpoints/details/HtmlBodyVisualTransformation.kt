package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import kotlin.math.max
import kotlin.math.min

class HtmlBodyVisualTransformation(
    private val bracket: SpanStyle,
    private val tagName: SpanStyle,
    private val attributeName: SpanStyle,
    private val attributeValue: SpanStyle,
    private val comment: SpanStyle,
    private val default: SpanStyle,
) : VisualTransformation {
    @Suppress("TOO_LONG_FUNCTION")
    override fun filter(text: AnnotatedString): TransformedText {
        val textLength = text.length
        return TransformedText(
            text = if (text.text.isEmpty()) {
                text
            } else {
                val body = text.text
                var cursor = 0
                buildAnnotatedString {
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
                        // next token may be ahead of cursor if non-tokens like text/whitespace
                        // are between the cursor and the token
                        val beforeToken = token.startIndex - cursor
                        if (beforeToken > 0) {
                            withStyle(default) {
                                append(
                                    text.substring(
                                        startIndex = cursor,
                                        endIndex = token.startIndex
                                    )
                                )
                            }
                        }
                        withStyle(spanStyle) {
                            append(
                                text.substring(
                                    startIndex = token.startIndex,
                                    endIndex = token.endIndex
                                )
                            )
                        }
                        cursor = token.endIndex
                    }
                }
            },
            offsetMapping = ClipOffsetMapping(textLength)
        )
    }

    private class ClipOffsetMapping(private val textLength: Int) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int =
            min(max(0, offset), textLength)

        override fun transformedToOriginal(offset: Int): Int =
            min(max(0, offset), textLength)
    }
}
