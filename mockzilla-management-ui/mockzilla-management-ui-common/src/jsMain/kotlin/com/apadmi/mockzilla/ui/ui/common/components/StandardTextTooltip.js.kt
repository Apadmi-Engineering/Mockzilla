package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.runtime.Composable

@Composable
actual fun StandardTextTooltip(
    text: String,
    content: @Composable (() -> Unit)
) {
    // TODO: Add tooltip here once it's not an experimental API
    content()
}
