@file:Suppress("FILE_NAME_MATCH_CLASS")

package com.apadmi.mockzilla.mobile.ui

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
import com.apadmi.mockzilla.ui.ui.common.theme.LocalForceDarkMode
import com.apadmi.mockzilla.ui.ui.common.widgets.debug.DebugWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.deviceconnection.UnsupportedDeviceMockzillaVersionWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.createeditpreset.CreateEditPresetWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.details.EndpointDetailsWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.endpoints.endpoints.EndpointsWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.globalcontrols.GlobalControlsWidget
import com.apadmi.mockzilla.ui.ui.common.widgets.metadata.MetaDataWidgetContent
import com.apadmi.mockzilla.ui.ui.common.widgets.metadata.MetaDataWidgetViewModel

import org.koin.core.parameter.parametersOf

@Composable
fun MetaDataHeader(
    state: MetaDataWidgetViewModel.State,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onRefresh: (() -> Unit)? = null,
    onGlobalControlsClick: (() -> Unit)? = null,
    strings: Strings = LocalStrings.current
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Mockzilla Logo and Title
        Surface(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onToggleExpand),
            shape = RoundedCornerShape(8.dp),
            color = colorScheme.onSurface.copy(alpha = 0.05f),
            border = androidx.compose.foundation.BorderStroke(1.dp, colorScheme.outline.copy(alpha = 0.1f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black.copy(alpha = 0.2f)
                ) {
                    Icon(
                        imageVector = Icons.MockzillaLogo,
                        contentDescription = null,
                        modifier = Modifier.padding(6.dp),
                        tint = Color.Unspecified
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = strings.widgets.deviceConnection.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface
                        )
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = colorScheme.onSurfaceVariant
                        )
                    }

                    if (state is MetaDataWidgetViewModel.State.DisplayMetaData) {
                        Text(
                            text = strings.widgets.metaData.overrides(0),  // Placeholder until count is available
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Action Buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            onGlobalControlsClick?.let {
                HeaderActionButton(icon = Icons.Default.DragIndicator, onClick = it)
            }
            onRefresh?.let {
                HeaderActionButton(icon = Icons.Default.Refresh, onClick = it)
            }
        }
    }
}

@Suppress("MAGIC_NUMBER", "UNUSED_PARAMETER")
@Composable
internal fun MobileAppRoot(
    strings: Strings = LocalStrings.current,
    onClose: () -> Unit,
) = AppTheme {
    val colorScheme = MaterialTheme.colorScheme
    val viewModel = getViewModel<AppRootViewModel>()
    val state by viewModel.state.collectAsState()
    val navController = rememberNavController()
    val isDark = LocalForceDarkMode.current
    val backgroundColor = if (isDark) colorScheme.surface else Color.White

    Column(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
        when (val currentState = state) {
            is State.Connected -> ConnectedState(
                navController = navController,
                currentState = currentState,
                onRefresh = viewModel::refreshAll,
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
@Composable
private fun ConnectedState(
    navController: NavHostController,
    currentState: State.Connected,
    onRefresh: () -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    val isDark = LocalForceDarkMode.current
    val backgroundColor = if (isDark) colorScheme.surface else Color.White

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""
    val isGlobalControls =
        currentRoute.contains("GlobalControls") || currentRoute.contains("Destination.GlobalControls")

    var isExpanded by remember { mutableStateOf(value = false) }
    val metaDataViewModel =
        getViewModel<MetaDataWidgetViewModel>(key = currentState.activeDevice.device.toString()) {
            parametersOf(currentState.activeDevice.device)
        }
    val metaDataState by metaDataViewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        if (!isGlobalControls) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
            ) {
                MetaDataHeader(
                    state = metaDataState,
                    isExpanded = isExpanded,
                    onToggleExpand = { isExpanded = !isExpanded },
                    onRefresh = onRefresh,
                    onGlobalControlsClick = {
                        navController.navigate(Destination.GlobalControls)
                    }
                )

                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    MetaDataWidgetContent(metaDataState, currentState.activeDevice.device)
                }
            }
        }
        NavHost(
            modifier = Modifier.weight(1f).background(color = backgroundColor),
            navController = navController,
            startDestination = Destination.EndpointList,
            enterTransition = {
                slideIntoContainer(
                    towards = SlideDirection.Start,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    ),
                ) + fadeIn(
                    animationSpec = tween(
                        300
                    )
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = SlideDirection.Start,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    ),
                ) + fadeOut(
                    animationSpec = tween(
                        300
                    )
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = SlideDirection.End,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    ),
                ) + fadeIn(
                    animationSpec = tween(
                        300
                    )
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = SlideDirection.End,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    ),
                ) + fadeOut(
                    animationSpec = tween(
                        300
                    )
                )
            },
        ) {
            composable<Destination.EndpointDetails> { backStackEntry ->
                Box(modifier = Modifier.fillMaxSize()) {
                    EndpointDetailsWidget(
                        device = currentState.activeDevice.device,
                        activeEndpoint = EndpointConfiguration.Key(
                            backStackEntry.toRoute<Destination.EndpointDetails>().key,
                        )
                    ) {
                        navController.navigate(Destination.CreateEditPreset(it.raw, true))
                    }
                }
            }

            composable<Destination.EndpointList> {
                Box(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
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
                Box(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
                    GlobalControlsWidget(
                        device = currentState.activeDevice.device,
                        onClose = { navController.popBackStack() }
                    )
                }
            }

            composable<Destination.CreateEditPreset> { backStackEntry ->
                Box(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
                    CreateEditPresetWidget(
                        device = currentState.activeDevice.device,
                        activeEndpoint = EndpointConfiguration.Key(
                            backStackEntry.toRoute<Destination.CreateEditPreset>().key,
                        ),
                        creatingNewPreset = backStackEntry.toRoute<Destination.CreateEditPreset>().creatingNewPreset,
                        onCancel = { navController.popBackStack() }
                    )
                }
            }

            composable<Destination.Debug> {
                Box(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
                    DebugWidget()
                }
            }
        }
    }
}

@Composable
private fun HeaderActionButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Surface(
        modifier = Modifier.size(44.dp),
        shape = RoundedCornerShape(10.dp),
        color = colorScheme.onSurface.copy(alpha = 0.05f),
        border = androidx.compose.foundation.BorderStroke(1.dp, colorScheme.outline.copy(alpha = 0.1f))
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
