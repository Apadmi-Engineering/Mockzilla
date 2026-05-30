@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.desktop.ui.scaffold

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import com.apadmi.mockzilla.ui.ui.common.scaffold.VerticalTab
import com.apadmi.mockzilla.ui.ui.common.scaffold.VerticalTabList

private const val leftPanelMinWidthDp = 280
private const val rightPanelMinWidthDp = 720

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
    initialLeftPanelWidth: Dp = 230.dp,
    initialRightPanelWidth: Dp = 280.dp,
) {
    val density = LocalDensity.current

    var totalWidth by remember { mutableStateOf(0.dp) }

    // Each panel gets a width and a settled width so we can consume drag deltas that would reduce
    // width/height below zero (when the user drags into the tab areas) but visually display the
    // restricted width/height that can't drop below 0. This ensures the dragged divider stays
    // with the cursor at all times.
    var leftPanelWidth by remember { mutableStateOf(leftPanelMinWidthDp.dp) }
    var leftPanelSettledWidth by remember { mutableStateOf(leftPanelMinWidthDp.dp) }
    var rightPanelWidth by remember { mutableStateOf(rightPanelMinWidthDp.dp) }
    var rightPanelSettledWidth by remember { mutableStateOf(rightPanelMinWidthDp.dp) }

    // Both of the horizontal panels must collectively not be so large that the center panel
    // runs out of space. We can enforce this by hoisting the width of each panel and preventing
    // either panel from exceeding the remaining space that the other panel's current width leaves.
    val centerMinWidth = 300.dp
    val leftPanelWidthRestriction = { width: Dp ->
        if (totalWidth > 0.dp) {
            val remaining = max(
                leftPanelMinWidthDp.dp,
                totalWidth - centerMinWidth - max(rightPanelSettledWidth, rightPanelMinWidthDp.dp)
            )
            min(max(leftPanelMinWidthDp.dp, width), remaining)
        } else {
            max(leftPanelMinWidthDp.dp, width)
        }
    }
    val rightPanelWidthRestriction = { width: Dp ->
        if (totalWidth > 0.dp) {
            val remaining = max(
                rightPanelMinWidthDp.dp,
                totalWidth - centerMinWidth - max(leftPanelSettledWidth, leftPanelMinWidthDp.dp)
            )
            min(max(rightPanelMinWidthDp.dp, width), remaining)
        } else {
            max(rightPanelMinWidthDp.dp, width)
        }
    }
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .onSizeChanged { size ->
                totalWidth = with(density) { size.width.toDp() }
            },
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
                    onSelected = onSelected,
                    defaultWidth = initialLeftPanelWidth
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
                    onSelected = onSelected,
                    defaultWidth = initialRightPanelWidth
                )
            }
            BottomPanel(content = bottom)
        }
    }
}

@Composable
private fun BottomPanel(content: List<Widget>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        content.forEach { widget -> widget.ui() }
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

    val alwaysVisible = content.isNotEmpty() && content.all { it.title == null }

    if (alwaysVisible) {
        Row(modifier = Modifier.fillMaxHeight()) {
            Surface(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxHeight()
                    .width(settledWidth)
            ) {
                Column {
                    content.forEachIndexed { index, widget ->
                        if (index != 0) {
                            HorizontalDivider(Modifier.padding(vertical = 8.dp))
                        }
                        widget.ui()
                    }
                }
            }
            HorizontalDraggableDivider(
                onDrag = { offset ->
                    with(density) {
                        onWidthChange(width + offset.toDp())
                    }
                },
                onDragStopped = onDragStopped,
            )
        }
        return
    }

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
        AnimatedVisibility(
            visible = selectedWidgets.isNotEmpty(),
            enter = expandHorizontally(
                expandFrom = Alignment.End,
                animationSpec = tween(durationMillis = 160),
            ) + fadeIn(animationSpec = tween(durationMillis = 120)),
            exit = shrinkHorizontally(
                shrinkTowards = Alignment.End,
                animationSpec = tween(durationMillis = 130),
            ) + fadeOut(animationSpec = tween(durationMillis = 100)),
        ) {
            Row {
                HorizontalDraggableDivider(
                    onDrag = { offset ->
                        with(density) {
                            onWidthChange(width - offset.toDp())
                        }
                    },
                    onDragStopped = onDragStopped,
                )
                Surface(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxHeight()
                        .width(settledWidth)
                ) {
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
        }

        val tabWidgets = content.filter { it.title != null }
        VerticalDivider(color = MaterialTheme.colorScheme.outline)
        VerticalTabList(
            tabs = tabWidgets.map { widget -> VerticalTab(title = widget.title) },
            clockwise = true,
            selected = tabWidgets.indices.filter { openWidgets.contains(tabWidgets[it].id) },
            onSelect = { index ->
                onSelected(tabWidgets[index].id)
                if (width < 20.dp) {
                    onWidthChange(defaultWidth)
                }
            }
        )
    }
}
