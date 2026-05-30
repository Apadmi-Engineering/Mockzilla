package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

import com.apadmi.mockzilla.ui.ui.common.theme.LocalMonoFontFamily
import com.apadmi.mockzilla.ui.ui.common.theme.success
import com.apadmi.mockzilla.ui.ui.common.theme.warning

enum class ChipTone {
    Accent, Err, Info, Neutral, Ok, Teal, Warn
}

/**
 * @property border
 * @property text
 * @property background
 */
private data class ChipColors(
    val border: Color,
    val text: Color,
    val background: Color
)

@Suppress("MAGIC_NUMBER")
@Composable
fun StatusChip(
    label: String,
    tone: ChipTone = ChipTone.Neutral,
    modifier: Modifier = Modifier,
) {
    val colors = chipColors(tone)
    val monoFont = LocalMonoFontFamily.current

    Text(
        text = label,
        modifier = modifier
            .background(colors.background, RoundedCornerShape(3.dp))
            .border(width = 1.dp, color = colors.border, shape = RoundedCornerShape(3.dp))
            .padding(horizontal = 4.dp, vertical = 1.dp),
        style = MaterialTheme.typography.labelSmall.copy(
            fontFamily = monoFont,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.02.em,
        ),
        color = colors.text,
    )
}

@Composable
private fun chipColors(tone: ChipTone): ChipColors {
    val cs = MaterialTheme.colorScheme
    return when (tone) {
        ChipTone.Ok -> {
            val color = cs.success.primary
            ChipColors(border = color, text = color, background = color.copy(alpha = 0.14f))
        }
        ChipTone.Warn -> {
            val color = cs.warning.primary
            ChipColors(border = color, text = color, background = color.copy(alpha = 0.14f))
        }
        ChipTone.Err -> ChipColors(
            border = cs.error,
            text = cs.error,
            background = cs.errorContainer,
        )
        ChipTone.Teal -> {
            val color = cs.primary
            ChipColors(border = color, text = color, background = color.copy(alpha = 0.14f))
        }
        ChipTone.Accent -> ChipColors(
            border = cs.primary,
            text = cs.primary,
            background = cs.primaryContainer,
        )
        ChipTone.Info -> ChipColors(
            border = cs.tertiary,
            text = cs.tertiary,
            background = cs.tertiaryContainer,
        )
        ChipTone.Neutral -> ChipColors(
            border = cs.outline,
            text = cs.onSurfaceVariant,
            background = cs.surfaceVariant,
        )
    }
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
