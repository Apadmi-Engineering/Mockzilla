package com.apadmi.mockzilla.desktop.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel as AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.apadmi.mockzilla.desktop.ui.widgets.DeviceConnectionViewModel
import com.apadmi.mockzilla.desktop.viewmodel.ViewModel as InternalViewModel
import org.koin.androidx.viewmodel.dsl.viewModel as koinViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModelFactory
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.GlobalContext
import org.koin.core.definition.Definition
import org.koin.core.instance.InstanceFactory
import org.koin.core.module.Module
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent
import org.koin.androidx.compose.getViewModel as koinGetViewModel

actual inline fun <reified T : InternalViewModel> Module.viewModel(
    qualifier: Qualifier?,
    noinline definition: Definition<T>
): Pair<Module, InstanceFactory<T>> = koinViewModel(qualifier = qualifier, definition = definition)

@Composable
actual inline fun <reified T : InternalViewModel> getViewModel(
    qualifier: Qualifier?,
    noinline parameters: ParametersDefinition?
): T = koinGetViewModel<T>(qualifier = qualifier, parameters = parameters)