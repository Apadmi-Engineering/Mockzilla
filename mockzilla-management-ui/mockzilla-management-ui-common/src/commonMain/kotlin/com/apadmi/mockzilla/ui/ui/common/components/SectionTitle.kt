package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em

import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted

import androidx.compose.ui.tooling.preview.Preview


@Suppress("MAGIC_NUMBER")
@Composable
fun SectionTitle(
    modifier: Modifier = Modifier,
    label: String,
) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 0.08.em,
            ),
            color = colorScheme.onSurfaceMuted,
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = colorScheme.outline,
        )
    }
}

@Preview
@Composable
private fun SectionTitlePreview() = PreviewSurface {
    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle(label = "Device")
        SectionTitle(label = "Session")
        SectionTitle(label = "Actions")
    }
}

@Preview
@Composable
private fun SectionTitleDarkPreview() = PreviewSurface(darkTheme = true) {
    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle(label = "Device")
        SectionTitle(label = "Session")
    }
}
