package com.apadmi.mockzilla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.apadmi.mockzilla.desktop.mock.simulateUseOfDevelopmentServer
import com.apadmi.mockzilla.desktop.mock.startDevelopmentMockzillaServer
import com.apadmi.mockzilla.desktop.ui.App
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val runtimeParams = startDevelopmentMockzillaServer(this)

        lifecycleScope.launch {
            // Fire off some requests to the dev server to simulate some logs appearing
            simulateUseOfDevelopmentServer(runtimeParams)
        }

        setContent {
            CompositionLocalProvider {
                App()
            }
        }
    }
}
