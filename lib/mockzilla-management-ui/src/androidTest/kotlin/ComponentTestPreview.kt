package com.apadmi.mockzilla

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent

class ComponentTestPreview(
    private val showkaseBrowserComponent: ShowkaseBrowserComponent
) : TestPreview {
    override val type = TestType.Component

    @Composable
    override fun content() = showkaseBrowserComponent.component()

    override fun toString(): String =
            "${showkaseBrowserComponent.group} - ${showkaseBrowserComponent.componentName}"
}
