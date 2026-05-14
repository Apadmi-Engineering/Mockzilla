package com.apadmi.mockzilla.ui.ui.common.components.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.ui.ui.common.assets.LightningBolt
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface

import org.jetbrains.compose.ui.tooling.preview.Preview

enum class ButtonVariant {
    Danger, Ghost, Outline, Soft, Solid
}
enum class ButtonSize {
    Lg, Md, Sm
}
enum class ButtonContentAlignment {
    Center, Start
}

@Suppress("MAGIC_NUMBER")
@Composable
fun BaseButton(
    modifier: Modifier = Modifier,
    label: String,
    leadingIcon: ImageVector? = null,
    variant: ButtonVariant = ButtonVariant.Solid,
    size: ButtonSize = ButtonSize.Md,
    contentAlignment: ButtonContentAlignment = ButtonContentAlignment.Center,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val bgTarget = when (variant) {
        ButtonVariant.Ghost -> if (isHovered && enabled) colorScheme.surfaceVariant else Color.Transparent
        ButtonVariant.Outline -> Color.Transparent
        ButtonVariant.Solid -> if (isHovered && enabled) colorScheme.inversePrimary else colorScheme.primary
        ButtonVariant.Soft -> colorScheme.surfaceVariant
        ButtonVariant.Danger -> Color.Transparent
    }
    val bgColor by animateColorAsState(targetValue = bgTarget, animationSpec = tween(140))

    val contentColor = when (variant) {
        ButtonVariant.Ghost -> colorScheme.onSurfaceVariant
        ButtonVariant.Outline -> colorScheme.onSurface
        ButtonVariant.Solid -> colorScheme.onPrimary
        ButtonVariant.Soft -> colorScheme.onSurface
        ButtonVariant.Danger -> colorScheme.error
    }

    val borderTarget = when (variant) {
        ButtonVariant.Ghost -> Color.Transparent
        ButtonVariant.Outline -> if (isHovered && enabled) colorScheme.outlineVariant else colorScheme.outline
        ButtonVariant.Solid -> Color.Transparent
        ButtonVariant.Soft -> colorScheme.outline
        ButtonVariant.Danger -> colorScheme.error.copy(alpha = 0.3f)
    }
    val borderColor by animateColorAsState(targetValue = borderTarget, animationSpec = tween(140))

    val contentPadding = when (size) {
        ButtonSize.Sm -> PaddingValues(vertical = 4.dp, horizontal = 8.dp)
        ButtonSize.Md -> PaddingValues(vertical = 6.dp, horizontal = 10.dp)
        ButtonSize.Lg -> PaddingValues(vertical = 10.dp, horizontal = 14.dp)
    }

    val iconSize = if (size == ButtonSize.Lg) 18.dp else 14.dp

    Button(
        modifier = modifier.alpha(if (enabled) 1f else 0.5f),
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonColors(
            containerColor = bgColor,
            contentColor = contentColor,
            disabledContainerColor = bgColor,
            disabledContentColor = contentColor,
        ),
        border = BorderStroke(1.dp, borderColor),
        contentPadding = contentPadding,
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
        interactionSource = interactionSource,
    ) {
        leadingIcon?.let { icon ->
            Icon(
                modifier = Modifier.size(iconSize),
                imageVector = icon,
                contentDescription = null,
            )
            Spacer(Modifier.width(8.dp))
        }
        Text(text = label)
        if (contentAlignment == ButtonContentAlignment.Start) {
            Spacer(Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun BaseButtonPreview() = PreviewSurface {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ButtonVariant.entries.forEach { variant ->
            BaseButton(label = variant.name, variant = variant, onClick = {})
        }
        BaseButton(
            label = "With Icon",
            leadingIcon = Icons.LightningBolt,
            variant = ButtonVariant.Solid,
            onClick = {},
        )
        BaseButton(label = "Disabled", variant = ButtonVariant.Solid, enabled = false, onClick = {})
    }
}

@Preview
@Composable
private fun BaseButtonDarkPreview() = PreviewSurface(darkTheme = true) {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ButtonVariant.entries.forEach { variant ->
            BaseButton(label = variant.name, variant = variant, onClick = {})
        }
    }
}
