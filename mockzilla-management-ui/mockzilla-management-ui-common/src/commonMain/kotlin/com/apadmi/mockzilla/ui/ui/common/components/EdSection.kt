package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em

import com.apadmi.mockzilla.ui.ui.common.theme.LocalMockzillaTokens

import org.jetbrains.compose.ui.tooling.preview.Preview

private val sectionShape = RoundedCornerShape(8.dp)

@Suppress("MAGIC_NUMBER")
@Composable
fun EdSection(
    label: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    initiallyExpanded: Boolean = true,
    content: @Composable () -> Unit,
) {
    val tokens = LocalMockzillaTokens.current
    var expanded by rememberSaveable { mutableStateOf(initiallyExpanded) }
    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f,
        animationSpec = tween(200),
    )

    Column(
        modifier = modifier
            .clip(sectionShape)
            .border(width = 1.dp, color = tokens.line1, shape = sectionShape),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(tokens.bg2)
                .clickable { expanded = !expanded }
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                modifier = Modifier.size(10.dp).rotate(chevronRotation),
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = tokens.fg3,
            )
            if (icon != null) {
                Icon(
                    modifier = Modifier.size(12.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = tokens.accent,
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.06.em),
                color = tokens.fg1,
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(tokens.bg1)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
            ) {
                content()
            }
        }
    }
}

@Preview
@Composable
private fun EdSectionPreview() = PreviewSurface {
    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        EdSection(label = "Identity", initiallyExpanded = true) {
            Text("Content here")
        }
        EdSection(label = "Response", initiallyExpanded = false) {
            Text("Hidden content")
        }
    }
}
