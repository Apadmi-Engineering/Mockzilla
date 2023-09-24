package com.apadmi.mockzilla.desktop.di

import org.koin.core.context.startKoin

internal fun startMockzillaKoin() = startKoin {
    modules(viewModelModule())
}