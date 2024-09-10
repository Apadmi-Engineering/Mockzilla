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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.desktop.di.utils.getViewModel
import com.apadmi.mockzilla.desktop.engine.device.Device
import com.apadmi.mockzilla.desktop.i18n.LocalStrings
import com.apadmi.mockzilla.desktop.i18n.Strings
import com.apadmi.mockzilla.desktop.ui.components.PreviewSurface
import com.apadmi.mockzilla.desktop.ui.components.ShowkaseComposable
import com.apadmi.mockzilla.desktop.ui.scaffold.HorizontalTab
import com.apadmi.mockzilla.desktop.ui.scaffold.HorizontalTabList
import com.apadmi.mockzilla.desktop.ui.theme.alternatingBackground
import com.apadmi.mockzilla.lib.internal.models.SerializableEndpointConfig
import com.apadmi.mockzilla.lib.models.DashboardOptionsConfig
import com.apadmi.mockzilla.lib.models.DashboardOverridePreset
import com.apadmi.mockzilla.lib.models.EndpointConfiguration

import io.ktor.http.HttpStatusCode
import org.koin.core.parameter.parametersOf

import kotlinx.coroutines.launch

private enum class Tab {
    Default,
    Error,
    Settings,
    ;
}

@Composable
fun EndpointDetailsWidget(
    device: Device,
    activeEndpoint: EndpointConfiguration.Key?
) {
    val viewModel = getViewModel<EndpointDetailsViewModel>(
        key = "${activeEndpoint?.raw}-$device"
    ) { parametersOf(activeEndpoint, device) }
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
        viewModel::onDefaultPresetSelected,
        viewModel::onErrorPresetSelected,
        viewModel::onResetAll
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
    onDefaultPresetSelected: (DashboardOverridePreset) -> Unit,
    onErrorPresetSelected: (DashboardOverridePreset) -> Unit,
    onResetAll: () -> Unit,
    strings: Strings = LocalStrings.current,
) = Column {
    val pagerState = rememberPagerState(initialPage = 0) { Tab.entries.size }
    val coroutineScope = rememberCoroutineScope()

    when (state) {
        is EndpointDetailsViewModel.State.Empty -> EmptyState()
        is EndpointDetailsViewModel.State.Endpoint -> {
            Text(
                text = state.config.name,
                modifier = Modifier.padding(horizontal = 8.dp),
                style = MaterialTheme.typography.displaySmall
            )

            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = strings.widgets.endpointDetails.failOptionsLabel,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Switch(
                    checked = state.fail == true,
                    onCheckedChange = onFailChange
                )
            }
            HorizontalTabList(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                tabs = Tab.entries.map { tabLabel ->
                    HorizontalTab(
                        when (tabLabel) {
                            Tab.Default -> strings.widgets.endpointDetails.defaultDataTab
                            Tab.Error -> strings.widgets.endpointDetails.errorDataTab
                            Tab.Settings -> strings.widgets.endpointDetails.generalTab
                        },
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
                            modifier = Modifier.alpha(if (state.fail == true) 0.9f else 1f),
                            statusCode = state.defaultStatus,
                            onStatusCodeChange = onDefaultStatusCodeChange,
                            body = state.defaultBody,
                            onResponseBodyChange = onDefaultBodyChange,
                            jsonEditing = state.jsonEditingDefault,
                            onJsonEditingChange = onJsonDefaultEditingChange,
                            bodyJsonError = state.defaultBodyJsonError,
                            onPresetSelected = onDefaultPresetSelected,
                            headers = state.defaultHeaders,
                            onHeadersChange = onDefaultHeadersChange,
                            presets = state.presets.successPresets
                        )

                        Tab.Error -> EndpointDetailsResponseBody(
                            statusCode = state.errorStatus,
                            onStatusCodeChange = onErrorStatusCodeChange,
                            body = state.errorBody,
                            onResponseBodyChange = onErrorBodyChange,
                            jsonEditing = state.jsonEditingError,
                            onJsonEditingChange = onJsonErrorEditingChange,
                            bodyJsonError = state.errorBodyJsonError,
                            onPresetSelected = onErrorPresetSelected,
                            headers = state.errorHeaders,
                            onHeadersChange = onErrorHeadersChange,
                            presets = state.presets.errorPresets
                        )

                        Tab.Settings -> Settings(
                            delayMillis = state.delayMillis,
                            onDelayChange = onDelayChange,
                            onResetAll = onResetAll
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
        onErrorPresetSelected = {},
        onDefaultPresetSelected = {},
        onResetAll = {}
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
        onErrorPresetSelected = {},
        onDefaultPresetSelected = {},
        onResetAll = {}
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
        onErrorPresetSelected = {},
        onDefaultPresetSelected = {},
        onResetAll = {}
    )
}

@Composable
private fun EmptyState() = Column(
    Modifier.fillMaxSize().padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterVertically)
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "\uD83D\uDC48",
        style = MaterialTheme.typography.displayLarge,
        textAlign = TextAlign.Center
    )
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Choose an Endpoint to start editing",
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun EndpointDetailsResponseBody(
    modifier: Modifier = Modifier,
    statusCode: HttpStatusCode?,
    onStatusCodeChange: (HttpStatusCode?) -> Unit,
    body: String?,
    onResponseBodyChange: (String?) -> Unit,
    jsonEditing: Boolean,
    onJsonEditingChange: (Boolean) -> Unit,
    bodyJsonError: String?,
    headers: List<Pair<String, String>>?,
    onHeadersChange: (List<Pair<String, String>>?) -> Unit,
    onPresetSelected: (DashboardOverridePreset) -> Unit,
    strings: Strings = LocalStrings.current,
    presets: List<DashboardOverridePreset>,
) = Column(modifier = modifier) {
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
    val inError = jsonEditing && bodyJsonError != null
    val localContentColor = LocalContentColor.current
    TextField(
        value = body ?: "",
        onValueChange = onResponseBodyChange,
        // Might not have enough screen real estate for a weight here, but don't particularly
        // want double scrolling either
        // Maybe we should have a button to open the body editor in a full screen size editor
        // rather than user being stuck with small text field inside widget
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp, max = 500.dp)
            .padding(horizontal = 8.dp)
            .then(
                if (inError) {
                    Modifier.semantics { error(strings.widgets.endpointDetails.invalidJson) }
                } else {
                    Modifier
                }
            ),
        enabled = body != null,
        label = {
            Text(
                text = body
                    ?.let { strings.widgets.endpointDetails.bodyLabel }
                    ?: strings.widgets.endpointDetails.bodyUnset
            )
        },
        supportingText = {
            if (inError) {
                // This text is decorative so we don't double up on the invalid JSON text
                // with it already applied to the text field semantics
                Text(
                    text = strings.widgets.endpointDetails.invalidJson,
                    modifier = Modifier.clearAndSetSemantics {
                        // Don't need to set any new semantics
                    },
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                )
            }
        },
        isError = jsonEditing && bodyJsonError != null,
        visualTransformation = EndpointBodyVisualTransformation(
            comment = SpanStyle(color = localContentColor.copy(alpha = 0.5F)),
            brace = SpanStyle(localContentColor.copy(alpha = 0.7F)),
            comma = SpanStyle(localContentColor.copy(alpha = 0.7F)),
            colon = SpanStyle(localContentColor.copy(alpha = 0.7F)),
            string = SpanStyle(),
            keyword = SpanStyle(),
            number = SpanStyle(),
            default = SpanStyle(localContentColor.copy(alpha = 0.7F)),
        ).takeIf { jsonEditing } ?: VisualTransformation.None,
        singleLine = false,
    )
    if (inError && !bodyJsonError.isNullOrEmpty()) {
        // We don't have control over how large the json error text is, most of the time it's only
        // a few lines but in extreme cases it can be quite a lot so this error message lives
        // outside the text field to avoid issues if it wouldn't fit.
        Text(
            text = bodyJsonError,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            color = MaterialTheme.colorScheme.error,
            // Monospace the error text to match the text input field
            style = MaterialTheme.typography.bodySmall.copy(fontFeatureSettings = "tnum"),
        )
    }
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
                body?.let {
                    onResponseBodyChange(null)
                } ?: onResponseBodyChange("")
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
    Spacer(modifier = Modifier.heightIn(8.dp))
    PresetsSelector(
        presets = presets,
        onPresetSelected = onPresetSelected,
    )
}

@Composable
private fun PresetsSelector(
    presets: List<DashboardOverridePreset>,
    onPresetSelected: (DashboardOverridePreset) -> Unit
) = Column {
    presets.forEachIndexed { index, preset ->
        Column(
            modifier = Modifier.fillMaxWidth().alternatingBackground(index).padding(8.dp),
        ) {
            Row {
                Column(
                    modifier = Modifier.weight(1F),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(preset.name)
                    Text(
                        modifier = Modifier.alpha(0.75f),
                        text = preset.description ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Button(onClick = { onPresetSelected(preset) }) {
                    Text("Apply Preset")
                }
            }
            val maxLines = if (index == 0) {
                20
            } else {
                3
            }
            Text(
                modifier = Modifier.alpha(0.5f),
                text = preset.response.body,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Suppress("LOCAL_VARIABLE_EARLY_DECLARATION")
@Composable
private fun Settings(
    delayMillis: String?,
    onDelayChange: (String?) -> Unit,
    onResetAll: () -> Unit,
    strings: Strings = LocalStrings.current,
) = Column {
    val regex = remember { Regex("[^0-9 ]") }
    var customDelay by remember(delayMillis != null) { mutableStateOf(delayMillis) }
    Spacer(Modifier.height(4.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = strings.widgets.endpointDetails.responseDelay,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
        TextField(
            value = customDelay ?: "",
            onValueChange = { newValue ->
                customDelay = regex.replace(newValue.take(6), "")
                onDelayChange(customDelay?.takeUnless { it.isBlank() })
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            singleLine = true,
            label = { Text(text = strings.widgets.endpointDetails.responseDelayLabel) },
            suffix = { Text(text = strings.widgets.endpointDetails.responseDelayUnits) },
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = onResetAll,  // TODO: Add confirmation popup
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
