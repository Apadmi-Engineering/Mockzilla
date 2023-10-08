package com.apadmi.mockzilla.desktop.ui.components

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.annotation.ShowkaseRoot
import com.airbnb.android.showkase.annotation.ShowkaseRootModule

@ShowkaseRoot
class MyRootModule: ShowkaseRootModule

@Composable
expect fun showkaseLauncher(): () -> Unit