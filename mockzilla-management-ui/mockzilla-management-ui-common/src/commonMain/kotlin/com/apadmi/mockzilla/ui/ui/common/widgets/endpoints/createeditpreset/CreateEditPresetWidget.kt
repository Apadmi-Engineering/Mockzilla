@file:Suppress(
    "FILE_NAME_MATCH_CLASS",
    "MAGIC_NUMBER",
    "TYPE_ALIAS"
)

package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AlignVerticalTop
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
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
import com.apadmi.mockzilla.ui.ui.common.components.EditorMode
import com.apadmi.mockzilla.ui.ui.common.components.EditorTextField
import com.apadmi.mockzilla.ui.ui.common.components.EmptyState
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.StatusChip
import com.apadmi.mockzilla.ui.ui.common.components.buttons.BaseButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonSize
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonVariant
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomOutlineButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.OutlineButtonVariant
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMonoFontFamily
import com.apadmi.mockzilla.ui.ui.common.theme.jsonHighlight
import com.apadmi.mockzilla.ui.ui.common.theme.jsonKey
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceFaint
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.theme.success
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset.CreateEditPresetViewModel.*
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.buildHighlightedAnnotatedString
import com.apadmi.mockzilla.ui.ui.common.widgets.monitorlogs.details.urlToTitle
import com.apadmi.mockzilla.ui.utils.blockedPointerIcon

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
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(horizontal = 12.dp, vertical = 16.dp),
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
            leadingIcon = Icons.Default.Done,
            onClick = onSave,
        )
    }

    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
}

@Composable
private fun ColumnScope.BodySection(
    state: State.Editing,
    onNewResponseBody: (String) -> Unit,
    onFormatResponseBody: () -> Unit,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier,
    strings: Strings.Widgets.CreateEditPreset = LocalStrings.current.widgets.createEditPreset,
) = Column(
    modifier = modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surfaceContainer),
) {
    val isJsonError = state.responseType == State.Editing.ResponseType.Json &&
            state.body?.isNotEmpty() == true && state.hasBodyError
    val isFormattable = state.responseType == State.Editing.ResponseType.Json

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = strings.bodyLabel,
            style = MaterialTheme.typography.bodyMedium,
        )

        if (isJsonError) {
            Text(
                text = "${strings.invalidLabel} · ${strings.responseCharacters(state.body.length)}",
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = LocalMonoFontFamily.current),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.weight(1f)
            )
        } else {
            Text(
                text = strings.responseCharacters(state.body?.length ?: 0),
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = LocalMonoFontFamily.current),
                color = MaterialTheme.colorScheme.onSurfaceFaint,
                modifier = Modifier.weight(1f)
            )
        }

        CustomOutlineButton(
            leadingIcon = Icons.Default.AlignVerticalTop,
            label = strings.responseBodyFormat,
            enabled = !isJsonError && isFormattable,
            variant = OutlineButtonVariant.Secondary,
            onClick = onFormatResponseBody,
            modifier = Modifier.alpha(if (isFormattable) 1f else 0f)
        )

        IconButton(
            onClick = onToggleExpand,
            modifier = Modifier.size(40.dp),
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                modifier = Modifier.size(18.dp),
            )
        }
    }

    EditorTextField(
        body = state.body ?: "",
        onBodyChange = onNewResponseBody,
        mode = when (state.responseType) {
            State.Editing.ResponseType.Json -> EditorMode.Json
            State.Editing.ResponseType.Html -> EditorMode.Html
            State.Editing.ResponseType.PlainText,
            State.Editing.ResponseType.None -> EditorMode.PlainText
        },
        isExpanded = isExpanded,
        placeholder = when (state.responseType) {
            State.Editing.ResponseType.Json -> strings.responseBodyPlaceholder
            State.Editing.ResponseType.Html -> strings.htmlBodyPlaceholder
            State.Editing.ResponseType.PlainText,
            State.Editing.ResponseType.None -> strings.plainBodyPlaceholder
        },
        parseError = state.bodyParseError.takeIf { isJsonError },
        modifier = Modifier
            .fillMaxWidth()
            .then(if (isExpanded) Modifier.weight(1f) else Modifier)
            .padding(horizontal = 16.dp),
    )

    if (!isExpanded) {
        Spacer(modifier = Modifier.height(16.dp))
    }
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
                .height(48.dp)
                .padding(start = 16.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = header.key,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.jsonKey,
                modifier = Modifier.weight(1f),
            )
            Row(
                modifier = Modifier.weight(3f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = ":  ${header.value}",
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
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
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
        val inputBg = MaterialTheme.colorScheme.surfaceContainerHigh
        val monoFont = LocalMonoFontFamily.current
        val titleStyle = MaterialTheme.typography.labelSmall.copy(
            fontFamily = monoFont,
            fontWeight = FontWeight.SemiBold,
        )
        CustomTextField(
            modifier = Modifier.weight(1f).height(30.dp),
            value = state.newHeader.key,
            singleLine = true,
            containerColor = inputBg,
            placeholder = {
                Text(
                    text = strings.addHeaderKeyPlaceholder,
                    style = titleStyle,
                    color = mutedColor,
                )
            },
            onValueChange = { onUpdateNewHeader(it, null) },
        )
        CustomTextField(
            modifier = Modifier.weight(1f).height(30.dp),
            value = state.newHeader.value,
            singleLine = true,
            containerColor = inputBg,
            placeholder = {
                Text(
                    text = strings.addHeaderValuePlaceholder,
                    style = titleStyle,
                    color = mutedColor,
                )
            },
            onValueChange = { onUpdateNewHeader(null, it) },
        )
        val canAdd = state.newHeader.key.isNotEmpty() && state.newHeader.value.isNotEmpty()
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(6.dp))
                .border(
                    width = 1.dp,
                    color = if (canAdd) {
                        MaterialTheme.colorScheme.outline
                    } else {
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.8f)
                    },
                    shape = RoundedCornerShape(6.dp),
                )
                .clickable(enabled = canAdd, onClick = onAddHeader)
                .pointerHoverIcon(if (canAdd) PointerIcon.Hand else blockedPointerIcon),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = strings.addHeaderButton,
                tint = if (canAdd) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                },
                modifier = Modifier.size(14.dp)
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
    onFormatResponseBody: () -> Unit,
    onUpdateNewHeader: (String?, String?) -> Unit,
    onAddHeader: () -> Unit,
    onRemoveHeader: (State.Editing.RequestHeader) -> Unit,
    isBodyExpanded: Boolean,
    onToggleBodyExpanded: () -> Unit,
    strings: Strings = LocalStrings.current,
) {
    val str = strings.widgets.createEditPreset

    AnimatedVisibility(
        visible = !isBodyExpanded,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
    ) {
        PanelHeader(state, endpointName, onCancel, onSave, strings)
    }

    AnimatedVisibility(
        visible = !isBodyExpanded,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
    ) {
        Column {
            // ── RESPONSE section ─────────────────────────────────────────────
            SectionLabel(str.responseSectionLabel)
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
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
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
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
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
    }

    if (state.responseType != State.Editing.ResponseType.None) {
        BodySection(
            state = state,
            onNewResponseBody = onNewResponseBody,
            onFormatResponseBody = onFormatResponseBody,
            strings = str,
            isExpanded = isBodyExpanded,
            onToggleExpand = onToggleBodyExpanded,
            modifier = if (isBodyExpanded) Modifier.weight(1f) else Modifier,
        )
    }

    AnimatedVisibility(
        visible = !isBodyExpanded,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom),
    ) {
        val headerCount = state.headers.size
        val headersLabel = if (headerCount > 0) {
            "${strings.widgets.createEditPreset.headersTitle} ($headerCount)"
        } else {
            strings.widgets.createEditPreset.headersTitle
        }
        Column {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            SectionLabel(headersLabel)
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            HeadersSection(
                state = state,
                onUpdateNewHeader = onUpdateNewHeader,
                onAddHeader = onAddHeader,
                onRemoveHeader = onRemoveHeader,
                strings = str,
            )
        }
    }
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
        onFormatResponseBody = viewModel::onFormatResponseBody,
        onUpdateNewHeader = viewModel::onUpdateNewHeader,
        onAddHeader = viewModel::onAddHeader,
        onRemoveHeader = viewModel::onRemoveHeader,
    )
}

