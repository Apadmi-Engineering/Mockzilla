@file:Suppress(
    "FILE_NAME_MATCH_CLASS",
    "MAGIC_NUMBER",
    "TYPE_ALIAS"
)

package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.ChipTone
import com.apadmi.mockzilla.ui.ui.common.components.CustomTextField
import com.apadmi.mockzilla.ui.ui.common.components.EmptyState
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.StatusChip
import com.apadmi.mockzilla.ui.ui.common.components.TogglableProgressIndicator
import com.apadmi.mockzilla.ui.ui.common.components.buttons.BaseButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonSize
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonVariant
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomOutlineButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.OutlineButtonVariant
import com.apadmi.mockzilla.ui.ui.common.theme.jsonHighlight
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.theme.success
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset.CreateEditPresetViewModel.*
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.buildHighlightedAnnotatedString
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.urlToTitle

import io.ktor.http.HttpStatusCode
import org.koin.core.parameter.parametersOf

// ──────────────────────────────────────────────────────────────────────────────
// Kept for EditResponseBody.kt compatibility – do NOT remove or rename.
// ──────────────────────────────────────────────────────────────────────────────

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
private fun ColumnScope.PanelHeader(
    state: State.Editing,
    endpointName: String?,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    strings: Strings = LocalStrings.current,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = when (state.variant) {
                    State.Editing.Variant.Create -> strings.widgets.createEditPreset.createTitle
                    State.Editing.Variant.Edit -> strings.widgets.createEditPreset.editTitle
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            endpointName?.let {
                Text(
                    text = strings.widgets.createEditPreset.endpointSubtitle(it),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        CustomOutlineButton(
            label = strings.widgets.createEditPreset.cancel,
            variant = OutlineButtonVariant.Secondary,
            onClick = onCancel,
        )
        BaseButton(
            label = strings.widgets.createEditPreset.save,
            variant = ButtonVariant.Solid,
            size = ButtonSize.Md,
            onClick = onSave,
        )
    }

    Box(Modifier.height(2.dp).fillMaxWidth().clipToBounds()) {
        TogglableProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            isLoading = state.isSaving,
            trackColor = Color.Transparent,
        )
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
}

@Composable
private fun ColumnScope.BodySection(
    state: State.Editing,
    onNewResponseBody: (String) -> Unit,
    strings: Strings.Widgets.CreateEditPreset = LocalStrings.current.widgets.createEditPreset,
) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surfaceContainer),
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = strings.bodyLabel,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
        )
        // Char count + JSON validity combined on the right
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = strings.responseCharacters(state.body?.length ?: 0),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.success.primary,
            )
            if (state.responseType == State.Editing.ResponseType.Json) {
                Icon(
                    imageVector = if (state.hasBodyError) Icons.Default.ErrorOutline else Icons.Default.Done,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(14.dp),
                )
            }
        }
    }

    JsonBodyTextField(
        body = state.body ?: "",
        onBodyChange = onNewResponseBody,
        placeholder = strings.responseBodyPlaceholder,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .heightIn(min = 200.dp, max = 400.dp),
    )

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun ColumnScope.HeadersSection(
    state: State.Editing,
    onUpdateNewHeader: (String?, String?) -> Unit,
    onAddHeader: () -> Unit,
    onRemoveHeader: (State.Editing.RequestHeader) -> Unit,
    strings: Strings.Widgets.CreateEditPreset = LocalStrings.current.widgets.createEditPreset,
) {
    state.headers.forEach { header ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 4.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = header.key,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = " : ${header.value}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = { onRemoveHeader(header) }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
    }

    // Add header input row
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val mutedColor = MaterialTheme.colorScheme.onSurfaceMuted
        val inputBg = MaterialTheme.colorScheme.surfaceContainerLowest
        CustomTextField(
            modifier = Modifier.weight(1f).height(48.dp),
            value = state.newHeader.key,
            singleLine = true,
            containerColor = inputBg,
            placeholder = {
                Text(
                    text = strings.addHeaderKeyPlaceholder,
                    color = mutedColor,
                )
            },
            onValueChange = { onUpdateNewHeader(it, null) },
        )
        CustomTextField(
            modifier = Modifier.weight(1f).height(48.dp),
            value = state.newHeader.value,
            singleLine = true,
            containerColor = inputBg,
            placeholder = {
                Text(
                    text = strings.addHeaderValuePlaceholder,
                    color = mutedColor,
                )
            },
            onValueChange = { onUpdateNewHeader(null, it) },
        )
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest, RoundedCornerShape(8.dp))
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                .clickable(onClick = onAddHeader),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = strings.addHeaderButton,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
}

