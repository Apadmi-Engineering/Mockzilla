package com.apadmi.mockzilla.ui.ui.common.components.buttons

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.ui.ui.common.assets.LightningBolt
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.theme.theme_accent
import com.apadmi.mockzilla.ui.ui.common.theme.theme_background_dark
import com.apadmi.mockzilla.ui.ui.common.theme.theme_success
import com.apadmi.mockzilla.ui.ui.common.theme.theme_warning
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SolidButton(
    modifier: Modifier = Modifier,
    label: String,
    leadingIcon: ImageVector? = null,
    backgroundColor: Color,
    contentColor: Color,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    onClick: () -> Unit
) = Button(
    modifier = modifier,
    onClick = onClick,
    shape = RoundedCornerShape(8.dp),
    colors = ButtonColors(
        containerColor = backgroundColor,
        contentColor = contentColor,
        disabledContentColor = backgroundColor,
        disabledContainerColor = contentColor
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
private fun SolidButtonPreview() = PreviewSurface {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SolidButton(
            label = "Click me",
            backgroundColor = theme_warning,
            contentColor = theme_success,
            onClick = {}
        )
        SolidButton(
            label = "Click me",
            leadingIcon = Icons.LightningBolt,
            backgroundColor = theme_background_dark,
            contentColor = theme_accent,
            onClick = {}
        )
    }
}
