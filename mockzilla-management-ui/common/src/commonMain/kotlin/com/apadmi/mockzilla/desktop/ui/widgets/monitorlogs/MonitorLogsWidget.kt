package com.apadmi.mockzilla.desktop.ui.widgets.monitorlogs

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.apadmi.mockzilla.desktop.di.utils.getViewModel
import com.apadmi.mockzilla.desktop.ui.components.PreviewSurface
import com.apadmi.mockzilla.lib.internal.models.LogEvent

@Composable
fun MonitorLogsWidget() {
    val viewModel = getViewModel<MonitorLogsViewModel>()
    val state by viewModel.state.collectAsState()

    MonitorLogsWidgetContent(state)
}

@Composable
fun MonitorLogsWidgetContent(state: MonitorLogsViewModel.State) = when(state) {
    is MonitorLogsViewModel.State.DisplayLogs -> MonitorLogsList(state.entries)
    MonitorLogsViewModel.State.Empty -> Text("No logs")
}

@Composable
private fun MonitorLogsList(entries: Sequence<LogEvent>) = LazyColumn {
    entries.forEach { logEvent ->
        item(logEvent.timestamp) {
            LogRow(logEvent)
        }
    }
}

@Composable
fun LogRow(logEvent: LogEvent) {
    Text("${logEvent.status} ${logEvent.method}: ${logEvent.url}")
}

@ShowkaseComposable("MonitorLogsWidget-Empty", "MonitorLogsWidget")
@Composable
@Preview
fun MonitorLogsWidgetContentPreview() = PreviewSurface {
    MonitorLogsWidgetContent(MonitorLogsViewModel.State.Empty)
}