@Composable
private fun ColumnScope.PopulatedState(
    state: State.Editing,
    endpointName: String?,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    onStatusCodeSelected: (HttpStatusCode) -> Unit,
    onNewResponseType: (State.Editing.ResponseType) -> Unit,
    onNewResponseBody: (String) -> Unit,
    onUpdateNewHeader: (String?, String?) -> Unit,
    onAddHeader: () -> Unit,
    onRemoveHeader: (State.Editing.RequestHeader) -> Unit,
    strings: Strings = LocalStrings.current,
) {
    val str = strings.widgets.createEditPreset

    PanelHeader(state, endpointName, onCancel, onSave, strings)

    // ── RESPONSE section ─────────────────────────────────────────────────────
    SectionLabel(str.responseSectionLabel)

    // Status code — label left, dropdown aligned with body-type toggle start
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = str.statusCodeRowLabel,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier.weight(3f),
            contentAlignment = Alignment.CenterStart,
        ) {
            StatusCodeDropdown(
                statusCode = state.statusCode,
                onSelected = onStatusCodeSelected,
                strings = str,
            )
        }
    }

    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.outlineVariant,
    )

    // Body type — label left, toggle starts at same x as dropdown above
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = str.bodyTypeLabel,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier.weight(3f),
            contentAlignment = Alignment.CenterStart,
        ) {
            BodyTypeToggle(
                selected = state.responseType,
                onSelect = onNewResponseType,
                strings = str,
            )
        }
    }

    if (state.responseType != State.Editing.ResponseType.None) {
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant,
        )

        BodySection(state, onNewResponseBody, str)
    }

    // ── HEADERS section ───────────────────────────────────────────────────────
    val headerCount = state.headers.size
    val headersLabel = if (headerCount > 0) {
        "${strings.widgets.createEditPreset.headersTitle} ($headerCount)"
    } else {
        strings.widgets.createEditPreset.headersTitle
    }
    SectionLabel(headersLabel)

    HeadersSection(
        state = state,
        onUpdateNewHeader = onUpdateNewHeader,
        onAddHeader = onAddHeader,
        onRemoveHeader = onRemoveHeader,
        strings = str,
    )
}

// ──────────────────────────────────────────────────────────────────────────────
// Public API
// ──────────────────────────────────────────────────────────────────────────────

@Composable
fun CreateEditPresetWidget(
    device: Device,
    activeEndpoint: EndpointConfiguration.Key,
    creatingNewPreset: Boolean,
    onCancel: () -> Unit = {},
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
        endpointName = urlToTitle(activeEndpoint.raw),
        onCancel = onCancel,
        onSave = viewModel::save,
        onStatusCodeSelected = viewModel::onNewStatusCode,
        onNewResponseType = viewModel::onNewResponseType,
        onNewResponseBody = viewModel::onNewResponseBody,
        onUpdateNewHeader = viewModel::onUpdateNewHeader,
        onAddHeader = viewModel::onAddHeader,
        onRemoveHeader = viewModel::onRemoveHeader,
    )
}

@Composable
fun CreateEditPresetWidgetContent(
    state: State,
    endpointName: String? = null,
    onCancel: () -> Unit = {},
    onSave: () -> Unit,
    onStatusCodeSelected: (HttpStatusCode) -> Unit,
    onNewResponseType: (State.Editing.ResponseType) -> Unit,
    onNewResponseBody: (String) -> Unit,
    onUpdateNewHeader: (String?, String?) -> Unit,
    onAddHeader: () -> Unit,
    onRemoveHeader: (State.Editing.RequestHeader) -> Unit,
    strings: Strings = LocalStrings.current,
) = Column(
    modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .background(color = MaterialTheme.colorScheme.surfaceContainer)
        .navigationBarsPadding(),
) {
    when (state) {
        is State.Loading -> EmptyState(
            title = strings.widgets.endpointDetails.emptyTitle,
            description = strings.widgets.endpointDetails.emptyDescription,
        )

        is State.Editing -> PopulatedState(
            state = state,
            endpointName = endpointName,
            onCancel = onCancel,
            onSave = onSave,
            onStatusCodeSelected = onStatusCodeSelected,
            onNewResponseType = onNewResponseType,
            onNewResponseBody = onNewResponseBody,
            onUpdateNewHeader = onUpdateNewHeader,
            onAddHeader = onAddHeader,
            onRemoveHeader = onRemoveHeader,
            strings = strings,
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
    Icon(imageVector = icon, modifier = Modifier.size(16.dp), contentDescription = null)
    Text(modifier = Modifier.weight(1f), text = title, style = MaterialTheme.typography.titleMedium)
    if (isSet) {
        IconButton(onClick = onReset) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Default.Restore,
                contentDescription = strings.common.resetDescription
            )
        }
    }
}

/**
 * Applies JSON syntax highlighting to [text] without reformatting it.
 * Uses the theme's [jsonHighlight] colour palette so it adapts to light/dark mode.
 * Suitable for editable fields because it never reorders the characters.
 *
 * @param text The raw JSON (or plain) string to highlight.
 * @return
 */
@Composable
internal fun buildJsonAnnotatedString(text: String) =
    buildHighlightedAnnotatedString(
        text = text,
        colors = MaterialTheme.colorScheme.jsonHighlight,
        reformat = false,
    )

// ──────────────────────────────────────────────────────────────────────────────
// Helpers
// ──────────────────────────────────────────────────────────────────────────────

