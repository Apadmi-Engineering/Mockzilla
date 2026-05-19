package com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlignVerticalTop
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    strings: Strings.Widgets.CreateEditPreset,
) = when (this) {
    State.Editing.ResponseType.Json -> strings.bodyTypeJson
    State.Editing.ResponseType.PlainText -> strings.bodyTypePlain
}

@Composable
internal fun EditResponseBody(
    modifier: Modifier = Modifier,
    state: State.Editing,
    onNewResponseType: (State.Editing.ResponseType) -> Unit,
    onNewResponseBody: (String) -> Unit,
    onFormatResponseBody: () -> Unit,
    onResetResponseBody: () -> Unit,
    strings: Strings.Widgets.CreateEditPreset = LocalStrings.current.widgets.createEditPreset
) = Column(
    modifier = modifier.card()
        .padding(bottom = 8.dp)
        .animateContentSize()
) {
    TitleRow(
        isSet = state.body != null,
        icon = Icons.Default.Code,
        title = strings.bodyTitle,
        onReset = onResetResponseBody
    )
    DropdownMenu(
        stringForItem = { it.string(strings) },
        items = State.Editing.ResponseType.entries,
        selectedLabel = state.responseType.string(strings),
        onSelected = onNewResponseType
    )
    Spacer(modifier = Modifier.height(4.dp))

    Column(
        modifier = Modifier.padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Editor(
            body = state.body,
            hasError = false,
            type = state.responseType,
            onResponseBodyChange = onNewResponseBody
        )

        Row(
            modifier = Modifier.padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = strings.responseCharacters(state.body?.length ?: 0),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.weight(1f))

            if (state.responseType == State.Editing.ResponseType.Json) {
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
                    }
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = when (state.hasBodyError) {
                        true -> strings.invalidLabel
                        false -> strings.validLabel
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = when (state.hasBodyError) {
                        true -> MaterialTheme.colorScheme.error
                        false -> MaterialTheme.colorScheme.success.primary
                    }
                )
            }
        }

        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AnimatedVisibility(
                enter = slideInVertically() + expandVertically(),
                exit = slideOutVertically() + shrinkVertically(),
                visible = state.responseType == State.Editing.ResponseType.Json
            ) {
                CustomOutlineButton(
                    leadingIcon = Icons.Default.AlignVerticalTop,
                    label = strings.responseBodyFormat,
                    enabled = !state.hasBodyError,
                    variant = OutlineButtonVariant.Secondary,
                    onClick = {
                        onFormatResponseBody()
                    }
                )
            }
        }
    }
}

@Composable
private fun Editor(
    body: String?,
    hasError: Boolean,
    type: State.Editing.ResponseType,
    onResponseBodyChange: (String) -> Unit,
    strings: Strings.Widgets.CreateEditPreset = LocalStrings.current.widgets.createEditPreset
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
                    Modifier.semantics { error(strings.invalidLabel) }
                } else {
                    Modifier
                }
            ),
        placeholder = {
            Text(
                text = strings.responseBodyPlaceholder,
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
