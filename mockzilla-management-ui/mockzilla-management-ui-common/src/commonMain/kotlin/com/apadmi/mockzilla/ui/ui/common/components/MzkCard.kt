package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.ui.ui.common.theme.LocalMockzillaTokens

import org.jetbrains.compose.ui.tooling.preview.Preview

@Suppress("MAGIC_NUMBER")
private val cardShape = RoundedCornerShape(10.dp)

@Suppress("MAGIC_NUMBER")
@Composable
fun MzkCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    titleIcon: ImageVector? = null,
    actions: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val tokens = LocalMockzillaTokens.current
    Column(
        modifier = modifier
            .clip(cardShape)
            .background(color = tokens.bg2)
            .border(width = 1.dp, color = tokens.line1, shape = cardShape),
    ) {
        if (title != null || titleIcon != null || actions != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    title?.let {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,
                            color = tokens.fg0,
                        )
                    }
                    subtitle?.let {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = tokens.fg2,
                        )
                    }
                }
                actions?.invoke()
            }
        }
        content()
    }
}

@Preview
@Composable
private fun MzkCardPreview() = PreviewSurface {
    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        MzkCard(title = "Card title", subtitle = "Subtitle") {
            Text("Content", Modifier.padding(12.dp))
        }
        MzkCard {
            Text("No header card", Modifier.padding(12.dp))
        }
    }
}
