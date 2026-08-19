package com.apadmi.mockzilla.desktop.ui.tools

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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

@Composable
internal fun CodeGenDialog(onDismiss: () -> Unit) {
    val viewModel = getViewModel<CodeGenViewModel>()
    val state by viewModel.state.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        CodeGenDialogContent(
            modifier = Modifier.padding(16.dp),
            onDismiss = onDismiss,
            state = state,
            onGenerate = { input, output -> viewModel.generateConfig(input, output) }
        )
    }
}

@Composable
private fun CodeGenDialogContent(
    onDismiss: () -> Unit,
    onGenerate: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    state: State
) = Surface(
    color = MaterialTheme.colorScheme.surfaceContainerLow,
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    modifier = modifier.widthIn(max = 620.dp),
    shape = MaterialTheme.shapes.small,
) {
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }
    // TODO: move strings
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(bottom = 32.dp)
    ) {
        CodeGenDialogHeader(onDismiss = onDismiss)
        HorizontalDivider()
        Text(
            "This tool allows you to input a swagger file (either yaml or json) to autogenerate Mockzilla Config. ",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        // TODO: update this to a file picker?
        CodeGenInputField(
            "INPUT FILE NAME",
            "Enter the full path to yaml/json swagger file.",
            input,
            { input = it },
            "/Users/example_path/example.yaml"
        )
        // TODO: update this to a download button?
        // TODO: expand to more file types
        CodeGenInputField(
            "OUTPUT FILE NAME",
            "Full path to where the new generated file should be written. Accepted file types: .dart",
            output,
            { output = it },
            "/Users/generated_path/mockzilla_config.g.dart"
        )
        CustomButton(
            modifier = Modifier.height(48.dp).width(144.dp),
            label = "Generate",
            onClick = { onGenerate(input, output) },
        )
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
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp).fillMaxWidth().height(48.dp),
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