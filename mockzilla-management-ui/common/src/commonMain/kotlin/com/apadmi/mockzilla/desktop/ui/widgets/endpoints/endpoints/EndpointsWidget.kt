package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.endpoints

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.apadmi.mockzilla.desktop.di.utils.getViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.endpoints.endpoints.EndpointsViewModel.*

@Composable
fun EndpointsWidget() {
    val viewModel = getViewModel<EndpointsViewModel>()
    val state by viewModel.state.collectAsState()

    EndpointsWidgetContent(state)
}

@Composable
fun EndpointsWidgetContent(state: State) = Column {
    when (state) {
        State.Empty -> Text("Empty")
        is State.EndpointsList -> state.endpoints.forEach {
            Text("Endpoint: ${it.key} | ${it.name}")
        }
    }
}
