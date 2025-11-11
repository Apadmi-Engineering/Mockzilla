@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.mobile.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.apadmi.mockzilla.lib.MockzillaBuildConfig

import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.mobile.ui.deviceconnection.MobileDeviceConnectionWidget
import com.apadmi.mockzilla.mobile.ui.utils.Destination
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.AppRootViewModel
import com.apadmi.mockzilla.ui.ui.common.AppRootViewModel.*
import com.apadmi.mockzilla.ui.ui.common.components.AnimatedErrorBanner
import com.apadmi.mockzilla.ui.ui.common.theme.AppTheme
import com.apadmi.mockzilla.ui.ui.common.widgets.debug.DebugWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.deviceconnection.UnsupportedDeviceMockzillaVersionWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset.CreateEditPresetWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointsWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.globalcontrols.GlobalControlsWidget

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
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(showBackButton) {
                IconButton(onClick = navController::navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentDescription = strings.common.backDescription
                    )
                }
            }

            if (MockzillaBuildConfig.isDevelopmentBuild) {
                IconButton(
                    onClick = { navController.navigate(Destination.Debug) }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Article,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentDescription = strings.common.debugDescription
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentDescription = strings.common.closeDescription
                )
            }
        }

        when (val currentState = state) {
            is State.Connected -> ConnectedState(
                navController = navController,
                currentState = currentState
            )

            State.NewDeviceConnection -> MobileDeviceConnectionWidget()
            State.UnsupportedDeviceMockzillaVersion -> UnsupportedDeviceMockzillaVersionWidget()
        }
    }

    AnimatedErrorBanner(
        (state as? State.Connected)?.error,
        viewModel::refreshAll,
        viewModel::dismissError
    )
}

@Composable
private fun ConnectedState(
    navController: NavHostController,
    currentState: State.Connected
) = NavHost(
    modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
    navController = navController,
    startDestination = Destination.EndpointList,
    enterTransition = {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(300))
    },
    exitTransition = {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        ) + fadeOut(animationSpec = tween(300))
    },
    popEnterTransition = {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(300))
    },
    popExitTransition = {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        ) + fadeOut(animationSpec = tween(300))
    }
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
                ),
                onCreatePreset = {
                    navController.navigate(Destination.CreateEditPreset(it.raw, true))
                },
                onEditPreset = {
                    navController.navigate(Destination.CreateEditPreset(it.raw, false))
                },
            )
        }
    }

    composable<Destination.EndpointList> {
        Surface {
            EndpointsWidget(
                device = currentState.activeDevice.device,
                onEndpointClicked = {
                    navController.navigate(Destination.EndpointDetails(it.raw))
                },
                onGlobalControlsClicked = {
                    navController.navigate(Destination.GlobalControls)
                }
            )
        }
    }

    // TODO: Replace this with a bottom sheet once they're out of Experimental status
    // (they already are in Android compose but not in KMP)
    composable<Destination.GlobalControls> {
        Surface {
            GlobalControlsWidget(
                device = currentState.activeDevice.device,
            )
        }
    }

    composable<Destination.CreateEditPreset> { backStackEntry ->
        Surface {
            CreateEditPresetWidget(
                device = currentState.activeDevice.device,
                activeEndpoint = EndpointConfiguration.Key(
                    backStackEntry.toRoute<Destination.CreateEditPreset>().key,
                ),
                creatingNewPreset = backStackEntry.toRoute<Destination.CreateEditPreset>().creatingNewPreset,
            )
        }
    }

    composable<Destination.Debug> {
        Surface {
            DebugWidget()
        }
    }
}