private fun chipToneForStatusCode(code: Int) = when (code) {
    in 200..299 -> ChipTone.Ok
    in 300..399 -> ChipTone.Info
    in 400..499 -> ChipTone.Warn
    else -> ChipTone.Err
}

/**
 * Editable text field that applies [buildJsonAnnotatedString] syntax highlighting
 * on every keystroke without reformatting the user's text.
 */
@Composable
private fun JsonBodyTextField(
    body: String,
    onBodyChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String,
) {
    val colorScheme = MaterialTheme.colorScheme
    // Capture at @Composable scope — these have @Composable getters and cannot be accessed
    // inside remember{}, LaunchedEffect{}, or onValueChange{}.
    val highlight = colorScheme.jsonHighlight
    val placeholderColor = colorScheme.onSurfaceMuted

    var fieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                annotatedString = buildHighlightedAnnotatedString(body, highlight, reformat = false)
            )
        )
    }

    // Sync external changes (e.g. ViewModel load) without resetting cursor
    LaunchedEffect(body) {
        if (fieldValue.text != body) {
            fieldValue = TextFieldValue(
                annotatedString = buildHighlightedAnnotatedString(body, highlight, reformat = false),
                selection = TextRange(body.length),
            )
        }
    }

    BasicTextField(
        value = fieldValue,
        onValueChange = { newValue ->
            fieldValue = newValue.copy(
                annotatedString = buildHighlightedAnnotatedString(newValue.text, highlight, reformat = false)
            )
            onBodyChange(newValue.text)
        },
        modifier = modifier
            .clipToBounds()
            .background(colorScheme.surfaceContainerLowest, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp),
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = colorScheme.onSurface),
        decorationBox = { innerTextField ->
            Box {
                if (fieldValue.text.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = placeholderColor,
                    )
                }
                innerTextField()
            }
        },
    )
}

// ──────────────────────────────────────────────────────────────────────────────
// Design components
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(title: String) {
    val mutedColor = MaterialTheme.colorScheme.onSurfaceMuted
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(horizontal = 16.dp, vertical = 7.dp)
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = mutedColor,
            letterSpacing = 0.8.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun StatusCodeDropdown(
    statusCode: HttpStatusCode?,
    onSelected: (HttpStatusCode) -> Unit,
    strings: Strings.Widgets.CreateEditPreset,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .clickable { expanded = true }
                .background(colorScheme.surfaceContainerLowest, RoundedCornerShape(8.dp))
                .border(1.dp, colorScheme.outline, RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            statusCode?.let {
                StatusChip(
                    label = "${statusCode.value}",
                    tone = chipToneForStatusCode(statusCode.value),
                )
                Text(
                    text = statusCode.description,
                    style = MaterialTheme.typography.bodyMedium,
                )
            } ?: Text(
                text = strings.noOverrideStatusCode,
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant,
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            HttpStatusCode.allStatusCodes.forEach { code ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            StatusChip(
                                label = "${code.value}",
                                tone = chipToneForStatusCode(code.value),
                            )
                            Text(
                                text = code.description,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    },
                    onClick = {
                        onSelected(code)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun BodyTypeToggle(
    selected: State.Editing.ResponseType,
    onSelect: (State.Editing.ResponseType) -> Unit,
    strings: Strings.Widgets.CreateEditPreset,
) {
    val colorScheme = MaterialTheme.colorScheme
    val chipShape = RoundedCornerShape(8.dp)
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        State.Editing.ResponseType.entries.forEach { type ->
            val isSelected = selected == type
            Box(
                modifier = Modifier
                    .clickable { onSelect(type) }
                    .background(colorScheme.surfaceContainerLowest, chipShape)
                    .then(
                        if (isSelected) {
                            Modifier.border(1.dp, colorScheme.primary, chipShape)
                        } else {
                            Modifier
                        }
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = when (type) {
                        State.Editing.ResponseType.Json -> strings.bodyTypeJson
                        State.Editing.ResponseType.PlainText -> strings.bodyTypePlain
                        State.Editing.ResponseType.Xml -> strings.bodyTypeXml
                        State.Editing.ResponseType.Html -> strings.bodyTypeHtml
                        State.Editing.ResponseType.None -> strings.bodyTypeNone
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isSelected) colorScheme.primary else colorScheme.onSurface,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CreateEditPresetWidgetPreview() = PreviewSurface {
    CreateEditPresetWidgetContent(
        state = State.Editing(
            isSaving = false,
            statusCode = HttpStatusCode.OK,
            body = "{\"key\": \"value\"}",
            hasBodyError = false,
            headers = listOf(
                State.Editing.RequestHeader(key = "Content-Type", value = "application/json"),
            ),
            responseType = State.Editing.ResponseType.Json,
            variant = State.Editing.Variant.Create,
        ),
        endpointName = "Repairs",
        onSave = {},
        onStatusCodeSelected = {},
        onNewResponseType = {},
        onNewResponseBody = {},
        onUpdateNewHeader = { _, _ -> },
        onAddHeader = {},
        onRemoveHeader = {},
    )
}
