package com.apadmi.mockzilla.desktop.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.inputStream

@Composable
fun rememberAppIcon() = remember {
    // Pulls from the icon generated by Conveyor
    // See: https://conveyor.hydraulic.dev/14.2/tutorial/tortoise/2-gradle/#setting-icons
    System.getProperty("app.dir")
        ?.let { Paths.get(it, "icon-512.png") }
        ?.takeIf { it.exists() }
        ?.inputStream()
        ?.buffered()
        ?.use { BitmapPainter(loadImageBitmap(it)) }
}
