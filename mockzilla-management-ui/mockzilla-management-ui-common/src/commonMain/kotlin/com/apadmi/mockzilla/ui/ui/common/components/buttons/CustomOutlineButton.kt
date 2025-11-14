@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.ui.ui.common.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.ui.ui.common.assets.LightningBolt
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val surfaceAlpha = 0.05f

internal enum class OutlineButtonVariant {
    Error,
    Primary,
    Secondary,
    ;

    @Composable
    internal fun colors(): ButtonColors {
        val base = ButtonDefaults.outlinedButtonColors()

        return when (this) {
            Secondary -> base.copy(
                contentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = surfaceAlpha)
            )

            Error -> base.copy(
                contentColor = MaterialTheme.colorScheme.error,
                containerColor = MaterialTheme.colorScheme.error.copy(alpha = surfaceAlpha)
                    .compositeOver(MaterialTheme.colorScheme.surface)
            )

            Primary -> base

        }
    }
}

@Composable
internal fun CustomOutlineButton(
    modifier: Modifier = Modifier,
    label: String,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    variant: OutlineButtonVariant,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    onClick: () -> Unit
) = OutlinedButton(
    modifier = modifier,
    onClick = onClick,
    colors = variant.colors(),
    enabled = enabled,
    shape = RoundedCornerShape(8.dp),
    border = BorderStroke(
        width = 1.dp, color = when (variant) {
            OutlineButtonVariant.Secondary -> MaterialTheme.colorScheme.outline
            OutlineButtonVariant.Error -> MaterialTheme.colorScheme.error
            OutlineButtonVariant.Primary -> MaterialTheme.colorScheme.primaryContainer
        }
    ),
    contentPadding = contentPadding
) {
    leadingIcon?.let { icon ->
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = icon,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(10.dp))
    }
    Text(text = label)
}

@Preview
@Composable
private fun CustomOutlineButtonPreview() = PreviewSurface {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CustomOutlineButton(
            label = "Click me",
            variant = OutlineButtonVariant.Error,
            onClick = {}
        )
        CustomOutlineButton(
            label = "Click me",
            leadingIcon = Icons.LightningBolt,
            variant = OutlineButtonVariant.Secondary,
            onClick = {}
        )
    }
}
