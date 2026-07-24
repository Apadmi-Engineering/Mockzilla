package com.apadmi.mockzilla.ui.ui.common.components.editor

import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle

internal class FindHighlightOutputTransformation(
    private val matches: List<IntRange>,
    private val currentMatchIndex: Int,
    private val activeColor: Color,
    private val inactiveColor: Color,
) : OutputTransformation {
    override fun TextFieldBuffer.transformOutput() {
        matches.forEachIndexed { i, range ->
            val bg = if (i == currentMatchIndex) activeColor else inactiveColor
            addStyle(SpanStyle(background = bg), range.first, range.last + 1)
        }
    }
}
