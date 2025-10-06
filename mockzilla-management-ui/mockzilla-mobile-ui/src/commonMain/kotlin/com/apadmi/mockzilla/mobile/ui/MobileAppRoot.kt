@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.mobile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.mobile.ui.deviceconnection.MobileDeviceConnectionWidget
import com.apadmi.mockzilla.mobile.ui.utils.Destination
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.AppRootViewModel
import com.apadmi.mockzilla.ui.ui.common.AppRootViewModel.*
import com.apadmi.mockzilla.ui.ui.common.theme.AppTheme
import com.apadmi.mockzilla.ui.ui.common.widgets.deviceconnection.UnsupportedDeviceMockzillaVersionWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointsWidget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MobileAppRoot(
    strings: Strings = LocalStrings.current,
    onClose: () -> Unit
) = AppTheme {
    val viewModel = getViewModel<AppRootViewModel>()
    val state by viewModel.state.collectAsState()
    val navController = rememberNavController()
    val showBackButton = navController.currentBackStack.collectAsState()
        .value
        .size > 2

    Column {
        TopAppBar(
            title = { /* No title */ },
            navigationIcon = {
                if (showBackButton) {
                    IconButton(onClick = navController::navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = strings.common.backDescription
                        )
                    }
                }
            },
            actions = {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = strings.common.closeDescription
                    )
                }
            }
        )

        when (val currentState = state) {
            is State.Connected -> ConnectedState(
                navController = navController,
                currentState = currentState
            )

            State.NewDeviceConnection -> MobileDeviceConnectionWidget()
            State.UnsupportedDeviceMockzillaVersion -> UnsupportedDeviceMockzillaVersionWidget()
        }
    }
}

@Composable
private fun ConnectedState(
    navController: NavHostController,
    currentState: State.Connected
) = NavHost(
    navController = navController,
    startDestination = Destination.EndpointList
) {
    composable<Destination.EndpointDetails> { backStackEntry ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            EndpointDetailsWidget(
                device = currentState.activeDevice.device,
                activeEndpoint = EndpointConfiguration.Key(
                    backStackEntry.toRoute<Destination.EndpointDetails>().key
                )
            )
        }
    }

    composable<Destination.EndpointList> {
        Surface {
            EndpointsWidget(
                device = currentState.activeDevice.device,
                onEndpointClicked = {
                    navController.navigate(Destination.EndpointDetails(it.raw))
                }
            )
        }
    }
}
