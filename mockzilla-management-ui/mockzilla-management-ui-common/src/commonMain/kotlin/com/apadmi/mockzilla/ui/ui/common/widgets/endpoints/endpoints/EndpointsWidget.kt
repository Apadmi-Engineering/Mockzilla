package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.lib.models.EndpointConfiguration.*
import com.apadmi.mockzilla.ui.internal.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.ChipTone
import com.apadmi.mockzilla.ui.ui.common.components.EmptyState
import com.apadmi.mockzilla.ui.ui.common.components.FilterTextField
import com.apadmi.mockzilla.ui.ui.common.components.PlatformVerticalScrollbar
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.StatusChip
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonVariant
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.RowDensityControls
import com.apadmi.mockzilla.ui.ui.common.components.drawIndicator
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceFaint
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.theme.warning
import com.apadmi.mockzilla.ui.utils.Platform

import org.koin.core.parameter.parametersOf

private const val minContentWidthDp = 300
private const val HOVER_ALPHA = 0.08f
private const val UNSELECTED_BORDER_ALPHA = 0.2f
private const val LEFT_BORDER_WIDTH_DP = 3
private const val CONTENT_START_PADDING_DP = 13
private const val COMPACT_VERTICAL_PADDING_DP = 10
private const val COMFY_VERTICAL_PADDING_DP = 14
private const val DELAY_TENTHS_DIVISOR = 100
private const val DELAY_TENTHS_MODULO = 10

private fun EndpointProperties.chipTone(): ChipTone = when (this) {
    EndpointProperties.Delay -> ChipTone.Warn
    else -> ChipTone.Teal
}

private fun RowDensity.verticalPadding(): Dp = when (this) {
    RowDensity.Compact -> COMPACT_VERTICAL_PADDING_DP.dp
    RowDensity.Comfy -> COMFY_VERTICAL_PADDING_DP.dp
}

@InternalMockzillaApi
@Composable
public fun EndpointsWidget(
    device: Device,
    onEndpointClicked: (Key?) -> Unit,
    onGlobalControlsClicked: () -> Unit
) {
    val viewModel = getViewModel<EndpointsViewModel>(device = device) {
        parametersOf(device)
    }
    val state by viewModel.state.collectAsState()
    var selectedKey by remember { mutableStateOf<Key?>(null) }

    EndpointsWidgetContent(
        state = state,
        selectedKey = selectedKey,
        onFilterUpdate = viewModel::onFilterChanged,
        onRowDensityChanged = viewModel::onRowDensityChanged,
        onEndpointClicked = { key ->
            if (selectedKey == key) {
                selectedKey = null
                onEndpointClicked(null)
            } else {
                selectedKey = key
                onEndpointClicked(key)
            }
        },
        onGlobalControlsClicked = onGlobalControlsClicked
    )
}

private fun formatDelaySeconds(delayMs: Int): String {
    val tenths = delayMs / DELAY_TENTHS_DIVISOR
    return "${tenths / DELAY_TENTHS_MODULO}.${tenths % DELAY_TENTHS_MODULO} s"
}

