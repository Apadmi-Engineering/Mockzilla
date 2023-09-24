package com.apadmi.mockzilla.desktop.ui.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import com.apadmi.mockzilla.desktop.ui.components.DeviceTabsWidget
import java.awt.Cursor


class Widget(
    val title: String,
    val ui: @Composable () -> Unit
)

@Composable
fun WidgetScaffold(
    modifier: Modifier,
    top: @Composable () -> Unit,
    left: List<Widget>,
    middle: List<Widget>,
    right: List<Widget>,
    bottom: List<Widget>,
    density: Density = LocalDensity.current
) = Column(modifier = modifier.background(MaterialTheme.colorScheme.background)) {
    val sidePanelsCanGrow = remember { mutableStateOf(true) }
    Row(modifier.fillMaxWidth().weight(1f)) {
        LeftPanel(left, sidePanelsCanGrow)
        Row(modifier.weight(1f).onSizeChanged {
            sidePanelsCanGrow.value = with(density) { it.width.toDp() } > 150.dp
        }) {
            middle.forEach {
                Text(it.title)
                it.ui()
            }
        }
        RightPanel(right, sidePanelsCanGrow)
    }

    BottomPanel(bottom)
}

@Composable
private fun BottomPanel(
    content: List<Widget>,
    defaultHeight: Dp = 100.dp
) {
    val density = LocalDensity.current
    var height by remember { mutableStateOf(defaultHeight) }
    var selectedWidget by remember { mutableStateOf(if (content.isEmpty()) null else 0) }

    Column(modifier = Modifier.fillMaxWidth()) {
        if (selectedWidget != null) {
            PanelTopBar {
                with(density) {
                    height = max(0.dp, height - it.y.toDp())
                }
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(if (selectedWidget == null) 0.dp else height)
                .background(Color.DarkGray)
        ) {
            selectedWidget?.let {
                content[it].ui()
            }
        }

        Row() {
            content.forEachIndexed { index, widget ->
                Row(modifier = Modifier
                    .clickable {
                        if (selectedWidget == index) {
                            selectedWidget = null
                        } else {
                            selectedWidget = index
                        }

                        if (height < 20.dp) {
                            height = defaultHeight
                        }
                    }
                    .padding(8.dp)) {
                    Text(widget.title + "  ${height}")
                }
            }
        }
    }
}

@Composable
private fun LeftPanel(
    content: List<Widget>,
    canGrow: MutableState<Boolean>,
    defaultWidth: Dp = 100.dp
) {
    val density = LocalDensity.current
    var width by remember { mutableStateOf(defaultWidth) }
    var selectedWidget by remember { mutableStateOf(if (content.isEmpty()) null else 0) }

    Row(modifier = Modifier.fillMaxHeight()) {
        Column() {
            content.forEachIndexed { index, widget ->
                Row(modifier = Modifier
                    .clickable {
                        if (width < 20.dp) {
                            width = defaultWidth
                            selectedWidget = index
                        } else if (selectedWidget == index) {
                            selectedWidget = null
                        } else {
                            selectedWidget = index

                        }

                    }
                    .padding(8.dp)) {
                    Text(modifier = Modifier.rotateVertically(), text = widget.title + "  ${width}")
                }
            }
        }

        Box(
            Modifier
                .width(if (selectedWidget == null) 0.dp else width)
                .background(Color.DarkGray)
        ) {
            selectedWidget?.let {
                content[it].ui()
            }
        }

        if (selectedWidget != null) {
            PanelLeftBar {
                val newWidth = with(density) { max(0.dp, width + it.x.toDp()) }
                if (newWidth < width || canGrow.value) {
                    width = newWidth
                }
            }
        }

    }
}

@Composable
private fun RightPanel(
    content: List<Widget>,
    canGrow: MutableState<Boolean>,
    defaultWidth: Dp = 100.dp
) {
    val density = LocalDensity.current
    var width by remember { mutableStateOf(defaultWidth) }
    var selectedWidget by remember { mutableStateOf(if (content.isEmpty()) null else 0) }

    Row(modifier = Modifier.fillMaxHeight()) {
        if (selectedWidget != null) {
            PanelLeftBar {
                with(density) {
                    val newWidth = max(0.dp, width - it.x.toDp())
                    if (newWidth < width || canGrow.value) {
                        width = newWidth
                    }
                }
            }
        }

        Box(
            Modifier
                .width(if (selectedWidget == null) 0.dp else width)
                .background(Color.DarkGray)
        ) {
            selectedWidget?.let {
                content[it].ui()
            }
        }

        Column() {
            content.forEachIndexed { index, widget ->
                Row(modifier = Modifier
                    .clickable {
                        if (selectedWidget == index) {
                            selectedWidget = null
                        } else {
                            selectedWidget = index
                        }

                        if (width < 20.dp) {
                            width = defaultWidth
                        }
                    }
                    .padding(8.dp)) {
                    Text(modifier = Modifier.rotateVertically(), text = widget.title + "  ${width}")
                }
            }
        }
    }
}


fun Modifier.rotateVertically(clockwise: Boolean = true): Modifier {
    val rotate = rotate(if (clockwise) 90f else -90f)

    val adjustBounds = layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.height, placeable.width) {
            placeable.place(
                x = -(placeable.width / 2 - placeable.height / 2),
                y = -(placeable.height / 2 - placeable.width / 2)
            )
        }
    }
    return rotate then adjustBounds
}

@Composable
private fun PanelTopBar(
    onDrag: (Offset) -> Unit
) {
    var shouldEmitDragEvents by remember { mutableStateOf(true) }
    Row(
        Modifier
            .fillMaxWidth()
            .onGloballyPositioned {
                shouldEmitDragEvents = it.positionInRoot().y > 40
            }
            .background(Color.Black)
    ) {
        Box(Modifier
            .weight(1f)
            .height(3.dp)
            .pointerHoverIcon(PointerIcon(Cursor(Cursor.N_RESIZE_CURSOR)))
            .pointerInput(Unit) {
                detectDragGestures { a, dragAmount ->
                    if (shouldEmitDragEvents || a.previousPosition.y < a.position.y) {
                        onDrag(dragAmount)
                    }
                }
            }) {
        }
    }
}


@Composable
private fun PanelLeftBar(
    onDrag: (Offset) -> Unit
) {
    var shouldEmitDragEvents by remember { mutableStateOf(true) }
    Row(
        Modifier
            .fillMaxHeight()
            .onGloballyPositioned {
                shouldEmitDragEvents = it.positionInRoot().x > 40
            }
            .background(Color.Black)
    ) {
        Box(Modifier
            .fillMaxHeight()
            .width(3.dp)
            .pointerHoverIcon(PointerIcon(Cursor(Cursor.W_RESIZE_CURSOR)))
            .pointerInput(Unit) {
                detectDragGestures { a, dragAmount ->
                    if (shouldEmitDragEvents || a.previousPosition.y > a.position.y) {
                        onDrag(dragAmount)
                    }
                }
            }) {
        }
    }
}
