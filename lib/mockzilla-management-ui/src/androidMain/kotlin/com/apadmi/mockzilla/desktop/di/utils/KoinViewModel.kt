package com.apadmi.mockzilla.desktop.di.utils

import androidx.compose.runtime.Composable

import com.apadmi.mockzilla.desktop.viewmodel.ViewModel as InternalViewModel

import org.koin.androidx.compose.getViewModel as koinGetViewModel
import org.koin.androidx.viewmodel.dsl.viewModel as koinViewModel
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

actual inline fun <reified T : InternalViewModel> Module.viewModel(
    qualifier: Qualifier?,
    noinline definition: Definition<T>
) = koinViewModel(qualifier = qualifier, definition = definition)

@Composable
actual inline fun <reified T : InternalViewModel> getViewModel(
    qualifier: Qualifier?,
    noinline parameters: ParametersDefinition?
): T = koinGetViewModel<T>(qualifier = qualifier, parameters = parameters)
