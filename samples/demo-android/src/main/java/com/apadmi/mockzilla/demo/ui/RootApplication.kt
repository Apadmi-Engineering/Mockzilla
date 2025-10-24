package com.apadmi.mockzilla.demo.ui

import android.app.Application
import com.apadmi.mockzilla.demo.engine.Repository
import com.apadmi.mockzilla.demo.engine.mock.startMockServer
import com.apadmi.mockzilla.demo.engine.network.MockzillaTokenProvider
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams
import com.apadmi.mockzilla.lib.service.AuthHeaderProvider
import com.apadmi.mockzilla.lib.stopMockzilla

class RootApplication : Application(), MockzillaTokenProvider {
    var params: MockzillaRuntimeParams? = null
    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()

        params = startMockServer(this, false)
        repository = Repository(params!!.mockBaseUrl, this)
    }

    fun setReleaseMode(isRelease: Boolean) {
        stopMockzilla()
        params = startMockServer(this, isRelease)
    }

    override suspend fun getTokenHeader() = params?.authHeaderProvider?.generateHeader()
        ?: AuthHeaderProvider.Header("", "")
}
