@file:Suppress("PACKAGE_NAME_MISSING")

import androidx.compose.ui.Alignment
import androidx.compose.ui.window.*

import com.apadmi.mockzilla.desktop.di.startDesktopMockzillaKoin
import com.apadmi.mockzilla.desktop.engine.connection.ZeroConfSdkWrapper
import com.apadmi.mockzilla.desktop.ui.DesktopApp
import com.apadmi.mockzilla.desktop.utils.DesktopLogWriter
import com.apadmi.mockzilla.desktop.utils.rememberAppIcon
import com.apadmi.mockzilla.ui.di.utils.MockzillaUiKoinContext

import co.touchlab.kermit.Logger

fun main() = application {
    val state = rememberWindowState(
        placement = WindowPlacement.Maximized,
        position = WindowPosition(Alignment.Center),
        isMinimized = false
    )

    Logger.setLogWriters(logWriters = listOf(DesktopLogWriter()))
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
            DesktopApp()
        }
    )
}
