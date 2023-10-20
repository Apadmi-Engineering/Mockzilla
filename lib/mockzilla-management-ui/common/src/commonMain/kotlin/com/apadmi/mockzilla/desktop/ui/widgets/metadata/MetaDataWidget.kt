package com.apadmi.mockzilla.desktop.ui.widgets.metadata

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import com.apadmi.mockzilla.desktop.di.utils.getViewModel
import com.apadmi.mockzilla.desktop.ui.components.PreviewSurface
import com.apadmi.mockzilla.desktop.ui.widgets.metadata.MetaDataWidgetViewModel.*

import com.airbnb.android.showkase.annotation.ShowkaseComposable

@Composable
fun MetaDataWidget() {
    val viewModel = getViewModel<MetaDataWidgetViewModel>()
    val state by viewModel.state.collectAsState()

    MetaDataWidgetContent(state)
}

@Composable
fun MetaDataWidgetContent(
    state: State
) = Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Text(state.toString())
}

@ShowkaseComposable("MetaDataWidget-NoDeviceConnected", "MetaDataWidget")
@Composable
@Preview
fun MetaDataWidgetContentPreview() = PreviewSurface {
    MetaDataWidgetContent(State.NoDeviceConnected)
}
