package com.apadmi.mockzilla.desktop.di.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.apadmi.mockzilla.desktop.viewmodel.ViewModel
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent

actual inline fun <reified T : ViewModel> Module.viewModel(
    qualifier: Qualifier?,
    noinline definition: Definition<T>
) = factory(qualifier = qualifier, definition = definition)

@Composable
actual inline fun <reified T : ViewModel> getViewModel(
    qualifier: Qualifier?,
    noinline parameters: ParametersDefinition?
): T = remember(qualifier, parameters) {
    KoinJavaComponent.getKoin().get<T>(qualifier = qualifier, parameters = parameters)
}
