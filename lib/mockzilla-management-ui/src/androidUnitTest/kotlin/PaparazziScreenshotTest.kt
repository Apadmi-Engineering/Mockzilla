package com.apadmi.mockzilla.androidUnitTest

import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Density
import androidx.lifecycle.Lifecycle
import com.android.ide.common.rendering.api.SessionParams

import com.apadmi.mockzilla.desktop.ui.getMetadata
import com.apadmi.mockzilla.desktop.ui.theme.LocalForceDarkMode

import app.cash.paparazzi.*
import com.airbnb.android.showkase.models.Showkase
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin

@RunWith(TestParameterInjector::class)
class PaparazziScreenshotTest {
    @Suppress("MAGIC_NUMBER")
    @get:Rule
    val paparazzi = Paparazzi(
        maxPercentDifference = 0.0,
        renderingMode = SessionParams.RenderingMode.SHRINK
    )

    object PreviewProvider : TestParameter.TestParameterValuesProvider {
        override fun provideValues(): List<TestPreview> {
            val metadata = Showkase.getMetadata()

            val colors = metadata.colorList.map(::ColorTestPreview)
            val components = metadata.componentList.map(::ComponentTestPreview)
            val typography = metadata.typographyList.map(::TypographyTestPreview)

            return colors + components + typography
        }
    }

    @Test
    fun previewTests(
        @TestParameter(valuesProvider = PreviewProvider::class) componentTestPreview: TestPreview,
        @TestParameter deviceConfigScenario: DeviceConfigScenario,
        @TestParameter fontScaleScenario: FontScaleScenario,
        @TestParameter theme: AppTheme,
    ) {
        // We don't need multiple device configs/font scales for previews other than components.
        if (
            componentTestPreview.type != TestType.Component &&
                    (!deviceConfigScenario.default || !fontScaleScenario.default)
        ) {
            return
        }
        paparazzi.unsafeUpdateConfig(
            deviceConfigScenario.deviceConfig.copy(
                softButtons = false,
            )
        )
        paparazzi.snapshot {
            val lifecycleOwner = LocalLifecycleOwner.current
            CompositionLocalProvider(
                LocalInspectionMode provides true,
                LocalForceDarkMode provides (theme == AppTheme.Dark),
                LocalDensity provides Density(
                    density = LocalDensity.current.density,
                    fontScale = fontScaleScenario.fontScale
                ),
                // Needed so that UI that uses it don't crash during screenshot tests
                LocalOnBackPressedDispatcherOwner provides object : OnBackPressedDispatcherOwner {
                    override val lifecycle: Lifecycle = lifecycleOwner.lifecycle
                    override val onBackPressedDispatcher: OnBackPressedDispatcher =
                            OnBackPressedDispatcher()
                }
            ) {
                componentTestPreview.Content()
            }
        }
    }

    /**
     * @property deviceConfig
     * @property default
     */
    enum class DeviceConfigScenario(
        val deviceConfig: DeviceConfig,
        val default: Boolean = false
    ) {
        Nexus10(DeviceConfig.NEXUS_10, true),
        ;
    }

    /**
     * @property fontScale
     * @property default
     */
    enum class FontScaleScenario(
        val fontScale: Float,
        val default: Boolean = false
    ) {
        Normal(1f, true),
        ;
    }

    enum class AppTheme {
        Dark,
        Light,
        ;
    }

    companion object {
        @BeforeClass
        fun setupKoin() {
            startKoin {
                /* Intentionally blank. */
            }
        }
    }
}

interface TestPreview {
    val type: TestType

    @Composable
    fun Content()
}

enum class TestType {
    Color,
    Component,
    Typography,
    ;
}
