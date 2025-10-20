package com.apadmi.mockzilla.mobile.ui.utils

import com.apadmi.mockzilla.lib.sharedstate.MockzillaSharedProcessStateHandler
import com.apadmi.mockzilla.mobile.ui.deviceconnection.MobileDeviceConnectionViewModel
import com.apadmi.mockzilla.ui.di.utils.MockzillaUiKoinContext
import com.apadmi.mockzilla.ui.di.utils.viewModel
import org.koin.core.module.Module

import org.koin.dsl.module

internal fun startMockzillaMobileUiKoin(platformModule: Module) {
    MockzillaUiKoinContext.startMockzillaUiKoinIfNeeded(listOf(platformModule, module {
        viewModel { MobileDeviceConnectionViewModel(get(), get(), get()) }
        single<MockzillaSharedProcessStateHandler> { MockzillaSharedProcessStateHandler(get()) }
    }))
}
