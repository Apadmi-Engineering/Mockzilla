package com.apadmi.mockzilla.ui.ui.common.components.editor

import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer

internal class CompositeOutputTransformation(
    private val first: OutputTransformation,
    private val second: OutputTransformation,
) : OutputTransformation {
    override fun TextFieldBuffer.transformOutput() {
        with(first) { transformOutput() }
        with(second) { transformOutput() }
    }
}
