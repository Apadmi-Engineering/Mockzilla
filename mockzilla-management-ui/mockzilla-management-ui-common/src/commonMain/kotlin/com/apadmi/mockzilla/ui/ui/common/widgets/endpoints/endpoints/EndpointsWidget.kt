package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.lib.models.EndpointConfiguration.*
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.StandardTextTooltip
import com.apadmi.mockzilla.ui.ui.common.theme.alternatingBackground
import com.apadmi.mockzilla.ui.ui.common.theme.theme_turquoise
import com.apadmi.mockzilla.ui.ui.common.theme.theme_white
import org.jetbrains.compose.ui.tooling.preview.Preview

import org.koin.core.parameter.parametersOf

@Suppress("MAGIC_NUMBER")
@Composable
private fun ColumnScope.EndpointsList(
    state: EndpointsViewModel.State.EndpointsList,
    onAllCheckboxChanged: (value: Boolean) -> Unit,
    onEndpointClicked: (Key) -> Unit,
    onGlobalControlsClicked: () -> Unit,
    onCheckboxChanged: (Key, value: Boolean) -> Unit,
    onFailChanged: (Key, value: Boolean) -> Unit,
    onFilterUpdate: (String) -> Unit,
    strings: Strings = LocalStrings.current
) {
    Filter(
        value = state.filter,
        onFilterUpdate = onFilterUpdate
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StandardTextTooltip(text = strings.widgets.endpoints.selectAllTooltip) {
            Checkbox(
                checked = state.selectAllTicked,
                onCheckedChange = { onAllCheckboxChanged(it) }
            )
        }
        Spacer(Modifier.weight(1f))
        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = strings.widgets.endpoints.errorSwitchLabel
        )
    }

    HorizontalDivider()

    state.endpoints.filter { it.display }.forEachIndexed { index, endpoint ->
        Row(
            modifier = Modifier
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
            Text(text = endpoint.name, modifier = Modifier.weight(1f))
            if (endpoint.hasValuesOverridden) {
                StandardTextTooltip(text = strings.widgets.endpoints.valuesOverriddenIndicatorTooltip) {
                    Icon(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        imageVector = Icons.Filled.DriveFileRenameOutline,
                        contentDescription = strings.widgets.endpoints.valuesOverriddenIndicatorTooltip
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(8.dp))
            }
            Switch(
                checked = endpoint.fail,
                onCheckedChange = { onFailChanged(endpoint.key, it) }
            )
        }
    }

    // This FAB will be aligned properly once the UI for the endpoints widget is updated
    Spacer(modifier = Modifier.height(20.dp))
    FloatingActionButton(
        modifier = Modifier
            .align(Alignment.End)
            .padding(end = 8.dp),
        onClick = onGlobalControlsClicked,
        containerColor = theme_turquoise,
        contentColor = theme_white
    ) {
        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = null
        )
    }
}

@Composable
fun EndpointsWidget(
    device: Device,
    onEndpointClicked: (Key) -> Unit,
    onGlobalControlsClicked: () -> Unit
) {
    val viewModel = getViewModel<EndpointsViewModel>(key = device.toString()) {
        parametersOf(device)
    }
    val state by viewModel.state.collectAsState()

    EndpointsWidgetContent(
        state = state,
        onAllCheckboxChanged = viewModel::onAllCheckboxChanged,
        onCheckboxChanged = viewModel::onCheckboxChanged,
        onFailChanged = viewModel::onFailChanged,
        onFilterUpdate = viewModel::onFilterChanged,
        onEndpointClicked = onEndpointClicked,
        onGlobalControlsClicked = onGlobalControlsClicked
    )
}

@Composable
fun EndpointsWidgetContent(
    state: EndpointsViewModel.State,
    onAllCheckboxChanged: (value: Boolean) -> Unit,
    onCheckboxChanged: (Key, value: Boolean) -> Unit,
    onFailChanged: (Key, value: Boolean) -> Unit,
    onFilterUpdate: (String) -> Unit,
    onEndpointClicked: (Key) -> Unit,
    onGlobalControlsClicked: () -> Unit
) = Column(
    modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
) {
    when (state) {
        EndpointsViewModel.State.Loading -> Text("Empty")
        is EndpointsViewModel.State.EndpointsList -> EndpointsList(
            state = state,
            onAllCheckboxChanged = onAllCheckboxChanged,
            onEndpointClicked = onEndpointClicked,
            onGlobalControlsClicked = onGlobalControlsClicked,
            onCheckboxChanged = onCheckboxChanged,
            onFailChanged = onFailChanged,
            onFilterUpdate = onFilterUpdate
        )
    }
}

@Composable
private fun Filter(
    value: String,
    strings: Strings = LocalStrings.current,
    onFilterUpdate: (String) -> Unit,
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onFilterUpdate,
        singleLine = true,
        label = { Text(strings.widgets.endpoints.filterLabel) },
        trailingIcon = {
            IconButton({ onFilterUpdate("") }) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = strings.widgets.endpoints.filterClear,
                )
            }
        }
    )
}

@Preview
@Composable
private fun EndpointsWidgetPreview() = PreviewSurface {
    EndpointsWidgetContent(
        state = EndpointsViewModel.State.EndpointsList(
            endpoints = listOf(
                EndpointsViewModel.State.EndpointConfig(
                    key = Key("1"),
                    name = "FooBar",
                    fail = false,
                    isCheckboxTicked = false,
                    hasValuesOverridden = false,
                    display = true
                ),
                EndpointsViewModel.State.EndpointConfig(
                    key = Key("2"),
                    name = "Foo",
                    fail = true,
                    isCheckboxTicked = true,
                    hasValuesOverridden = false,
                    display = true
                ),
                EndpointsViewModel.State.EndpointConfig(
                    key = Key("3"),
                    name = "FooBuzz",
                    fail = false,
                    isCheckboxTicked = false,
                    hasValuesOverridden = true,
                    display = true
                ),
                EndpointsViewModel.State.EndpointConfig(
                    key = Key("4"),
                    name = "Foobar",
                    fail = false,
                    isCheckboxTicked = false,
                    hasValuesOverridden = false,
                    display = true
                )
            ),
            filter = "Foo",
        ),
        onAllCheckboxChanged = {},
        onCheckboxChanged = { _, _ -> },
        onFailChanged = { _, _ -> },
        onFilterUpdate = {},
        onEndpointClicked = {},
        onGlobalControlsClicked = {}
    )
}
