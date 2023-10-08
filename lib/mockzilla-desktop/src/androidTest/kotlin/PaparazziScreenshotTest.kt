package screenshots

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

import app.cash.paparazzi.*
import com.airbnb.android.showkase.models.Showkase
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.apadmi.mockzilla.desktop.ui.components.getMetadata
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

//            val colors = metadata.colorList.map(::ColorTestPreview)
            val components = metadata.componentList.map(::ComponentTestPreview)
//            val typography = metadata.typographyList.map(::TypographyTestPreview)

            // FIXME: Include drawables in snapshots
            // val drawables = DrawableTestPreview.getDrawables()

//            return colors + components + typography
            return  components
        }
    }

    @Test
    fun previewTests(
        @TestParameter(valuesProvider = PreviewProvider::class) componentTestPreview: TestPreview,
        @TestParameter deviceConfigScenario: DeviceConfigScenario,
        @TestParameter fontScaleScenario: FontScaleScenario
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

    enum class DeviceConfigScenario(
        val deviceConfig: DeviceConfig,
        val default: Boolean = false
    ) {
        Nexus10(DeviceConfig.NEXUS_10, true),
        ;
    }

    enum class FontScaleScenario(
        val fontScale: Float,
        val default: Boolean = false
    ) {
        Normal(1f, true),
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
    Drawable,
    Typography,
    ;
}

class ComponentTestPreview(
    private val showkaseBrowserComponent: ShowkaseBrowserComponent
) : TestPreview {
    override val type = TestType.Component

    @Composable
    override fun Content() = showkaseBrowserComponent.component()

    override fun toString(): String =
        "${showkaseBrowserComponent.group} - ${showkaseBrowserComponent.componentName}"
}
