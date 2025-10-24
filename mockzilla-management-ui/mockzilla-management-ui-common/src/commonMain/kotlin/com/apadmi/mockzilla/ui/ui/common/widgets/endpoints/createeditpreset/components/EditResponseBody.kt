package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlignVerticalTop
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.components.CustomTextField
import com.apadmi.mockzilla.ui.ui.common.components.DropdownMenu
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomOutlineButton
import com.apadmi.mockzilla.ui.ui.common.components.buttons.OutlineButtonVariant
import com.apadmi.mockzilla.ui.ui.common.theme.success
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset.CreateEditPresetViewModel.State
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset.TitleRow
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset.card
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointBodyVisualTransformation

private fun State.Editing.ResponseType.string(
    strings: Strings,
) = when (this) {
    State.Editing.ResponseType.Json -> strings.widgets.createEditPreset.bodyTypeJson
    State.Editing.ResponseType.PlainText -> strings.widgets.createEditPreset.bodyTypePlain
}

@Composable
internal fun EditResponseBody(
    state: State.Editing,
    onNewResponseBody: (String) -> Unit,
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

    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        Spacer(modifier = Modifier.height(4.dp))
        Editor(
            body = state.body,
            hasError = false,
            type = state.responseType,
            onResponseBodyChange = onNewResponseBody
        )

        Spacer(modifier = Modifier.height(8.dp))
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
        Spacer(modifier = Modifier.height(8.dp))
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
        singleLine = false
    )
}
