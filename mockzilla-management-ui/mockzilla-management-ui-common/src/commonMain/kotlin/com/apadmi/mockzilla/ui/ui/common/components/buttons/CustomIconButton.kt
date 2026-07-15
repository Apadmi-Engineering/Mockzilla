package com.apadmi.mockzilla.ui.ui.common.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.ui.ui.common.assets.LightningBolt
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
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

@Composable
@Preview
private fun CustomIconButtonPreview(darkTheme: Boolean = false) = PreviewSurface(darkTheme) {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CustomIconButton(
            onClick = {},
            imageVector = Icons.LightningBolt,
            iconTint = LocalContentColor.current,
            contentDescription = "",
        )
        CustomIconButton(
            onClick = {},
            imageVector = Icons.LightningBolt,
            iconTint = LocalContentColor.current,
            contentDescription = "",
            enabled = false,
        )
    }
}

@Composable
@Preview
private fun CustomIconButtonDarkPreview() = CustomIconButtonPreview(darkTheme = true)
