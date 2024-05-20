@file:Suppress("MAGIC_NUMBER", "FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.details

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.desktop.di.utils.getViewModel
import com.apadmi.mockzilla.desktop.i18n.LocalStrings
import com.apadmi.mockzilla.desktop.i18n.Strings
import com.apadmi.mockzilla.desktop.ui.components.PreviewSurface
import com.apadmi.mockzilla.desktop.ui.scaffold.HorizontalTab
import com.apadmi.mockzilla.desktop.ui.scaffold.HorizontalTabList

import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.ktor.http.HttpStatusCode

private enum class Tab {
    Default,
    Error,
    Settings,
    ;
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ColumnScope.EndpointDetailsResponseBody(
    statusCode: HttpStatusCode?,
    onStatusCodeChange: (HttpStatusCode?) -> Unit,
    body: String?,
    onResponseBodyChange: (String?) -> Unit,
    jsonEditing: Boolean,
    onJsonEditingChange: (Boolean) -> Unit,
    strings: Strings = LocalStrings.current,
) {
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
            // TODO: Work out how to also let user type in custom number like on web portal
            onValueChange = {},
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            readOnly = true,
            singleLine = true,
            label = { Text(text = "Status code") },
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
    body?.let {
        TextField(
            value = body,
            onValueChange = onResponseBodyChange,
            // Might not have enough screen real estate for a weight here, but don't particularly
            // want double scrolling either
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = 500.dp)
                .padding(horizontal = 8.dp),
            label = { Text(text = strings.widgets.endpointDetails.bodyLabel) },
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
                                text = strings.widgets.endpointDetails.jsonEditingLabel(
                                    option
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        ),
                        // Remove icon as we don't have much horizontal space to work with
                        // due to row here and sometimes very minimal horizontal area
                        icon = {},
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            Button(
                // TODO: Might want warning here before losing mock data
                onClick = { onResponseBodyChange(null) },
            ) {
                Text(text = strings.widgets.endpointDetails.reset)
            }
        }
    } ?: run {
        Text(
            text = strings.widgets.endpointDetails.noOverrideBody,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Button(
            onClick = { onResponseBodyChange("") },
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(text = strings.widgets.endpointDetails.edit)
        }
    }
}

@Composable
private fun ColumnScope.Settings() {
    // TODO
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
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
    strings: Strings = LocalStrings.current,
) = Column {
    var tab by remember { mutableStateOf(Tab.Default) }
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
            // TODO: Needs to scroll horizontally if really constrained on horizontal space
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
            // TODO: Pager here as side swipe animation is really useful feedback
            HorizontalTabList(
                modifier = Modifier.fillMaxWidth(),
                tabs = Tab.entries.map { tabLabel ->
                    HorizontalTab(
                        when (tabLabel) {
                            Tab.Default -> strings.widgets.endpointDetails.defaultDataTab
                            Tab.Error -> strings.widgets.endpointDetails.errorDataTab
                            Tab.Settings -> strings.widgets.endpointDetails.settingsTab
                        }
                    )
                },
                selected = tab.ordinal,
                onSelect = { tab = Tab.entries[it] }
            )
            when (tab) {
                Tab.Default -> EndpointDetailsResponseBody(
                    statusCode = state.defaultStatus,
                    onStatusCodeChange = onDefaultStatusCodeChange,
                    body = state.defaultBody,
                    onResponseBodyChange = onDefaultBodyChange,
                    jsonEditing = state.jsonEditingDefault,
                    onJsonEditingChange = onJsonDefaultEditingChange,
                )

                Tab.Error -> EndpointDetailsResponseBody(
                    statusCode = state.errorStatus,
                    onStatusCodeChange = onErrorStatusCodeChange,
                    body = state.errorBody,
                    onResponseBodyChange = onErrorBodyChange,
                    jsonEditing = state.jsonEditingError,
                    onJsonEditingChange = onJsonErrorEditingChange,
                )
                Tab.Settings -> Settings()
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
    )
}
