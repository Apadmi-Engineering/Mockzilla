@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.mobile.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.mobile.ui.components.MobileConnectionHeader
import com.apadmi.mockzilla.mobile.ui.deviceconnection.MobileDeviceConnectionWidget
import com.apadmi.mockzilla.mobile.ui.utils.Destination
import com.apadmi.mockzilla.ui.di.utils.MockzillaUiKoinContext
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.MonitorLogsUseCase
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.AppRootViewModel
import com.apadmi.mockzilla.ui.ui.common.AppRootViewModel.State
import com.apadmi.mockzilla.ui.ui.common.components.AnimatedErrorBanner
import com.apadmi.mockzilla.ui.ui.common.theme.AppTheme
import com.apadmi.mockzilla.ui.ui.common.widgets.debug.DebugWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.deviceconnection.UnsupportedDeviceMockzillaVersionWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset.CreateEditPresetWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointsWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.globalcontrols.GlobalControlsWidget
import kotlinx.coroutines.delay

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

    Column(modifier = Modifier.fillMaxSize().background(colorScheme.surface)) {
        when (val currentState = state) {
            is State.Connected -> ConnectedState(
                navController = navController,
                currentState = currentState,
                onRefresh = viewModel::refreshAll
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

@Suppress("MAGIC_NUMBER")
private fun formatUptime(totalSeconds: Int): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}

@Composable
private fun ConnectedState(
    navController: NavHostController,
    currentState: State.Connected,
    onRefresh: () -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    val monitorLogsUseCase = MockzillaUiKoinContext.koin.get<MonitorLogsUseCase>()
    val endpointsService = MockzillaUiKoinContext.koin.get<MockzillaManagement.EndpointsService>()

    var requestCount by remember { mutableStateOf(0) }
    var activeOverridesCount by remember { mutableStateOf(0) }
    var uptimeSeconds by remember { mutableStateOf(0) }

    LaunchedEffect(currentState.activeDevice.device) {
        while (true) {
            endpointsService.fetchAllEndpointConfigs(currentState.activeDevice.device).onSuccess { list ->
                activeOverridesCount = list.count { it.shouldFail == true || it.delayMs != null || it.appliedPresetOverride != null }
            }
            monitorLogsUseCase.getMonitorLogs(currentState.activeDevice.device).onSuccess {
                requestCount = it.count()
            }
            delay(5000)
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            uptimeSeconds++
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        MobileConnectionHeader(
            statefulDevice = currentState.activeDevice,
            requestCount = requestCount,
            activeOverridesCount = activeOverridesCount,
            uptime = formatUptime(uptimeSeconds),
            onRefresh = onRefresh
        )
        NavHost(
            modifier = Modifier.weight(1f).background(color = colorScheme.surface),
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
                Box(modifier = Modifier.fillMaxSize()) {
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
                Box(modifier = Modifier.fillMaxSize().background(colorScheme.surface)) {
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
                Box(modifier = Modifier.fillMaxSize().background(colorScheme.surface)) {
                    GlobalControlsWidget(device = currentState.activeDevice.device)
                }
            }

            composable<Destination.CreateEditPreset> { backStackEntry ->
                Box(modifier = Modifier.fillMaxSize().background(colorScheme.surface)) {
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
                Box(modifier = Modifier.fillMaxSize().background(colorScheme.surface)) {
                    DebugWidget()
                }
            }
        }
    }
}
