package com.apadmi.mockzilla.ui.di.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.apadmi.mockzilla.ui.engine.device.Device
import com.apadmi.mockzilla.ui.viewmodel.ViewModel
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

import kotlinx.coroutines.cancel

// Composition-local `remember` is discarded when a composable leaves the tree (e.g. tab switch).
// Per-device VMs (identified by a non-null `key`) must survive tab switches, so they are stored
// here instead. Widget VMs (no key) are fine to recreate and use the plain `remember` path below.
val desktopViewModelCache = mutableMapOf<String, Any>()

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
): T = remember(qualifier, keyPrefix, device) {
    device?.let {
        // Keyed VMs: check the persistent cache so the same instance is returned after the
        // composable re-enters composition (e.g. switching back to a device tab).
        val cacheKey = "${T::class.qualifiedName}|$qualifier|$keyPrefix|$device"
        @Suppress("UNCHECKED_CAST")
        desktopViewModelCache.getOrPut(cacheKey) {
            MockzillaUiKoinContext.koin.get<T>(qualifier = qualifier, parameters = parameters)
        } as T
    } ?: MockzillaUiKoinContext.koin.get<T>(qualifier = qualifier, parameters = parameters)
}

// Called when a device is fully removed from allDevices. Cancels the coroutine scope directly
// because the base ViewModel class has no clear() method.
actual fun evictDesktopViewModelsForKey(device: Device, keyPrefix: String?) {
    desktopViewModelCache.keys
        .filter { it.endsWith("${"|$keyPrefix".takeIf { keyPrefix != null } ?: ""}|$device") }
        .forEach { cacheKey ->
            (desktopViewModelCache.remove(cacheKey) as? ViewModel)?.viewModelScope?.cancel()
        }
}
