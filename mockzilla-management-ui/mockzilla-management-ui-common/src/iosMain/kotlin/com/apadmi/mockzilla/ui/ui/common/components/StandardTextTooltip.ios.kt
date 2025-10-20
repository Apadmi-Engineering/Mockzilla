package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.runtime.Composable

@Composable
actual fun StandardTextTooltip(
    text: String,
    content: @Composable (() -> Unit)
) {
    /* Tooltips aren't a thing on iOS devices */
    content()
}
