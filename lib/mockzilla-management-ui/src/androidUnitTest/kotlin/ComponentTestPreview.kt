package com.apadmi.mockzilla.androidUnitTest

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent

class ComponentTestPreview(
    private val showkaseBrowserComponent: ShowkaseBrowserComponent
) : TestPreview {
    override val type = TestType.Component

    @Composable
    override fun Content() = showkaseBrowserComponent.component()

    override fun toString(): String =
            "${showkaseBrowserComponent.group} - ${showkaseBrowserComponent.componentName}"
}
