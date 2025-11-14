package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.background
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

@Composable
fun SurfaceHeader(
    title: String,
    subtitle: String?,
    actions: @Composable () -> Unit
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colorScheme.surface)
        .padding(vertical = 20.dp, horizontal = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    Column(
        modifier = Modifier.weight(1f)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        subtitle?.let {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
    actions()
}
