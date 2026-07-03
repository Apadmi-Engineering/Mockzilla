package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import com.apadmi.mockzilla.lib.InternalMockzillaApi

@InternalMockzillaApi
@Composable
public actual fun PlatformHorizontalScrollbar(scrollState: ScrollState, modifier: Modifier): Unit =
    HorizontalScrollbar(
        adapter = rememberScrollbarAdapter(scrollState),
        modifier = modifier,
        style = scrollbarStyle(),
    )

@InternalMockzillaApi
@Composable
public actual fun PlatformVerticalScrollbar(scrollState: ScrollState, modifier: Modifier): Unit =
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(scrollState),
        modifier = modifier,
        style = scrollbarStyle(),
    )

@Composable
internal actual fun PlatformVerticalScrollbar(scrollState: LazyListState, modifier: Modifier) =
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(scrollState),
        modifier = modifier,
        style = scrollbarStyle(),
    )

@Composable
private fun scrollbarStyle() = defaultScrollbarStyle().copy(
    unhoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
    hoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
)
