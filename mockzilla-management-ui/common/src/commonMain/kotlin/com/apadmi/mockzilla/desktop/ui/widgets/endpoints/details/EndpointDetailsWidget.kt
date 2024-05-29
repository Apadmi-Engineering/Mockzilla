@file:Suppress("MAGIC_NUMBER", "FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.details

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.desktop.di.utils.getViewModel
import com.apadmi.mockzilla.desktop.i18n.LocalStrings
import com.apadmi.mockzilla.desktop.i18n.Strings
import com.apadmi.mockzilla.desktop.ui.components.PreviewSurface
import com.apadmi.mockzilla.desktop.ui.components.ShowkaseComposable
import com.apadmi.mockzilla.desktop.ui.scaffold.HorizontalTab
import com.apadmi.mockzilla.desktop.ui.scaffold.HorizontalTabList
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig

import io.ktor.http.HttpStatusCode

import kotlinx.coroutines.launch

private enum class Tab {
    Default,
    Error,
    Settings,
    ;
}

@Composable
fun EndpointDetailsWidget() {
    val viewModel = getViewModel<EndpointDetailsViewModel>()
    val state by viewModel.state

    EndpointDetailsWidgetContent(
        state,
        viewModel::onDefaultBodyChange,
        viewModel::onErrorBodyChange,
        viewModel::onFailChange,
        viewModel::onJsonDefaultEditingChange,
        viewModel::onJsonErrorEditingChange,
        viewModel::onDefaultStatusChange,
        viewModel::onErrorStatusChange,
        viewModel::onDelayChange,
        viewModel::onDefaultHeadersChange,
        viewModel::onErrorHeadersChange,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EndpointDetailsWidgetContent(
    state: EndpointDetailsViewModel.State,
    onDefaultBodyChange: (String?) -> Unit,
    onErrorBodyChange: (String?) -> Unit,
    onFailChange: (Boolean?) -> Unit,
    onJsonDefaultEditingChange: (Boolean) -> Unit,
    onJsonErrorEditingChange: (Boolean) -> Unit,
    onDefaultStatusCodeChange: (HttpStatusCode?) -> Unit,
    onErrorStatusCodeChange: (HttpStatusCode?) -> Unit,
    onDelayChange: (String?) -> Unit,
    onDefaultHeadersChange: (List<Pair<String, String>>?) -> Unit,
    onErrorHeadersChange: (List<Pair<String, String>>?) -> Unit,
    strings: Strings = LocalStrings.current,
) = Column {
    val pagerState = rememberPagerState(initialPage = 0) { Tab.entries.size }
    val coroutineScope = rememberCoroutineScope()

    when (state) {
        is EndpointDetailsViewModel.State.Empty -> Text(text = strings.widgets.endpointDetails.none)
        is EndpointDetailsViewModel.State.Endpoint -> {
            Text(
                text = state.config.name,
                modifier = Modifier.padding(horizontal = 8.dp),
                style = MaterialTheme.typography.displaySmall
            )
            Text(
                text = strings.widgets.endpointDetails.failOptionsLabel,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.padding(horizontal = 8.dp),
            ) {
                val options = listOf(null, false, true)
                options.forEachIndexed { index, option ->
                    SegmentedButton(
                        selected = state.fail == option,
                        onClick = { onFailChange(option) },
                        label = {
                            Text(
                                text = strings.widgets.endpointDetails.failLabel(option),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        ),
                        // Remove icon as we don't have much horizontal space to work with
                        // due to three options here and sometimes very minimal horizontal area
                        icon = {},
                    )
                }
            }
            HorizontalTabList(
                modifier = Modifier.fillMaxWidth(),
                tabs = Tab.entries.map { tabLabel ->
                    HorizontalTab(
                        when (tabLabel) {
                            Tab.Default -> strings.widgets.endpointDetails.defaultDataTab
                            Tab.Error -> strings.widgets.endpointDetails.errorDataTab
                            Tab.Settings -> strings.widgets.endpointDetails.generalTab
                        }
                    )
                },
                selected = pagerState.currentPage,
                onSelect = { coroutineScope.launch { pagerState.animateScrollToPage(page = it) } }
            )
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                // May want to enable this for mobile, but on desktop with no snapping to pages
                // this doesn't feel very good
                userScrollEnabled = false,
            ) { tabIndex ->
                val tab = Tab.entries[tabIndex]
                Column {
                    when (tab) {
                        Tab.Default -> EndpointDetailsResponseBody(
                            statusCode = state.defaultStatus,
                            onStatusCodeChange = onDefaultStatusCodeChange,
                            body = state.defaultBody,
                            onResponseBodyChange = onDefaultBodyChange,
                            jsonEditing = state.jsonEditingDefault,
                            onJsonEditingChange = onJsonDefaultEditingChange,
                            headers = state.defaultHeaders,
                            onHeadersChange = onDefaultHeadersChange,
                        )

                        Tab.Error -> EndpointDetailsResponseBody(
                            statusCode = state.errorStatus,
                            onStatusCodeChange = onErrorStatusCodeChange,
                            body = state.errorBody,
                            onResponseBodyChange = onErrorBodyChange,
                            jsonEditing = state.jsonEditingError,
                            onJsonEditingChange = onJsonErrorEditingChange,
                            headers = state.errorHeaders,
                            onHeadersChange = onErrorHeadersChange,
                        )

                        Tab.Settings -> Settings(
                            delayMillis = state.delayMillis,
                            onDelayChange = onDelayChange,
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@ShowkaseComposable("EndpointDetails-None", "EndpointDetails")
@Composable
@Preview
fun EndpointDetailsWidgetNonePreview() = PreviewSurface {
    EndpointDetailsWidgetContent(
        state = EndpointDetailsViewModel.State.Empty,
        onDefaultBodyChange = {},
        onErrorBodyChange = {},
        onFailChange = {},
        onJsonDefaultEditingChange = {},
        onJsonErrorEditingChange = {},
        onDefaultStatusCodeChange = {},
        onErrorStatusCodeChange = {},
        onDelayChange = {},
        onDefaultHeadersChange = {},
        onErrorHeadersChange = {},
    )
}

@ShowkaseComposable("EndpointDetails-SetContent", "EndpointDetails")
@Composable
@Preview
fun EndpointDetailsWidgetPreview() = PreviewSurface {
    EndpointDetailsWidgetContent(
        state = EndpointDetailsViewModel.State.Endpoint(
            config = SerializableEndpointConfig.allNulls(
                key = "key",
                name = "getCows",
                versionCode = 1
            ),
            defaultBody = """{ "cows": [] }""",
            defaultStatus = HttpStatusCode.OK,
            defaultHeaders = listOf("a" to "b"),
            errorBody = """{ "error": 500 }""",
            errorStatus = HttpStatusCode.InternalServerError,
            errorHeaders = listOf(),
            fail = false,
            delayMillis = "100",
            jsonEditingDefault = true,
            jsonEditingError = true,
            presets = DashboardOptionsConfig(listOf(), listOf()),
        ),
        onDefaultBodyChange = {},
        onErrorBodyChange = {},
        onFailChange = {},
        onJsonDefaultEditingChange = {},
        onJsonErrorEditingChange = {},
        onDefaultStatusCodeChange = {},
        onErrorStatusCodeChange = {},
        onDelayChange = {},
        onDefaultHeadersChange = {},
        onErrorHeadersChange = {},
    )
}

@ShowkaseComposable("EndpointDetails-UnsetContent", "EndpointDetails")
@Composable
@Preview
fun EndpointDetailsWidgetUnsetPreview() = PreviewSurface {
    EndpointDetailsWidgetContent(
        state = EndpointDetailsViewModel.State.Endpoint(
            config = SerializableEndpointConfig.allNulls(
                key = "key",
                name = "getCows",
                versionCode = 1
            ),
            defaultBody = null,
            defaultStatus = null,
            defaultHeaders = null,
            errorBody = null,
            errorStatus = null,
            errorHeaders = null,
            fail = null,
            delayMillis = null,
            jsonEditingDefault = true,
            jsonEditingError = true,
            presets = DashboardOptionsConfig(listOf(), listOf()),
        ),
        onDefaultBodyChange = {},
        onErrorBodyChange = {},
        onFailChange = {},
        onJsonDefaultEditingChange = {},
        onJsonErrorEditingChange = {},
        onDefaultStatusCodeChange = {},
        onErrorStatusCodeChange = {},
        onDelayChange = {},
        onDefaultHeadersChange = {},
        onErrorHeadersChange = {},
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun EndpointDetailsResponseBody(
    statusCode: HttpStatusCode?,
    onStatusCodeChange: (HttpStatusCode?) -> Unit,
    body: String?,
    onResponseBodyChange: (String?) -> Unit,
    jsonEditing: Boolean,
    onJsonEditingChange: (Boolean) -> Unit,
    headers: List<Pair<String, String>>?,
    onHeadersChange: (List<Pair<String, String>>?) -> Unit,
    strings: Strings = LocalStrings.current,
) = Column {
    Spacer(Modifier.height(4.dp))
    var pickingStatusCode by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = pickingStatusCode,
        onExpandedChange = { pickingStatusCode = it },
        modifier = Modifier.padding(horizontal = 8.dp),
    ) {
        TextField(
            value = statusCode
                ?.let { strings.widgets.endpointDetails.statusCodeLabel(it) }
                ?: strings.widgets.endpointDetails.noOverrideStatusCode,
            // TODO: Work out how to also let user type in custom status number like on web portal
            onValueChange = {},
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            readOnly = true,
            singleLine = true,
            label = { Text(text = strings.widgets.endpointDetails.statusCode) },
        )
        ExposedDropdownMenu(
            expanded = pickingStatusCode,
            onDismissRequest = { pickingStatusCode = false },
        ) {
            DropdownMenuItem(
                text = { Text(text = strings.widgets.endpointDetails.noOverrideStatusCode) },
                onClick = {
                    onStatusCodeChange(null)
                    pickingStatusCode = false
                },
            )
            HttpStatusCode.allStatusCodes.forEach { statusCode ->
                DropdownMenuItem(
                    text = {
                        Text(text = strings.widgets.endpointDetails.statusCodeLabel(statusCode))
                    },
                    onClick = {
                        onStatusCodeChange(statusCode)
                        pickingStatusCode = false
                    },
                )
            }
        }
    }
    TextField(
        // TODO: Might want to show what the response body defaults to for reference purposes
        // so users can get an idea of what mockzilla is returning by default especially to
        // later edit
        value = body ?: "",
        onValueChange = onResponseBodyChange,
        // Might not have enough screen real estate for a weight here, but don't particularly
        // want double scrolling either
        // Maybe we should have a button to open the body editor in a full screen size editor
        // rather than user being stuck with small text field inside widget
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp, max = 500.dp)
            .padding(horizontal = 8.dp),
        enabled = body != null,
        label = {
            Text(
                text = body
                    ?.let { strings.widgets.endpointDetails.bodyLabel }
                    ?: strings.widgets.endpointDetails.bodyUnset
            )
        },
        singleLine = false,
    )
    FlowRow(
        modifier = Modifier.align(Alignment.End).padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.End,
    ) {
        SingleChoiceSegmentedButtonRow {
            val options = listOf(true, false)
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    selected = jsonEditing == option,
                    onClick = { onJsonEditingChange(option) },
                    label = {
                        Text(
                            text = strings.widgets.endpointDetails.jsonEditingLabel(option),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    enabled = body != null,
                    // Remove icon as we don't have much horizontal space to work with
                    // due to row here and sometimes very minimal horizontal area
                    icon = {},
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            // TODO: Might want warning here before losing mock data
            onClick = {
                // TODO: Might want this to prompt user to pick from presets if available
                onResponseBodyChange(body?.let { null } ?: "")
            },
        ) {
            Text(
                text = body
                    ?.let { strings.widgets.endpointDetails.reset }
                    ?: strings.widgets.endpointDetails.edit
            )
        }
    }
    Spacer(modifier = Modifier.heightIn(8.dp))
    HeadersEditor(headers, onHeadersChange)
}

@Suppress("LOCAL_VARIABLE_EARLY_DECLARATION")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Settings(
    delayMillis: String?,
    onDelayChange: (String?) -> Unit,
    strings: Strings = LocalStrings.current,
) = Column {
    var customDelay by remember { mutableStateOf("") }
    Spacer(Modifier.height(4.dp))
    Text(
        text = strings.widgets.endpointDetails.responseDelay,
        modifier = Modifier.padding(horizontal = 8.dp),
    )
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        val options = listOf(
            strings.widgets.endpointDetails.noOverrideResponseDelay,
            strings.widgets.endpointDetails.customResponseDelay
        )
        options.forEachIndexed { index, label ->
            SegmentedButton(
                selected = if (index == 0) {
                    delayMillis == null
                } else {
                    delayMillis != null
                },
                onClick = {
                    if (index == 0) {
                        onDelayChange(null)
                    } else {
                        onDelayChange(customDelay)
                    }
                },
                label = {
                    Text(
                        text = label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size,
                ),
                icon = {},
            )
        }
    }
    TextField(
        value = customDelay,
        onValueChange = {
            customDelay = it
            onDelayChange(it)
        },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
        enabled = delayMillis != null,
        singleLine = true,
        label = { Text(text = strings.widgets.endpointDetails.responseDelayLabel) },
        suffix = { Text(text = strings.widgets.endpointDetails.responseDelayUnits) },
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = { /* TODO, maybe with popup to confirm? */ },
        modifier = Modifier.padding(horizontal = 8.dp),
    ) {
        Text(text = strings.widgets.endpointDetails.resetAll)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HeadersEditor(
    headers: List<Pair<String, String>>?,
    onHeadersChange: (List<Pair<String, String>>?) -> Unit,
    strings: Strings = LocalStrings.current,
) = Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
    Text(text = strings.widgets.endpointDetails.headersLabel)
    Spacer(modifier = Modifier.height(4.dp))
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        headers?.forEachIndexed { index, entry ->
            Surface(
                color = MaterialTheme.colorScheme.background,
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    Column(
                        modifier = Modifier.weight(1F)
                    ) {
                        TextField(
                            value = entry.first,
                            onValueChange = {
                                onHeadersChange(
                                    headers
                                        .toMutableList()
                                        .apply { set(index, entry.copy(first = it)) }
                                        .toList()
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(text = strings.widgets.endpointDetails.headerKeyLabel)
                            },
                            singleLine = true,
                        )
                        Spacer(Modifier.height(4.dp))
                        TextField(
                            value = entry.second,
                            onValueChange = {
                                onHeadersChange(
                                    headers
                                        .toMutableList()
                                        .apply { set(index, entry.copy(second = it)) }
                                        .toList()
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(text = strings.widgets.endpointDetails.headerValueLabel)
                            },
                            singleLine = true,
                        )
                    }
                    IconButton(
                        onClick = {
                            onHeadersChange(
                                headers
                                    .toMutableList()
                                    .apply { removeAt(index) }
                                    .toList()
                            )
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = strings.widgets.endpointDetails.headerDeleteContentDescription
                        )
                    }
                }
            }
        }
    }
    if (headers != null && headers.isEmpty()) {
        Text(text = strings.widgets.endpointDetails.noHeaders)
    }
    headers ?: Text(text = strings.widgets.endpointDetails.headersUnset)
    Spacer(modifier = Modifier.height(4.dp))
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        Button(
            onClick = {
                headers
                    ?.let { onHeadersChange(headers + ("" to "")) }
                    ?: onHeadersChange(listOf())
            },
        ) {
            Text(
                text = headers
                    ?.let { strings.widgets.endpointDetails.addHeader }
                    ?: strings.widgets.endpointDetails.editHeaders
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        Button(
            onClick = { onHeadersChange(null) },
            enabled = headers != null,
        ) {
            Text(text = strings.widgets.endpointDetails.resetHeaders)
        }
    }
}