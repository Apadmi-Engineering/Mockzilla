package com.apadmi.mockzilla.ui.di.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.viewmodel.ViewModel
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

actual inline fun <reified T : ViewModel> Module.viewModel(
    qualifier: Qualifier?,
    noinline definition: Definition<T>
) = factory(qualifier = qualifier, definition = definition)

@Composable
actual inline fun <reified T : ViewModel> getViewModel(
    qualifier: Qualifier?,
    device: Device?,
    keyPrefix: String?,
    noinline parameters: ParametersDefinition?
): T = remember(qualifier, device) {
    MockzillaUiKoinContext.koin.get<T>(qualifier = qualifier, parameters = parameters)
}

actual fun evictDesktopViewModelsForKey(device: Device) = Unit
