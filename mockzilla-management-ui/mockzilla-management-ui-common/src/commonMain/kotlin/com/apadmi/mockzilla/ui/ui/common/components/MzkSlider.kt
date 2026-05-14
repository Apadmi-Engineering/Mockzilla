package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.ui.ui.common.theme.LocalMockzillaTokens

import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockzillaSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    enabled: Boolean = true,
    onValueChangeFinished: (() -> Unit)? = null,
) {
    val tokens = LocalMockzillaTokens.current
    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        onValueChangeFinished = onValueChangeFinished,
        colors = SliderDefaults.colors(
            thumbColor = tokens.accent,
            activeTrackColor = tokens.accent,
            inactiveTrackColor = tokens.bg4,
            disabledThumbColor = tokens.fg3,
            disabledActiveTrackColor = tokens.fg3,
            disabledInactiveTrackColor = tokens.bg4,
        ),
    )
}

@Preview
@Composable
private fun MockzillaSliderPreview() = PreviewSurface {
    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        MockzillaSlider(value = 0.3f, onValueChange = {})
        MockzillaSlider(value = 0.7f, onValueChange = {}, enabled = false)
    }
}
