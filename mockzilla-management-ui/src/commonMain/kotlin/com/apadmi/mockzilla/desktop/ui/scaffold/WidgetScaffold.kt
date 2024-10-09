@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.desktop.ui.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min

/**
 * @property title
 * @property ui
 * @property id
 */
data class Widget(
    val id: String,
    val title: String? = null,
    val ui: @Composable () -> Unit
)

@Suppress(
    "TOO_LONG_FUNCTION",
    "LOCAL_VARIABLE_EARLY_DECLARATION",
    "MAGIC_NUMBER"
)
@Composable
fun WidgetScaffold(
    modifier: Modifier,
    openWidgets: Set<String>,
    left: List<Widget>,
    middle: List<Widget>,
    right: List<Widget>,
    bottom: List<Widget>,
    onSelected: (String) -> Unit,
    top: @Composable () -> Unit,
) {
    val density = LocalDensity.current

    var totalWidth by remember { mutableStateOf(0.dp) }

    // Each panel gets a width and a settled width so we can consume drag deltas that would reduce
    // width/height below zero (when the user drags into the tab areas) but visually display the
    // restricted width/height that can't drop below 0. This ensures the dragged divider stays
    // with the cursor at all times.
    var leftPanelWidth by remember { mutableStateOf(350.dp) }
    var leftPanelSettledWidth by remember { mutableStateOf(350.dp) }
    var rightPanelWidth by remember { mutableStateOf(500.dp) }
    var rightPanelSettledWidth by remember { mutableStateOf(500.dp) }
    var bottomPanelHeight by remember { mutableStateOf(200.dp) }
    var bottomPanelSettledHeight by remember { mutableStateOf(200.dp) }

    // Both of the horizontal panels must collectively not be so large that the center panel
    // runs out of space. We can enforce this by hoisting the width of each panel and preventing
    // either panel from exceeding the remaining space that the other panel's current width leaves.
    val centerMinWidth = 300.dp
    val leftPanelWidthRestriction = { width: Dp ->
        if (totalWidth > 0.dp) {
            val remaining = totalWidth - centerMinWidth - max(rightPanelSettledWidth, 0.dp)
            min(max(0.dp, width), remaining)
        } else {
            max(0.dp, width)
        }
    }
    val rightPanelWidthRestriction = { width: Dp ->
        if (totalWidth > 0.dp) {
            val remaining = totalWidth - centerMinWidth - max(leftPanelSettledWidth, 0.dp)
            min(max(0.dp, width), remaining)
        } else {
            max(0.dp, width)
        }
    }
    val bottomPanelHeightRestriction = { height: Dp -> max(0.dp, height) }

    Surface(
        modifier = modifier.onSizeChanged { size ->
            totalWidth = with(density) { size.width.toDp() }
        },
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            top()
            Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                LeftPanel(
                    content = left,
                    openWidgets = openWidgets,
                    width = leftPanelWidth,
                    settledWidth = leftPanelSettledWidth,
                    onWidthChange = {
                        leftPanelWidth = it
                        leftPanelSettledWidth = leftPanelWidthRestriction(it)
                    },
                    onDragStopped = {
                        leftPanelWidth = leftPanelSettledWidth
                    },
                    onSelected = onSelected
                )
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    middle.forEach { widget ->
                        widget.title?.let { Text(it) }
                        widget.ui()
                    }
                }
                RightPanel(
                    content = right,
                    openWidgets = openWidgets,
                    width = rightPanelWidth,
                    settledWidth = rightPanelSettledWidth,
                    onWidthChange = {
                        rightPanelWidth = it
                        rightPanelSettledWidth = rightPanelWidthRestriction(it)
                    },
                    onDragStopped = {
                        rightPanelWidth = rightPanelSettledWidth
                    },
                    onSelected = onSelected
                )
            }
            BottomPanel(
                content = bottom,
                height = bottomPanelHeight,
                settledHeight = bottomPanelSettledHeight,
                onHeightChange = {
                    bottomPanelHeight = it
                    bottomPanelSettledHeight = bottomPanelHeightRestriction(it)
                },
                onDragStopped = {
                    bottomPanelHeight = bottomPanelSettledHeight
                }
            )
        }
    }
}

