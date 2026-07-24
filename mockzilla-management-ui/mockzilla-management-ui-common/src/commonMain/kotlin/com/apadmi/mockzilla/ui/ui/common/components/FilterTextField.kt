package com.apadmi.mockzilla.ui.ui.common.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceMuted

@InternalMockzillaApi
@Composable
public fun FilterTextField(
    value: String,
    onFilterUpdate: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
): Unit = CustomTextField(
    modifier = modifier,
    value = value,
    onValueChange = onFilterUpdate,
    placeholderText = placeholder,
    leadingIcon = {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceMuted,
        )
    },
    singleLine = true,
)
