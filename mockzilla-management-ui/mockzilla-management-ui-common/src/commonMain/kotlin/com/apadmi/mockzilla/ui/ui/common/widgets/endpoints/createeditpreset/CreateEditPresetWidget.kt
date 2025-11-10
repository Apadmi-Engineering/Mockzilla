@file:Suppress(
    "FILE_NAME_MATCH_CLASS",
    "MAGIC_NUMBER",
    "TYPE_ALIAS"
)

package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.CustomTextField
import com.apadmi.mockzilla.ui.ui.common.components.DropdownMenu
import com.apadmi.mockzilla.ui.ui.common.components.EmptyState
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.SurfaceHeader
import com.apadmi.mockzilla.ui.ui.common.components.Tag
import com.apadmi.mockzilla.ui.ui.common.components.TogglableProgressIndicator
import com.apadmi.mockzilla.ui.ui.common.components.buttons.BaseButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomOutlineButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.OutlineButtonVariant
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset.CreateEditPresetViewModel.*
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset.components.EditResponseBody

import io.ktor.http.HttpStatusCode
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.parameter.parametersOf

@Composable
internal fun Modifier.card() = fillMaxWidth()
    .background(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp)
    )
    .border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outline,
        shape = RoundedCornerShape(12.dp)
    )

@Composable
private fun ColumnScope.PopulatedState(
    state: State.Editing,
    onSave: () -> Unit,
    onStatusCodeSelected: (HttpStatusCode) -> Unit,
    onResetStatusCode: () -> Unit,
    onNewResponseType: (State.Editing.ResponseType) -> Unit,
    onNewResponseBody: (String) -> Unit,
    onFormatResponseBody: () -> Unit,
    onResetResponseBody: () -> Unit,
    onUpdateNewHeader: (String?, String?) -> Unit,
    onUpdateHeader: (State.Editing.RequestHeader, String?, String?) -> Unit,
    onAddHeader: () -> Unit,
    onRemoveHeader: (State.Editing.RequestHeader) -> Unit,
    onResetHeaders: () -> Unit,
    strings: Strings = LocalStrings.current
) {
    Box {
        SurfaceHeader(
            title = when (state.variant) {
                State.Editing.Variant.Create -> strings.widgets.createEditPreset.createTitle
                State.Editing.Variant.Edit -> strings.widgets.createEditPreset.editTitle
            },
            subtitle = null
        ) {
            BaseButton(
                label = strings.widgets.createEditPreset.save,
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = onSave
            )
        }

        Box(Modifier.height(12.dp).fillMaxWidth().clipToBounds()) {
            TogglableProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                isLoading = state.isSaving,
                trackColor = Color.Transparent
            )
        }
    }

    EditStatusCode(
        modifier = Modifier.padding(horizontal = 12.dp),
        statusCode = state.statusCode,
        onResetStatusCode = onResetStatusCode,
        onStatusCodeSelected = onStatusCodeSelected
    )

    EditResponseBody(
        modifier = Modifier.padding(horizontal = 12.dp),
        state = state,
        onNewResponseType = onNewResponseType,
        onNewResponseBody = onNewResponseBody,
        onFormatResponseBody = onFormatResponseBody,
        onResetResponseBody = onResetResponseBody,
    )

    EditHeaders(
        modifier = Modifier.padding(horizontal = 12.dp),
        state = state,
        onUpdateNewHeader = onUpdateNewHeader,
        onUpdateHeader = onUpdateHeader,
        onAddHeader = onAddHeader,
        onRemoveHeader = onRemoveHeader,
        onResetHeaders = onResetHeaders
    )
}

