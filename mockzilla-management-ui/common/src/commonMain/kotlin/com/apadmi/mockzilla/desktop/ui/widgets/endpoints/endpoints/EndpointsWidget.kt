package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.endpoints

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.desktop.di.utils.getViewModel
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.i18n.LocalStrings
import com.apadmi.mockzilla.desktop.i18n.Strings
import com.apadmi.mockzilla.desktop.ui.components.StandardTextTooltip
import com.apadmi.mockzilla.desktop.ui.theme.alternatingBackground
import com.apadmi.mockzilla.desktop.ui.widgets.endpoints.endpoints.EndpointsViewModel.*
import com.apadmi.mockzilla.lib.models.EndpointConfiguration.*
import org.koin.core.parameter.parametersOf

@Composable
fun EndpointsWidget(
    device: Device,
    onEndpointClicked: (Key) -> Unit
) {
    val viewModel = getViewModel<EndpointsViewModel>(key = device.toString()) { parametersOf(device) }
    val state by viewModel.state.collectAsState()

    EndpointsWidgetContent(
        state,
        viewModel::onAllCheckboxChanged,
        viewModel::onCheckboxChanged,
        viewModel::onFailChanged,
        onEndpointClicked
    )
}

@Composable
fun EndpointsWidgetContent(
    state: State,
    onAllCheckboxChanged: (value: Boolean) -> Unit,
    onCheckboxChanged: (Key, value: Boolean) -> Unit,
    onFailChanged: (Key, value: Boolean) -> Unit,
    onEndpointClicked: (Key) -> Unit
) = Column(
    modifier = Modifier
        .verticalScroll(rememberScrollState())
) {
    when (state) {
        State.Loading -> Text("Empty")
        is State.EndpointsList -> EndpointsList(
            state,
            onAllCheckboxChanged,
            onEndpointClicked,
            onCheckboxChanged,
            onFailChanged
        )
    }
}

@Composable
private fun EndpointsList(
    state: State.EndpointsList,
    onAllCheckboxChanged: (value: Boolean) -> Unit,
    onEndpointClicked: (Key) -> Unit,
    onCheckboxChanged: (Key, value: Boolean) -> Unit,
    onFailChanged: (Key, value: Boolean) -> Unit,
    strings: Strings = LocalStrings.current,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StandardTextTooltip(text = strings.widgets.endpoints.selectAllTooltip) {
            Checkbox(
                checked = state.selectAllTicked,
                onCheckedChange = { onAllCheckboxChanged(it) }
            )
        }
        Spacer(Modifier.weight(1f))
        Text(modifier = Modifier.padding(end = 8.dp), text = strings.widgets.endpoints.errorSwitchLabel)
    }
    HorizontalDivider()
    state.endpoints.forEachIndexed { index, endpoint ->
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { onEndpointClicked(endpoint.key) }
            .alternatingBackground(index)
            .padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = endpoint.isCheckboxTicked,
                onCheckedChange = { onCheckboxChanged(endpoint.key, it) }
            )
            Text(endpoint.name)
            Spacer(Modifier.weight(1f))
            if (endpoint.hasValuesOverridden) {
                StandardTextTooltip(text = strings.widgets.endpoints.valuesOverriddenIndicatorTooltip) {
                    Icon(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        imageVector = Icons.Filled.DriveFileRenameOutline,
                        contentDescription = strings.widgets.endpoints.valuesOverriddenIndicatorTooltip
                    )
                }
            }
            Switch(
                checked = endpoint.fail,
                onCheckedChange = { onFailChanged(endpoint.key, it) }
            )
        }
    }
}
