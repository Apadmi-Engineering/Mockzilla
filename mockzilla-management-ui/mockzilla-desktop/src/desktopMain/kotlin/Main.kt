@file:Suppress("PACKAGE_NAME_MISSING")

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*

import com.apadmi.mockzilla.desktop.di.startDesktopMockzillaKoin
import com.apadmi.mockzilla.desktop.engine.connection.ZeroConfSdkWrapper
import com.apadmi.mockzilla.desktop.ui.DesktopApp
import com.apadmi.mockzilla.desktop.ui.licenses.LicensesDialog
import com.apadmi.mockzilla.desktop.utils.DesktopLogWriter
import com.apadmi.mockzilla.desktop.utils.handleOsxZoomBehaviour
import com.apadmi.mockzilla.desktop.utils.rememberAppIcon
import com.apadmi.mockzilla.ui.di.utils.MockzillaUiKoinContext
import com.apadmi.mockzilla.ui.i18n.EnStrings

import co.touchlab.kermit.Logger
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs

import java.awt.Dimension
import java.awt.GraphicsEnvironment

private const val minWindowSizeDp = 400

// Calculating the exact toolbar height would need to be done deeper in the layout and bubbled up
// which is messy when it doesn't really matter, this is only used to handle double clicks on the toolbar
private const val roughToolbarHeightDp = 52

@Composable
private fun FrameWindowScope.MockzillaWindowContent(state: WindowState) {
    var showLicenses by remember { mutableStateOf(false) }

    MenuBar {
        Menu(EnStrings.common.openSourceLicenses) {
            Item(EnStrings.common.openSourceLicenses) { showLicenses = true }
        }
    }

    if (showLicenses) {
        LicensesDialog(onDismiss = { showLicenses = false })
    }

    // Makes the UI edge to edge on OSX so that the tabs appear more naturally
    if (hostOs == OS.MacOS) {
        window.rootPane.apply {
            putClientProperty("apple.awt.fullWindowContent", true)
            putClientProperty("apple.awt.transparentTitleBar", true)
            putClientProperty("apple.awt.windowTitleVisible", false)
        }

        window.handleOsxZoomBehaviour(
            state,
            with(LocalDensity.current) { roughToolbarHeightDp.dp.toPx() })
    }

    window.minimumSize = getMinWindowSize()

    DesktopApp()
}

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
        content = { MockzillaWindowContent(state) }
    )
}

@Composable
private fun getMinWindowSize(): Dimension = with(LocalDensity.current) {
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
