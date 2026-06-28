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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.ui.di.utils.evictDesktopViewModelsForKey
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.ChipTone
import com.apadmi.mockzilla.ui.ui.common.components.CustomTextField
import com.apadmi.mockzilla.ui.ui.common.components.ErrorRetry
import com.apadmi.mockzilla.ui.ui.common.components.PreviewSurface
import com.apadmi.mockzilla.ui.ui.common.components.StatusChip
import com.apadmi.mockzilla.ui.ui.common.components.TogglableProgressIndicator
import com.apadmi.mockzilla.ui.ui.common.components.buttons.BaseButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonSize
import com.apadmi.mockzilla.ui.ui.common.components.buttons.ButtonVariant
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomOutlineButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.OutlineButtonVariant
import com.apadmi.mockzilla.ui.ui.common.components.editor.EditorMode
import com.apadmi.mockzilla.ui.ui.common.components.editor.FindableEditorTextField
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMonoFontFamily
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceFaint
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset.CreateEditPresetViewModel.*
import com.apadmi.mockzilla.ui.utils.blockedPointerIcon
import com.apadmi.mockzilla.ui.utils.iconButtonSize
import com.apadmi.mockzilla.ui.utils.minimumTouchTarget

import io.ktor.http.HttpStatusCode
import org.koin.core.parameter.parametersOf