@Suppress(
    "TOO_LONG_FUNCTION",
    "LOCAL_VARIABLE_EARLY_DECLARATION",
    "MAGIC_NUMBER"
)
@Composable
private fun BottomPanel(
    content: List<Widget>,
    height: Dp,
    settledHeight: Dp,
    defaultHeight: Dp = 200.dp,
    onDragStopped: () -> Unit,
    onHeightChange: (Dp) -> Unit,
) {
    val density = LocalDensity.current
    var selectedWidget by remember { mutableStateOf(if (content.isEmpty()) null else 0) }

    Column(modifier = Modifier.fillMaxWidth()) {
        selectedWidget?.let {
            VerticalDraggableDivider(
                onDrag = { offset ->
                    with(density) {
                        onHeightChange(height - offset.toDp())
                    }
                },
                onDragStopped = onDragStopped
            )
        }

        Surface(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .height(selectedWidget?.let { settledHeight } ?: 0.dp)
        ) {
            selectedWidget?.let {
                content.getOrNull(it)?.ui?.invoke()
            }
        }

        HorizontalTabList(
            tabs = content.map { widget -> HorizontalTab(title = widget.title) },
            selected = selectedWidget,
            onSelect = { widget ->
                selectedWidget = widget.takeUnless { widget == selectedWidget }
                if (height < 20.dp) {
                    onHeightChange(defaultHeight)
                }
            }
        )
    }
}

@Suppress("TOO_LONG_FUNCTION")
@Composable
private fun LeftPanel(
    content: List<Widget>,
    openWidgets: Set<String>,
    width: Dp,
    settledWidth: Dp,
    onWidthChange: (Dp) -> Unit,
    onDragStopped: () -> Unit,
    onSelected: (String) -> Unit,
    defaultWidth: Dp = 100.dp
) {
    val density = LocalDensity.current
    val selectedWidgets = remember(openWidgets) {
        content.indices.filter { openWidgets.contains(content[it].id) }
    }

    Row(modifier = Modifier.fillMaxHeight()) {
        VerticalTabList(
            tabs = content.map { widget -> VerticalTab(title = widget.title) },
            clockwise = false,
            selected = selectedWidgets,
            onSelect = { widget ->
                onSelected(content[widget].id)
                if (width < 20.dp) {
                    onWidthChange(defaultWidth)
                }
            }
        )

        Surface(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxHeight()
                .width(
                    if (selectedWidgets.isEmpty()) {
                        0.dp
                    } else {
                        settledWidth
                    }
                )
        ) {
            if (selectedWidgets.isNotEmpty()) {
                Column {
                    selectedWidgets.sorted().forEachIndexed { index, widget ->
                        if (index != 0) {
                            HorizontalDivider(Modifier.padding(vertical = 8.dp))
                        }
                        content.getOrNull(widget)?.ui?.invoke()
                    }
                }
            }
        }

        if (selectedWidgets.isNotEmpty()) {
            HorizontalDraggableDivider(
                onDrag = { offset ->
                    with(density) {
                        onWidthChange(width + offset.toDp())
                    }
                },
                onDragStopped = onDragStopped,
            )
        }
    }
}

@Suppress("TOO_LONG_FUNCTION")
@Composable
private fun RightPanel(
    content: List<Widget>,
    openWidgets: Set<String>,
    width: Dp,
    settledWidth: Dp,
    onWidthChange: (Dp) -> Unit,
    onDragStopped: () -> Unit,
    defaultWidth: Dp = 100.dp,
    onSelected: (String) -> Unit
) {
    val density = LocalDensity.current
    val selectedWidgets = remember(openWidgets) {
        content.indices.filter { openWidgets.contains(content[it].id) }
    }

    Row(modifier = Modifier.fillMaxHeight()) {
        if (selectedWidgets.isNotEmpty()) {
            HorizontalDraggableDivider(
                onDrag = { offset ->
                    with(density) {
                        onWidthChange(width - offset.toDp())
                    }
                },
                onDragStopped = onDragStopped,
            )
        }

        Surface(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxHeight()
                .width(if (selectedWidgets.isEmpty()) {
                    0.dp
                } else {
                    settledWidth
                })
        ) {
            if (selectedWidgets.isNotEmpty()) {
                Column {
                    selectedWidgets.sorted().forEachIndexed { index, widget ->
                        if (index != 0) {
                            HorizontalDivider(Modifier.padding(vertical = 8.dp))
                        }
                        content.getOrNull(widget)?.ui?.invoke()
                    }
                }
            }
        }

        VerticalTabList(
            tabs = content.map { widget -> VerticalTab(title = widget.title) },
            clockwise = true,
            selected = selectedWidgets,
            onSelect = { widget ->
                onSelected(content[widget].id)
                if (width < 20.dp) {
                    onWidthChange(defaultWidth)
                }
            }
        )
    }
}
