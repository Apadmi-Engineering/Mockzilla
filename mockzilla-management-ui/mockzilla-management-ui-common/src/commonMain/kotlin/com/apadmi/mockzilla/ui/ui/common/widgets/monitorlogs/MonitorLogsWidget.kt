package com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.ChipTone
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.StatusChip
import com.apadmi.mockzilla.ui.ui.common.components.buttons.BaseButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonSize
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonVariant
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMockzillaTokens
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMonoFontFamily
import com.apadmi.mockzilla.ui.ui.common.utils.methodColor

import io.ktor.http.HttpStatusCode
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.parameter.parametersOf

@Suppress("MAGIC_NUMBER")
private fun HttpStatusCode.chipTone(): ChipTone = when (this.value) {
    in 200..299 -> ChipTone.Ok
    in 400..499 -> ChipTone.Warn
    in 500..599 -> ChipTone.Err
    else -> ChipTone.Neutral
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

@Composable
fun MonitorLogsWidgetContent(
    state: MonitorLogsViewModel.State.DisplayLogs,
    onClearAll: () -> Unit,
    onViewDetail: (LogEvent) -> Unit,
    strings: Strings = LocalStrings.current,
) {
    val tokens = LocalMockzillaTokens.current
    Column(modifier = Modifier.background(tokens.bg0)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = strings.widgets.logs.title,
                style = MaterialTheme.typography.labelSmall,
                color = tokens.fg2,
            )
            BaseButton(
                label = strings.widgets.logs.clearAll,
                variant = ButtonVariant.Soft,
                size = ButtonSize.Sm,
                onClick = onClearAll,
            )
        }
        MonitorLogsList(
            entries = state.entries,
            onViewDetail = onViewDetail,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
fun LogRow(
    modifier: Modifier,
    event: LogEvent,
) {
    val tokens = LocalMockzillaTokens.current
    val monoFont = LocalMonoFontFamily.current
    Row(
        modifier = modifier.padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        StatusChip(label = event.status.value.toString(), tone = event.status.chipTone())
        Text(
            text = event.method,
            style = MaterialTheme.typography.labelSmall.copy(fontFamily = monoFont),
            color = event.method.methodColor(tokens),
        )
        Spacer(Modifier.width(2.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = event.url,
            style = MaterialTheme.typography.bodySmall.copy(fontFamily = monoFont),
            color = tokens.fg1,
            maxLines = 1,
        )
        Text(
            text = "${event.delay}ms",
            style = MaterialTheme.typography.labelSmall.copy(fontFamily = monoFont),
            color = if ((event.delay ?: 0) > 1000) tokens.warn else tokens.fg3,
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
                    timestamp = 1000,
                    url = "https://www.example.com/repairs/list",
                    requestBody = "request body",
                    requestHeaders = mapOf(),
                    responseHeaders = mapOf(),
                    responseBody = "response body",
                    status = HttpStatusCode.OK,
                    delay = 50,
                    method = "GET",
                    isIntendedFailure = false,
                ),
                LogEvent(
                    timestamp = 2000,
                    url = "https://www.example.com/auth/login",
                    requestBody = "{}",
                    requestHeaders = mapOf(),
                    responseHeaders = mapOf(),
                    responseBody = "{}",
                    status = HttpStatusCode.NotFound,
                    delay = 1200,
                    method = "POST",
                    isIntendedFailure = true,
                ),
            ),
        ),
        onClearAll = {},
        onViewDetail = { _ -> },
    )
}

@Composable
private fun MonitorLogsList(
    entries: Sequence<LogEvent>,
    onViewDetail: (LogEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalMockzillaTokens.current
    val state = rememberLazyListState()
    val entryList = entries.toList()
    var previousSize by remember { mutableStateOf(entryList.size) }
    LaunchedEffect(previousSize, entryList.size) {
        val previous = previousSize
        val current = entryList.size
        if (previous != current) {
            if (current > previous) {
                val previousLastItemIndex = state.layoutInfo.visibleItemsInfo.maxOf { it.index }
                if (previousLastItemIndex >= previous - 1) {
                    state.scrollToItem(entryList.lastIndex)
                }
            }
            previousSize = current
        }
    }
    LazyColumn(modifier = modifier, state = state) {
        entryList.forEachIndexed { index, logEvent ->
            item {
                LogRow(
                    modifier = Modifier
                        .clickable(onClick = { onViewDetail(logEvent) }, role = Role.Button)
                        .fillMaxWidth()
                        .background(if (index % 2 == 0) tokens.bg1 else tokens.bg2),
                    event = logEvent,
                )
            }
        }
    }
}
