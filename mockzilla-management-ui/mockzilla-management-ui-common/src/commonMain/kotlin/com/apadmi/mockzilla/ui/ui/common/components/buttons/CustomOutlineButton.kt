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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla_management_ui_common.generated.resources.Res
import com.apadmi.mockzilla_management_ui_common.generated.resources.lightning_bolt
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CustomOutlineButton(
    modifier: Modifier = Modifier,
    label: String,
    leadingIcon: Painter? = null,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    borderColor: Color = MaterialTheme.colorScheme.primary,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    onClick: () -> Unit
) = OutlinedButton(
    modifier = modifier,
    onClick = onClick,
    colors = colors,
    enabled = enabled,
    shape = RoundedCornerShape(8.dp),
    border = BorderStroke(width = 1.dp, color = borderColor),
    contentPadding = contentPadding
) {
    leadingIcon?.let { icon ->
        Icon(
            modifier = Modifier.size(16.dp),
            painter = icon,
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
            onClick = {}
        )
        CustomOutlineButton(
            label = "Click me",
            leadingIcon = painterResource(resource = Res.drawable.lightning_bolt),
            onClick = {}
        )
    }
}
