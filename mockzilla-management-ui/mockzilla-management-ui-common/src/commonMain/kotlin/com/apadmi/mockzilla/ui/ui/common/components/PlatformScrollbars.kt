package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal expect fun PlatformVerticalScrollbar(scrollState: ScrollState, modifier: Modifier = Modifier)

@Composable
internal expect fun PlatformVerticalScrollbar(scrollState: LazyListState, modifier: Modifier = Modifier)

@Composable
expect fun PlatformHorizontalScrollbar(scrollState: ScrollState, modifier: Modifier = Modifier)
