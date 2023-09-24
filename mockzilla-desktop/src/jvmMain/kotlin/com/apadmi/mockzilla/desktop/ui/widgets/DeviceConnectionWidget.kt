package com.apadmi.mockzilla.desktop.ui.widgets

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import com.apadmi.mockzilla.desktop.ui.components.PreviewSurface

@Composable
fun DeviceConnectionWidget() {
    DeviceConnectionContent()
}

@Composable
fun DeviceConnectionContent() {

}

@Composable
@Preview
fun DeviceConnectionWidgetPreview() = PreviewSurface {
    DeviceConnectionContent()
}