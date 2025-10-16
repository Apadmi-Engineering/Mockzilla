@file:Suppress("MAGIC_NUMBER", "FLOAT_IN_ACCURATE_CALCULATIONS")

package com.apadmi.mockzilla.ui.ui.common.widgets.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.apadmi.mockzilla.ui.ui.common.theme.partialFailure
import com.apadmi.mockzilla.ui.ui.common.theme.success

@Composable
private fun RowScope.ColorCell(
    pair: Pair<String, Color>,
    colorLuminance: Double
) = Box(
    Modifier
        .fillMaxHeight()
        .weight(1f)
        .background(pair.second),
    contentAlignment = Alignment.Center
) {
    Text(
        pair.first,
        textAlign = TextAlign.Center,
        color = if (colorLuminance * 255 > 128) Color.Black else Color.White
    )
}

@Composable
internal fun DebugColorsWidget() = Column(Modifier.fillMaxHeight().navigationBarsPadding()) {
    for (rowVals in colors().toList().chunked(3)) {
        Row(Modifier.weight(1f)) {
            rowVals.forEach {
                val colorLuminance = (0.2126 * it.second.red) +
                        (0.7152 * it.second.green) +
                        (0.0722 * it.second.blue)
                ColorCell(it, colorLuminance)
            }
        }
    }
}

@Composable
private fun colors() = mapOf(
    "primary" to MaterialTheme.colorScheme.primary,
    "onPrimary" to MaterialTheme.colorScheme.onPrimary,
    "primaryContainer" to MaterialTheme.colorScheme.primaryContainer,
    "onPrimaryContainer" to MaterialTheme.colorScheme.onPrimaryContainer,
    "inversePrimary" to MaterialTheme.colorScheme.inversePrimary,
    "secondary" to MaterialTheme.colorScheme.secondary,
    "onSecondary" to MaterialTheme.colorScheme.onSecondary,
    "secondaryContainer" to MaterialTheme.colorScheme.secondaryContainer,
    "onSecondaryContainer" to MaterialTheme.colorScheme.onSecondaryContainer,
    "tertiary" to MaterialTheme.colorScheme.tertiary,
    "onTertiary" to MaterialTheme.colorScheme.onTertiary,
    "tertiaryContainer" to MaterialTheme.colorScheme.tertiaryContainer,
    "onTertiaryContainer" to MaterialTheme.colorScheme.onTertiaryContainer,
    "background" to MaterialTheme.colorScheme.background,
    "onBackground" to MaterialTheme.colorScheme.onBackground,
    "surface" to MaterialTheme.colorScheme.surface,
    "onSurface" to MaterialTheme.colorScheme.onSurface,
    "surfaceVariant" to MaterialTheme.colorScheme.surfaceVariant,
    "onSurfaceVariant" to MaterialTheme.colorScheme.onSurfaceVariant,
    "surfaceTint" to MaterialTheme.colorScheme.surfaceTint,
    "inverseSurface" to MaterialTheme.colorScheme.inverseSurface,
    "inverseOnSurface" to MaterialTheme.colorScheme.inverseOnSurface,
    "error" to MaterialTheme.colorScheme.error,
    "onError" to MaterialTheme.colorScheme.onError,
    "errorContainer" to MaterialTheme.colorScheme.errorContainer,
    "onErrorContainer" to MaterialTheme.colorScheme.onErrorContainer,
    "outline" to MaterialTheme.colorScheme.outline,
    "outlineVariant" to MaterialTheme.colorScheme.outlineVariant,
    "scrim" to MaterialTheme.colorScheme.scrim,
    "surfaceBright" to MaterialTheme.colorScheme.surfaceBright,
    "surfaceDim" to MaterialTheme.colorScheme.surfaceDim,
    "surfaceContainer" to MaterialTheme.colorScheme.surfaceContainer,
    "surfaceContainerHigh" to MaterialTheme.colorScheme.surfaceContainerHigh,
    "surfaceContainerHighest" to MaterialTheme.colorScheme.surfaceContainerHighest,
    "surfaceContainerLow" to MaterialTheme.colorScheme.surfaceContainerLow,
    "surfaceContainerLowest" to MaterialTheme.colorScheme.surfaceContainerLowest,
    "successPrimary" to MaterialTheme.colorScheme.success.primary,
    "successContainer" to MaterialTheme.colorScheme.success.container,
    "partialFailurePrimary" to MaterialTheme.colorScheme.partialFailure.primary,
    "partialFailureContainer" to MaterialTheme.colorScheme.partialFailure.container
)
