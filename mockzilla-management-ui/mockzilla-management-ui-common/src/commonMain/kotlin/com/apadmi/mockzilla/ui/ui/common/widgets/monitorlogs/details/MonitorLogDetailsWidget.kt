@file:Suppress("COMPLEX_EXPRESSION")

package com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.EmptyState
import com.apadmi.mockzilla.ui.ui.common.components.PlatformVerticalScrollbar
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.SectionTitle
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMonoFontFamily
import com.apadmi.mockzilla.ui.ui.common.theme.jsonHighlight
import com.apadmi.mockzilla.ui.ui.common.theme.jsonKey
import com.apadmi.mockzilla.ui.ui.common.theme.success
import com.apadmi.mockzilla.ui.ui.common.theme.warning
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.MonitorLogDetailsViewModel.State.ViewDetails
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.MonitorLogDetailsViewModel.State.ViewDetails.Tab

import io.ktor.http.HttpStatusCode
import kotlinx.datetime.Instant
import org.koin.core.parameter.parametersOf

@Composable
fun MonitorLogDetailsWidget(
    device: Device,
    logDetail: LogEvent?,
    onClose: () -> Unit = {},
) {
    val viewModel = getViewModel<MonitorLogDetailsViewModel>(
        key = logDetail?.id ?: "empty"
    ) { parametersOf(device, logDetail) }
    val state by viewModel.state.collectAsState()

    when (val s = state) {
        is MonitorLogDetailsViewModel.State.Empty -> MonitorLogDetailsEmptyContent()
        is MonitorLogDetailsViewModel.State.ViewDetails -> MonitorLogDetailsContent(
            state = s,
            onTabSelected = viewModel::onTabSelected,
            onClose = onClose,
        )
    }
}

@Composable
fun MonitorLogDetailsWidgetEmptyPreview() = PreviewSurface {
    Box(modifier = Modifier.size(300.dp)) {
        MonitorLogDetailsEmptyContent()
    }
}

@Suppress("COMPLEX_EXPRESSION", "MAGIC_NUMBER")
@Preview
@Composable
fun MonitorLogDetailsWidgetPreview() {
    val previewBody = """{"repairs":[{"id":"HSR-9455","repairStatus":"Upcoming","faultDescription":"Boiler pilot light"}]}"""
    val previewStatus = HttpStatusCode.OK
    val authHeader = "Authorization" to "Bearer token123"
    val contentTypeHeader = "Content-Type" to "application/json"
    val previewRequestHeaders = mapOf(authHeader)
    val previewResponseHeaders = mapOf(contentTypeHeader)
    val previewState = ViewDetails(
        logEvent = LogEvent(
            timestamp = 1_716_474_257_201L,
            url = "https://api.example.com/repairs",
            requestBody = "",
            requestHeaders = previewRequestHeaders,
            responseHeaders = previewResponseHeaders,
            responseBody = previewBody,
            status = previewStatus,
            delay = 342,
            method = "GET",
            isIntendedFailure = false,
        ),
        selectedTab = Tab.Response,
        requestBodyState = MonitorLogDetailsViewModel.State.BodyState.Available(""),
        responseBodyState = MonitorLogDetailsViewModel.State.BodyState.Available(previewBody),
    )
    PreviewSurface {
        LogDetailsContent(
            state = previewState,
            onTabSelected = {},
        )
    }
}

@Composable
internal fun MonitorLogDetailsContent(
    state: ViewDetails,
    onTabSelected: (Tab) -> Unit,
    onClose: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LogDetailsContent(state = state, onTabSelected = onTabSelected, onClose = onClose)
    }
}