@Composable
fun CreateEditPresetWidget(
    device: Device,
    activeEndpoint: EndpointConfiguration.Key,
    creatingNewPreset: Boolean
) {
    val viewModel = getViewModel<CreateEditPresetViewModel>(
        key = "${activeEndpoint.raw}-$device"
    ) {
        parametersOf(
            activeEndpoint,
            device,
            when (creatingNewPreset) {
                true -> State.Editing.Variant.Create
                false -> State.Editing.Variant.Edit
            }
        )
    }
    val state by viewModel.state

    CreateEditPresetWidgetContent(
        state = state,
        onSave = viewModel::save,
        onStatusCodeSelected = viewModel::onNewStatusCode,
        onResetStatusCode = viewModel::clearStatusCode,
        onNewResponseType = viewModel::onNewResponseType,
        onNewResponseBody = viewModel::onNewResponseBody,
        onFormatResponseBody = viewModel::onFormatResponseBody,
        onResetResponseBody = viewModel::clearResponseBody,
        onUpdateNewHeader = viewModel::onUpdateNewHeader,
        onUpdateHeader = viewModel::onUpdateHeader,
        onAddHeader = viewModel::onAddHeader,
        onRemoveHeader = viewModel::onRemoveHeader,
        onResetHeaders = viewModel::clearHeaders
    )
}

