package com.apadmi.mockzilla.ui.ui.common.utils.formatting

import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.ui.text.AnnotatedString

internal interface HighlightingTransformation : OutputTransformation {
    fun highlight(body: String): AnnotatedString
}
