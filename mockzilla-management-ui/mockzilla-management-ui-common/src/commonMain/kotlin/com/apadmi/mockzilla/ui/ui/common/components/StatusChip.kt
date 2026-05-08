package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

import com.apadmi.mockzilla.ui.ui.common.theme.LocalMockzillaTokens
import com.apadmi.mockzilla.ui.ui.common.theme.MockzillaTokens

import org.jetbrains.compose.ui.tooling.preview.Preview

enum class ChipTone { Ok, Warn, Err, Accent, Info, Neutral }

private data class ChipColors(val border: Color, val background: Color, val text: Color)

private fun MockzillaTokens.chipColors(tone: ChipTone) = when (tone) {
    ChipTone.Ok -> ChipColors(ok, okSoft, ok)
    ChipTone.Warn -> ChipColors(warn, warnSoft, warn)
    ChipTone.Err -> ChipColors(err, errSoft, err)
    ChipTone.Accent -> ChipColors(accent, accentSoft, accent)
    ChipTone.Info -> ChipColors(info, infoSoft, info)
    ChipTone.Neutral -> ChipColors(line1, bg3, fg2)
}

@Suppress("MAGIC_NUMBER")
@Composable
fun StatusChip(
    label: String,
    tone: ChipTone = ChipTone.Neutral,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalMockzillaTokens.current
    val colors = tokens.chipColors(tone)
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
