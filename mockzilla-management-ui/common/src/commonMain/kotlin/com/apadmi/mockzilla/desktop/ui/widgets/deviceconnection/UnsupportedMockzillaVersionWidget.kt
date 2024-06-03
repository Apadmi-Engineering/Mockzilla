package com.apadmi.mockzilla.desktop.ui.widgets.deviceconnection

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.desktop.i18n.LocalStrings
import com.apadmi.mockzilla.desktop.i18n.Strings

import com.apadmi.common.generated.resources.Res
import com.apadmi.common.generated.resources.mockzilla_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun UnsupportedDeviceMockzillaVersionWidget() {
    UnsupportedDeviceMockzillaVersionContent()
}

@Composable
fun UnsupportedDeviceMockzillaVersionContent(
    strings: Strings = LocalStrings.current
) = Column(
    Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterVertically)
) {
    Image(
        modifier = Modifier.height(100.dp).padding(bottom = 8.dp),
        painter = painterResource(Res.drawable.mockzilla_logo),
        contentDescription = null
    )
    Text(
        text = strings.widgets.unsupportedMockzilla.heading,
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center
    )
    Text(
        text = strings.widgets.unsupportedMockzilla.subtitle,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineSmall
    )
    Spacer(Modifier.height(4.dp))
    Text(
        text = strings.widgets.unsupportedMockzilla.footer,
        style = MaterialTheme.typography.labelSmall,
        textAlign = TextAlign.Center
    )
}
