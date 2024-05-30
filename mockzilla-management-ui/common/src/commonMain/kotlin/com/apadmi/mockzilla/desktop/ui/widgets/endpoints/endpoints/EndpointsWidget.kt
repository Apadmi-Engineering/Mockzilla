package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.endpoints

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.desktop.di.utils.getViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.endpoints.endpoints.EndpointsViewModel.*
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.EndpointConfiguration

@Composable
fun EndpointsWidget(
    onEndpointClicked: (EndpointConfiguration.Key) -> Unit
) {
    val viewModel = getViewModel<EndpointsViewModel>()
    val state by viewModel.state.collectAsState()

    EndpointsWidgetContent(state, onEndpointClicked)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EndpointsWidgetContent(
    state: State,
    onEndpointClicked: (EndpointConfiguration.Key) -> Unit
) = Column {
    when (state) {
        State.Empty -> Text("Empty")
        is State.EndpointsList -> state.endpoints.forEach {
            Text(
                modifier = Modifier
                    .padding(12.dp)
                    .clickable { onEndpointClicked(it.key) },
                text = "Endpoint: ${it.key} | ${it.name}"
            )
        }
    }
}
