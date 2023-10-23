package com.apadmi.mockzilla.desktop.ui.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
                modifier = Modifier
                    .rotateVertically(clockwise)
                    .background(
                        color = if (selected == index) {
                            MaterialTheme.colorScheme.surfaceVariant
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    )
                    .selectable(
                        selected = selected == index,
                        onClick = {
                            if (selected != index) {
                                onSelect(index)
                            }
                        },
                    )
                    .minimumInteractiveComponentSize(),
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
                icon = tab.icon,
                selected = selected == index,
                modifier = Modifier
                    .background(
                        color = if (selected == index) {
                            MaterialTheme.colorScheme.surfaceVariant
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    )
                    .selectable(
                        selected = selected == index,
                        onClick = {
                            if (selected != index) {
                                onSelect(index)
                            }
                        },
                    )
                    .minimumInteractiveComponentSize(),
            )
        }
    }
}

@Composable
private fun TabItem(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    Surface(
        color = if (selected) {
            MaterialTheme.colorScheme.surfaceVariant
        } else {
            MaterialTheme.colorScheme.surface
        },
        modifier = modifier.padding(8.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = title,
            )
        }
    }
}
