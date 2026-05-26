@file:Suppress("MAGIC_NUMBER")

package com.apadmi.mockzilla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

import com.apadmi.mockzilla.lib.internal.utils.FileIo
import com.apadmi.mockzilla.mobile.ui.MobileAppRoot
import com.apadmi.mockzilla.mobile.ui.utils.startMockzillaMobileUiKoin
import com.apadmi.mockzilla.ui.ui.common.theme.AppTheme

import org.koin.core.context.stopKoin
import org.koin.dsl.module

import kotlin.math.roundToInt
import kotlinx.coroutines.launch

private const val sheetHeightFraction = 0.96f
private const val scrimAlpha = 0.4f
private const val dismissDragFraction = 0.25f
private const val dismissVelocityThreshold = 1500f

class MockzillaManagementSdkActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        startMockzillaMobileUiKoin(module {
            single { FileIo(applicationContext.cacheDir) }
        })

        setContent {
            AppTheme {
                BottomSheetWrapper(onDismiss = ::dismissWithNoTransition)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }

    private fun dismissWithNoTransition() {
        finish()
        // Using deprecation since alternative isn't available until API level 34
        overridePendingTransition(0, 0)
    }
}

@Composable
private fun BottomSheetWrapper(onDismiss: () -> Unit) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
    val screenHeightPx = with(LocalDensity.current) { screenHeightDp.toPx() }
    val sheetHeightPx = screenHeightPx * sheetHeightFraction

    val offset = remember { Animatable(sheetHeightPx) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        offset.animateTo(
            targetValue = 0f,
            animationSpec = spring(dampingRatio = 0.85f, stiffness = 400f),
        )
    }

    @Suppress("AVOID_NESTED_FUNCTIONS")
    fun triggerDismiss() {
        scope.launch {
            offset.animateTo(
                targetValue = sheetHeightPx,
                animationSpec = spring(dampingRatio = 1f, stiffness = 600f),
            )
            onDismiss()
        }
    }

    @Suppress("FLOAT_IN_ACCURATE_CALCULATIONS")
    val visibilityFraction = 1 - (offset.value / sheetHeightPx).coerceIn(0f, 1f)

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = scrimAlpha * visibilityFraction))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = ::triggerDismiss,
                ),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeightDp * sheetHeightFraction)
                .align(Alignment.BottomCenter)
                .offset { IntOffset(x = 0, y = offset.value.roundToInt()) }
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .consumeWindowInsets(WindowInsets.statusBars),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .draggable(
                            orientation = Orientation.Vertical,
                            state = rememberDraggableState { delta ->
                                scope.launch {
                                    offset.snapTo((offset.value + delta).coerceAtLeast(0f))
                                }
                            },
                            onDragStopped = { velocity ->
                                if (offset.value > sheetHeightPx * dismissDragFraction || velocity > dismissVelocityThreshold) {
                                    triggerDismiss()
                                } else {
                                    scope.launch {
                                        offset.animateTo(
                                            targetValue = 0f,
                                            animationSpec = spring(dampingRatio = 0.85f, stiffness = 400f),
                                        )
                                    }
                                }
                            },
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .height(4.dp)
                            .fillMaxWidth(0.1f)
                            .background(
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(2.dp),
                            ),
                    )
                }

                MobileAppRoot(onClose = ::triggerDismiss)
            }
        }
    }
}
