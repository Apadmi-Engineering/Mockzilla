package com.apadmi.mockzilla.mock.android

import android.app.Application
import com.apadmi.mockzilla.lib.startMockzilla
import com.apadmi.mockzilla.mock.Repository
import com.apadmi.mockzilla.mock.mockzillaConfig

class RootApplication : Application() {

    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()

        val params = startMockzilla(mockzillaConfig, this)
        repository = Repository(params.mockBaseUrl)
    }
}