package com.apadmi.mockzilla.ui.internal.di.utils

import androidx.compose.runtime.Composable
import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.internal.viewmodel.ViewModel
import org.koin.core.definition.Definition
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

@InternalMockzillaApi
public expect inline fun <reified T : ViewModel> Module.viewModel(
    qualifier: Qualifier? = null,
    noinline definition: Definition<T>
): KoinDefinition<T>

@InternalMockzillaApi
@Composable
public expect inline fun <reified T : ViewModel> getViewModel(
    qualifier: Qualifier? = null,
    device: Device? = null,
    keyPrefix: String? = null,
    noinline parameters: ParametersDefinition? = null
): T

@InternalMockzillaApi
public expect fun evictDesktopViewModelsForKey(device: Device, keyPrefix: String? = null)
