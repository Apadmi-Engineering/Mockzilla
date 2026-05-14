package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.ui.ui.common.theme.LocalMockzillaTokens

@Composable
fun SurfaceHeader(
    title: String,
    subtitle: String?,
    actions: @Composable () -> Unit,
) {
    val tokens = LocalMockzillaTokens.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = tokens.bg2)
            .border(width = 1.dp, color = tokens.line1)
            .padding(vertical = 10.dp, horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = tokens.fg0,
            )
            subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = tokens.fg2,
                )
            }
        }
        actions()
    }
}
