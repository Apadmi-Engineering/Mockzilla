package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.lib.models.EndpointConfiguration.*
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.utils.conditional
import com.apadmi.mockzilla_management_ui_common.generated.resources.Res
import com.apadmi.mockzilla_management_ui_common.generated.resources.lightning_bolt
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import org.koin.core.parameter.parametersOf

@Suppress("MAGIC_NUMBER")
@Composable
private fun ColumnScope.EndpointsList(
    state: EndpointsViewModel.State.EndpointsList,
    onEndpointClicked: (Key) -> Unit,
    onGlobalControlsClicked: () -> Unit,
    onFilterUpdate: (String) -> Unit,
    strings: Strings = LocalStrings.current
) {
    FilterTextField(
        value = state.filter,
        onFilterUpdate = onFilterUpdate
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = strings.widgets.endpoints.numberOfEndpointsShown(
            state.endpoints.filter { it.display }.size,
            state.endpoints.size
        ),
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.labelSmall
    )

    Spacer(modifier = Modifier.height(4.dp))

    state.endpoints.filter { it.display }.forEach { endpoint ->
        EndpointCard(
            endpoint = endpoint,
            onEndpointClicked = onEndpointClicked
        )
        Spacer(modifier = Modifier.height(8.dp))
    }

    // This FAB will be aligned properly once the UI for the endpoints widget is updated
    Spacer(modifier = Modifier.height(20.dp))
    FloatingActionButton(
        modifier = Modifier
            .align(Alignment.End)
            .padding(end = 8.dp),
        onClick = onGlobalControlsClicked,
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
        onFilterUpdate = viewModel::onFilterChanged,
        onEndpointClicked = onEndpointClicked,
        onGlobalControlsClicked = onGlobalControlsClicked
    )
}

@Composable
private fun EndpointCard(
    endpoint: EndpointsViewModel.State.EndpointConfig,
    onEndpointClicked: (Key) -> Unit,
    strings: Strings = LocalStrings.current
) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .background(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(10.dp)
        )
) {
    val failureBorderColor = MaterialTheme.colorScheme.error

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEndpointClicked(endpoint.key) }
            .background(
                color = if (endpoint.fail) {
                    MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                } else {
                    MaterialTheme.colorScheme.surface
                },
                shape = RoundedCornerShape(10.dp)
            )
            .conditional(
                predicate = endpoint.fail,
                positive = {
                    border(
                        width = 1.dp,
                        color = failureBorderColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = endpoint.name,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (endpoint.fail) {
                Icon(
                    modifier = Modifier
                        .background(
                            color = Color.Red,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 2.dp),
                    painter = painterResource(resource = Res.drawable.lightning_bolt),
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }

    if (endpoint.overriddenProperties.isNotEmpty() && !endpoint.fail) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.Red,
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 10.dp,
                        bottomEnd = 10.dp
                    )
                )
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            itemVerticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = strings.widgets.endpoints.overrides(
                    endpoint.overriddenProperties.size
                ),
                style = MaterialTheme.typography.labelMedium
            )
            endpoint.overriddenProperties.forEach { property ->
                Text(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    text = property.toString(),  // TODO: Map property to actual string
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}

@Composable
private fun EndpointsWidgetContent(
    state: EndpointsViewModel.State,
    onFilterUpdate: (String) -> Unit,
    onEndpointClicked: (Key) -> Unit,
    onGlobalControlsClicked: () -> Unit
) = Column(
    modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 12.dp, vertical = 15.dp)
) {
    when (state) {
        EndpointsViewModel.State.Loading -> CircularProgressIndicator()
        is EndpointsViewModel.State.EndpointsList -> EndpointsList(
            state = state,
            onEndpointClicked = onEndpointClicked,
            onGlobalControlsClicked = onGlobalControlsClicked,
            onFilterUpdate = onFilterUpdate
        )
    }
}

@Composable
private fun FilterTextField(
    value: String,
    onFilterUpdate: (String) -> Unit,
    strings: Strings = LocalStrings.current
) = TextField(
    modifier = Modifier.fillMaxWidth(),
    value = value,
    onValueChange = onFilterUpdate,
    textStyle = MaterialTheme.typography.titleMedium,
    placeholder = { Text(strings.widgets.endpoints.filterPlaceholder) },
    leadingIcon = {
        Icon(imageVector = Icons.Default.Search, contentDescription = null)
    },
    singleLine = true,
    shape = RoundedCornerShape(8.dp),
    colors = TextFieldDefaults.colors().copy(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        disabledContainerColor = MaterialTheme.colorScheme.surface,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent
    )
)

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
                    overriddenProperties = listOf(
                        EndpointProperties.Delay,
                        EndpointProperties.Body
                    ),
                    display = true
                ),
                EndpointsViewModel.State.EndpointConfig(
                    key = Key("2"),
                    name = "Foo",
                    fail = true,
                    isCheckboxTicked = true,
                    overriddenProperties = emptyList(),
                    display = true
                ),
                EndpointsViewModel.State.EndpointConfig(
                    key = Key("3"),
                    name = "FooBuzz",
                    fail = false,
                    isCheckboxTicked = false,
                    overriddenProperties = listOf(
                        EndpointProperties.Delay,
                        EndpointProperties.Body,
                        EndpointProperties.Headers,
                        EndpointProperties.ErrorHeaders,
                        EndpointProperties.ErrorBody
                    ),
                    display = true
                ),
                EndpointsViewModel.State.EndpointConfig(
                    key = Key("4"),
                    name = "Foobar",
                    fail = false,
                    isCheckboxTicked = false,
                    overriddenProperties = emptyList(),
                    display = true
                )
            ),
            filter = "Foo",
        ),
        onFilterUpdate = {},
        onEndpointClicked = {},
        onGlobalControlsClicked = {}
    )
}
