package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.runtime.Composable

import com.apadmi.mockzilla.lib.InternalMockzillaApi

@InternalMockzillaApi
@Composable
public expect fun StandardTextTooltip(text: String, content: @Composable () -> Unit)
