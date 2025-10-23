@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AlignVerticalTop
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.CustomTextField
import com.apadmi.mockzilla.ui.ui.common.components.DropdownMenu
import com.apadmi.mockzilla.ui.ui.common.components.EmptyState
import com.apadmi.mockzilla.ui.ui.common.components.SurfaceHeader
import com.apadmi.mockzilla.ui.ui.common.components.Tag
import com.apadmi.mockzilla.ui.ui.common.components.TogglableProgressIndicator
import com.apadmi.mockzilla.ui.ui.common.components.buttons.BaseButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomOutlineButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.OutlineButtonVariant
import com.apadmi.mockzilla.ui.ui.common.theme.success
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset.CreateEditPresetViewModel.*

import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointBodyVisualTransformation
import io.ktor.http.HttpStatusCode
import org.koin.core.parameter.parametersOf

private fun State.Editing.ResponseType.string(
    strings: Strings,
) = when (this) {
    State.Editing.ResponseType.Json -> strings.widgets.createEditPreset.bodyTypeJson
    State.Editing.ResponseType.PlainText -> strings.widgets.createEditPreset.bodyTypePlain
}

@Composable
@Suppress("MAGIC_NUMBER")
private fun Modifier.card() = fillMaxWidth()
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
fun CreateEditPresetWidget(
    device: Device,
    activeEndpoint: EndpointConfiguration.Key,
    creatingNewPreset: Boolean
) {
    val viewModel = getViewModel<CreateEditPresetViewModel>(
        key = "${activeEndpoint.raw}-$device"
    ) {
        parametersOf(activeEndpoint, device, when (creatingNewPreset) {
            true -> State.Editing.Variant.Create
            false -> State.Editing.Variant.Edit
        })
    }
    val state by viewModel.state

    CreateEditPresetWidgetContent(
        state,
        onSave = viewModel::save,
        onResetStatusCode = {},
        onResetResponseBody = {},
        onResetHeaders = {},
    )
}

@Composable
fun CreateEditPresetWidgetContent(
    state: State,
    onSave: () -> Unit,
    onResetStatusCode: () -> Unit,
    onResetResponseBody: () -> Unit,
    onResetHeaders: () -> Unit,
    strings: Strings = LocalStrings.current,
) = Column(
    Modifier.fillMaxSize()
        .background(color = MaterialTheme.colorScheme.surface)
        .navigationBarsPadding()
        .background(color = MaterialTheme.colorScheme.background)
        .padding(bottom = 12.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
) {
    when (state) {
        is State.Loading -> EmptyState(
            title = strings.widgets.endpointDetails.emptyTitle,
            description = strings.widgets.endpointDetails.emptyDescription
        )

        is State.Editing -> PopulatedState(
            state,
            onSave = onSave,
            onResetStatusCode = onResetStatusCode,
            onResetResponseBody = onResetResponseBody,
            onResetHeaders = onResetHeaders
        )
    }
}

@Composable
private fun PopulatedState(
    state: State.Editing,
    onSave: () -> Unit,
    onResetStatusCode: () -> Unit,
    onResetResponseBody: () -> Unit,
    onResetHeaders: () -> Unit,
    strings: Strings = LocalStrings.current
) {
    Box {
        SurfaceHeader(
            title = when (state.variant) {
                State.Editing.Variant.Create -> strings.widgets.createEditPreset.createTitle
                State.Editing.Variant.Edit -> strings.widgets.createEditPreset.editTitle
            },
            subtitle = "",
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
        onChooseStatusCode = {}
    )

    EditResponseBody(
        modifier = Modifier.padding(horizontal = 12.dp),
        state = state,
        onResetResponseBody = onResetResponseBody,
    )

    EditHeaders(
        modifier = Modifier.padding(horizontal = 12.dp),
        headers = state.headers,
        onResetHeaders = { },
        onUpdateHeaders = { },
    )
}

@Composable
private fun EditStatusCode(
    statusCode: HttpStatusCode?,
    onResetStatusCode: () -> Unit,
    onChooseStatusCode: (HttpStatusCode) -> Unit,
    modifier: Modifier = Modifier,
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
        onSelected = onChooseStatusCode
    )
}

@Composable
private fun EditHeaders(
    headers: Map<String, String>?,
    onResetHeaders: () -> Unit,
    onUpdateHeaders: (Map<String, String>) -> Unit,
    modifier: Modifier = Modifier,
    strings: Strings = LocalStrings.current
) = Column(
    modifier = modifier.card()
) {
    TitleRow(
        isSet = headers != null,
        icon = Icons.Default.Settings,
        onReset = onResetHeaders,
        title = strings.widgets.createEditPreset.headersTitle
    )

    headers?.entries?.forEach { (key, value) ->
        Box(contentAlignment = Alignment.CenterEnd) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(8.dp).card(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomTextField(modifier = Modifier.fillMaxWidth(), value = key, onValueChange = {})
                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = value,
                    onValueChange = {})
            }

            IconButton(
                onClick = {},
                modifier = Modifier.padding(end = 8.dp),
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

    Spacer(Modifier.size(8.dp))
    HorizontalDivider(Modifier.fillMaxWidth())

    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = strings.widgets.createEditPreset.addHeaderTitle,
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(Modifier.size(4.dp))
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            value = "",
            onValueChange = {},
            placeholder = { Text(text = strings.widgets.createEditPreset.addHeaderKeyPlaceholder) },
        )
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            value = "",
            placeholder = { Text(text = strings.widgets.createEditPreset.addHeaderValuePlaceholder) },
            onValueChange = {})
        CustomOutlineButton(
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = rememberVectorPainter(Icons.Default.Add),
            label = strings.widgets.createEditPreset.addHeaderButton,
            variant = OutlineButtonVariant.Secondary,
            onClick = {}
        )
    }
}

