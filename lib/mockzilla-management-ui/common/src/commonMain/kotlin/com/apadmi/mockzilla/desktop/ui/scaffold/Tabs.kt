package com.apadmi.mockzilla.desktop.ui.scaffold

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
import com.apadmi.mockzilla.desktop.ui.utils.rotateVertically

@Immutable
internal data class VerticalTab(
    val title: String,
)

@Immutable
internal data class HorizontalTab(
    val title: String,
    val icon: ImageVector? = null,
    val subtitle: String? = null,
)

@Composable
internal fun VerticalTabList(
    tabs: List<VerticalTab>,
    clockwise: Boolean,
    selected: Int?,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        tabs.forEachIndexed { index, tab ->
            TabItem(
                title = tab.title,
                selected = selected == index,
                onSelect = {
                    if (selected != index) {
                        onSelect(index)
                    }
                },
                modifier = Modifier.rotateVertically(clockwise)
            )
        }
    }
}

@Composable
internal fun HorizontalTabList(
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
                onSelect = {
                    if (selected != index) {
                        onSelect(index)
                    }
                },
                icon = tab.icon,
                subtitle = tab.subtitle,
            )
        }
    }
}

@Composable
private fun TabItem(
    title: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    subtitle: String? = null,
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
            icon?.let {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                )
                subtitle?.let {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}
