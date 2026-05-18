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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceFaint
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockzillaSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    enabled: Boolean = true,
    activeTrackColor: Color? = null,
    onValueChangeFinished: (() -> Unit)? = null,
) {
    val colorScheme = MaterialTheme.colorScheme
    val activeColor = activeTrackColor ?: colorScheme.primary

    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.height(24.dp),
        enabled = enabled,
        valueRange = valueRange,
        onValueChangeFinished = onValueChangeFinished,

        thumb = {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .shadow(
                        elevation = 2.dp,
                        shape = CircleShape,
                        clip = false
                    )
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(
                            if (enabled) activeColor
                            else colorScheme.onSurfaceFaint,
                            CircleShape
                        )
                )
            }
        },

        track = { sliderState ->
            SliderDefaults.Track(
                sliderState = sliderState,
                modifier = Modifier.height(4.dp),
                colors = SliderDefaults.colors(
                    activeTrackColor = activeColor,
                    inactiveTrackColor = colorScheme.outline.copy(alpha = 0.25f),
                    disabledActiveTrackColor = colorScheme.onSurfaceFaint.copy(alpha = 0.3f),
                    disabledInactiveTrackColor = colorScheme.outline.copy(alpha = 0.1f),
                    thumbColor = Color.Transparent,
                ),
                drawStopIndicator = null,
                thumbTrackGapSize = 0.dp,
            )
        },

        colors = SliderDefaults.colors(
            thumbColor = Color.Transparent,
            activeTrackColor = activeColor,
            inactiveTrackColor = colorScheme.outline.copy(alpha = 0.25f),
        ),
    )
}

@Preview
@Composable
private fun MockzillaSliderPreview() = PreviewSurface {
    Column(
        Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        MockzillaSlider(
            value = 0.4f,
            onValueChange = {},
        )

        MockzillaSlider(
            value = 0.7f,
            onValueChange = {},
            enabled = false,
        )
    }
}