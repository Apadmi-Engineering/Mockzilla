package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.endpoints

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.desktop.di.utils.getViewModel
import com.apadmi.mockzilla.desktop.ui.widgets.endpoints.endpoints.EndpointsViewModel.*
import com.apadmi.mockzilla.lib.models.EndpointConfiguration

@Composable
fun EndpointsWidget(
    onEndpointClicked: (EndpointConfiguration.Key) -> Unit
) {
    val viewModel = getViewModel<EndpointsViewModel>()
    val state by viewModel.state.collectAsState()

    EndpointsWidgetContent(state, viewModel::onCheckboxChanged, onEndpointClicked)
}

@Composable
fun EndpointsWidgetContent(
    state: State,
    onCheckboxChanged: (EndpointConfiguration.Key, value: Boolean) -> Unit,
    onEndpointClicked: (EndpointConfiguration.Key) -> Unit
) = Column {
    when (state) {
        State.Empty -> Text("Empty")
        is State.EndpointsList -> state.endpoints.forEachIndexed { index, endpoint ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable { onEndpointClicked(endpoint.key) }
                .background(
                    if (index % 2 == 0) {
                        MaterialTheme.colorScheme.background
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = endpoint.isCheckboxTicked,
                    onCheckedChange = { onCheckboxChanged(endpoint.key, it) }
                )
                Text(endpoint.name)
            }
        }
    }
}
