package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.theme.success
import com.apadmi.mockzilla.ui.ui.common.theme.warning

import org.jetbrains.compose.ui.tooling.preview.Preview

enum class ChipTone {
    Accent, Err, Info, Neutral, Ok, Warn
}

/**
 * @property border
 * @property background
 * @property text
 */
private data class ChipColors(
    val border: Color,
    val background: Color,
    val text: Color
)

@Composable
private fun ColorScheme.chipColors(tone: ChipTone) = when (tone) {
    ChipTone.Ok -> ChipColors(success.primary, success.container, success.primary)
    ChipTone.Warn -> ChipColors(warning.primary, warning.container, warning.primary)
    ChipTone.Err -> ChipColors(error, errorContainer, error)
    ChipTone.Accent -> ChipColors(primary, primaryContainer, primary)
    ChipTone.Info -> ChipColors(tertiary, tertiaryContainer, tertiary)
    ChipTone.Neutral -> ChipColors(outline, surfaceVariant, onSurfaceMuted)
}

@Suppress("MAGIC_NUMBER")
@Composable
fun StatusChip(
    label: String,
    tone: ChipTone = ChipTone.Neutral,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme.chipColors(tone)
    val monoFont = com.apadmi.mockzilla.ui.ui.common.theme.LocalMonoFontFamily.current

    Text(
        text = label,
        modifier = modifier
            .background(color = colors.background, shape = RoundedCornerShape(10.dp))
            .border(width = 1.dp, color = colors.border, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        style = MaterialTheme.typography.labelSmall.copy(
            fontFamily = monoFont,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.02.em,
        ),
        color = colors.text,
    )
}

@Preview
@Composable
private fun StatusChipPreview() = PreviewSurface {
    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ChipTone.entries.forEach { tone ->
            StatusChip(label = tone.name, tone = tone)
        }
    }
}

@Preview
@Composable
private fun StatusChipDarkPreview() = PreviewSurface(darkTheme = true) {
    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ChipTone.entries.forEach { tone ->
            StatusChip(label = tone.name, tone = tone)
        }
    }
}
