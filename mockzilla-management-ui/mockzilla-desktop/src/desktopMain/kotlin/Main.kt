@file:Suppress("PACKAGE_NAME_MISSING")

import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*

import com.apadmi.mockzilla.desktop.di.startDesktopMockzillaKoin
import com.apadmi.mockzilla.desktop.engine.connection.ZeroConfSdkWrapper
import com.apadmi.mockzilla.desktop.ui.DesktopApp
import com.apadmi.mockzilla.desktop.utils.rememberAppIcon
import com.apadmi.mockzilla.ui.di.utils.MockzillaUiKoinContext

import java.awt.Dimension
import java.awt.GraphicsEnvironment

private const val minWindowSizeDp = 400

fun main() = application {
    val state = rememberWindowState(
        placement = WindowPlacement.Maximized,
        position = WindowPosition(Alignment.Center),
        isMinimized = false
    )

    startDesktopMockzillaKoin()

    Window(
        title = "Mockzilla",
        resizable = true,
        state = state,
        icon = rememberAppIcon(),
        onCloseRequest = {
            MockzillaUiKoinContext.koin.get<ZeroConfSdkWrapper>().stop()
            exitApplication()
        },
        content = {
            window.minimumSize = with(LocalDensity.current) {
                val screenSize = GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .defaultScreenDevice
                    .displayMode

                val minDimension = minOf(
                    screenSize.height,
                    screenSize.width,
                    minWindowSizeDp.dp.toPx().toInt()
                )
                Dimension(minDimension, minDimension)
            }
            
            DesktopApp()
        }
    )
}
