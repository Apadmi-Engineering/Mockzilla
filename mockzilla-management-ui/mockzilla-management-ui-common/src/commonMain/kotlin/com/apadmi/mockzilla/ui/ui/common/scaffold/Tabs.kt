package com.apadmi.mockzilla.ui.ui.common.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMockzillaTokens
import com.apadmi.mockzilla.ui.ui.desktop.utils.rotateVertically

import org.jetbrains.compose.ui.tooling.preview.Preview

@Immutable
data class VerticalTab(
    val title: String?,
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
    val tokens = LocalMockzillaTokens.current
    Column(
        modifier = modifier
            .background(tokens.bg1)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        tabs.forEachIndexed { index, tab ->
            val isSelected = selected.contains(index)
            TabItem(
                title = tab.title,
                selected = isSelected,
                onSelect = { onSelect(index) },
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
    val tokens = LocalMockzillaTokens.current
    Row(
        modifier = modifier
            .background(tokens.bg1)
            .border(width = 1.dp, color = tokens.line1)
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
    val tokens = LocalMockzillaTokens.current
    Box(
        modifier = modifier
            .background(if (selected) tokens.bg3 else Color.Transparent)
            .then(
                if (selected) Modifier.border(width = 1.dp, color = tokens.line2) else Modifier
            )
            .selectable(selected = selected, onClick = onSelect)
            .heightIn(min = 36.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingContent?.invoke()
            leadingIcon?.let {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = if (selected) tokens.accent else tokens.fg2,
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            leadingContent?.let {
                Spacer(modifier = Modifier.width(4.dp))
            }
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {
                title?.let {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (selected) tokens.fg0 else tokens.fg1,
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
            trailing?.let {
                Spacer(modifier = Modifier.width(4.dp))
                it()
            }
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
