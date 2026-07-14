package com.apadmi.mockzilla.ui.ui.common.components.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.ui.utils.iconButtonSize

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
    IconButton(
        onClick = onClick,
        modifier = modifier.iconButtonSize(),
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
