@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.mobile.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

import com.apadmi.mockzilla.lib.MockzillaBuildConfig
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.mobile.ui.deviceconnection.MobileDeviceConnectionWidget
import com.apadmi.mockzilla.mobile.ui.utils.Destination
import com.apadmi.mockzilla.ui.internal.di.utils.MockzillaUiKoinContext
import com.apadmi.mockzilla.ui.internal.di.utils.getViewModel
import com.apadmi.mockzilla.ui.engine.device.ActiveDeviceMonitor
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.DeviceRootViewModel
import com.apadmi.mockzilla.ui.ui.common.assets.Globe
import com.apadmi.mockzilla.ui.ui.common.assets.MockzillaLogo
import com.apadmi.mockzilla.ui.ui.common.components.AnimatedErrorBanner
import com.apadmi.mockzilla.ui.ui.common.theme.AppTheme
import com.apadmi.mockzilla.ui.ui.common.widgets.debug.DebugWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.deviceconnection.UnsupportedDeviceMockzillaVersionWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset.CreateEditPresetWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointsWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.globalcontrols.GlobalControlsWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.metadata.MetaDataWidget
import com.apadmi.mockzilla.ui.utils.iconButtonSize
import com.apadmi.mockzilla.ui.utils.minimumTouchTarget

import org.koin.core.parameter.parametersOf

@Suppress("MAGIC_NUMBER")
@Composable
internal fun MobileAppRoot(
    strings: Strings = LocalStrings.current,
    onClose: () -> Unit,
) = AppTheme {
    val colorScheme = MaterialTheme.colorScheme
    val activeDeviceMonitor = remember { MockzillaUiKoinContext.koin.get<ActiveDeviceMonitor>() }
    val selectedStatefulDevice by activeDeviceMonitor.selectedDevice.collectAsState()
    val selectedDevice = selectedStatefulDevice?.device
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isOnGlobalControls = currentBackStackEntry?.destination?.hasRoute<Destination.GlobalControls>() == true
    val showBackButton = navController.currentBackStack.collectAsState()
        .value
        .size > 2

    Column(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.surfaceContainer)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(showBackButton) {
                IconButton(
                    modifier = Modifier
                        .minimumTouchTarget()
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorScheme.surfaceVariant),
                    onClick = navController::navigateUp
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = colorScheme.onSurface,
                        contentDescription = strings.common.backDescription,
                    )
                }
            }

            AnimatedVisibility(!showBackButton) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorScheme.surface)
                        .clickable {
                            navController.navigate(Destination.MetaData)
                        }
                ) {
                    Image(
                        modifier = Modifier
                            .iconButtonSize()
                            .padding(vertical = 1.dp, horizontal = 4.dp),
                        imageVector = Icons.MockzillaLogo,
                        contentDescription = null
                    )
                }
            }

            Spacer(Modifier.weight(1f))

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

            IconButton(
                enabled = !isOnGlobalControls,
                onClick = {
                    navController.navigate(Destination.GlobalControls)
                }, modifier = Modifier
                    .minimumTouchTarget()
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorScheme.surfaceVariant)
            ) {
                Icon(
                    imageVector = Icons.Filled.Globe,
                    tint = colorScheme.onSurfaceVariant.copy(
                        alpha = if (isOnGlobalControls) {
                            0.5f
                        } else {
                            1f
                        }
                    ),
                    contentDescription = strings.common.closeDescription,
                )
            }

            Spacer(Modifier.width(4.dp))

            IconButton(
                onClick = onClose, modifier = Modifier
                    .minimumTouchTarget()
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorScheme.surfaceVariant)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    tint = colorScheme.onSurfaceVariant,
                    contentDescription = strings.common.closeDescription,
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = colorScheme.outline,
        )

        selectedDevice?.let {
            DeviceContent(device = selectedDevice, navController = navController)
        } ?: MobileDeviceConnectionWidget()
    }
}

@Composable
private fun DeviceContent(device: Device, navController: NavHostController) {
    val viewModel = getViewModel<DeviceRootViewModel>(device = device) { parametersOf(device) }
    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is DeviceRootViewModel.State.Connected -> ConnectedState(
            navController = navController,
            currentState = currentState,
        )
        DeviceRootViewModel.State.UnsupportedDeviceMockzillaVersion -> UnsupportedDeviceMockzillaVersionWidget()
        else -> {
            // this is a generated else block
        }
    }

    AnimatedErrorBanner(
        (state as? DeviceRootViewModel.State.Connected)?.error,
        viewModel::refreshAll,
        viewModel::dismissError,
    )
}

@Composable
private fun ConnectedState(
    navController: NavHostController,
    currentState: DeviceRootViewModel.State.Connected,
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
            composable<Destination.MetaData> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(colorScheme.background)
                ) {
                    MetaDataWidget(device = currentState.activeDevice.device)
                }
            }
            composable<Destination.EndpointDetails> { backStackEntry ->
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    EndpointDetailsWidget(
                        device = currentState.activeDevice.device,
                        activeEndpoint = backStackEntry.toRoute<Destination.EndpointDetails>().key?.let {
                            EndpointConfiguration.Key(it)
                        },
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
                Box(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {
                    EndpointsWidget(
                        device = currentState.activeDevice.device,
                        onEndpointClicked = {
                            navController.navigate(Destination.EndpointDetails(it?.raw))
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
                        onSave = navController::navigateUp,
                        onCancel = navController::navigateUp,
                    )
                }
            }

            composable<Destination.Debug> {
                Box(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {
                    DebugWidget()
                }
            }
        }
    }
}
