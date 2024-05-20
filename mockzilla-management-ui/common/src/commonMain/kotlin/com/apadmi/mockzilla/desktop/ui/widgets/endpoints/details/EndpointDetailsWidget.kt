@file:Suppress("MAGIC_NUMBER")

package com.apadmi.mockzilla.desktop.ui.widgets.endpoints.details

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.desktop.di.utils.getViewModel
import com.apadmi.mockzilla.desktop.i18n.LocalStrings
import com.apadmi.mockzilla.desktop.i18n.Strings
import com.apadmi.mockzilla.desktop.ui.components.PreviewSurface
import com.apadmi.mockzilla.desktop.ui.scaffold.HorizontalTab
import com.apadmi.mockzilla.desktop.ui.scaffold.HorizontalTabList

import com.airbnb.android.showkase.annotation.ShowkaseComposable
import io.ktor.http.HttpStatusCode

@Composable
fun ColumnScope.EndpointDetailsResponseBody(
    statusCode: HttpStatusCode?,
    onStatusCodeChange: (HttpStatusCode?) -> Unit,
    body: String?,
    onResponseBodyChange: (String?) -> Unit,
    strings: Strings = LocalStrings.current,
) {
    Spacer(Modifier.height(4.dp))
    statusCode?.let {
        // FIXME Add picker
        Text(
            text = strings.widgets.endpointDetails.statusCodeLabel(statusCode),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
        )
        Button(onClick = {}, modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(text = strings.widgets.endpointDetails.reset)
        }
    } ?: run {
        Text(
            text = strings.widgets.endpointDetails.noOverrideStatusCode,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Button(onClick = {}, modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(text = strings.widgets.endpointDetails.edit)
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
        // TODO: Might want warning here before losing mock data
        Button(
            onClick = { onResponseBodyChange(null) },
            modifier = Modifier.align(Alignment.End).padding(horizontal = 8.dp)
        ) {
            Text(text = strings.widgets.endpointDetails.reset)
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
fun EndpointDetailsWidget() {
    val viewModel = getViewModel<EndpointDetailsViewModel>()
    val state by viewModel.state

    EndpointDetailsWidgetContent(
        state,
        viewModel::onDefaultBodyChange,
        viewModel::onErrorBodyChange,
        viewModel::onFailChange
    )
}

@Composable
fun EndpointDetailsWidgetContent(
    state: EndpointDetailsViewModel.State,
    onDefaultBodyChange: (String?) -> Unit,
    onErrorBodyChange: (String?) -> Unit,
    onFailChange: (Boolean?) -> Unit,
    strings: Strings = LocalStrings.current,
) = Column {
    var tab by remember { mutableStateOf(0) }
    when (state) {
        is EndpointDetailsViewModel.State.Empty -> Text(text = strings.widgets.endpointDetails.none)
        is EndpointDetailsViewModel.State.Endpoint -> {
            Text(text = state.config.name, style = MaterialTheme.typography.displaySmall)
            val failState = when (state.fail) {
                null -> ToggleableState.Indeterminate
                true -> ToggleableState.On
                false -> ToggleableState.Off
            }
            Row(
                modifier = Modifier.triStateToggleable(
                    state = failState,
                    onClick = { onFailChange(failState == ToggleableState.Off) }
                )
            ) {
                TriStateCheckbox(
                    state = failState,
                    onClick = null,
                )
                Text(text = strings.widgets.endpointDetails.useErrorResponse)
                // TODO: Reset button to go back to indeterminate state
            }
            // TODO: Pager here as side swipe animation is really useful feedback
            HorizontalTabList(
                modifier = Modifier.fillMaxWidth(),
                tabs = listOf(
                    HorizontalTab(
                        strings.widgets.endpointDetails.defaultData
                    ),
                    HorizontalTab(
                        strings.widgets.endpointDetails.errorData
                    )
                ),
                selected = tab,
                onSelect = { tab = it }
            )
            when (tab) {
                0 -> EndpointDetailsResponseBody(
                    statusCode = state.defaultStatus,
                    onStatusCodeChange = {},
                    body = state.defaultBody,
                    onResponseBodyChange = onDefaultBodyChange,
                )

                1 -> EndpointDetailsResponseBody(
                    statusCode = state.defaultStatus,
                    onStatusCodeChange = {},
                    body = state.errorBody,
                    onResponseBodyChange = onErrorBodyChange,
                )
                else -> {
                    // this is a generated else block
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
    )
}
