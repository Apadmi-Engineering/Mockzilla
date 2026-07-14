@file:Suppress("COMPLEX_EXPRESSION")

package com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.internal.di.utils.getViewModel
import com.apadmi.mockzilla.ui.ui.common.components.EmptyState
import com.apadmi.mockzilla.ui.ui.common.components.PlatformVerticalScrollbar
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.SectionTitle
import com.apadmi.mockzilla.ui.ui.common.components.editor.EditorMode
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMonoFontFamily
import com.apadmi.mockzilla.ui.ui.common.theme.jsonKey
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceFaint
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.theme.success
import com.apadmi.mockzilla.ui.ui.common.theme.warning
import com.apadmi.mockzilla.ui.ui.common.utils.formatting.BodyVisualTransformation
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.MonitorLogDetailsViewModel.State.ViewDetails
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.MonitorLogDetailsViewModel.State.ViewDetails.Tab

import io.ktor.http.HttpStatusCode
import org.koin.core.parameter.parametersOf

@InternalMockzillaApi
@Composable
public fun MonitorLogDetailsWidget(
    device: Device,
    logDetail: LogEvent?,
    onClose: () -> Unit = {},
) {
    val viewModel = getViewModel<MonitorLogDetailsViewModel>(
        device = device,
        keyPrefix = logDetail?.id
    ) { parametersOf(device, logDetail) }
    val state by viewModel.state.collectAsState()

    Crossfade(
        targetState = logDetail,
        modifier = Modifier.fillMaxSize(),
        animationSpec = tween(durationMillis = 200)
    ) { newState ->
        MonitorLogDetailsContent(
            state = state,
            onTabSelected = viewModel::onTabSelected,
            onClose = onClose,
        )
    }
}

@Preview
@Composable
internal fun MonitorLogDetailsWidgetEmptyPreview() = PreviewSurface {
    Box(modifier = Modifier.size(300.dp)) {
        MonitorLogDetailsEmptyContent()
    }
}

@Suppress("COMPLEX_EXPRESSION", "MAGIC_NUMBER")
@Preview
@Composable
internal fun MonitorLogDetailsWidgetPreview() {
    val previewBody =
        """{"repairs":[{"id":"HSR-9455","repairStatus":"Upcoming","faultDescription":"Boiler pilot light"}]}"""
    val previewStatus = HttpStatusCode.OK
    val authHeader = "Authorization" to "Bearer token123"
    val contentTypeHeader = "Content-Type" to "application/json"
    val previewRequestHeaders = mapOf(authHeader)
    val previewResponseHeaders = mapOf(contentTypeHeader)
    val previewEvent = LogEvent(
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
    )
    val previewState = ViewDetails(previewEvent, selectedTab = Tab.Response)
    PreviewSurface {
        LogDetailsContent(
            state = previewState,
            onTabSelected = {},
        )
    }
}

@Composable
internal fun MonitorLogDetailsContent(
    state: MonitorLogDetailsViewModel.State,
    onTabSelected: (Tab) -> Unit,
    onClose: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            MonitorLogDetailsViewModel.State.Empty -> MonitorLogDetailsEmptyContent()
            is ViewDetails -> LogDetailsContent(
                state = state,
                onTabSelected = onTabSelected,
                onClose = onClose
            )
        }
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
        LogHeaderBar(logDetail = state.logEvent, onClose = onClose)
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
                    BodyContent(state.responseBodyState, state.logEvent.responseTypeFormat)
                }

                Tab.Request -> {
                    SectionTitle(label = strings.widgets.logDetails.requestHeaders)
                    HeadersContent(state.logEvent.requestHeaders.toList(), strings)
                    Spacer(Modifier.height(8.dp))
                    SectionTitle(label = strings.widgets.logDetails.requestBody)
                    BodyContent(state.requestBodyState, state.logEvent.requestTypeFormat)
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
                tint = MaterialTheme.colorScheme.onSurfaceFaint
            )
        }
    )
}

@Composable
private fun LogHeaderBar(
    logDetail: LogEvent,
    onClose: () -> Unit
) = Box {
    val monoFont = LocalMonoFontFamily.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            LogStatusBadge(status = logDetail.status)
            Text(
                text = logDetail.url,
                style = MaterialTheme.typography.titleMedium,
                fontFamily = monoFont,
                maxLines = 1,
                overflow = TextOverflow.StartEllipsis,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f).padding(end = 20.dp),
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MetaItem(icon = "⏱", label = "${logDetail.delay}ms")
            logDetail.requestSizeBytes?.let { size -> MetaItem(icon = "↑", label = size.toKbLabel()) }
            logDetail.responseSizeBytes?.let { size -> MetaItem(icon = "↓", label = size.toKbLabel()) }

            Text(
                text = formatTimestamp(logDetail.timestamp),
                style = MaterialTheme.typography.labelMedium.copy(fontFamily = monoFont),
                color = MaterialTheme.colorScheme.onSurfaceFaint,
            )
        }
    }

    IconButton(onClick = onClose, Modifier.align(Alignment.TopEnd)) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp),
        )
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
            .background(
                color = statusColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(percent = 50)
            )
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
) {
    val monoFont = LocalMonoFontFamily.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceMuted,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontFamily = monoFont),
            color = MaterialTheme.colorScheme.onSurfaceMuted,
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Tab.entries.forEach { tab ->
            val isSelected = tab == selectedTab

            val selectedColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceMuted
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
    body: MonitorLogDetailsViewModel.State.BodyState,
    mode: EditorMode,
) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .background(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = RoundedCornerShape(8.dp),
        )
        .padding(12.dp),
) {
    when {
        BodyVisualTransformation.isBodyTooLarge(body.bodyOrPreview) -> {
            Text(
                text = LocalStrings.current.components.editor.largeFileSyntaxHighlightError,
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = LocalMonoFontFamily.current),
                color = MaterialTheme.colorScheme.onSurfaceMuted
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = body.bodyOrPreview,
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = LocalMonoFontFamily.current),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        body.bodyOrPreview.isEmpty() ->
            Text(
                text = LocalStrings.current.widgets.logDetails.emptyBody,
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = LocalMonoFontFamily.current),
                color = MaterialTheme.colorScheme.onSurfaceFaint,
            )
        else -> {
            val annotatedBody =
                BodyVisualTransformation.buildEditorOutputTransformation(mode)?.highlight(body.bodyOrPreview)
            Text(
                text = annotatedBody ?: AnnotatedString(body.bodyOrPreview),
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = LocalMonoFontFamily.current),
                color = MaterialTheme.colorScheme.onSurface,
            )
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
            color = MaterialTheme.colorScheme.onSurfaceMuted,
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
