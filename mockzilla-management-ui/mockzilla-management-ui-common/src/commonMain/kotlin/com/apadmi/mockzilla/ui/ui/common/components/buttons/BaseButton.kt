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
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMockzillaTokens

import org.jetbrains.compose.ui.tooling.preview.Preview

enum class ButtonVariant { Ghost, Outline, Solid, Soft, Danger }
enum class ButtonSize { Sm, Md, Lg }

@Suppress("MAGIC_NUMBER")
@Composable
fun BaseButton(
    modifier: Modifier = Modifier,
    label: String,
    leadingIcon: ImageVector? = null,
    variant: ButtonVariant = ButtonVariant.Solid,
    size: ButtonSize = ButtonSize.Md,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val tokens = LocalMockzillaTokens.current
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val bgTarget = when (variant) {
        ButtonVariant.Ghost -> if (isHovered && enabled) tokens.bg3 else Color.Transparent
        ButtonVariant.Outline -> Color.Transparent
        ButtonVariant.Solid -> if (isHovered && enabled) tokens.accent2 else tokens.accent
        ButtonVariant.Soft -> tokens.bg3
        ButtonVariant.Danger -> Color.Transparent
    }
    val bgColor by animateColorAsState(targetValue = bgTarget, animationSpec = tween(140))

    val contentColor = when (variant) {
        ButtonVariant.Ghost -> tokens.fg1
        ButtonVariant.Outline -> tokens.fg0
        ButtonVariant.Solid -> tokens.accentFg
        ButtonVariant.Soft -> tokens.fg0
        ButtonVariant.Danger -> tokens.err
    }

    val borderTarget = when (variant) {
        ButtonVariant.Ghost -> Color.Transparent
        ButtonVariant.Outline -> if (isHovered && enabled) tokens.line2 else tokens.line1
        ButtonVariant.Solid -> Color.Transparent
        ButtonVariant.Soft -> tokens.line1
        ButtonVariant.Danger -> tokens.err.copy(alpha = 0.3f)
    }
    val borderColor by animateColorAsState(targetValue = borderTarget, animationSpec = tween(140))

    val contentPadding = when (size) {
        ButtonSize.Sm -> PaddingValues(vertical = 4.dp, horizontal = 8.dp)
        ButtonSize.Md -> PaddingValues(vertical = 6.dp, horizontal = 10.dp)
        ButtonSize.Lg -> PaddingValues(vertical = 10.dp, horizontal = 14.dp)
    }

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
                modifier = Modifier.size(14.dp),
                imageVector = icon,
                contentDescription = null,
            )
            Spacer(Modifier.width(6.dp))
        }
        Text(text = label)
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