@Suppress("TOO_LONG_FUNCTION")
@Composable
internal fun LogDetailsContent(
    state: ViewDetails,
    onTabSelected: (Tab) -> Unit,
    onClose: () -> Unit = {},
    strings: Strings = LocalStrings.current,
) = Box {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        LogHeaderBar(logEvent = state.logEvent, onClose = onClose)
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
        LogTabBar(selectedTab = state.selectedTab, onTabSelected = onTabSelected)
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            when (state.selectedTab) {
                Tab.Response -> {
                    SectionTitle(label = strings.widgets.logDetails.responseHeaders)
                    HeadersContent(state.logEvent.responseHeaders.toList(), strings)
                    Spacer(Modifier.height(8.dp))
                    SectionTitle(label = strings.widgets.logDetails.responseBody)
                    BodyContent(state.responseBodyState, strings)
                }

                Tab.Request -> {
                    SectionTitle(label = strings.widgets.logDetails.requestHeaders)
                    HeadersContent(state.logEvent.requestHeaders.toList(), strings)
                    Spacer(Modifier.height(8.dp))
                    SectionTitle(label = strings.widgets.logDetails.requestBody)
                    BodyContent(state.requestBodyState, strings)
                }
            }
        }
    }

    PlatformVerticalScrollbar(
        scrollState = scrollState,
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .fillMaxHeight()
    )
}

@Composable
internal fun MonitorLogDetailsEmptyContent(
    strings: Strings = LocalStrings.current,
) {
    EmptyState(
        title = strings.widgets.logDetails.emptyTitle,
        description = strings.widgets.logDetails.emptyDescription,
        modifier = Modifier.fillMaxSize(),
        icon = {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    )
}

// ── Header bar ────────────────────────────────────────────────────────────────

@Suppress("MAGIC_NUMBER")
@Composable
private fun LogHeaderBar(logEvent: LogEvent, onClose: () -> Unit) {
    val metaColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
    val monoFont = LocalMonoFontFamily.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            LogStatusBadge(status = logEvent.status)
            Text(
                text = urlToTitle(logEvent.url),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MetaItem(icon = "⏱", label = "${logEvent.delay}ms", color = metaColor)

            logEvent.requestSizeBytes?.let { size ->
                MetaItem(icon = "↑", label = size.toKbLabel(), color = metaColor)
            }

            logEvent.responseSizeBytes?.let { size ->
                MetaItem(icon = "↓", label = size.toKbLabel(), color = metaColor)
            }

            Text(
                text = formatTimestamp(logEvent.timestamp),
                style = MaterialTheme.typography.labelMedium.copy(fontFamily = monoFont),
                color = metaColor,
            )
        }
    }
}

@Suppress("MAGIC_NUMBER")
@Composable
private fun LogStatusBadge(status: HttpStatusCode) {
    val monoFont = LocalMonoFontFamily.current
    val cs = MaterialTheme.colorScheme
    val statusClass = status.value / 100
    val statusColor = when (statusClass) {
        2 -> cs.success.primary
        3 -> cs.warning.primary
        else -> cs.error
    }
    Text(
        text = status.value.toString(),
        modifier = Modifier
            .border(width = 1.5.dp, color = statusColor, shape = RoundedCornerShape(percent = 50))
            .background(color = statusColor.copy(alpha = 0.1f), shape = RoundedCornerShape(percent = 50))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        style = MaterialTheme.typography.labelMedium.copy(fontFamily = monoFont),
        color = statusColor,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun MetaItem(
    icon: String,
    label: String,
    color: Color
) {
    val monoFont = LocalMonoFontFamily.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.labelSmall,
            color = color,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontFamily = monoFont),
            color = color,
        )
    }
}

// ── Tab bar ───────────────────────────────────────────────────────────────────

@Suppress("MAGIC_NUMBER")
@Composable
private fun LogTabBar(
    selectedTab: Tab,
    onTabSelected: (Tab) -> Unit,
) {
    Row {
        Tab.entries.forEach { tab ->

            val isSelected = tab == selectedTab

            val selectedColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            }

            Column(
                modifier = Modifier
                    .width(100.dp)  // Fixed width
                    .clickable { onTabSelected(tab) },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = tab.name,
                    modifier = Modifier.padding(vertical = 10.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isSelected) {
                        FontWeight.SemiBold
                    } else {
                        FontWeight.Normal
                    },
                    color = selectedColor,
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(
                            if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                Color.Transparent
                            },
                        ),
                )
            }
        }
    }
}
// ── Content sections ──────────────────────────────────────────────────────────

