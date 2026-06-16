package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun PlatformVerticalScrollbar(scrollState: ScrollState, modifier: Modifier) {
    val colors = MaterialTheme.colorScheme
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(scrollState),
        modifier = modifier,
        style = defaultScrollbarStyle().copy(
            unhoverColor = colors.onSurface.copy(alpha = 0.3f),
            hoverColor = colors.onSurface.copy(alpha = 0.7f),
        ),
    )
}

@Composable
internal actual fun PlatformVerticalScrollbar(scrollState: LazyListState, modifier: Modifier) {
    val colors = MaterialTheme.colorScheme
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(scrollState),
        modifier = modifier,
        style = defaultScrollbarStyle().copy(
            unhoverColor = colors.onSurface.copy(alpha = 0.3f),
            hoverColor = colors.onSurface.copy(alpha = 0.7f),
        ),
    )
}
