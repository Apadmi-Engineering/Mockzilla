package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun PlatformVerticalScrollbar(scrollState: ScrollState, modifier: Modifier) = Unit

@Composable
internal actual fun PlatformVerticalScrollbar(scrollState: LazyListState, modifier: Modifier) = Unit