@Composable
private fun EndpointsList(
    state: EndpointsViewModel.State.EndpointsList,
    selectedKey: Key?,
    onEndpointClicked: (Key) -> Unit,
    onFilterUpdate: (String) -> Unit,
    onRowDensityChanged: (RowDensity) -> Unit,
    onGlobalControlsClicked: () -> Unit,
    modifier: Modifier = Modifier,
) = Column(modifier = modifier) {
    Column(modifier = Modifier.padding(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            FilterTextField(
                modifier = Modifier.weight(1f),
                value = state.filter,
                onFilterUpdate = onFilterUpdate,
                placeholder = LocalStrings.current.widgets.endpoints.filterPlaceholder
            )
            if (Platform.current != Platform.Desktop) {
                Spacer(Modifier.width(4.dp))
                RowDensityControls(
                    selected = state.rowDensity,
                    onChanged = onRowDensityChanged
                )
            }
        }
        EndpointsHeader(
            displayedCount = state.endpoints.size,
            totalCount = state.allEndpoints.size,
            selectedRowDensity = state.rowDensity,
            onRowDensityChanged = onRowDensityChanged,
        )

        if (Platform.current == Platform.Desktop) {
            GlobalControlsButton(
                isOpen = false,
                onClick = onGlobalControlsClicked
            )
        }
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
    if (state.endpoints.isEmpty()) {
        val strings = LocalStrings.current
        EmptyState(
            title = strings.widgets.endpoints.emptyTitle,
            description = strings.widgets.endpoints.emptyDescription,
            modifier = Modifier.fillMaxSize()
        )
    } else {
        val listState = rememberLazyListState()
        Box {
            LazyColumn(state = listState) {
                items(state.endpoints) { endpoint ->
                    EndpointRow(
                        endpoint = endpoint,
                        rowDensity = state.rowDensity,
                        isSelected = endpoint.key == selectedKey,
                        onEndpointClicked = onEndpointClicked,
                    )
                }
            }
            PlatformVerticalScrollbar(
                scrollState = listState,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
private fun GlobalControlsButton(
    isOpen: Boolean,
    strings: Strings = LocalStrings.current,
    onClick: () -> Unit
) {
    CustomButton(
        variant = if (isOpen) ButtonVariant.Solid else ButtonVariant.Outline,
        leadingIcon = Icons.Filled.Tune,
        label = strings.widgets.globalControls.title,
        onClick = onClick,
    )
}

@Composable
private fun EndpointsHeader(
    displayedCount: Int,
    totalCount: Int,
    selectedRowDensity: RowDensity,
    onRowDensityChanged: (RowDensity) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = "$displayedCount/$totalCount",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        if (Platform.current == Platform.Desktop) {
            RowDensityControls(
                selected = selectedRowDensity,
                onChanged = onRowDensityChanged
            )
        }
    }
}

@Composable
private fun EndpointRow(
    endpoint: EndpointsViewModel.State.EndpointConfig,
    rowDensity: RowDensity,
    isSelected: Boolean,
    onEndpointClicked: (Key) -> Unit,
    strings: Strings = LocalStrings.current,
) {
    val cs = MaterialTheme.colorScheme
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val leftBorderColor = when {
        isSelected && Platform.current == Platform.Desktop -> cs.primary
        endpoint.fail -> cs.error
        endpoint.overriddenProperties.any { it != EndpointProperties.Delay } -> cs.primary
        endpoint.overriddenProperties.isNotEmpty() -> cs.warning.primary
        else -> Color.Transparent
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .hoverable(interactionSource = interactionSource)
            .background(
                if (isSelected || isHovered) {
                    cs.onSurface.copy(alpha = HOVER_ALPHA)
                } else {
                    Color.Transparent
                }
            )
            .drawIndicator(leftBorderColor)
            .clickable { onEndpointClicked(endpoint.key) }
            .padding(
                start = CONTENT_START_PADDING_DP.dp,
                end = 12.dp,
                top = rowDensity.verticalPadding(),
                bottom = rowDensity.verticalPadding(),
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            EndpointRowMainContent(endpoint = endpoint)
            if (rowDensity != RowDensity.Compact) {
                Spacer(Modifier.height(4.dp))
                EndpointRowChips(endpoint = endpoint)
            }
        }
        if (endpoint.fail && Platform.current == Platform.Desktop) {
            StatusChip(label = strings.widgets.endpoints.forced, tone = ChipTone.Err)
        }
        endpoint.delayMs?.let { delay ->
            Text(
                text = formatDelaySeconds(delay),
                style = MaterialTheme.typography.labelSmall,
                color = cs.warning.primary,
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = cs.onSurfaceFaint,
        )
    }
    HorizontalDivider(color = cs.onSurface.copy(alpha = 0.12f))
}

@Composable
private fun EndpointRowMainContent(endpoint: EndpointsViewModel.State.EndpointConfig) {
    Text(
        text = endpoint.name,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
private fun EndpointRowChips(
    endpoint: EndpointsViewModel.State.EndpointConfig,
    strings: Strings = LocalStrings.current,
) {
    if (!endpoint.fail && endpoint.overriddenProperties.isEmpty()) {
        Text(
            text = strings.widgets.endpoints.noOverrides,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceMuted,
        )
        return
    }
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (endpoint.fail) {
            StatusChip(label = strings.widgets.endpoints.forced, tone = ChipTone.Err)
        }
        endpoint.overriddenProperties.forEach { property ->
            StatusChip(
                label = property.displayName.uppercase(),
                tone = property.chipTone(),
            )
        }
    }
}

@Composable
private fun EndpointsWidgetContent(
    state: EndpointsViewModel.State,
    selectedKey: Key?,
    onFilterUpdate: (String) -> Unit,
    onRowDensityChanged: (RowDensity) -> Unit,
    onEndpointClicked: (Key) -> Unit,
    onGlobalControlsClicked: () -> Unit,
    strings: Strings = LocalStrings.current
) {
    val scrollState = rememberScrollState()
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        val contentWidth = maxOf(maxWidth, minContentWidthDp.dp)
        Box(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .width(contentWidth)
                .fillMaxHeight()
        ) {
            when (state) {
                EndpointsViewModel.State.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                is EndpointsViewModel.State.EndpointsList -> EndpointsList(
                    state = state,
                    selectedKey = selectedKey,
                    onFilterUpdate = onFilterUpdate,
                    onRowDensityChanged = onRowDensityChanged,
                    onEndpointClicked = onEndpointClicked,
                    onGlobalControlsClicked = onGlobalControlsClicked,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview
@Composable
private fun EndpointsWidgetPreview() = PreviewSurface(darkTheme = true) {
    EndpointsWidgetContent(
        selectedKey = null,
        state = EndpointsViewModel.State.EndpointsList(
            allEndpoints = listOf(
                EndpointsViewModel.State.EndpointConfig(
                    key = Key("1"),
                    name = "Repairs",
                    fail = false,
                    overriddenProperties = listOf(
                        EndpointProperties.Body,
                        EndpointProperties.Status
                    ),
                    delayMs = null,
                ),
                EndpointsViewModel.State.EndpointConfig(
                    key = Key("2"),
                    name = "Cancel Repair",
                    fail = false,
                    overriddenProperties = listOf(EndpointProperties.Delay),
                    delayMs = 4900,
                ),
                EndpointsViewModel.State.EndpointConfig(
                    key = Key("3"),
                    name = "Reschedule Repair",
                    fail = false,
                    overriddenProperties = emptyList(),
                    delayMs = null,
                ),
                EndpointsViewModel.State.EndpointConfig(
                    key = Key("4"),
                    name = "Auth — Token",
                    fail = true,
                    overriddenProperties = emptyList(),
                    delayMs = null,
                ),
            ),
            filter = "",
            rowDensity = RowDensity.Comfy,
        ),
        onFilterUpdate = {},
        onRowDensityChanged = {},
        onEndpointClicked = {},
        onGlobalControlsClicked = {}
    )
}
