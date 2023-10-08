package com.apadmi.mockzilla.desktop.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.airbnb.android.showkase.annotation.ShowkaseRoot
import com.airbnb.android.showkase.annotation.ShowkaseRootModule
import com.airbnb.android.showkase.models.Showkase
import com.apadmi.mockzilla.desktop.ui.widgets.DeviceConnectionWidgetPreview

@Composable
actual fun showkaseLauncher(): () -> Unit {
    val context = LocalContext.current
    return remember(context) {
        {
            context.startActivity(Showkase.getBrowserIntent(context))
        }
    }
}

@ShowkaseComposable("b", "a")
@Composable
fun a() {
    Text(text = "Hello world")
}

@Preview("cc", "bb")
@Composable
fun b()  {
    Text(text = "Hello tow")
}
