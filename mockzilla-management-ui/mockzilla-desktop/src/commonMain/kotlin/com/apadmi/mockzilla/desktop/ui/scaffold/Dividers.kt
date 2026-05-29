package com.apadmi.mockzilla.desktop.ui.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.desktop.ui.utils.horizontalResizeCursor
import com.apadmi.mockzilla.desktop.ui.utils.verticalResizeCursor
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface

import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun HorizontalDraggableDivider(
    onDrag: (Float) -> Unit,
    onDragStopped: () -> Unit,
) {
    val dividerColor = MaterialTheme.colorScheme.outline
    Box(
        modifier = Modifier
            .background(color = dividerColor)
            .fillMaxHeight()
            .width(1.dp)
            .horizontalResizeCursor()
            .draggable(
                state = rememberDraggableState { dragAmount ->
                    onDrag(dragAmount)
                },
                orientation = Orientation.Horizontal,
                onDragStopped = { onDragStopped() },
            )
    )
}

@Composable
internal fun VerticalDraggableDivider(
    onDrag: (Float) -> Unit,
    onDragStarted: () -> Unit = {},
    onDragStopped: () -> Unit,
) {
    val dividerColor = MaterialTheme.colorScheme.outline
    Box(
        modifier = Modifier
            .background(color = dividerColor)
            .fillMaxWidth()
            .height(1.dp)
            .verticalResizeCursor()
            .draggable(
                state = rememberDraggableState { dragAmount ->
                    onDrag(dragAmount)
                },
                orientation = Orientation.Vertical,
                onDragStarted = { _ -> onDragStarted() },
                onDragStopped = { onDragStopped() },
            )
    )
}

@Preview
@Composable
private fun HorizontalDraggableDividerPreview() = PreviewSurface {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(20.dp)
    ) {
        HorizontalDraggableDivider(
            onDrag = {},
            onDragStopped = {}
        )
    }
}

@Preview
@Composable
private fun VerticalDraggableDividerPreview() = PreviewSurface {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(20.dp)
    ) {
        VerticalDraggableDivider(
            onDrag = {},
            onDragStopped = {}
        )
    }
}