@Suppress("MAGIC_NUMBER")
@Composable
private fun BodyContent(
    bodyState: MonitorLogDetailsViewModel.State.BodyState,
    strings: Strings,
) {
    val monoFont = LocalMonoFontFamily.current
    val cs = MaterialTheme.colorScheme
    val boxModifier = Modifier
        .fillMaxWidth()
        .background(
            color = cs.onSurface.copy(alpha = 0.06f),
            shape = RoundedCornerShape(6.dp),
        )
        .padding(10.dp)

    Text("${bodyState::class}")
    when (bodyState) {
        is MonitorLogDetailsViewModel.State.BodyState.Available -> {
            val trimmed = bodyState.text.trim()
            val isEmpty = trimmed.isEmpty() || trimmed == "{}" || trimmed == "[]"
            Box(modifier = boxModifier) {
                if (isEmpty) {
                    Text(
                        text = strings.widgets.logDetails.emptyBody,
                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = monoFont),
                        color = cs.onSurface.copy(alpha = 0.5f),
                    )
                } else {
                    SelectionContainer {
                        Text(
                            text = buildHighlightedAnnotatedString(
                                bodyState.text,
                                MaterialTheme.colorScheme.jsonHighlight
                            ),
                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = monoFont),
                        )
                    }
                }
            }
        }

        is MonitorLogDetailsViewModel.State.BodyState.Loading -> {
            Box(modifier = boxModifier) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (bodyState.preview.isNotBlank()) {
                        SelectionContainer {
                            Text(
                                text = buildHighlightedAnnotatedString(
                                    bodyState.preview,
                                    MaterialTheme.colorScheme.jsonHighlight
                                ),
                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = monoFont),
                            )
                        }
                    }
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = cs.onSurface.copy(alpha = 0.4f),
                    )
                }
            }
        }

        is MonitorLogDetailsViewModel.State.BodyState.Error -> {
            Box(modifier = boxModifier) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (bodyState.preview.isNotBlank()) {
                        SelectionContainer {
                            Text(
                                text = buildHighlightedAnnotatedString(
                                    bodyState.preview,
                                    MaterialTheme.colorScheme.jsonHighlight
                                ),
                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = monoFont),
                            )
                        }
                    }
                    Text(
                        text = strings.widgets.logDetails.bodyLoadError,
                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = monoFont),
                        color = cs.error,
                    )
                }
            }
        }
    }
}

@Suppress("MAGIC_NUMBER")
@Composable
private fun HeadersContent(headers: List<Pair<String, String>>, strings: Strings) {
    val monoFont = LocalMonoFontFamily.current
    if (headers.isEmpty()) {
        Text(
            text = strings.widgets.logDetails.noHeaders,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.padding(vertical = 4.dp),
        )
        return
    }
    val dividerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    SelectionContainer {
        Column(modifier = Modifier.fillMaxWidth()) {
            headers.forEach { (key, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    Text(
                        text = key,
                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = monoFont),
                        color = MaterialTheme.colorScheme.jsonKey,
                        modifier = Modifier.width(110.dp),
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = monoFont),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                    )
                }
                DashedDivider(color = dividerColor)
            }
        }
    }
}

@Suppress("MAGIC_NUMBER")
@Composable
private fun DashedDivider(color: Color) {
    Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)) {
        drawLine(
            color = color,
            start = androidx.compose.ui.geometry.Offset(0f, 0f),
            end = androidx.compose.ui.geometry.Offset(size.width, 0f),
            strokeWidth = 1.dp.toPx(),
            cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(4.dp.toPx(), 4.dp.toPx()),
                phase = 0f,
            ),
        )
    }
}
