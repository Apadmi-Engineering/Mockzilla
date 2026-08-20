package com.apadmi.mockzilla.desktop.ui.tools

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.internal.di.utils.getViewModel
import com.apadmi.mockzilla.ui.ui.common.components.CustomTextField
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomIconButton
import com.apadmi.mockzilla.desktop.ui.tools.CodeGenViewModel.*
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomButton
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMonoFontFamily
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted
import com.apadmi.mockzilla.ui.ui.common.theme.success

@Composable
internal fun CodeGenDialog(onDismiss: () -> Unit) {
    val viewModel = getViewModel<CodeGenViewModel>()
    val state by viewModel.state.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        CodeGenDialogContent(
            modifier = Modifier.padding(16.dp),
            onDismiss = onDismiss,
            state = state,
            onGenerate = { input, output -> viewModel.generateConfig(input, output) },
            onTextUpdated = { viewModel.updatedText() }
        )
    }
}

@Composable
private fun CodeGenDialogContent(
    onDismiss: () -> Unit,
    onGenerate: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    state: State,
    onTextUpdated: () -> Unit
) = Surface(
    color = MaterialTheme.colorScheme.surfaceContainerLow,
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    modifier = modifier.widthIn(max = 620.dp),
    shape = MaterialTheme.shapes.small,
) {
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }

    val infoMessage = when (state) {
        is State.GeneratorError -> LocalStrings.current.widgets.codeGen.generatorError(state.err)
        is State.InputError -> state.errorMessage()
        State.Success -> LocalStrings.current.widgets.codeGen.success
        State.Inputting, State.Loading -> ""
    }

    // TODO: move strings
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(bottom = 32.dp)
    ) {
        CodeGenDialogHeader(onDismiss = onDismiss)
        HorizontalDivider()
        Text(
            LocalStrings.current.widgets.codeGen.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        // TODO: update this to a file picker?
        CodeGenInputField(
            LocalStrings.current.widgets.codeGen.inputTitle,
            LocalStrings.current.widgets.codeGen.inputDesc,
            input,
            {
                onTextUpdated()
                input = it.trim()
            },
            LocalStrings.current.widgets.codeGen.inputPlaceholder
        )
        // TODO: expand to more file types
        CodeGenInputField(
            LocalStrings.current.widgets.codeGen.outputTitle,
            LocalStrings.current.widgets.codeGen.outputDesc,
            output,
            {
                onTextUpdated()
                output = it.trim()
            },
            LocalStrings.current.widgets.codeGen.outputPlaceholder
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                infoMessage,
                color = if (state.isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.success.primary,
                textAlign = TextAlign.Center
            )
            Column(modifier = Modifier.height(48.dp)) {
                when (state) {
                    State.Inputting, is State.InputError, is State.GeneratorError, State.Success ->
                        CustomButton(
                            label = LocalStrings.current.widgets.codeGen.button,
                            onClick = { onGenerate(input, output) },
                            modifier = Modifier.fillMaxHeight().width(144.dp),
                            enabled = state !is State.InputError
                        )

                    State.Loading -> CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun CodeGenInputField(
    title: String,
    desc: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurfaceMuted,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold,
            ),
        )
        Text(
            text = desc,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
        CustomTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = LocalMonoFontFamily.current,
            ),
            singleLine = true,
            modifier = Modifier.padding(top = 8.dp).fillMaxWidth().height(48.dp),
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = LocalMonoFontFamily.current
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceMuted
                )
            },
        )
    }
}

@Composable
private fun CodeGenDialogHeader(onDismiss: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = LocalStrings.current.widgets.codeGen.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        CustomIconButton(
            onClick = onDismiss,
            imageVector = Icons.Filled.Close,
            iconTint = LocalContentColor.current,
            contentDescription = LocalStrings.current.common.closeDescription,
            iconSize = 18.dp,
        )
    }
}

@Composable
private fun State.InputError.errorMessage() : String {
    val inputInvalidMsg = if (inputInvalid) LocalStrings.current.widgets.codeGen.inputInvalid else null
    val outputInvalidMsg = if (outputInvalid) LocalStrings.current.widgets.codeGen.outputInvalid else null
    return arrayOf(inputInvalidMsg, outputInvalidMsg).filterNotNull().joinToString(". ")
}