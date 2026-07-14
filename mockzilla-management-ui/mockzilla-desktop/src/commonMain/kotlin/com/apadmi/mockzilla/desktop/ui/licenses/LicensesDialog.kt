package com.apadmi.mockzilla.desktop.ui.licenses

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

import com.apadmi.mockzilla.desktop.engine.licenses.LibraryForAttribution
import com.apadmi.mockzilla.desktop.engine.licenses.LicenseDisplayModel
import com.apadmi.mockzilla.desktop.ui.licenses.LicensesViewModel.*
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.internal.di.utils.getViewModel
import com.apadmi.mockzilla.ui.ui.common.components.buttons.CustomIconButton
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMonoFontFamily
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted

@Composable
internal fun LicensesDialog(onDismiss: () -> Unit) {
    val viewModel = getViewModel<LicensesViewModel>()
    val state by viewModel.state.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        LicenceDialogContent(modifier = Modifier.padding(16.dp), onDismiss = onDismiss, state = state)
    }
}

@Composable
private fun LicenceDialogContent(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    state: State
) = Surface(
    modifier = modifier.widthIn(max = 620.dp),
    shape = MaterialTheme.shapes.small,
    color = MaterialTheme.colorScheme.surfaceContainer,
    tonalElevation = 6.dp,
) {
    Column {
        LicensesDialogHeader(onDismiss = onDismiss)
        HorizontalDivider()
        when (state) {
            is State.Loading -> CircularProgressIndicator()
            is State.ErrorLoading -> Text(
                text = LocalStrings.current.widgets.openSourceLicenses.error,
                modifier = Modifier.padding(24.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )

            is State.Populated -> LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                items(state.libraries) { library ->
                    LibraryRow(library)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
            State.DevBuild -> Text(
                text = LocalStrings.current.widgets.openSourceLicenses.devBuildsMessage,
                modifier = Modifier.padding(24.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
private fun LicensesDialogHeader(onDismiss: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = LocalStrings.current.widgets.openSourceLicenses.title,
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
private fun LibraryRow(library: LibraryForAttribution) {
    val monoFont = LocalMonoFontFamily.current
    val hasContent = library.licenses.any { it.content != null }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
            .then(if (hasContent) Modifier.clickable { expanded = !expanded } else Modifier)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = library.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                library.licenses.forEach { license ->
                    LicenseChip(license)
                }
            }

            library.version?.let { version ->
                Text(
                    text = version,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceMuted,
                )
            }

            Icon(
                imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.padding(start = 8.dp).size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceMuted.copy(if (hasContent) 1f else 0f),
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 10.dp)) {
                library.licenses.forEach { license ->
                    license.content?.let { content ->
                        Text(
                            text = content,
                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = monoFont),
                            color = MaterialTheme.colorScheme.onSurfaceMuted,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LicenseChip(license: LicenseDisplayModel) = Text(
    text = buildString {
        append(license.name)
        license.spdxId?.let { append(" ($it)") }
        license.url?.let { append(" · $it") }
    },
    style = MaterialTheme.typography.labelSmall,
    color = MaterialTheme.colorScheme.onSurfaceMuted,
)
