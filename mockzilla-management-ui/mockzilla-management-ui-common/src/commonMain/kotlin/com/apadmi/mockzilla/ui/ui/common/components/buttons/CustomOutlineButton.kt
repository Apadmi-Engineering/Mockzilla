@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.ui.ui.common.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.ui.ui.common.assets.LightningBolt
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface

internal enum class OutlineButtonVariant {
    Error,
    Primary,
    Secondary,
    ;
}

@Composable
internal fun CustomOutlineButton(
    modifier: Modifier = Modifier,
    label: String,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    variant: OutlineButtonVariant,
    @Suppress("UNUSED_PARAMETER") contentPadding: PaddingValues = PaddingValues(),
    onClick: () -> Unit,
) = BaseButton(
    modifier = modifier,
    label = label,
    leadingIcon = leadingIcon,
    variant = when (variant) {
        OutlineButtonVariant.Error -> ButtonVariant.Danger
        OutlineButtonVariant.Primary -> ButtonVariant.Outline
        OutlineButtonVariant.Secondary -> ButtonVariant.Soft
    },
    size = ButtonSize.Md,
    enabled = enabled,
    onClick = onClick,
)

@Preview
@Composable
private fun CustomOutlineButtonPreview() = PreviewSurface {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CustomOutlineButton(label = "Error", variant = OutlineButtonVariant.Error, onClick = {})
        CustomOutlineButton(label = "Primary", variant = OutlineButtonVariant.Primary, onClick = {})
        CustomOutlineButton(
            label = "With Icon",
            leadingIcon = Icons.LightningBolt,
            variant = OutlineButtonVariant.Secondary,
            onClick = {},
        )
    }
}
