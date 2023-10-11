package com.apadmi.mockzilla

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.desktop.ui.components.PreviewSurface

import com.airbnb.android.showkase.models.ShowkaseBrowserColor

class ColorTestPreview(
    private val showkaseBrowserColor: ShowkaseBrowserColor
) : TestPreview {
    override val type = TestType.Color
    @Composable
    override fun content() = PreviewSurface(color = MaterialTheme.colorScheme.surface) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .width(300.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(showkaseBrowserColor.color)
                    .height(200.dp)
                    .fillMaxWidth()
            ) {
                /* Intentionally blank. */
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Text(
                    text = showkaseBrowserColor.colorName,
                )
                Text(text = showkaseBrowserColor.color.toString())
            }
        }
    }

    override fun toString(): String =
            "${showkaseBrowserColor.colorGroup} - ${showkaseBrowserColor.colorName}"
}
