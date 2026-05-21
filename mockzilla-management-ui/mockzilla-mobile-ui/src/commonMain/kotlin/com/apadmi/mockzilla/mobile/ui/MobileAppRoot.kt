@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.mobile.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

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
import com.apadmi.mockzilla.ui.ui.common.theme.onSurfaceFaint
import com.apadmi.mockzilla.ui.ui.common.widgets.debug.DebugWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.deviceconnection.UnsupportedDeviceMockzillaVersionWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset.CreateEditPresetWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointsWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.globalcontrols.GlobalControlsWidget

@Suppress("MAGIC_NUMBER")
@Composable
internal fun MobileAppRoot(
    strings: Strings = LocalStrings.current,
    onClose: () -> Unit,
) = AppTheme {
    val colorScheme = MaterialTheme.colorScheme
    val viewModel = getViewModel<AppRootViewModel>()
    val state by viewModel.state.collectAsState()
    val navController = rememberNavController()
    val showBackButton = navController.currentBackStack.collectAsState()
        .value
        .size > 2

    Column(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {
        // App bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.surface)
                .statusBarsPadding()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(showBackButton) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    IconButton(onClick = navController::navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = colorScheme.onSurface,
                            contentDescription = strings.common.backDescription,
                        )
                    }
                }
            }

            if (MockzillaBuildConfig.isDevelopmentBuild) {
                IconButton(
                    onClick = { navController.navigate(Destination.Debug) },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Article,
                        tint = colorScheme.onSurfaceVariant,
                        contentDescription = strings.common.debugDescription,
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        tint = colorScheme.onSurfaceVariant,
                        contentDescription = strings.common.closeDescription,
                    )
                }
            }
        }

        when (val currentState = state) {
            is State.Connected -> ConnectedState(
                navController = navController,
                currentState = currentState,
            )
            State.NewDeviceConnection -> MobileDeviceConnectionWidget()
            State.UnsupportedDeviceMockzillaVersion -> UnsupportedDeviceMockzillaVersionWidget()
        }
    }

    AnimatedErrorBanner(
        (state as? State.Connected)?.error,
        viewModel::refreshAll,
        viewModel::dismissError,
    )
}

@Composable
private fun ConnectedState(
    navController: NavHostController,
    currentState: State.Connected,
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(modifier = Modifier.fillMaxSize()) {
        NavHost(
            modifier = Modifier.weight(1f).background(color = colorScheme.background),
            navController = navController,
            startDestination = Destination.EndpointList,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                ) + fadeOut(animationSpec = tween(300))
            },
        ) {
            composable<Destination.EndpointDetails> { backStackEntry ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    EndpointDetailsWidget(
                        device = currentState.activeDevice.device,
                        activeEndpoint = EndpointConfiguration.Key(
                            backStackEntry.toRoute<Destination.EndpointDetails>().key,
                        ),
                        onCreatePreset = {
                            navController.navigate(Destination.CreateEditPreset(it.raw, true))
                        },
                    )
                }
            }

            composable<Destination.EndpointList> {
                Box(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {
                    EndpointsWidget(
                        device = currentState.activeDevice.device,
                        onEndpointClicked = {
                            navController.navigate(Destination.EndpointDetails(it.raw))
                        },
                        onGlobalControlsClicked = {
                            navController.navigate(Destination.GlobalControls)
                        },
                    )
                }
            }

            composable<Destination.GlobalControls> {
                Box(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {
                    GlobalControlsWidget(device = currentState.activeDevice.device)
                }
            }

            composable<Destination.CreateEditPreset> { backStackEntry ->
                Box(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {
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
                Box(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {
                    DebugWidget()
                }
            }
        }

        // Gesture indicator bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(width = 104.dp, height = 2.dp)
                    .background(colorScheme.onSurfaceFaint, shape = RoundedCornerShape(1.dp)),
            )
        }
    }
}
