package com.apadmi.mockzilla.desktop.ui.widgets.deviceconnection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import com.apadmi.mockzilla.desktop.ui.AppRootViewModel
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign
import com.apadmi.mockzilla.desktop.engine.Config


@Composable
fun UnsupportedDeviceMockzillaVersionWidget() {
    UnsupportedDeviceMockzillaVersionContent()
}

@Composable
fun UnsupportedDeviceMockzillaVersionContent() = Column(
    Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
) {
    Text(
        text = "Unsupported SDK",
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center
    )
    Text(
        "Unfortunately this version of Mockzilla is not supported by this app",
        textAlign = TextAlign.Center
    )
    Text(
        "Minimum Supported SDK version: ${Config.minSupportedMockzillaVersion}",
        style = MaterialTheme.typography.displaySmall,
        textAlign = TextAlign.Center
    )
}