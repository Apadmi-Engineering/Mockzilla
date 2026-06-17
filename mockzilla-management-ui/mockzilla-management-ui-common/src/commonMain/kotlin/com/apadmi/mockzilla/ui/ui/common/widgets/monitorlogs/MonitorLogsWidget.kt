package com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.ChipTone
import com.apadmi.mockzilla.ui.ui.common.components.EmptyState
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.StatusChip
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMonoFontFamily
import com.apadmi.mockzilla.ui.ui.common.theme.success
import com.apadmi.mockzilla.ui.ui.common.theme.warning

import io.ktor.http.HttpStatusCode
import org.koin.core.parameter.parametersOf
import kotlinx.datetime.Instant

/** Extracts `HH:mm:ss.mmm` from epoch millis (UTC). */
private fun Long.formatTimestamp(): String =
    Instant.fromEpochMilliseconds(this)
        .toString()
        .substringAfter("T")
        .substringBeforeLast("Z")

@Suppress("MAGIC_NUMBER")
@Composable
private fun HttpStatusCode.statusColor(isIntendedFailure: Boolean): Color {
    val cs = MaterialTheme.colorScheme
    return when {
        value in 200..299 -> cs.success.primary
        isIntendedFailure -> cs.warning.primary
        value >= 400 -> cs.error
        else -> cs.onSurface.copy(alpha = 0.5f)
    }
}

@Composable
fun MonitorLogsWidget(
    device: Device,
    onViewDetail: (LogEvent) -> Unit,
) {
    val viewModel = getViewModel<MonitorLogsViewModel>(key = device.toString()) { parametersOf(device) }
    val state by viewModel.state.collectAsState()

    MonitorLogsWidgetContent(
        state = state,
        onViewDetail = onViewDetail,
        onClearAll = viewModel::clearLogs,
    )
}

@Suppress("MAGIC_NUMBER")
@Composable
fun LogRow(
    modifier: Modifier,
    event: LogEvent,
) {
    val monoFont = LocalMonoFontFamily.current
    val cs = MaterialTheme.colorScheme
    val isSlowRequest = (event.delay ?: 0) > 1000
    val isRealError = event.status.value >= 400 && !event.isIntendedFailure
    val errorColor = cs.error
    val errorBgColor = cs.errorContainer
    val faintColor = cs.onSurface.copy(alpha = 0.3f)
    val slowColor = cs.warning.primary

    Row(
        modifier = modifier
            .background(if (isRealError) errorBgColor else Color.Transparent)
            .drawBehind {
                if (isRealError) {
                    drawRect(color = errorColor, size = Size(3.dp.toPx(), size.height))
                }
            }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = event.timestamp.formatTimestamp(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = monoFont,
                    fontWeight = FontWeight.Normal,
                ),
                color = faintColor,
                modifier = Modifier.defaultMinSize(minWidth = 88.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = event.url,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = monoFont,
                    fontWeight = FontWeight.Medium,
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )
            if (event.isIntendedFailure || isRealError) {
                Spacer(modifier = Modifier.width(8.dp))
                if (event.isIntendedFailure) {
                    StatusChip(label = "FORCED", tone = ChipTone.Err)
                } else {
                    StatusChip(label = "REAL", tone = ChipTone.Err)
                }
            }
        }

        Text(
            text = event.status.value.toString(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontFamily = monoFont,
                fontWeight = FontWeight.SemiBold,
            ),
            color = event.status.statusColor(event.isIntendedFailure),
            textAlign = TextAlign.End,
            modifier = Modifier.defaultMinSize(minWidth = 32.dp),
        )

        Text(
            text = "${event.delay}ms",
            style = MaterialTheme.typography.labelSmall.copy(fontFamily = monoFont),
            color = when {
                isRealError -> errorColor
                isSlowRequest -> slowColor
                else -> faintColor
            },
            textAlign = TextAlign.End,
            modifier = Modifier.defaultMinSize(minWidth = 52.dp),
        )
    }
}