@Composable
fun CreateEditPresetWidgetContent(
    state: State,
    onSave: () -> Unit,
    onStatusCodeSelected: (HttpStatusCode) -> Unit,
    onResetStatusCode: () -> Unit,
    onNewResponseType: (State.Editing.ResponseType) -> Unit,
    onNewResponseBody: (String) -> Unit,
    onFormatResponseBody: () -> Unit,
    onResetResponseBody: () -> Unit,
    onUpdateNewHeader: (String?, String?) -> Unit,
    onUpdateHeader: (State.Editing.RequestHeader, String?, String?) -> Unit,
    onAddHeader: () -> Unit,
    onRemoveHeader: (State.Editing.RequestHeader) -> Unit,
    onResetHeaders: () -> Unit,
    strings: Strings = LocalStrings.current
) = Column(
    modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .background(color = MaterialTheme.colorScheme.background)
        .navigationBarsPadding()
        .padding(bottom = 12.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
) {
    when (state) {
        is State.Loading -> EmptyState(
            title = strings.widgets.endpointDetails.emptyTitle,
            description = strings.widgets.endpointDetails.emptyDescription
        )

        is State.Editing -> PopulatedState(
            state = state,
            onSave = onSave,
            onStatusCodeSelected = onStatusCodeSelected,
            onResetStatusCode = onResetStatusCode,
            onNewResponseType = onNewResponseType,
            onNewResponseBody = onNewResponseBody,
            onFormatResponseBody = onFormatResponseBody,
            onResetResponseBody = onResetResponseBody,
            onUpdateNewHeader = onUpdateNewHeader,
            onUpdateHeader = onUpdateHeader,
            onAddHeader = onAddHeader,
            onRemoveHeader = onRemoveHeader,
            onResetHeaders = onResetHeaders
        )
    }
}

@Composable
internal fun TitleRow(
    isSet: Boolean,
    icon: ImageVector,
    onReset: () -> Unit,
    title: String,
    strings: Strings = LocalStrings.current,
) = Row(
    modifier = Modifier.padding(start = 12.dp, end = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    Icon(
        imageVector = icon,
        modifier = Modifier.size(16.dp),
        contentDescription = null
    )
    Text(
        modifier = Modifier.weight(1f),
        text = title,
        style = MaterialTheme.typography.titleMedium
    )

    if (isSet) {
        IconButton(onClick = onReset) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Default.Restore,
                contentDescription = strings.common.resetDescription
            )
        }
    } else {
        Tag(
            modifier = Modifier.padding(top = 12.dp, bottom = 12.dp, end = 12.dp),
            label = strings.widgets.createEditPreset.unset,
            textColor = MaterialTheme.colorScheme.onSurface,
            borderColor = MaterialTheme.colorScheme.onSurface,
            backgroundColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
private fun EditStatusCode(
    modifier: Modifier = Modifier,
    statusCode: HttpStatusCode?,
    onResetStatusCode: () -> Unit,
    onStatusCodeSelected: (HttpStatusCode) -> Unit,
    strings: Strings = LocalStrings.current
) = Column(
    modifier = modifier.card()
) {
    TitleRow(
        isSet = statusCode != null,
        icon = Icons.Default.Dialpad,
        onReset = onResetStatusCode,
        title = strings.widgets.createEditPreset.statusCodeTitle
    )

    DropdownMenu(
        stringForItem = { strings.widgets.createEditPreset.statusCodeLabel(it) },
        items = HttpStatusCode.allStatusCodes,
        selectedLabel = statusCode
            ?.let { strings.widgets.createEditPreset.statusCodeLabel(it) }
            ?: strings.widgets.createEditPreset.noOverrideStatusCode,
        onSelected = onStatusCodeSelected
    )
}

@Composable
private fun EditHeaders(
    modifier: Modifier = Modifier,
    state: State.Editing,
    onUpdateNewHeader: (String?, String?) -> Unit,
    onUpdateHeader: (State.Editing.RequestHeader, String?, String?) -> Unit,
    onAddHeader: () -> Unit,
    onRemoveHeader: (State.Editing.RequestHeader) -> Unit,
    onResetHeaders: () -> Unit,
    strings: Strings = LocalStrings.current
) = Column(
    modifier = modifier.card()
) {
    TitleRow(
        isSet = state.headers.isNotEmpty(),
        icon = Icons.Default.Settings,
        onReset = onResetHeaders,
        title = strings.widgets.createEditPreset.headersTitle
    )

    state.headers.forEach { requestHeader ->
        Box(contentAlignment = Alignment.CenterEnd) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(12.dp).card(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = requestHeader.key,
                    onValueChange = { onUpdateHeader(requestHeader, it, null) }
                )
                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = requestHeader.value,
                    onValueChange = { onUpdateHeader(requestHeader, null, it) }
                )
            }

            IconButton(
                onClick = { onRemoveHeader(requestHeader) },
                modifier = Modifier.padding(end = 12.dp),
                colors = IconButtonDefaults.iconButtonColors().copy(
                    containerColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Default.Delete,
                    contentDescription = strings.common.deleteDescription,
                    tint = MaterialTheme.colorScheme.surfaceDim
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.outline
    )

    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = strings.widgets.createEditPreset.addHeaderTitle,
            style = MaterialTheme.typography.titleSmall
        )

        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.newHeader.key,
            placeholder = { Text(text = strings.widgets.createEditPreset.addHeaderKeyPlaceholder) },
            onValueChange = { onUpdateNewHeader(it, null) }
        )

        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.newHeader.value,
            placeholder = { Text(text = strings.widgets.createEditPreset.addHeaderValuePlaceholder) },
            onValueChange = { onUpdateNewHeader(null, it) }
        )

        CustomOutlineButton(
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Default.Add,
            label = strings.widgets.createEditPreset.addHeaderButton,
            variant = OutlineButtonVariant.Secondary,
            onClick = onAddHeader
        )
    }
}

@Preview
@Composable
private fun CreateEditPresetWidgetPreview() = PreviewSurface {
    CreateEditPresetWidgetContent(
        state = State.Editing(
            isSaving = false,
            statusCode = null,
            body = "",
            hasBodyError = false,
            headers = emptyList(),
            responseType = State.Editing.ResponseType.Json,
            variant = State.Editing.Variant.Create
        ),
        onSave = {},
        onStatusCodeSelected = {},
        onNewResponseType = {},
        onNewResponseBody = {},
        onFormatResponseBody = {},
        onResetStatusCode = {},
        onResetResponseBody = {},
        onUpdateNewHeader = { _, _ -> },
        onUpdateHeader = { _, _, _ -> },
        onAddHeader = {},
        onRemoveHeader = {},
        onResetHeaders = {}
    )
}
