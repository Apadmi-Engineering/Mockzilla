package com.apadmi.mockzilla.demo

import android.app.Application
import com.apadmi.mockzilla.lib.models.MockzillaRuntimeParams
import com.apadmi.mockzilla.lib.service.AuthHeaderProvider
import com.apadmi.mockzilla.lib.stopMockzilla

class RootApplication : Application(), MockzillaTokenProvider {
    var params: MockzillaRuntimeParams? = null
    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()

        params = startMockServer(this@RootApplication, false)
        repository = Repository(params!!.mockBaseUrl, this@RootApplication)
    }

    suspend fun setReleaseMode(isRelease: Boolean) {
        stopMockzilla()
        params = startMockServer(this@RootApplication, isRelease)
    }

    override suspend fun getTokenHeader() = params?.authHeaderProvider?.generateHeader() ?: AuthHeaderProvider.Header("", "")
}
