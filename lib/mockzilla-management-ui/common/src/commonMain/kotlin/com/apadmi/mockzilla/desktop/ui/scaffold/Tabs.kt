package com.apadmi.mockzilla.desktop.ui.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.desktop.ui.utils.rotateVertically

@Immutable
internal data class VerticalTab(
    val title: String,
)

@Immutable
internal data class HorizontalTab(
    val title: String,
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
) {
    Surface(
        color = if (selected) {
            MaterialTheme.colorScheme.surfaceVariant
        } else {
            MaterialTheme.colorScheme.surface
        }
    ) {
        Text(
            text = title,
            modifier = modifier.padding(8.dp),
        )
    }
}
