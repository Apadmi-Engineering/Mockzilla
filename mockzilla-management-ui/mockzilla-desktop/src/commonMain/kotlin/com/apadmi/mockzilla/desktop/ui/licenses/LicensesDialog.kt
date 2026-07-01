package com.apadmi.mockzilla.desktop.ui.licenses

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.IconButton
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

import com.apadmi.mockzilla.desktop.engine.licenses.LicenseDisplayModel
import com.apadmi.mockzilla.desktop.engine.licenses.LibraryForAttribution
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.i18n.EnStrings
import com.apadmi.mockzilla.ui.ui.common.theme.AppTheme
import com.apadmi.mockzilla.ui.ui.common.theme.LocalMonoFontFamily
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted

@Composable
internal fun LicensesDialog(onDismiss: () -> Unit) {
    val viewModel = getViewModel<LicensesViewModel>()
    val state by viewModel.state.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
            Surface(
                modifier = Modifier.widthIn(max = 560.dp).heightIn(max = 600.dp),
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceContainer,
                tonalElevation = 6.dp,
            ) {
                Column {
                    LicensesDialogHeader(onDismiss = onDismiss)
                    HorizontalDivider()
                    when (val s = state) {
                        is LicensesViewModel.State.Loading -> {
                            CircularProgressIndicator()
                        }
                        is LicensesViewModel.State.ErrorLoading -> {
                            Text(
                                text = "Failed to load licenses.",
                                modifier = Modifier.padding(24.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                        is LicensesViewModel.State.Populated -> {
                            LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                                items(s.libraries) { library ->
                                    LibraryRow(library)
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                                }
                            }
                        }
                        LicensesViewModel.State.DevBuild -> Text(
                            text = "Licenses not generated for debug builds",
                            modifier = Modifier.padding(24.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
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
            text = EnStrings.common.openSourceLicenses,
            style = MaterialTheme.typography.titleMedium,
        )
        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = EnStrings.common.closeDescription,
                modifier = Modifier.size(18.dp),
            )
        }
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = library.name,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    library.version?.let { version ->
                        Text(
                            text = version,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceMuted,
                        )
                    }
                }
                library.licenses.forEach { license ->
                    LicenseChip(license)
                }
            }
            if (hasContent) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 8.dp).size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceMuted,
                )
            }
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
private fun LicenseChip(license: LicenseDisplayModel) {
    val label = buildString {
        append(license.name)
        license.spdxId?.let { append(" ($it)") }
        license.url?.let { append(" · $it") }
    }
    Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceMuted,
    )
}
