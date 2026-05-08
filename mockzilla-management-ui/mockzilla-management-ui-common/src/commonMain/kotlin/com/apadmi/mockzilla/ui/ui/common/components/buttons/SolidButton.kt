package com.apadmi.mockzilla.ui.ui.common.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.ui.ui.common.assets.LightningBolt
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface

import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SolidButton(
    modifier: Modifier = Modifier,
    label: String,
    leadingIcon: ImageVector? = null,
    @Suppress("UNUSED_PARAMETER") contentPadding: PaddingValues = PaddingValues(),
    enabled: Boolean = true,
    onClick: () -> Unit,
) = BaseButton(
    modifier = modifier,
    label = label,
    leadingIcon = leadingIcon,
    variant = ButtonVariant.Solid,
    size = ButtonSize.Md,
    enabled = enabled,
    onClick = onClick,
)

@Preview
@Composable
private fun SolidButtonPreview() = PreviewSurface {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SolidButton(label = "Save", onClick = {})
        SolidButton(label = "With Icon", leadingIcon = Icons.LightningBolt, onClick = {})
        SolidButton(label = "Disabled", enabled = false, onClick = {})
    }
}
