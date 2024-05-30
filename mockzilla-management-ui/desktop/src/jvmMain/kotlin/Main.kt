@file:Suppress("PACKAGE_NAME_MISSING")

import androidx.compose.ui.Alignment
import androidx.compose.ui.window.*
import com.apadmi.mockzilla.desktop.di.utils.startMockzillaKoin
import com.apadmi.mockzilla.desktop.ui.App

fun main() = application {
    val state = rememberWindowState(
        placement = WindowPlacement.Maximized,
        position = WindowPosition(Alignment.Center),
        isMinimized = false
    )

    startMockzillaKoin()

    Window(
        title = "Mockzilla",
        resizable = true,
        state = state,
        icon = null,
        onCloseRequest = ::exitApplication,
        content = {
            App()
        }
    )
}
