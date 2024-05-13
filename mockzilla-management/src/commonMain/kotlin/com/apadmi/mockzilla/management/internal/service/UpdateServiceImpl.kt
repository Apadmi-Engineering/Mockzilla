package com.apadmi.mockzilla.management.internal.service

import com.apadmi.mockzilla.management.MockzillaConnectionConfig
import com.apadmi.mockzilla.management.MockzillaManagement
import com.apadmi.mockzilla.management.internal.MockzillaManagementRepository
import kotlin.time.Duration

internal class UpdateServiceImpl(private val repo: MockzillaManagementRepository) : MockzillaManagement.UpdateService {
    override fun setShouldFail(connection: MockzillaConnectionConfig, shouldFail: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setDelay(connection: MockzillaConnectionConfig, delay: Duration?) {
        TODO("Not yet implemented")
    }
}
