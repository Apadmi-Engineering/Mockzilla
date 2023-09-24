package com.apadmi.mockzilla.desktop.ui.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import java.awt.Cursor


@Composable
internal fun HorizontalDraggableDivider(
    onDrag: (Offset) -> Unit
) {
    Row(
        Modifier
            .fillMaxHeight()
            .background(Color.Black)
    ) {
        Box(
            Modifier
            .fillMaxHeight()
            .width(3.dp)
            .pointerHoverIcon(PointerIcon(Cursor(Cursor.W_RESIZE_CURSOR)))
            .pointerInput(Unit) {
                detectDragGestures { a, dragAmount ->
                    if (a.previousPosition.y > a.position.y) {
                        onDrag(dragAmount)
                    }
                }
            }) {
        }
    }
}


@Composable
fun VerticalDraggableDivider(
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
