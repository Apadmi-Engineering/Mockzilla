package com.apadmi.mockzilla.desktop

import android.app.Application
import com.apadmi.mockzilla.desktop.di.startMockzillaKoin

class RootApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startMockzillaKoin()
    }
}
