package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

import com.apadmi.mockzilla.lib.models.EndpointConfiguration.*
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.assets.LightningBolt
import com.apadmi.mockzilla.ui.ui.common.components.CustomTextField
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.SectionTitle
import com.apadmi.mockzilla.ui.ui.common.components.StatusChip
import com.apadmi.mockzilla.ui.ui.common.components.ChipTone
import com.apadmi.mockzilla.ui.ui.common.components.buttons.BaseButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonSize
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonVariant
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMockzillaTokens

import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.parameter.parametersOf

private enum class EndpointDensity(val minHeight: Int) {
    Compact(36), Cozy(44), Comfy(52)
}

@Composable
fun EndpointsWidget(
    device: Device,
    onEndpointClicked: (Key) -> Unit,
    onGlobalControlsClicked: () -> Unit,
) {
    val viewModel = getViewModel<EndpointsViewModel>(key = device.toString()) {
        parametersOf(device)
    }
    val state by viewModel.state.collectAsState()

    EndpointsWidgetContent(
        state = state,
        onFilterUpdate = viewModel::onFilterChanged,
        onEndpointClicked = onEndpointClicked,
        onGlobalControlsClicked = onGlobalControlsClicked,
    )
}

@Suppress("MAGIC_NUMBER")
@Composable
private fun EndpointsList(
    state: EndpointsViewModel.State.EndpointsList,
    onEndpointClicked: (Key) -> Unit,
    onFilterUpdate: (String) -> Unit,
    density: EndpointDensity,
    strings: Strings = LocalStrings.current,
) {
    val tokens = LocalMockzillaTokens.current
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.filter,
            onValueChange = onFilterUpdate,
            singleLine = true,
            placeholder = { Text(strings.widgets.endpoints.filterPlaceholder) },
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = strings.widgets.endpoints.numberOfEndpointsShown(
                    state.endpoints.filter { it.display }.size,
                    state.endpoints.size,
                ),
                color = tokens.fg2,
                style = MaterialTheme.typography.labelSmall,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                EndpointDensity.entries.forEach { d ->
                    BaseButton(
                        label = d.name.first().toString(),
                        variant = if (density == d) ButtonVariant.Soft else ButtonVariant.Ghost,
                        size = ButtonSize.Sm,
                        onClick = { /* density change handled by parent */ },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        state.endpoints.filter { it.display }.forEach { endpoint ->
            EndpointCard(
                endpoint = endpoint,
                onEndpointClicked = onEndpointClicked,
                density = density,
            )
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Suppress("MAGIC_NUMBER")
@Composable
private fun EndpointCard(
    endpoint: EndpointsViewModel.State.EndpointConfig,
    onEndpointClicked: (Key) -> Unit,
    density: EndpointDensity,
    strings: Strings = LocalStrings.current,
) {
    val tokens = LocalMockzillaTokens.current
    val railColor = if (endpoint.fail) tokens.err else tokens.accent
    val cardShape = RoundedCornerShape(8.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(color = if (endpoint.fail) tokens.errSoft else tokens.bg2)
            .border(width = 1.dp, color = tokens.line1, shape = cardShape)
            .clickable { onEndpointClicked(endpoint.key) }
            .heightIn(min = density.minHeight.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Left colour rail
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(density.minHeight.dp)
                .background(railColor),
        )
        Spacer(Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f).padding(vertical = 8.dp)) {
            Text(
                text = endpoint.name,
                color = tokens.fg0,
                style = MaterialTheme.typography.bodyLarge,
            )
            if (endpoint.overriddenProperties.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    endpoint.overriddenProperties.forEach { property ->
                        StatusChip(label = property.displayName, tone = ChipTone.Accent)
                    }
                }
            }
        }

        if (endpoint.fail) {
            StatusChip(
                label = strings.widgets.endpoints.overrides(1),
                tone = ChipTone.Err,
                modifier = Modifier.padding(end = 10.dp),
            )
        }
    }
}

@Composable
private fun EndpointsWidgetContent(
    state: EndpointsViewModel.State,
    onFilterUpdate: (String) -> Unit,
    onEndpointClicked: (Key) -> Unit,
    onGlobalControlsClicked: () -> Unit,
    strings: Strings = LocalStrings.current,
) {
    val tokens = LocalMockzillaTokens.current
    var density by rememberSaveable { mutableStateOf(EndpointDensity.Cozy) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = tokens.bg0)
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .navigationBarsPadding(),
    ) {
        when (state) {
            EndpointsViewModel.State.Loading -> CircularProgressIndicator(color = tokens.accent)
            is EndpointsViewModel.State.EndpointsList -> {
                EndpointsList(
                    state = state,
                    onEndpointClicked = onEndpointClicked,
                    onFilterUpdate = onFilterUpdate,
                    density = density,
                )

                BaseButton(
                    modifier = Modifier
                        .padding(bottom = 8.dp, end = 8.dp)
                        .align(Alignment.BottomEnd)
                        .zIndex(1f),
                    label = strings.widgets.globalControls.title,
                    leadingIcon = Icons.LightningBolt,
                    variant = ButtonVariant.Soft,
                    size = ButtonSize.Md,
                    onClick = onGlobalControlsClicked,
                )
            }
        }
    }
}

@Preview
@Composable
private fun EndpointsWidgetPreview() = PreviewSurface {
    EndpointsWidgetContent(
        state = EndpointsViewModel.State.EndpointsList(
            endpoints = listOf(
                EndpointsViewModel.State.EndpointConfig(
                    key = Key("1"), name = "GET /repairs/list",
                    fail = false, overriddenProperties = listOf(EndpointProperties.Delay, EndpointProperties.Body),
                    display = true,
                ),
                EndpointsViewModel.State.EndpointConfig(
                    key = Key("2"), name = "POST /auth/login",
                    fail = true, overriddenProperties = emptyList(), display = true,
                ),
                EndpointsViewModel.State.EndpointConfig(
                    key = Key("3"), name = "GET /catalog/search",
                    fail = false, overriddenProperties = listOf(EndpointProperties.Status), display = true,
                ),
            ),
            filter = "",
        ),
        onFilterUpdate = {},
        onEndpointClicked = {},
        onGlobalControlsClicked = {},
    )
}

@Preview
@Composable
private fun EndpointsWidgetDarkPreview() = PreviewSurface(darkTheme = true) {
    EndpointsWidgetContent(
        state = EndpointsViewModel.State.EndpointsList(
            endpoints = listOf(
                EndpointsViewModel.State.EndpointConfig(
                    key = Key("1"), name = "GET /repairs/list",
                    fail = false, overriddenProperties = listOf(EndpointProperties.Body),
                    display = true,
                ),
                EndpointsViewModel.State.EndpointConfig(
                    key = Key("2"), name = "DELETE /auth/session",
                    fail = true, overriddenProperties = emptyList(), display = true,
                ),
            ),
            filter = "",
        ),
        onFilterUpdate = {},
        onEndpointClicked = {},
        onGlobalControlsClicked = {},
    )
}
