package com.apadmi.mockzilla.ui.ui.common.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.theme.darkSurface
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.desktop.utils.rotateVertically

@Immutable
data class VerticalTab(
    val title: String?,
    val leadingIcon: ImageVector? = null,
    val leadingContent: (@Composable () -> Unit)? = null,
)

@Immutable
data class HorizontalTab(
    val title: String?,
    val leadingIcon: ImageVector? = null,
    val leadingContent: (@Composable () -> Unit)? = null,
    val subtitle: String? = null,
    val trailing: (@Composable () -> Unit)? = null,
    val modifier: Modifier = Modifier,
)

@Composable
fun VerticalTabList(
    tabs: List<VerticalTab>,
    clockwise: Boolean,
    selected: Collection<Int>,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    val isDark = colorScheme.surface == darkSurface
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(colorScheme.surface)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
    ) {
        tabs.forEachIndexed { index, tab ->
            val isSelected = selected.contains(index)
            TabItem(
                title = tab.title,
                selected = isSelected,
                onSelect = { onSelect(index) },
                leadingIcon = tab.leadingIcon,
                leadingContent = tab.leadingContent,
                modifier = Modifier.rotateVertically(clockwise),
            )
        }
    }
}

@Composable
fun HorizontalTabList(
    modifier: Modifier = Modifier,
    tabs: List<HorizontalTab>,
    selected: Int?,
    onSelect: (Int) -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = modifier
            .background(colorScheme.surfaceContainer)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = colorScheme.outline,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }
            .horizontalScroll(rememberScrollState()),
    ) {
        tabs.forEachIndexed { index, tab ->
            TabItem(
                title = tab.title,
                selected = selected == index,
                onSelect = { onSelect(index) },
                leadingIcon = tab.leadingIcon,
                leadingContent = tab.leadingContent,
                subtitle = tab.subtitle,
                trailing = tab.trailing,
                modifier = tab.modifier,
            )
        }
    }
}

@Suppress("MAGIC_NUMBER")
@Composable
private fun TabItem(
    title: String?,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    subtitle: String? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    val colorScheme = MaterialTheme.colorScheme
    val indicatorColor = if (selected) colorScheme.primary else Color.Transparent
    Column(
        modifier = modifier
            .background(
                if (selected) colorScheme.surfaceContainerHighest.copy(alpha = 0.5f) else Color.Transparent
            )
            .drawBehind {
                val strokePx = 2.dp.toPx()
                drawRect(
                    color = indicatorColor,
                    topLeft = Offset(0f, size.height - strokePx),
                    size = Size(size.width, strokePx),
                )
            }
            .selectable(selected = selected, onClick = onSelect),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingContent?.invoke()
            leadingIcon?.let {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = if (selected) colorScheme.primary else colorScheme.onSurfaceMuted,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            leadingContent?.let {
                Spacer(modifier = Modifier.width(8.dp))
            }
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {
                title?.let {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (selected) colorScheme.primary else colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        softWrap = false,
                    )
                }
                subtitle?.let {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSurfaceMuted,
                    )
                }
            }
            trailing?.let {
                Spacer(modifier = Modifier.width(8.dp))
                it()
            }
        }
        if (selected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(colorScheme.primary)
            )
        } else {
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}

@Preview
@Composable
private fun VerticalTabListPreview() = PreviewSurface {
    VerticalTabList(
        tabs = listOf(
            VerticalTab(title = "Endpoints"),
            VerticalTab(title = "Logs"),
            VerticalTab(title = "Debug"),
        ),
        clockwise = false,
        selected = listOf(0),
        onSelect = {},
    )
}

@Preview
@Composable
private fun HorizontalTabListPreview() = PreviewSurface {
    HorizontalTabList(
        tabs = listOf(
            HorizontalTab(title = "Device 1", subtitle = "Connected"),
            HorizontalTab(title = "Device 2", subtitle = "Disconnected"),
            HorizontalTab(title = "+ Add device"),
        ),
        selected = 0,
        onSelect = {},
    )
}
