package com.apadmi.mockzilla.ui.ui.common.components

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
import com.apadmi.mockzilla.ui.ui.common.theme.jsonKey
import com.apadmi.mockzilla.ui.ui.common.theme.success
import com.apadmi.mockzilla.ui.ui.common.theme.warning
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.ALPHA_MUTED
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.JsonHighlightColors
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.buildJsonAnnotatedString

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
actual fun StandardTextTooltip(text: String, content: @Composable () -> Unit) {
    if (text.isBlank()) {
        content()
        return
    }

    BasicTooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            val cs = MaterialTheme.colorScheme
            val jsonColors = JsonHighlightColors(
                keyColor = cs.jsonKey,
                stringColor = cs.success.primary,
                numberColor = cs.tertiary,
                boolColor = cs.warning.primary,
                nullColor = cs.onSurface.copy(alpha = ALPHA_MUTED)
            )
            Text(
                modifier = Modifier.padding(2.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    .padding(vertical = 2.dp, horizontal = 6.dp),
                text = buildJsonAnnotatedString(text, jsonColors)
            )
        },
        state = rememberBasicTooltipState(),
        content = content
    )
}
