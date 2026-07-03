package com.apadmi.mockzilla.ui.internal.di.utils

import androidx.compose.runtime.Composable
import com.apadmi.mockzilla.lib.InternalMockzillaApi
import com.apadmi.mockzilla.ui.engine.device.Device

import com.apadmi.mockzilla.ui.internal.viewmodel.ViewModel as InternalViewModel

import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.definition.Definition
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel as koinViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

@InternalMockzillaApi
public actual inline fun <reified T : InternalViewModel> Module.viewModel(
    qualifier: Qualifier?,
    noinline definition: Definition<T>
): KoinDefinition<T> = koinViewModel(qualifier = qualifier, definition = definition)

@InternalMockzillaApi
@OptIn(KoinInternalApi::class)
@Composable
public actual inline fun <reified T : InternalViewModel> getViewModel(
    qualifier: Qualifier?,
    device: Device?,
    keyPrefix: String?,
    noinline parameters: ParametersDefinition?
): T = koinViewModel<T>(
    qualifier = qualifier,
    key = keyPrefix + device.toString(),
    parameters = parameters,
    scope = MockzillaUiKoinContext.koin.scopeRegistry.rootScope
)

@InternalMockzillaApi
public actual fun evictDesktopViewModelsForKey(device: Device, keyPrefix: String?): Unit = Unit
