package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted

@InternalMockzillaApi
@Suppress("MAGIC_NUMBER")
@Composable
public fun CustomToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colorScheme = MaterialTheme.colorScheme

    val trackColor by animateColorAsState(
        targetValue = if (checked) colorScheme.primary else colorScheme.surfaceContainer,
        animationSpec = tween(140),
    )
    val thumbColor by animateColorAsState(
        targetValue = if (checked) colorScheme.onPrimary else colorScheme.onSurfaceMuted,
        animationSpec = tween(140),
    )
    val thumbOffset by animateFloatAsState(
        targetValue = if (checked) 15f else 3f,
        animationSpec = tween(140),
    )

    Box(
        modifier = modifier
            .size(width = 32.dp, height = 18.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(trackColor)
            .border(width = 1.dp, color = colorScheme.outline, shape = RoundedCornerShape(10.dp))
            .clickable(enabled = enabled) { onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset.dp)
                .size(12.dp)
                .clip(CircleShape)
                .background(thumbColor),
        )
    }
}

@Preview
@Composable
private fun MzkTogglePreview() = PreviewSurface {
    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CustomToggle(checked = false, onCheckedChange = {})
            CustomToggle(checked = true, onCheckedChange = {})
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CustomToggle(checked = false, enabled = false, onCheckedChange = {})
            CustomToggle(checked = true, enabled = false, onCheckedChange = {})
        }
    }
}
