import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.*
import com.apadmi.mockzilla.desktop.di.startMockzillaKoin
import com.apadmi.mockzilla.desktop.ui.App
import com.apadmi.mockzilla.desktop.ui.components.DeviceTabsWidget
import com.apadmi.mockzilla.desktop.ui.scaffold.Widget
import com.apadmi.mockzilla.desktop.ui.scaffold.WidgetScaffold
import com.apadmi.mockzilla.desktop.ui.theme.AppTheme
import com.apadmi.mockzilla.desktop.ui.widgets.DeviceConnectionWidget
import java.awt.Dimension

fun main() = application {
    val state = rememberWindowState(
        placement = WindowPlacement.Floating,
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
            SideEffect {
                window.minimumSize = Dimension(600, 600)
            }
            App()
        }
    )
}