@Composable
internal fun CreateEditPresetWidgetContent(
    state: State,
    endpointName: String? = null,
    onCancel: () -> Unit = {},
    onSave: () -> Unit,
    onStatusCodeSelected: (HttpStatusCode) -> Unit,
    onNewResponseType: (State.Editing.ResponseType) -> Unit,
    onNewResponseBody: (String) -> Unit,
    onFormatResponseBody: () -> Unit = {},
    onUpdateNewHeader: (String?, String?) -> Unit,
    onAddHeader: () -> Unit,
    onRemoveHeader: (State.Editing.RequestHeader) -> Unit,
    strings: Strings = LocalStrings.current,
) {
    var isBodyExpanded by remember { mutableStateOf(false) }

    LaunchedEffect((state as? State.Editing)?.responseType) {
        if ((state as? State.Editing)?.responseType == State.Editing.ResponseType.None) {
            isBodyExpanded = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(if (!isBodyExpanded) Modifier.verticalScroll(rememberScrollState()) else Modifier)
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
                onFormatResponseBody = onFormatResponseBody,
                onUpdateNewHeader = onUpdateNewHeader,
                onAddHeader = onAddHeader,
                onRemoveHeader = onRemoveHeader,
                isBodyExpanded = isBodyExpanded,
                onToggleBodyExpanded = { isBodyExpanded = !isBodyExpanded },
                strings = strings,
            )
        }
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
    val monoFont = LocalMonoFontFamily.current
    val titleStyle = MaterialTheme.typography.labelSmall.copy(
        fontFamily = monoFont,
        fontWeight = FontWeight.SemiBold,
    )
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .width(130.dp)
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
                    style = titleStyle
                )
            } ?: Text(
                text = strings.noOverrideStatusCode,
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.weight(1f))

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
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        State.Editing.ResponseType.entries.forEach { type ->
            val isSelected = selected == type
            val interactionSource = remember { MutableInteractionSource() }
            val isHovered by interactionSource.collectIsHoveredAsState()
            Box(
                modifier = Modifier
                    .clip(chipShape)
                    .background(
                        color = when {
                            isSelected -> colorScheme.primary.copy(alpha = 0.15f)
                            isHovered -> colorScheme.onSurface.copy(alpha = 0.08f)
                            else -> colorScheme.surfaceContainerHigh
                        },
                    )
                    .then(
                        if (isSelected) {
                            Modifier.border(1.dp, colorScheme.primary, chipShape)
                        } else {
                            Modifier
                        }
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { onSelect(type) },
                    )
                    .pointerHoverIcon(PointerIcon.Hand)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = when (type) {
                        State.Editing.ResponseType.Json -> strings.bodyTypeJson
                        State.Editing.ResponseType.PlainText -> strings.bodyTypePlain

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
