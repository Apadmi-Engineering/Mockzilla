package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import com.apadmi.mockzilla.lib.InternalMockzillaApi

@InternalMockzillaApi
@Composable
public actual fun PlatformHorizontalScrollbar(scrollState: ScrollState, modifier: Modifier): Unit = Unit

@InternalMockzillaApi
@Composable
public actual fun PlatformVerticalScrollbar(scrollState: ScrollState, modifier: Modifier): Unit = Unit

@Composable
internal actual fun PlatformVerticalScrollbar(scrollState: LazyListState, modifier: Modifier) = Unit
