package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun CustomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    steps: Int = 0,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onValueChangeFinished: () -> Unit = {},
) {
    val colorScheme = MaterialTheme.colorScheme

    Slider(
        modifier = modifier,
        value = value,
        onValueChange = { onValueChange(it) },
        onValueChangeFinished = onValueChangeFinished,
        steps = steps,
        valueRange = valueRange,
        thumb = {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(colorScheme.primary)
            )
        },
        track = { state ->
            SliderDefaults.Track(
                sliderState = state,
                modifier = Modifier.height(2.dp),
                colors = SliderDefaults.colors(
                    activeTrackColor = colorScheme.primary,
                    inactiveTrackColor = colorScheme.outline,
                    activeTickColor = Color.Transparent,
                    inactiveTickColor = Color.Transparent,
                ),
            )
        }
    )
}

@Preview
@Composable
private fun MockzillaSliderPreview() = PreviewSurface {
    Column(
        Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        CustomSlider(
            value = 0.4f,
            onValueChange = {},
            onValueChangeFinished = {}
        )
    }
}
