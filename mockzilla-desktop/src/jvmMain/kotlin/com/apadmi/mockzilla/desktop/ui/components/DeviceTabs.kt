package com.apadmi.mockzilla.desktop.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun DeviceTabsWidget(
    modifier: Modifier
) = DeviceTabsWidgetContent(modifier, devices = listOf("Device 1", "Device 2"))

@Composable
fun DeviceTabsWidgetContent(
    modifier: Modifier,
    devices: List<String>
) = Row(modifier = modifier.fillMaxWidth()) {
    devices.forEach {
        Text("$it |", color = MaterialTheme.colorScheme.onBackground)
    }
    Text(text = "+ Add Device")
}