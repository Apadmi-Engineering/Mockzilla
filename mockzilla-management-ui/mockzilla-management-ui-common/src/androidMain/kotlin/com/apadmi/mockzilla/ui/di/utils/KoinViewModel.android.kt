package com.apadmi.mockzilla.ui.di.utils

import androidx.compose.runtime.Composable

import com.apadmi.mockzilla.ui.viewmodel.ViewModel as InternalViewModel

import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel as koinViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

actual inline fun <reified T : InternalViewModel> Module.viewModel(
    qualifier: Qualifier?,
    noinline definition: Definition<T>
) = koinViewModel(qualifier = qualifier, definition = definition)

@OptIn(KoinInternalApi::class)
@Composable
actual inline fun <reified T : InternalViewModel> getViewModel(
    qualifier: Qualifier?,
    key: String?,
    noinline parameters: ParametersDefinition?
): T = koinViewModel<T>(
    qualifier = qualifier,
    key = key,
    parameters = parameters,
    scope = MockzillaUiKoinContext.koin.scopeRegistry.rootScope
)