@Composable
private fun ColumnScope.HeadersSection(
    state: State.Editing,
    onAddHeader: (key: String, value: String) -> Unit,
    onRemoveHeader: (State.Editing.RequestHeader) -> Unit,
    strings: Strings.Widgets.CreateEditPreset = LocalStrings.current.widgets.createEditPreset,
) {
    var localKey by remember { mutableStateOf("") }
    var localValue by remember { mutableStateOf("") }

    // Reset draft inputs whenever the server data reloads
    LaunchedEffect(state.syncToken) {
        localKey = ""
        localValue = ""
    }

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
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
            value = localKey,
            singleLine = true,
            containerColor = inputBg,
            placeholder = {
                Text(
                    text = strings.addHeaderKeyPlaceholder,
                    style = titleStyle,
                    color = mutedColor,
                )
            },
            onValueChange = { localKey = it },
        )
        CustomTextField(
            modifier = Modifier.weight(1f).height(30.dp),
            value = localValue,
            singleLine = true,
            containerColor = inputBg,
            placeholder = {
                Text(
                    text = strings.addHeaderValuePlaceholder,
                    style = titleStyle,
                    color = mutedColor,
                )
            },
            onValueChange = { localValue = it },
        )
        val canAdd = localKey.isNotEmpty() && localValue.isNotEmpty()
        IconButton(
            onClick = {
                onAddHeader(localKey, localValue)
                localKey = ""
                localValue = ""
            },
            enabled = canAdd,
            modifier = Modifier
                .iconButtonSize(28.dp)
                .pointerHoverIcon(if (canAdd) PointerIcon.Hand else blockedPointerIcon),
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = strings.addHeaderButton,
                modifier = Modifier.size(14.dp),
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
    onAddHeader: (key: String, value: String) -> Unit,
    onRemoveHeader: (State.Editing.RequestHeader) -> Unit,
    isBodyExpanded: Boolean,
    onToggleBodyExpanded: () -> Unit,
    strings: Strings.Widgets.CreateEditPreset = LocalStrings.current.widgets.createEditPreset,
) {
    AnimatedVisibility(
        visible = !isBodyExpanded,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
    ) {
        PanelHeader(state, onCancel, onSave)
    }

    AnimatedVisibility(
        visible = !isBodyExpanded,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
    ) {
        Column {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            SectionLabel(strings.responseSectionLabel)
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = strings.statusCodeRowLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                Box(
                    modifier = Modifier.weight(3f),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    StatusCodeDropdown(
                        statusCode = state.statusCode,
                        onSelected = onStatusCodeSelected,
                        strings = strings,
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
                    text = strings.bodyTypeLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                Box(
                    modifier = Modifier.weight(3f),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    BodyTypeToggle(
                        selected = state.responseType,
                        onSelect = onNewResponseType,
                        strings = strings,
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
            isExpanded = isBodyExpanded,
            onToggleExpand = onToggleBodyExpanded,
            modifier = if (isBodyExpanded) Modifier.weight(1f) else Modifier,
            strings = strings,
        )
    }

    AnimatedVisibility(
        visible = !isBodyExpanded,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom),
    ) {
        val headerCount = state.headers.size
        val headersLabel = if (headerCount > 0) {
            "${strings.headersTitle} ($headerCount)"
        } else {
            strings.headersTitle
        }
        Column {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            SectionLabel(headersLabel)
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            HeadersSection(
                state = state,
                onAddHeader = onAddHeader,
                onRemoveHeader = onRemoveHeader,
                strings = strings,
            )
        }
    }
}

@Composable
fun CreateEditPresetWidget(
    device: Device,
    activeEndpoint: EndpointConfiguration.Key,
    creatingNewPreset: Boolean,
    onCancel: () -> Unit = {},
    onSave: () -> Unit = {},
) {
    val vmKey = "CreateEditPresetViewModel-${activeEndpoint.raw}-$device"
    val cleanupVm = { evictDesktopViewModelsForKey(key = vmKey) }
    val viewModel = getViewModel<CreateEditPresetViewModel>(
        key = vmKey
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

    LaunchedEffect((state as? State.Editing)?.navigateUp) {
        if ((state as? State.Editing)?.navigateUp == true) {
            cleanupVm()
            onSave()
            viewModel.consumeNavigateUp()
        }
    }

    CreateEditPresetWidgetContent(
        state = state,
        endpointName = activeEndpoint.raw,
        onCancel = {
            cleanupVm()
            onCancel()
        },
        onSave = viewModel::save,
        onStatusCodeSelected = viewModel::onNewStatusCode,
        onNewResponseType = viewModel::onNewResponseType,
        onNewResponseBody = viewModel::onNewResponseBody,
        onFormatResponseBody = viewModel::onFormatResponseBody,
        onAddHeader = viewModel::onAddHeader,
        onRemoveHeader = viewModel::onRemoveHeader,
        onRetry = viewModel::retry
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
    onAddHeader: (key: String, value: String) -> Unit,
    onRemoveHeader: (State.Editing.RequestHeader) -> Unit,
    onRetry: () -> Unit,
    strings: Strings = LocalStrings.current,
) {
    @Suppress("SAY_NO_TO_VAR")
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
        verticalArrangement = if (state is State.FailedToLoad) Arrangement.Center else Arrangement.Top
    ) {
        when (state) {
            State.FailedToLoad -> Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                ErrorRetry(onRetry = onRetry)
                BaseButton(
                    variant = ButtonVariant.Ghost,
                    label = strings.widgets.createEditPreset.cancel,
                    onClick = onCancel
                )
            }
            is State.Loading -> TogglableProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                isLoading = true
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
                onAddHeader = onAddHeader,
                onRemoveHeader = onRemoveHeader,
                isBodyExpanded = isBodyExpanded,
                onToggleBodyExpanded = { isBodyExpanded = !isBodyExpanded }
            )

        }
    }
}

@Composable
private fun BodySection(
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
    // Local state drives the text field to avoid cursor-jump glitches from the VM round-trip.
    // Syncs from VM only when syncToken changes (server reload or format applied).
    var localBody by remember { mutableStateOf(state.body ?: "") }
    LaunchedEffect(state.syncToken) { localBody = state.body ?: "" }

    val isJsonError = state.responseType == State.Editing.ResponseType.Json &&
            localBody.isNotEmpty() && state.bodyParseError != null
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
            color = MaterialTheme.colorScheme.onSurface,
        )

        if (isJsonError) {
            Text(
                text = strings.responseCharacters(localBody.length),
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = LocalMonoFontFamily.current),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.weight(1f)
            )
        } else {
            Text(
                text = strings.responseCharacters(localBody.length),
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
            modifier = Modifier.iconButtonSize(40.dp),
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }

    FindableEditorTextField(
        body = localBody,
        onBodyChange = {
            localBody = it
            onNewResponseBody(it)
        },
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

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun PanelHeader(
    state: State.Editing,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    strings: Strings = LocalStrings.current,
) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
) {
    Box(Modifier.height(6.dp).fillMaxWidth().clipToBounds()) {
        TogglableProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            isLoading = state.isSaving,
            trackColor = Color.Transparent,
            delayMs = 100  // Usually saves are so fast the loading animation is a flicker so delay for these cases
        )
    }

    Row(
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = when (state.variant) {
                    State.Editing.Variant.Create -> strings.widgets.createEditPreset.createTitle
                    State.Editing.Variant.Edit -> strings.widgets.createEditPreset.editTitle
                } + if (state.isDirty) "*" else "",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = if (state.isDirty) FontWeight.ExtraBold else FontWeight.Bold,
            )
            Text(
                text = strings.widgets.createEditPreset.endpointSubtitle(state.endpointName),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceMuted,
            )
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
}

private fun chipToneForStatusCode(code: Int) = when (code) {
    in 200..299 -> ChipTone.Ok
    in 300..399 -> ChipTone.Info
    in 400..499 -> ChipTone.Warn
    else -> ChipTone.Err
}

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
                .clip(RoundedCornerShape(8.dp))
                .background(colorScheme.surfaceContainerLowest, RoundedCornerShape(8.dp))
                .border(1.dp, colorScheme.outline, RoundedCornerShape(8.dp))
                .clickable { expanded = true }
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
                    style = titleStyle,
                    color = MaterialTheme.colorScheme.onSurface,
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
                    .minimumTouchTarget()
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
            syncToken = 0L,
            statusCode = HttpStatusCode.OK,
            body = "{\"key\": \"value\"}",
            headers = listOf(
                State.Editing.RequestHeader(key = "Content-Type", value = "application/json"),
            ),
            responseType = State.Editing.ResponseType.Json,
            variant = State.Editing.Variant.Create,
            committedBody = null,
            committedStatusCode = null,
            committedHeaders = emptyList(),
        ),
        endpointName = "Repairs",
        onSave = {},
        onStatusCodeSelected = {},
        onNewResponseType = {},
        onNewResponseBody = {},
        onAddHeader = { _, _ -> },
        onRemoveHeader = {},
        onRetry = {}
    )
}
