package com.apadmi.mockzilla.desktop.ui.components

import com.airbnb.android.showkase.annotation.ShowkaseComposable as ActualShowkaseComposable
import com.airbnb.android.showkase.annotation.ShowkaseRoot
import com.airbnb.android.showkase.annotation.ShowkaseRootModule

actual typealias ShowkaseComposable = ActualShowkaseComposable

@ShowkaseRoot
class RootShowkaseModule : ShowkaseRootModule
