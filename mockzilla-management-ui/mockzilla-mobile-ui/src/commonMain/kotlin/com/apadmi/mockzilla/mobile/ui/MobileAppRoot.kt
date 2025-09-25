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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.mobile.ui.deviceconnection.MobileDeviceConnectionWidget
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.AppRootViewModel
import com.apadmi.mockzilla.ui.ui.common.AppRootViewModel.*
import com.apadmi.mockzilla.ui.ui.common.theme.AppTheme
import com.apadmi.mockzilla.ui.ui.common.widgets.deviceconnection.UnsupportedDeviceMockzillaVersionWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointsWidget

import kotlinx.serialization.Serializable

/**
 * @property key
 */
// TODO: Replace these with a sealed class so their usages are exhaustive
@Serializable
internal data class EndpointDetails(val key: String)

@Serializable
internal data object EndpointList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MobileAppRoot(
    strings: Strings = LocalStrings.current,
    onClose: () -> Unit
) = AppTheme {
    val viewModel = getViewModel<AppRootViewModel>()
    val state by viewModel.state.collectAsState()
    var showBackButton = remember { mutableStateOf(false) }
    Column {
        TopAppBar(
            title = { },
            navigationIcon = {
                // TODO: Wire up back nav
                if (showBackButton.value) {
                    IconButton(onClick = onClose) {
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
            },
        )

        when (val currentState = state) {
            is State.Connected -> ConnectedState(
                currentState = currentState,
                showBackButton = showBackButton
            )

            State.NewDeviceConnection -> MobileDeviceConnectionWidget()
            State.UnsupportedDeviceMockzillaVersion -> UnsupportedDeviceMockzillaVersionWidget()
        }
    }
}

@Composable
private fun ConnectedState(
    navController: NavHostController = rememberNavController(),
    currentState: State.Connected,
    showBackButton: MutableState<Boolean>
) {
    val backStack by navController.currentBackStack.collectAsState()
    LaunchedEffect(backStack) {
        showBackButton.value = backStack.size > 1
    }

    NavHost(
        navController = navController,
        startDestination = EndpointList
    ) {
        composable<EndpointDetails>() { backStackEntry ->
            Surface(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                EndpointDetailsWidget(
                    currentState.activeDevice.device,
                    activeEndpoint = EndpointConfiguration.Key(backStackEntry.toRoute<EndpointDetails>().key)
                )
            }
        }

        composable<EndpointList> {
            Surface {
                EndpointsWidget(
                    currentState.activeDevice.device
                ) {
                    navController.navigate(EndpointDetails(it.raw))
                }
            }
        }
    }
}