@Composable
private fun EditResponseBody(
    state: State.Editing,
    onResetResponseBody: () -> Unit,
    modifier: Modifier = Modifier,
    strings: Strings = LocalStrings.current
) = Column(
    modifier = modifier.card().padding(bottom = 8.dp)
) {
    TitleRow(
        isSet = state.body != null,
        icon = Icons.Default.Code,
        title = strings.widgets.createEditPreset.bodyTitle,
        onReset = onResetResponseBody
    )
    DropdownMenu(
        stringForItem = { it.string(strings) },
        items = State.Editing.ResponseType.entries,
        selectedLabel = state.responseType.string(strings),
        onSelected = { }
    )
    Column(Modifier.padding(horizontal = 8.dp)) {
        Spacer(Modifier.size(4.dp))
        Editor(state.body, false, state.responseType, {})

        Spacer(Modifier.size(8.dp))
        Row(
            modifier = Modifier.padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = strings.widgets.createEditPreset.responseCharacters(state.body?.length ?: 0),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.weight(1f))
            Icon(
                modifier = Modifier.height(16.dp),
                imageVector = when (state.hasBodyError) {
                    false -> Icons.Default.Done
                    true -> Icons.Default.ErrorOutline
                },
                contentDescription = null,
                tint = when (state.hasBodyError) {
                    true -> MaterialTheme.colorScheme.error
                    false -> MaterialTheme.colorScheme.success.primary
                },
            )
            Spacer(Modifier.size(2.dp))
            Text(
                text = when (state.hasBodyError) {
                    true -> strings.widgets.createEditPreset.invalidLabel
                    false -> strings.widgets.createEditPreset.validLabel
                },
                style = MaterialTheme.typography.labelMedium,
                color = when (state.hasBodyError) {
                    true -> MaterialTheme.colorScheme.error
                    false -> MaterialTheme.colorScheme.success.primary
                }
            )
        }
        Spacer(Modifier.size(8.dp))
        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CustomOutlineButton(
                leadingIcon = rememberVectorPainter(Icons.Default.AlignVerticalTop),
                label = strings.widgets.createEditPreset.responseBodyFormat,
                variant = OutlineButtonVariant.Secondary,
                onClick = {}
            )
            CustomOutlineButton(
                leadingIcon = rememberVectorPainter(Icons.Default.CopyAll),
                label = strings.widgets.createEditPreset.responseBodyCopy,
                variant = OutlineButtonVariant.Secondary,
                onClick = {}
            )
        }
    }
}

@Composable
private fun TitleRow(
    isSet: Boolean,
    icon: ImageVector,
    onReset: () -> Unit,
    title: String,
    strings: Strings = LocalStrings.current,
) = Row(
    modifier = Modifier.padding(start = 12.dp),
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
private fun Editor(
    body: String?,
    hasError: Boolean,
    type: State.Editing.ResponseType,
    onResponseBodyChange: (String) -> Unit,
    strings: Strings = LocalStrings.current
) {
    val localContentColor = LocalContentColor.current
    CustomTextField(
        value = body ?: "",
        onValueChange = onResponseBodyChange,
        // Might not have enough screen real estate for a weight here, but don't particularly
        // want double scrolling either
        // Maybe we should have a button to open the body editor in a full screen size editor
        // rather than user being stuck with small text field inside widget
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp, max = 500.dp)
            .then(
                if (hasError) {
                    Modifier.semantics { error(strings.widgets.createEditPreset.invalidLabel) }
                } else {
                    Modifier
                }
            ),
        enabled = body != null,
        placeholder = {
            Text(
                strings.widgets.createEditPreset.responseBodyPlaceholder,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        isError = hasError,
        visualTransformation = EndpointBodyVisualTransformation(
            comment = SpanStyle(color = localContentColor.copy(alpha = 0.5F)),
            brace = SpanStyle(localContentColor.copy(alpha = 0.7F)),
            comma = SpanStyle(localContentColor.copy(alpha = 0.7F)),
            colon = SpanStyle(localContentColor.copy(alpha = 0.7F)),
            string = SpanStyle(),
            keyword = SpanStyle(),
            number = SpanStyle(),
            default = SpanStyle(localContentColor.copy(alpha = 0.7F)),
        ).takeIf { type == State.Editing.ResponseType.Json } ?: VisualTransformation.None,
        singleLine = false,
    )
}
