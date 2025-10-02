package com.apadmi.mockzilla.mobile.ui.deviceconnection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.apadmi.mockzilla.mobile.ui.deviceconnection.MobileDeviceConnectionViewModel.*
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings

@Composable
internal fun MobileDeviceConnectionWidget(
    strings: Strings = LocalStrings.current
) {
    val viewModel = getViewModel<MobileDeviceConnectionViewModel>()
    val state by viewModel.state.collectAsState()

    return Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state) {
            State.Connecting -> CircularProgressIndicator(
                modifier = Modifier.padding(end = 8.dp).size(20.dp)
            )

            is State.Error -> {
                Text(
                    text = strings.widgets.deviceConnection.errorTitle,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )

                Button(
                    onClick = viewModel::attemptLocalConnection
                ) {
                    Text(
                        text = strings.widgets.errorBanner.refreshButton,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Text(
                    text = strings.widgets.deviceConnection.errorMessage,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }

            State.Connected -> Text(strings.widgets.deviceConnection.connected)
        }
    }
}
