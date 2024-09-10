package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.details

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import com.apadmi.mockzilla.desktop.ui.widgets.endpoints.details.JsonTokens.nextToken
import kotlin.math.max
import kotlin.math.min

class EndpointBodyVisualTransformation(
    private val comment: SpanStyle,
    private val brace: SpanStyle,
    private val comma: SpanStyle,
    private val colon: SpanStyle,
    private val string: SpanStyle,
    private val keyword: SpanStyle,
    private val number: SpanStyle,
    private val default: SpanStyle,
) : VisualTransformation {

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
                        val token = nextToken(body, cursor)
                        val spanStyle = when (token.token) {
                            Token.OpenObject, Token.CloseObject -> brace
                            Token.OpenArray, Token.CloseArray -> brace
                            Token.BlockComment, Token.LineComment -> comment
                            Token.String -> string
                            Token.Boolean, Token.Null -> keyword
                            Token.ValueSeparator -> comma
                            Token.KeySeparator -> colon
                            Token.Number -> number
                            null -> default
                        }
                        // next token may be ahead of cursor if non tokens like whitespace are
                        // between the cursor and the token
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
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int =
                    min(max(0, offset), textLength)

                override fun transformedToOriginal(offset: Int): Int =
                    min(max(0, offset), textLength)
            }
        )
    }
}