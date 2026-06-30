package com.apadmi.mockzilla.ui.ui.common.components.buttons

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Expand
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceFaint

import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.RowDensity
import com.apadmi.mockzilla.ui.utils.Platform
import com.apadmi.mockzilla.ui.utils.minimumTouchTarget

private const val UNSELECTED_BORDER_ALPHA = 0.2f

private val RowDensity.icon get() = when (this) {
    RowDensity.Comfy -> Icons.Default.Expand
    RowDensity.Compact -> Icons.Default.Compress
}

@Composable
internal fun RowDensityControls(
    modifier: Modifier = Modifier,
    selected: RowDensity = RowDensity.Compact,
    onChanged: (RowDensity) -> Unit = {}
) = Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
    listOf(RowDensity.Compact, RowDensity.Comfy).forEach { density ->
        RowDensityButton(
            label = density.name.lowercase(),
            icon = density.icon,
            isSelected = selected == density,
            onClick = { onChanged(density) },
        )
    }
}

@Composable
private fun RowDensityButton(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val borderColor =
        if (isSelected) cs.onSurface else cs.onSurface.copy(alpha = UNSELECTED_BORDER_ALPHA)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .minimumTouchTarget()
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(4.dp))
            .clickable(onClick = onClick),
    ) {
        if (Platform.current == Platform.Desktop) {
            Text(
                text = label,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) cs.onSurface else cs.onSurfaceVariant,
            )
        } else {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = cs.onSurfaceFaint,
            )
        }
    }
}
