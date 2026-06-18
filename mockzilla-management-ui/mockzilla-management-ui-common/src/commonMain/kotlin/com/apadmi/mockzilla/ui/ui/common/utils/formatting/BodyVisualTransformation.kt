package com.apadmi.mockzilla.ui.ui.common.utils.formatting

import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.SpanStyle
import com.apadmi.mockzilla.ui.ui.common.components.editor.EditorMode
import com.apadmi.mockzilla.ui.ui.common.theme.jsonHighlight

internal object BodyVisualTransformation {
    internal fun isBodyTooLarge(body: String?) = body != null && body.length >= 25_000

    @Composable
    internal fun buildEditorOutputTransformation(mode: EditorMode): HighlightingTransformation? {
        val colorScheme = MaterialTheme.colorScheme

        val highlight = colorScheme.jsonHighlight
        return remember(mode, highlight, colorScheme.onSurface, colorScheme.onSurfaceVariant) {
            when (mode) {
                EditorMode.Json -> JsonBodyVisualTransformation(
                    comment = SpanStyle(color = colorScheme.onSurface.copy(alpha = 0.5f)),
                    brace = SpanStyle(color = colorScheme.onSurfaceVariant),
                    comma = SpanStyle(color = colorScheme.onSurfaceVariant),
                    colon = SpanStyle(color = colorScheme.onSurfaceVariant),
                    key = SpanStyle(color = highlight.keyColor),
                    string = SpanStyle(color = highlight.stringColor),
                    keyword = SpanStyle(color = highlight.boolColor),
                    number = SpanStyle(color = highlight.numberColor),
                    default = SpanStyle(color = colorScheme.onSurface),
                )

                EditorMode.Html -> HtmlBodyVisualTransformation(
                    bracket = SpanStyle(color = colorScheme.onSurfaceVariant),
                    tagName = SpanStyle(color = highlight.keyColor),
                    attributeName = SpanStyle(color = highlight.stringColor),
                    attributeValue = SpanStyle(color = highlight.numberColor),
                    comment = SpanStyle(color = colorScheme.onSurface.copy(alpha = 0.5f)),
                    default = SpanStyle(color = colorScheme.onSurface),
                )

                EditorMode.PlainText -> null
            }
        }
    }
}