package com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.lib.internal.models.LogEvent
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.EmptyState
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import io.ktor.http.HttpStatusCode
import org.jetbrains.compose.ui.tooling.preview.Preview

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
fun MonitorLogDetailsWidget(
    logDetail: LogEvent?
) {
    val viewModel = getViewModel<MonitorLogDetailsViewModel>()
    val state by viewModel.state.collectAsState()

    // We need to hoist the view model above the Crossfade so switching between logs doesn't
    // reset the view model
    Crossfade(
        targetState = logDetail,
        animationSpec = tween(durationMillis = 200)
    ) { newState ->
        MonitorLogDetailsContent(
            logDetail = newState,
            state = state,
            onViewRequestHeaders = viewModel::onViewRequestHeaders,
            onViewRequestBody = viewModel::onViewRequestBody,
            onViewResponseHeaders = viewModel::onViewResponseHeaders,
            onViewResponseBody = viewModel::onViewResponseBody
        )
    }
}

@Composable
fun MonitorLogDetailsContent(
    logDetail: LogEvent?,
    state: MonitorLogDetailsViewModel.State.ViewDetails,
    onViewRequestHeaders: (Boolean) -> Unit,
    onViewRequestBody: (Boolean) -> Unit,
    onViewResponseHeaders: (Boolean) -> Unit,
    onViewResponseBody: (Boolean) -> Unit,
) {
    logDetail?.let {
        LogDetailsContent(
            logDetail = logDetail,
            visible = state,
            onViewRequestHeaders = onViewRequestHeaders,
            onViewRequestBody = onViewRequestBody,
            onViewResponseHeaders = onViewResponseHeaders,
            onViewResponseBody = onViewResponseBody,
        )
    } ?: MonitorLogDetailsEmptyContent()
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalTime::class)
@Composable
fun LogDetailsContent(
    logDetail: LogEvent,
    visible: MonitorLogDetailsViewModel.State.ViewDetails,
    onViewRequestHeaders: (Boolean) -> Unit,
    onViewRequestBody: (Boolean) -> Unit,
    onViewResponseHeaders: (Boolean) -> Unit,
    onViewResponseBody: (Boolean) -> Unit,
    strings: Strings = LocalStrings.current,
) = Column(
    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    SelectionContainer {
        Text(
            text = "${logDetail.status} - ${logDetail.method} - ${logDetail.url}",
            style = MaterialTheme.typography.displaySmall
        )
    }
    SelectionContainer {
        Text(
            text = Instant.fromEpochMilliseconds(logDetail.timestamp).toString(),
            style = MaterialTheme.typography.bodyLarge
        )
    }
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SelectionContainer {
            Text(
                text = "${logDetail.delay} ${strings.widgets.logDetails.responseDelayUnits}",
            )
        }
        Text(
            text = if (logDetail.isIntendedFailure) {
                strings.widgets.logDetails.intendedFailure
            } else {
                strings.widgets.logDetails.intendedSuccess
            }
        )
    }
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        // 2 per row groups request + response toggles nicely, 3 in first row looks bad
        maxItemsInEachRow = 2,
    ) {
        ViewToggleCheckbox(
            checked = visible.requestHeaders,
            onCheckedChanged = onViewRequestHeaders,
            title = "Request headers"
        )
        ViewToggleCheckbox(
            checked = visible.requestBody,
            onCheckedChanged = onViewRequestBody,
            title = strings.widgets.logDetails.requestBody
        )
        ViewToggleCheckbox(
            checked = visible.responseHeaders,
            onCheckedChanged = onViewResponseHeaders,
            title = "Response headers"
        )
        ViewToggleCheckbox(
            checked = visible.responseBody,
            onCheckedChanged = onViewResponseBody,
            title = strings.widgets.logDetails.responseBody
        )
    }
    Column {
        AnimatedVisibility(visible = visible.requestHeaders) {
            HeaderDetail(
                title = strings.widgets.logDetails.requestHeaders,
                headers = logDetail.requestHeaders.toList()
            )
        }
        AnimatedVisibility(visible = visible.requestBody) {
            BodyDetail(
                title = strings.widgets.logDetails.requestBody,
                body = logDetail.requestBody
            )
        }
        AnimatedVisibility(visible = visible.responseHeaders) {
            HeaderDetail(
                title = strings.widgets.logDetails.responseHeaders,
                headers = logDetail.responseHeaders.toList()
            )
        }
        AnimatedVisibility(visible = visible.responseBody) {
            BodyDetail(
                title = strings.widgets.logDetails.responseBody,
                body = logDetail.responseBody
            )
        }
    }
}

@Composable
fun MonitorLogDetailsEmptyContent(
    strings: Strings = LocalStrings.current,
) {
    EmptyState(
        title = strings.widgets.logDetails.emptyTitle,
        description = strings.widgets.logDetails.emptyDescription
    )
}

@Preview
@Composable
fun MonitorLogDetailsWidgetPreview() = PreviewSurface {
    LogDetailsContent(
        logDetail = LogEvent(
            timestamp = 1000,
            url = "https://www.example.com/url",
            requestBody = "request body",
            requestHeaders = mapOf(),
            responseHeaders = mapOf(),
            responseBody = "response body",
            status = HttpStatusCode.OK,
            delay = 50,
            method = "GET",
            isIntendedFailure = false
        ),
        visible = MonitorLogDetailsViewModel.State.ViewDetails(
            requestHeaders = true,
            requestBody = true,
            responseHeaders = true,
            responseBody = true
        ),
        onViewRequestHeaders = {},
        onViewRequestBody = {},
        onViewResponseHeaders = {},
        onViewResponseBody = {}
    )
}

@Preview
@Composable
fun MonitorLogDetailsWidgetEmptyPreview() = PreviewSurface {
    Box(modifier = Modifier.size(300.dp)) {
        MonitorLogDetailsEmptyContent()
    }
}

@Composable
private fun ViewToggleCheckbox(
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    title: String,
) {
    Row(
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .toggleable(
                value = checked,
                onValueChange = onCheckedChanged,
                role = Role.Checkbox,
            )
            .padding(8.dp)
    ) {
        Text(text = title, modifier = Modifier.weight(1F, fill = false))
        Spacer(modifier = Modifier.width(4.dp))
        Checkbox(
            checked = checked,
            onCheckedChange = null,
        )
    }
}

@Composable
private fun BodyDetail(
    title: String,
    body: String,
    strings: Strings = LocalStrings.current,
) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        SelectionContainer {
            Text(text = body, fontFamily = FontFamily.Monospace)
        }
        if (body.isEmpty()) {
            Text(text = strings.widgets.logDetails.noBody)
        }
    }
}

@Composable
private fun HeaderDetail(
    title: String,
    headers: List<Pair<String, String>>,
    strings: Strings = LocalStrings.current,
) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        SelectionContainer {
            Column {
                headers.forEach { (key, value) ->
                    Text(
                        text = "$key: $value",
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
        if (headers.isEmpty()) {
            Text(text = strings.widgets.logDetails.noHeaders)
        }
    }
}
