package com.apadmi.mockzilla.ui.internal.di.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.internal.viewmodel.ViewModel
import org.koin.core.definition.Definition
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

@InternalMockzillaApi
public actual inline fun <reified T : ViewModel> Module.viewModel(
    qualifier: Qualifier?,
    noinline definition: Definition<T>
): KoinDefinition<T> = factory(qualifier = qualifier, definition = definition)

@InternalMockzillaApi
@Composable
public actual inline fun <reified T : ViewModel> getViewModel(
    qualifier: Qualifier?,
    device: Device?,
    keyPrefix: String?,
    noinline parameters: ParametersDefinition?
): T = remember(qualifier, device) {
    MockzillaUiKoinContext.koin.get<T>(qualifier = qualifier, parameters = parameters)
}

@InternalMockzillaApi
public actual fun evictDesktopViewModelsForKey(device: Device, keyPrefix: String?): Unit = Unit
