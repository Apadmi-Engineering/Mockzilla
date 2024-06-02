package com.apadmi.mockzilla.desktop.ui.components

import androidx.compose.foundation.BasicTooltipBox
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
actual fun StandardTextTooltip(text: String, content: @Composable () -> Unit) = BasicTooltipBox(
    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
    tooltip = {
        Text(
            modifier = Modifier.padding(2.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                .padding(vertical = 2.dp, horizontal = 6.dp),
            text = text
        )
    },
    state = rememberBasicTooltipState(),
    content = content
)
