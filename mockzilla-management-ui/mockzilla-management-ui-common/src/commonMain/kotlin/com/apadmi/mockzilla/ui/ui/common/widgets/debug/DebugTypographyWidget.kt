package com.apadmi.mockzilla.ui.ui.common.widgets.debug

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DebugTypographyWidget() = Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    val styles = mapOf(
        "DisplayLarge" to MaterialTheme.typography.displayLarge,
        "DisplayMedium" to MaterialTheme.typography.displayMedium,
        "DisplaySmall" to MaterialTheme.typography.displaySmall,
        "HeadlineLarge" to MaterialTheme.typography.headlineLarge,
        "HeadlineMedium" to MaterialTheme.typography.headlineMedium,
        "HeadlineSmall" to MaterialTheme.typography.headlineSmall,
        "TitleLarge" to MaterialTheme.typography.titleLarge,
        "TitleMedium" to MaterialTheme.typography.titleMedium,
        "TitleSmall" to MaterialTheme.typography.titleSmall,
        "BodyLarge" to MaterialTheme.typography.bodyLarge,
        "BodyMedium" to MaterialTheme.typography.bodyMedium,
        "BodySmall" to MaterialTheme.typography.bodySmall,
        "LabelLarge" to MaterialTheme.typography.labelLarge,
        "LabelMedium" to MaterialTheme.typography.labelMedium,
        "LabelSmall" to MaterialTheme.typography.labelSmall,
    )

    styles.forEach { (label, style) ->
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, style = style)
            Spacer(Modifier.weight(1f))
            Text(style.fontSize.toString(), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview
@Composable
fun DebugTypographyWidgetPreview() = PreviewSurface {
    DebugTypographyWidget()
}
