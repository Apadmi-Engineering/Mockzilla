package com.apadmi.mockzilla.ui.ui.common.scaffold

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
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
    Column(modifier = modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        tabs.forEachIndexed { index, tab ->
            TabItem(
                title = tab.title,
                selected = selected.contains(index),
                onSelect = { onSelect(index) },
                modifier = Modifier.rotateVertically(clockwise)
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
    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {
        tabs.forEachIndexed { index, tab ->
            TabItem(
                title = tab.title,
                selected = selected == index,
                onSelect = { onSelect(index) },
                leadingIcon = tab.leadingIcon,
                subtitle = tab.subtitle,
                trailing = tab.trailing,
                modifier = tab.modifier
            )
        }
    }
}

@Composable
private fun TabItem(
    title: String?,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    subtitle: String? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    Surface(
        color = if (selected) {
            MaterialTheme.colorScheme.surfaceVariant
        } else {
            MaterialTheme.colorScheme.surface
        },
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .selectable(
                    selected = selected,
                    onClick = onSelect,
                )
                .minimumInteractiveComponentSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIcon?.let {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                title?.let { Text(text = title) }
                subtitle?.let {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
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
            VerticalTab(title = "Tab 1"),
            VerticalTab(title = "Tab 2"),
            VerticalTab(title = "Tab 3"),
            VerticalTab(title = "Tab 4")
        ),
        clockwise = false,
        selected = listOf(),
        onSelect = {}
    )
}

@Preview
@Composable
private fun HorizontalTabListPreview() = PreviewSurface {
    HorizontalTabList(
        tabs = listOf(
            HorizontalTab(title = "Tab 1"),
            HorizontalTab(title = "Tab 2"),
            HorizontalTab(title = "Tab 3"),
            HorizontalTab(title = "Tab 4")
        ),
        selected = 0,
        onSelect = {}
    )
}
