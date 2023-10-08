package com.apadmi.mockzilla.desktop.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import androidx.activity.compose.setContent
import com.apadmi.mockzilla.desktop.ui.widgets.DeviceConnectionWidget


class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CompositionLocalProvider {
                App()
            }
        }
    }
}