@Preview
@Composable
fun MonitorLogsWidgetPreview() = PreviewSurface {
    MonitorLogsWidgetContent(
        state = MonitorLogsViewModel.State.DisplayLogs(
            entries = sequenceOf(
                LogEvent(
                    timestamp = 1_716_566_657_201,
                    url = "https://www.example.com/repairs/list",
                    requestBody = "",
                    requestHeaders = mapOf(),
                    responseHeaders = mapOf(),
                    responseBody = """{"items":[{"id":1,"status":"upcoming"}]}""",
                    status = HttpStatusCode.OK,
                    delay = 342,
                    method = "GET",
                    isIntendedFailure = false,
                ),
                LogEvent(
                    timestamp = 1_716_566_659_014,
                    url = "https://www.example.com/customer/get",
                    requestBody = "",
                    requestHeaders = mapOf(),
                    responseHeaders = mapOf(),
                    responseBody = """{"id":42,"name":"John"}""",
                    status = HttpStatusCode.OK,
                    delay = 12,
                    method = "GET",
                    isIntendedFailure = false,
                ),
                LogEvent(
                    timestamp = 1_716_566_661_889,
                    url = "https://www.example.com/auth/token",
                    requestBody = """{"user":"test"}""",
                    requestHeaders = mapOf(),
                    responseHeaders = mapOf(),
                    responseBody = """{"error":"forced failure"}""",
                    status = HttpStatusCode.InternalServerError,
                    delay = 1,
                    method = "POST",
                    isIntendedFailure = true,
                ),
                LogEvent(
                    timestamp = 1_716_566_664_501,
                    url = "https://www.example.com/repairs/expired",
                    requestBody = "",
                    requestHeaders = mapOf(),
                    responseHeaders = mapOf(),
                    responseBody = """{"error":"unauthorised"}""",
                    status = HttpStatusCode.Unauthorized,
                    delay = 1502,
                    method = "GET",
                    isIntendedFailure = false,
                ),
            ),
        ),
        onClearAll = {},
        onViewDetail = { _ -> },
    )
}

@Suppress("MAGIC_NUMBER")
@Composable
internal fun MonitorLogsWidgetContent(
    state: MonitorLogsViewModel.State.DisplayLogs,
    onClearAll: () -> Unit,
    onViewDetail: (LogEvent) -> Unit,
    onOpenPanel: () -> Unit = {},
    strings: Strings = LocalStrings.current,
) {
    val monoFont = LocalMonoFontFamily.current
    val cs = MaterialTheme.colorScheme
    val streamingColor = cs.success.primary
    val dimColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    val faintColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    val entryList = state.entries.toList()
    val titleStyle = MaterialTheme.typography.labelSmall.copy(
        fontFamily = monoFont,
        fontWeight = FontWeight.SemiBold,
    )
    val logsTitle = strings.widgets.logs.title.uppercase()

    var isExpanded by remember { mutableStateOf(false) }
    val chevronRotation by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        animationSpec = tween(durationMillis = 150),
        label = "chevronRotation",
    )

    Column(modifier = Modifier.background(cs.background)) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = faintColor,
                modifier = Modifier.size(14.dp).rotate(chevronRotation),
            )
            Text(
                text = logsTitle,
                style = titleStyle,
                color = dimColor,
            )
            Canvas(modifier = Modifier.size(6.dp)) { drawCircle(color = streamingColor) }
            Text(
                text = strings.widgets.logs.streaming,
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = monoFont),
                color = streamingColor,
            )
            Text(
                text = strings.widgets.logs.clickToInspect,
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = monoFont),
                color = faintColor,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.clickable(onClick = onOpenPanel),
                text = strings.widgets.logs.openInPanel,
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = monoFont),
                color = dimColor,
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(
                expandFrom = Alignment.Top,
                animationSpec = tween(durationMillis = 160),
            ) + fadeIn(animationSpec = tween(durationMillis = 120)),
            exit = shrinkVertically(
                shrinkTowards = Alignment.Top,
                animationSpec = tween(durationMillis = 130),
            ) + fadeOut(animationSpec = tween(durationMillis = 100)),
        ) {
            MonitorLogsList(
                entryList = entryList,
                onViewDetail = onViewDetail,
                modifier = Modifier.height(280.dp),
            )
        }
    }
}

@Suppress("MAGIC_NUMBER")
@Composable
private fun MonitorLogsList(
    entryList: List<LogEvent>,
    onViewDetail: (LogEvent) -> Unit,
    modifier: Modifier = Modifier,
    strings: Strings = LocalStrings.current,
) {
    if (entryList.isEmpty()) {
        val iconTint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        EmptyState(
            title = strings.widgets.logs.emptyTitle,
            description = strings.widgets.logs.emptyDescription,
            modifier = modifier,
            icon = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(28.dp),
                )
            },
        )
        return
    }

    val listState = rememberLazyListState()
    var previousSize by remember { mutableStateOf(entryList.size) }

    LaunchedEffect(previousSize, entryList.size) {
        val previous = previousSize
        val current = entryList.size
        if (previous != current) {
            if (current > previous) {
                val previousLastItemIndex = listState.layoutInfo.visibleItemsInfo.maxOf { it.index }
                if (previousLastItemIndex >= previous - 1) {
                    listState.scrollToItem(entryList.lastIndex)
                }
            }
            previousSize = current
        }
    }

    LazyColumn(modifier = modifier, state = listState) {
        entryList.forEach { logEvent ->
            item(key = logEvent.id) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    LogRow(
                        modifier = Modifier
                            .clickable(onClick = { onViewDetail(logEvent) }, role = Role.Button)
                            .fillMaxWidth(),
                        event = logEvent,
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}
