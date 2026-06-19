@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.mobile.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.apadmi.mockzilla.lib.models.EndpointConfiguration
import com.apadmi.mockzilla.mobile.ui.deviceconnection.MobileDeviceConnectionWidget
import com.apadmi.mockzilla.mobile.ui.utils.Destination
import com.apadmi.mockzilla.ui.di.utils.getViewModel
import com.apadmi.mockzilla.ui.i18n.LocalStrings
import com.apadmi.mockzilla.ui.i18n.Strings
import com.apadmi.mockzilla.ui.ui.common.AppRootViewModel
import com.apadmi.mockzilla.ui.ui.common.AppRootViewModel.State
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

@Suppress("MAGIC_NUMBER", "TOO_LONG_FUNCTION")
@Composable
internal fun MobileAppRoot(
    onClose: () -> Unit,
) = AppTheme {
    val colorScheme = MaterialTheme.colorScheme.copy(
        background = Color(0xFF_141_71C),
        surface = Color(0xFF_1E2_228),
        outline = Color(0xFF_2E3_23A)
    )
    MaterialTheme(colorScheme = colorScheme) {
        val viewModel = getViewModel<AppRootViewModel>()
        val state by viewModel.state.collectAsState()
        val navController = rememberNavController()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                )
        ) {
            when (val currentState = state) {
                is State.Connected -> ConnectedState(
                    navController = navController,
                    currentState = currentState,
                    onRefresh = viewModel::refreshAll,
                    colorScheme = colorScheme,
                    onClose = onClose
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
}

@Composable
private fun MobilePageHeader(
    onBack: () -> Unit,
    onClose: () -> Unit,
    colorScheme: ColorScheme,
    strings: Strings = LocalStrings.current,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorScheme.background)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = colorScheme.onSurface,
                    contentDescription = strings.common.backDescription,
                )
            }
        }

        Spacer(Modifier.weight(1f))

        AnimatedVisibility(false) {
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
    }
}

@Suppress("TOO_LONG_FUNCTION")
@Composable
private fun ConnectedState(
    navController: NavHostController,
    currentState: State.Connected,
    onRefresh: () -> Unit,
    onClose: () -> Unit,
    colorScheme: ColorScheme,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        if (navBackStackEntry?.destination?.hasRoute<Destination.EndpointList>() == true) {
            MockzillaBanner(
                onClick = { navController.navigate(Destination.MetaData) },
                onRefresh = onRefresh,
                colorScheme = colorScheme
            )
        } else {
            MobilePageHeader(
                onBack = navController::navigateUp,
                onClose = onClose,
                colorScheme = colorScheme
            )
        }

        NavHost(
            modifier = Modifier.weight(1f).background(color = colorScheme.background),
            navController = navController,
            startDestination = Destination.EndpointList,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
                ) + fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
                ) + fadeOut(animationSpec = tween(400))
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
                ) + fadeIn(animationSpec = tween(400))
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
                ) + fadeOut(animationSpec = tween(400))
            },
        ) {
            composable<Destination.EndpointDetails> { backStackEntry ->
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

            composable<Destination.EndpointList> {
                MaterialTheme(colorScheme = colorScheme.copy(surface = colorScheme.background)) {
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

            composable<Destination.MetaData> {
                MaterialTheme(
                    colorScheme = colorScheme,
                    typography = MaterialTheme.typography.copy(
                        titleLarge = MaterialTheme.typography.titleLarge.copy(color = Color.White)
                    )
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                        MetaDataWidget(device = currentState.activeDevice.device)
                    }
                }
            }

            composable<Destination.GlobalControls> {
                GlobalControlsWidget(device = currentState.activeDevice.device)
            }

            composable<Destination.CreateEditPreset> { backStackEntry ->
                CreateEditPresetWidget(
                    device = currentState.activeDevice.device,
                    activeEndpoint = EndpointConfiguration.Key(
                        backStackEntry.toRoute<Destination.CreateEditPreset>().key,
                    ),
                    creatingNewPreset = backStackEntry.toRoute<Destination.CreateEditPreset>().creatingNewPreset,
                )
            }

            composable<Destination.Debug> {
                DebugWidget()
            }
        }
    }
}

@Composable
private fun MockzillaBanner(
    onClick: () -> Unit,
    onRefresh: () -> Unit,
    colorScheme: ColorScheme,
    strings: Strings = LocalStrings.current
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onClick),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorScheme.background)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.MockzillaLogo,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        tint = colorScheme.primary
                    )
                }
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = strings.widgets.deviceConnection.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                    }
                    Text(
                        text = strings.widgets.metaData.viewAppMetaData,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF_4DB_6AC)
                    )
                }
            }

            IconButton(
                onClick = onRefresh,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorScheme.background)
                    .border(1.dp, colorScheme.outline, RoundedCornerShape(8.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = strings.widgets.errorBanner.refreshButton,
                    tint = colorScheme.onSurfaceVariant
                )
            }
        }
        HorizontalDivider(
            color = colorScheme.outline.copy(alpha = 0.5f)
        )
    }
}
