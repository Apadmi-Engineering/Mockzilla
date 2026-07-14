package com.apadmi.mockzilla.ui.ui.common.components.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.apadmi.mockzilla.lib.InternalMockzillaApi

@InternalMockzillaApi
public typealias IconDecorationScope = @Composable (Modifier) -> Unit

@InternalMockzillaApi
@Composable
public fun CustomIconButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    iconTint: Color,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconSize: Dp? = null,
    iconDecoration: @Composable (IconDecorationScope) -> Unit = { content ->
        content(Modifier)
    },
) {
    ButtonTooltip(
        label = contentDescription,
    ) {
        IconButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
        ) {
            iconDecoration { iconModifier ->
                Icon(
                    imageVector = imageVector,
                    contentDescription = contentDescription,
                    modifier = iconSize?.let { iconModifier.size(it) } ?: iconModifier,
                    tint = iconTint,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ButtonTooltip(
    label: String,
    content: @Composable () -> Unit,
) {
    TooltipBox(
        positionProvider = TooltipDefaults
            .rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
        tooltip = {
            PlainTooltip {
                Text(text = label, style = MaterialTheme.typography.labelSmall)
            }
        },
        state = rememberTooltipState(),
        content = content,
    )
}
